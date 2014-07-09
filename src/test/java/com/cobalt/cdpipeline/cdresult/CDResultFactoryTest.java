package com.cobalt.cdpipeline.cdresult;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.*;

import com.atlassian.applinks.api.ApplicationLink;
import com.atlassian.bamboo.applinks.JiraApplinksService;
import com.atlassian.bamboo.jira.rest.JiraRestService;
import com.atlassian.bamboo.resultsummary.ResultsSummary;

public class CDResultFactoryTest {
	
	//ContributorBuilder cb = new ContributorBuilder(null, null);
	ContributorBuilder cb;

	@Before
	public void setup() {
		// mockup JiraApplinksService
		JiraApplinksService jiraApplinksService = mock(JiraApplinksService.class);		
		Iterator<ApplicationLink> itr = (Iterator<ApplicationLink>) mock(Iterator.class);
		when(itr.hasNext()).thenReturn(false);	
		Iterable<ApplicationLink> iterable = (Iterable<ApplicationLink>) mock(Iterable.class);
		when(iterable.iterator()).thenReturn(itr);	
		when(jiraApplinksService.getJiraApplicationLinks()).thenReturn(iterable);
		// mockup JiraRestService	
		JiraRestService jiraRestService = mock(JiraRestService.class);	
		
		cb = new ContributorBuilder(jiraApplinksService, jiraRestService);	 
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreateCDResultWithNullArguments() {
		CDResultFactory.createCDResult(null, null, null, null, null, cb);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorWithInvalidPlanName() {
		CDResultFactory.createCDResult("project", "projectplan", "projectKey", "planKey", new ArrayList<ResultsSummary>(), cb);
	}
	
	@Test
	public void testConstructorWithNormalArguments() {
		CDResult cdresult = CDResultFactory.createCDResult("Project", "Project - Plan", "project", "plan", new ArrayList<ResultsSummary>(), cb);
		
		assertEquals("Project name doesn't match.", "Project", cdresult.getProjectName());
		assertEquals("Plan name doesn't match.", "Plan", cdresult.getPlanName());
	}
}
