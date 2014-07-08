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
	ApplicationLink appLink;
	JiraRestService jiraRestService;
	
	public ContributorBuilder(JiraApplinksService jiraApplinksService, JiraRestService jiraRestService){
		// User need to make sure they put the JIRA applink as the primary application link
		if(jiraApplinksService == null || jiraRestService == null){
			appLink = null;
		}else{
		
			Iterator<ApplicationLink> appLinks = jiraApplinksService.getJiraApplicationLinks().iterator();
			if (!appLinks.hasNext()) {
				appLink = null;
			} else {
				appLink = appLinks.next();
			}
			this.jiraRestService = jiraRestService;
		}
	}
	
	public Contributor createContributor(String username, Date lastCommitDate){	
		
  		if(appLink != null){
	  		try {
		  		JiraRestResponse res = jiraRestService.doRestCallViaApplink(appLink, "rest/api/latest/user?username=" + username, 
		  															MethodType.GET, null, BasicAuthenticationProvider.class);
		  		String userJsonStr = res.body;
		  		if (userJsonStr != null) {
			  		// get all info needed to construct a Contributor obj (fullname, picurl, pageurl)
					JSONObject userJsonObj = new JSONObject(userJsonStr);
					String fullname = userJsonObj.getString("displayName"); 
					JSONObject picsAllSizesJsonObj = new JSONObject(userJsonObj.getString("avatarUrls")); // same pic w/ various sizes
					String pictureUrl = picsAllSizesJsonObj.getString("32x32");			
					String profilePageUrl = appLink.getDisplayUrl() + "/secure/ViewProfile.jspa?name=" + username; // page on Jira
					
					return new Contributor(username, lastCommitDate, fullname, pictureUrl, profilePageUrl);
		  		}
	  		} catch (CredentialsRequiredContextException e) {
	  			return new Contributor(username, lastCommitDate, null, null, null);
	  		} catch(JSONException e) {
	  			return new Contributor(username, lastCommitDate, null, null, null);
	  		}
  		}
  		
  		return new Contributor(username, lastCommitDate, null, null, null);
	}
}
