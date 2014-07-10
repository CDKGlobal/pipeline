package com.cobalt.cdpipeline.cdresult;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Iterator;

import org.junit.Test;

import com.atlassian.applinks.api.ApplicationLink;
import com.atlassian.applinks.api.auth.types.BasicAuthenticationProvider;
import com.atlassian.bamboo.applinks.CredentialsRequiredContextException;
import com.atlassian.bamboo.applinks.JiraApplinksService;
import com.atlassian.bamboo.jira.rest.JiraRestResponse;
import com.atlassian.bamboo.jira.rest.JiraRestResponse.JiraRestResponseBuilder;
import com.atlassian.bamboo.jira.rest.JiraRestService;
import com.atlassian.sal.api.net.Request.MethodType;

public class ContributorBuilderTest {
	JiraApplinksService jiraApplinks;
	JiraRestService jiraRest;
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorWithNullArguments() {
		@SuppressWarnings("unused")
		ContributorBuilder builder = new ContributorBuilder(null, null);
	}
	
	@Test
	public void testCreateContributorWithNoApplinks() {
		setUpNoApplinksJiraApplinksService();
		testCreateContributor(null, null, null);
	}
	
	@Test
	public void testCreateContributorWithNoSuchUsernameExistInJira() {
		setUpApplinksThrowsCredentialRequiredContextException();
		testCreateContributor(null, null, null);
	}
	
	@Test
	public void testCreateContributorWithCredentialsRequired() {
		setUpApplinksWithNoSuchUser();
		testCreateContributor(null, null, null);
	}
	
	@Test
	public void testCreateContributorWithIncompleteJsonResponse() {
		setUpApplinksWithIncompleteJSONResponse();
		testCreateContributor(null, null, null);
	}
	
	
	@Test
	public void testCreateContributorWithNormalCompleteJsonResponse() {
		setUpApplinksWithNormalCompleteResponse();
		testCreateContributor("User", "https://jira.com/secure/useravatar?size=medium&avatarId=1", 
								"https://jira.com/secure/ViewProfile.jspa?name=username");
	}
	
	
	// ========== Private Helper Methods ==========
	
	// Tests createContributor with the given expected results
	private void testCreateContributor(String expectedName, 
										String expectedPicUrl, String expectedProfUrl) {
		ContributorBuilder builder = new ContributorBuilder(jiraApplinks, jiraRest);
		Date commitDate = new Date();
		Contributor contributor = builder.createContributor("username", commitDate);
		assertEquals("Username of the contributor returned doesn't match", 
					"username", contributor.getUsername());
		assertEquals("Commit date of the contributor returned doesn't match", 
					commitDate, contributor.getLastCommitTime());
		assertEquals("Full name of the contributor returned doesn't match.", 
					expectedName, contributor.getFullname());
		assertEquals("Picture URL of the contributor returned doesn't match.", 
					expectedPicUrl, contributor.getPictureUrl());
		assertEquals("Profile page URL of the contributor returned doesn't match.", 
					expectedProfUrl, contributor.getProfilePageUrl());
	}
	
	// Set up JiraApplinksService and JiraRestService that has no applinks
	private void setUpNoApplinksJiraApplinksService() {
		jiraApplinks = mock(JiraApplinksService.class);
    	
		@SuppressWarnings("unchecked")
		Iterable<ApplicationLink> applinks = (Iterable<ApplicationLink>) mock(Iterable.class);
    	when(jiraApplinks.getJiraApplicationLinks()).thenReturn(applinks);
    	
    	@SuppressWarnings("unchecked")
		Iterator<ApplicationLink> applinksIter = (Iterator<ApplicationLink>) mock(Iterator.class);
    	when(applinks.iterator()).thenReturn(applinksIter);
    	when(applinksIter.hasNext()).thenReturn(false);
    	
    	jiraRest = mock(JiraRestService.class);
	}
	
	// Set up JiraApplinksService that has an applink and return that applink
	// This method is used by all the set-up methods below.
	private ApplicationLink setUpBasicJiraApplinksService() {
		jiraApplinks = mock(JiraApplinksService.class);
    	
		@SuppressWarnings("unchecked")
		Iterable<ApplicationLink> applinks = (Iterable<ApplicationLink>) mock(Iterable.class);
    	when(jiraApplinks.getJiraApplicationLinks()).thenReturn(applinks);
    	
    	@SuppressWarnings("unchecked")
		Iterator<ApplicationLink> applinksIter = (Iterator<ApplicationLink>) mock(Iterator.class);
    	when(applinks.iterator()).thenReturn(applinksIter);
    	when(applinksIter.hasNext()).thenReturn(true);
    	
    	ApplicationLink applink = mock(ApplicationLink.class);
    	when(applinksIter.next()).thenReturn(applink);
    	
    	return applink;
	}
	
	// Set up JiraApplinksService and JiraRestService that represents the situation where
	// the username we want to find doesn't exist in Jira
	private void setUpApplinksWithNoSuchUser() {
		ApplicationLink applink = setUpBasicJiraApplinksService();
    	
    	jiraRest = mock(JiraRestService.class);
    	JiraRestResponseBuilder responseBuilder = new JiraRestResponseBuilder(0, "");
    	responseBuilder.body(null);
    	JiraRestResponse response = responseBuilder.build();
    	
    	try {
			when(jiraRest.doRestCallViaApplink(applink, "rest/api/latest/user?username=username", 
			  		MethodType.GET, null, BasicAuthenticationProvider.class)).thenReturn(response);
		} catch (CredentialsRequiredContextException e) { }
    	
	}
	
	// Set up JiraApplinksService and JiraRestService that represents the situation where
	// credentials is required when accessing Jira
	private void setUpApplinksThrowsCredentialRequiredContextException() {
		ApplicationLink applink = setUpBasicJiraApplinksService();
    	
    	jiraRest = mock(JiraRestService.class);
    	try {
			when(jiraRest.doRestCallViaApplink(applink, "rest/api/latest/user?username=username", 
					MethodType.GET, null, BasicAuthenticationProvider.class)).thenThrow(
							new CredentialsRequiredContextException(null, null));
		} catch (CredentialsRequiredContextException e) { }
	}
	
	// Set up JiraApplinksService and JiraRestService that represents the situation where
	// user exists and a JSON response is returned, but the response doesn't contain all the
	// information we need.
	private void setUpApplinksWithIncompleteJSONResponse() {
		ApplicationLink applink = setUpBasicJiraApplinksService();
		
		jiraRest = mock(JiraRestService.class);
    	JiraRestResponseBuilder responseBuilder = new JiraRestResponseBuilder(0, "");
    	// response lack the tag "avatarUrls"
    	responseBuilder.body("{\"displayName\":\"User\"}");
    	JiraRestResponse response = responseBuilder.build();
    	
    	try {
			when(jiraRest.doRestCallViaApplink(applink, "rest/api/latest/user?username=username", 
			  		MethodType.GET, null, BasicAuthenticationProvider.class)).thenReturn(response);
		} catch (CredentialsRequiredContextException e) { }
	}
	
	// Set up JiraApplinksService and JiraRestService that represents the situation where
	// user exists and a complete JSON response is returned.
	private void setUpApplinksWithNormalCompleteResponse() {
		ApplicationLink applink = setUpBasicJiraApplinksService();
		try {
			when(applink.getDisplayUrl()).thenReturn(new URI("https://jira.com"));
		} catch (URISyntaxException e1) { }
		
		jiraRest = mock(JiraRestService.class);
    	JiraRestResponseBuilder responseBuilder = new JiraRestResponseBuilder(0, "");
    	// response lack the tag "avatarUrls"
    	responseBuilder.body("{\"displayName\":\"User\",\"avatarUrls\":"
    			+ "{\"24x24\":\"https://jira.com/secure/useravatar?size=small&avatarId=1\","
    			+ "\"32x32\":\"https://jira.com/secure/useravatar?size=medium&avatarId=1\","
    			+ "\"48x48\":\"https://jira.com/secure/useravatar?avatarId=1\"}}");
    	JiraRestResponse response = responseBuilder.build();
    	
    	try {
			when(jiraRest.doRestCallViaApplink(applink, "rest/api/latest/user?username=username", 
			  		MethodType.GET, null, BasicAuthenticationProvider.class)).thenReturn(response);
		} catch (CredentialsRequiredContextException e) { }
	}
}
