package com.cobalt.cdpipeline.cdresult;

import java.util.Date;
import java.util.Iterator;

import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.atlassian.applinks.api.ApplicationLink;
import com.atlassian.applinks.api.auth.types.BasicAuthenticationProvider;
import com.atlassian.bamboo.applinks.CredentialsRequiredContextException;
import com.atlassian.bamboo.applinks.JiraApplinksService;
import com.atlassian.bamboo.jira.rest.JiraRestResponse;
import com.atlassian.bamboo.jira.rest.JiraRestService;
import com.atlassian.sal.api.net.Request.MethodType;

public class ContributorBuilder {
	private static final String JIRA_USER_AVATAR_PATH = "/secure/useravatar?ownerId=";
	private static final String JIRA_PROFILE_PATH = "/secure/ViewProfile.jspa?name=";
	
	private String baseUrl;
	
	/**
	 * Constructs a ContributorBuilder object.
	 * 
	 * @param jiraApplinksService to connect to Jira via Application Link.
	 */
	public ContributorBuilder(JiraApplinksService jiraApplinksService){
		// User need to make sure they put the JIRA applink as the primary application link
		if(jiraApplinksService == null){
			throw new IllegalArgumentException("Arguments can't be null.");
		}
		
		Iterator<ApplicationLink> appLinks = jiraApplinksService.getJiraApplicationLinks().iterator();
		if (appLinks.hasNext()) {
			baseUrl = appLinks.next().getRpcUrl().toString();
		} else {
			baseUrl = null;
		}
	}
	
	/**
	 * Create a Contributor base on the username, last commit date.
	 * ContributorBuilder will try to access Jira via the given services in constructor and
	 * gather user information from Jira.
	 * 
	 * @param username of the contributor
	 * @param lastCommitDate of the contributor
	 * @param fullName of the contributor from Bamboo
	 * @return the Contributor created. If there are application link to Jira,
	 *         Contributor's picture url and profile page url will be set accordingly, 
	 *         otherwise, those fields will be null.
	 */
	public Contributor createContributor(String username, Date lastCommitDate, String fullName){
		if (baseUrl != null) {
			String pictureUrl = baseUrl + JIRA_USER_AVATAR_PATH + username;
			String profilePageUrl = baseUrl + JIRA_PROFILE_PATH + username;
			return new Contributor(username, lastCommitDate, fullName, pictureUrl, profilePageUrl);
		} else {
			return new Contributor(username, lastCommitDate, fullName, null, null);
		}
	}
}
