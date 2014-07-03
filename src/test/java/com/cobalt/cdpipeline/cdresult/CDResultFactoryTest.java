package com.cobalt.cdpipeline.cdresult;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.*;

import com.atlassian.bamboo.resultsummary.ResultsSummary;

public class CDResultFactoryTest {
	ContributorBuilder cb = new ContributorBuilder("testHostname", "testUsername", "testPassword");

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
