package com.cobalt.cdpipeline.cdresult;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;

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
	private String hostname;
	private String loginname;
	private String password;
	
	public ContributorBuilder(JiraApplinksService jiraApplinksService, JiraRestService jiraRestService){
		this.hostname = "jira.cobalt.com";
		this.password = "4eyesPet!";
		this.loginname = "liuch";
	}
	
	public Contributor createContributor(String username, Date lastCommitDate){				
		
		// Original Code
		try {
			URL url = new URL("https://" + hostname + "/rest/api/2/user?username=" + username);			
			URLConnection conn = url.openConnection();
			String loginPassword = loginname + ":" + password;	
			String encoded = new sun.misc.BASE64Encoder().encode (loginPassword.getBytes());
			conn.setRequestProperty ("Authorization", "Basic " + encoded);		
			InputStream is = conn.getInputStream();
			
			// get all info needed to construct a Contributor obj (fullname, picurl, pageurl)
			String userJsonStr = IOUtils.toString(is, "UTF-8");
			JSONObject userJsonObj = new JSONObject(userJsonStr);
			String fullname = userJsonObj.getString("displayName"); 
			JSONObject picsAllSizesJsonObj = new JSONObject(userJsonObj.getString("avatarUrls")); // same pic w/ various sizes
			String pictureUrl = picsAllSizesJsonObj.getString("48x48");			
			String profilePageUrl = "https://" + hostname + "/secure/ViewProfile.jspa?name=" + username; // page on Jira
			
			return new Contributor(username, lastCommitDate, fullname, pictureUrl, profilePageUrl); // TODO change date
		} catch(MalformedURLException e) {
			e.printStackTrace();
			return new Contributor(username, lastCommitDate, null, null, null);
		} catch(IOException e) {
			e.printStackTrace();
			return new Contributor(username, lastCommitDate, null, null, null);
		} catch(JSONException e) {
			e.printStackTrace();
			return new Contributor(username, lastCommitDate, null, null, null);
		}
		
		// New Code can be used here.
		/*Iterator<ApplicationLink> appLinks = jiraApplinksService.getJiraApplicationLinks().iterator();
	  	String s = "";
  		if(appLinks.hasNext()){
	  		ApplicationLink appLink = appLinks.next();
	  		s += appLink.getName();		  
	  		try {
		  		JiraRestResponse res = jiraRestService.doRestCallViaApplink(appLink, "rest/api/latest/user?username=admin", MethodType.GET, null, BasicAuthenticationProvider.class);
		  		s += "<br>" + res.toString();
	  		} catch (CredentialsRequiredContextException e) {
				// TODO Auto-generated catch block
		  		e.printStackTrace();
	  		}
  		}*/
	}
}
