package com.cobalt.cdpipeline.cdresult;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.*;

import com.atlassian.bamboo.resultsummary.ResultsSummary;

public class CDResultFactoryTest {

	@Test(expected = IllegalArgumentException.class)
	public void testCreateCDResultWithNullArguments() {
		CDResultFactory.createCDResult(null, null, null, null, null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorWithInvalidPlanName() {
		CDResultFactory.createCDResult("project", "projectplan", "projectKey", "planKey", new ArrayList<ResultsSummary>());
	}
	
	@Test
	public void testConstructorWithNormalArguments() {
		CDResult cdresult = CDResultFactory.createCDResult("Project", "Project - Plan", "project", "plan", new ArrayList<ResultsSummary>());
		
		assertEquals("Project name doesn't match.", "Project", cdresult.getProjectName());
		assertEquals("Plan name doesn't match.", "Plan", cdresult.getPlanName());
	}
}
