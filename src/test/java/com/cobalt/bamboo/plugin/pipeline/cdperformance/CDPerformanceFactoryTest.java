package com.cobalt.bamboo.plugin.pipeline.cdperformance;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.*;

import com.atlassian.applinks.api.ApplicationLink;
import com.atlassian.bamboo.applinks.JiraApplinksService;
import com.atlassian.bamboo.resultsummary.ResultsSummary;
import com.cobalt.bamboo.plugin.pipeline.cdresult.ContributorBuilder;


public class CDPerformanceFactoryTest {
	ContributorBuilder cb;
	
	@Before
	public void setup(){
		// mockup JiraApplinksService
		JiraApplinksService jiraApplinksService = mock(JiraApplinksService.class);
		Iterator<ApplicationLink> itr = (Iterator<ApplicationLink>) mock(Iterator.class);
		when(itr.hasNext()).thenReturn(false);
		Iterable<ApplicationLink> iterable = (Iterable<ApplicationLink>) mock(Iterable.class);
		when(iterable.iterator()).thenReturn(itr);	
		when(jiraApplinksService.getJiraApplicationLinks()).thenReturn(iterable);
				
		cb = new ContributorBuilder(jiraApplinksService);
	}
	
	@Test
	public void testNoBuild(){
		CDPerformance cdp = CDPerformanceFactory.createCDPerformance(new ArrayList<ResultsSummary>(), cb);
		assertEquals("No build, return null", null, cdp);
	}
	
}
