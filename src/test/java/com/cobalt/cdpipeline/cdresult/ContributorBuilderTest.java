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
import com.atlassian.bamboo.applinks.JiraApplinksService;
import com.atlassian.bamboo.jira.rest.JiraRestService;

public class ContributorBuilderTest {
	JiraApplinksService jiraApplinks;
	JiraRestService jiraRest;
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorWithNullArguments() {
		ContributorBuilder builder = new ContributorBuilder(null);
	}
	
	@Test
	public void testCreateContributorWithNoApplinks() {
		setUpNoApplinksJiraApplinksService();
		testCreateContributor(null, null);
	}
	
	@Test
	public void testCreateContributorWithNormalApplinks() {
		setUpNormalJiraApplinksService();
		testCreateContributor("www.baseurl.com/secure/useravatar?ownerId=username", 
							"www.baseurl.com/secure/ViewProfile.jspa?name=username");
	}
	
//	@Test
//	public void testCreateContributorWithNoSuchUsernameExistInJira() {
//		setUpApplinksThrowsCredentialRequiredContextException();
//		testCreateContributor(null, null);
//	}
//	
//	@Test
//	public void testCreateContributorWithCredentialsRequired() {
//		setUpApplinksWithNoSuchUser();
//		testCreateContributor(null, null);
//	}
//	
//	@Test
//	public void testCreateContributorWithIncompleteJsonResponse() {
//		setUpApplinksWithIncompleteJSONResponse();
//		testCreateContributor(null, null);
//	}
//	
//	
//	@Test
//	public void testCreateContributorWithNormalCompleteJsonResponse() {
//		setUpApplinksWithNormalCompleteResponse();
//		testCreateContributor("https://jira.com/secure/useravatar?avatarId=1", 
//								"https://jira.com/secure/ViewProfile.jspa?name=username");
//	}
	
	
	// ========== Private Helper Methods ==========
	
	// Tests createContributor with the given expected results
	private void testCreateContributor(String expectedPicUrl, String expectedProfUrl) {
		ContributorBuilder builder = new ContributorBuilder(jiraApplinks);
		Date commitDate = new Date();
		Contributor contributor = builder.createContributor("username", commitDate, "User");
		assertEquals("Username of the contributor returned doesn't match", 
					"username", contributor.getUsername());
		assertEquals("Commit date of the contributor returned doesn't match", 
					commitDate, contributor.getLastCommitTime());
		assertEquals("Full name of the contributor returned doesn't match.", 
					"User", contributor.getFullname());
		assertEquals("Picture URL of the contributor returned doesn't match.", 
					expectedPicUrl, contributor.getPictureUrl());
		assertEquals("Profile page URL of the contributor returned doesn't match.", 
					expectedProfUrl, contributor.getProfilePageUrl());
	}
	
	// Set up JiraApplinksService and JiraRestService that has no applinks
	private void setUpNoApplinksJiraApplinksService() {
		jiraApplinks = mock(JiraApplinksService.class);
    	
		Iterable<ApplicationLink> applinks = (Iterable<ApplicationLink>) mock(Iterable.class);
    	when(jiraApplinks.getJiraApplicationLinks()).thenReturn(applinks);
    	
		Iterator<ApplicationLink> applinksIter = (Iterator<ApplicationLink>) mock(Iterator.class);
    	when(applinks.iterator()).thenReturn(applinksIter);
    	when(applinksIter.hasNext()).thenReturn(false);
	}
	
	private void setUpNormalJiraApplinksService() {
		jiraApplinks = mock(JiraApplinksService.class);
    	
		Iterable<ApplicationLink> applinks = (Iterable<ApplicationLink>) mock(Iterable.class);
    	when(jiraApplinks.getJiraApplicationLinks()).thenReturn(applinks);
    	
		Iterator<ApplicationLink> applinksIter = (Iterator<ApplicationLink>) mock(Iterator.class);
    	when(applinks.iterator()).thenReturn(applinksIter);
    	when(applinksIter.hasNext()).thenReturn(true);
    	
    	ApplicationLink appLink = mock(ApplicationLink.class);
    	when(applinksIter.next()).thenReturn(appLink);
    	try {
			when(appLink.getRpcUrl()).thenReturn(new URI("www.baseurl.com"));
		} catch (URISyntaxException e) { }
	}
	
//	// Set up JiraApplinksService that has an applink and return that applink
//	// This method is used by all the set-up methods below.
//	private ApplicationLink setUpBasicJiraApplinksService() {
//		jiraApplinks = mock(JiraApplinksService.class);
//    	
//		Iterable<ApplicationLink> applinks = (Iterable<ApplicationLink>) mock(Iterable.class);
//    	when(jiraApplinks.getJiraApplicationLinks()).thenReturn(applinks);
//    	
//		Iterator<ApplicationLink> applinksIter = (Iterator<ApplicationLink>) mock(Iterator.class);
//    	when(applinks.iterator()).thenReturn(applinksIter);
//    	when(applinksIter.hasNext()).thenReturn(true);
//    	
//    	ApplicationLink applink = mock(ApplicationLink.class);
//    	when(applinksIter.next()).thenReturn(applink);
//    	
//    	return applink;
//	}
//	
//	// Set up JiraApplinksService and JiraRestService that represents the situation where
//	// the username we want to find doesn't exist in Jira
//	private void setUpApplinksWithNoSuchUser() {
//		ApplicationLink applink = setUpBasicJiraApplinksService();
//    	
//    	jiraRest = mock(JiraRestService.class);
//    	JiraRestResponseBuilder responseBuilder = new JiraRestResponseBuilder(0, "");
//    	responseBuilder.body(null);
//    	JiraRestResponse response = responseBuilder.build();
//    	
//    	try {
//			when(jiraRest.doRestCallViaApplink(applink, "rest/api/latest/user?username=username", 
//			  		MethodType.GET, null, BasicAuthenticationProvider.class)).thenReturn(response);
//		} catch (CredentialsRequiredContextException e) { }
//    	
//	}
//	
//	// Set up JiraApplinksService and JiraRestService that represents the situation where
//	// credentials is required when accessing Jira
//	private void setUpApplinksThrowsCredentialRequiredContextException() {
//		ApplicationLink applink = setUpBasicJiraApplinksService();
//    	
//    	jiraRest = mock(JiraRestService.class);
//    	try {
//			when(jiraRest.doRestCallViaApplink(applink, "rest/api/latest/user?username=username", 
//					MethodType.GET, null, BasicAuthenticationProvider.class)).thenThrow(
//							new CredentialsRequiredContextException(null, null));
//		} catch (CredentialsRequiredContextException e) { }
//	}
//	
//	// Set up JiraApplinksService and JiraRestService that represents the situation where
//	// user exists and a JSON response is returned, but the response doesn't contain all the
//	// information we need.
//	private void setUpApplinksWithIncompleteJSONResponse() {
//		ApplicationLink applink = setUpBasicJiraApplinksService();
//		
//		jiraRest = mock(JiraRestService.class);
//    	JiraRestResponseBuilder responseBuilder = new JiraRestResponseBuilder(0, "");
//    	// response lack the tag "avatarUrls"
//    	responseBuilder.body("{\"displayName\":\"User\"}");
//    	JiraRestResponse response = responseBuilder.build();
//    	
//    	try {
//			when(jiraRest.doRestCallViaApplink(applink, "rest/api/latest/user?username=username", 
//			  		MethodType.GET, null, BasicAuthenticationProvider.class)).thenReturn(response);
//		} catch (CredentialsRequiredContextException e) { }
//	}
//	
//	// Set up JiraApplinksService and JiraRestService that represents the situation where
//	// user exists and a complete JSON response is returned.
//	private void setUpApplinksWithNormalCompleteResponse() {
//		ApplicationLink applink = setUpBasicJiraApplinksService();
//		try {
//			when(applink.getRpcUrl()).thenReturn(new URI("https://jira.com"));
//		} catch (URISyntaxException e1) { }
//		
//		jiraRest = mock(JiraRestService.class);
//    	JiraRestResponseBuilder responseBuilder = new JiraRestResponseBuilder(0, "");
//    	// response lack the tag "avatarUrls"
//    	responseBuilder.body("{\"displayName\":\"User\",\"avatarUrls\":"
//    			+ "{\"24x24\":\"https://jira.com/secure/useravatar?size=small&avatarId=1\","
//    			+ "\"32x32\":\"https://jira.com/secure/useravatar?size=medium&avatarId=1\","
//    			+ "\"48x48\":\"https://jira.com/secure/useravatar?avatarId=1\"}}");
//    	JiraRestResponse response = responseBuilder.build();
//    	
//    	try {
//			when(jiraRest.doRestCallViaApplink(applink, "rest/api/latest/user?username=username", 
//			  		MethodType.GET, null, BasicAuthenticationProvider.class)).thenReturn(response);
//		} catch (CredentialsRequiredContextException e) { }
//	}
}
