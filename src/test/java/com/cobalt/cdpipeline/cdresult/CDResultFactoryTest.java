package com.cobalt.cdpipeline.cdresult;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.junit.*;

import com.atlassian.bamboo.resultsummary.ResultsSummary;

@RunWith(Suite.class)
@SuiteClasses({ AddAllAuthorsInCommitsTest.class, PipelineStagesTest.class,
		SetCurrentBuildInfoTest.class, SetLastDeploymentInfoTest.class })
public class CDResultFactoryTest {

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorWithNullArguments() {
		CDResultFactory fac = new CDResultFactory(null, null, null, null, null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorWithInvalidPlanName() {
		CDResultFactory fac = new CDResultFactory("project", "projectplan", "projectKey", "planKey", new ArrayList<ResultsSummary>());
	}
	
	@Test
	public void testConstructorWithNormalArguments() {
		CDResultFactory fac = new CDResultFactory("Project", "Project - Plan", "project", "plan", new ArrayList<ResultsSummary>());
		
		assertEquals("Project name doesn't match.", "Project", fac.cdresult.getProjectName());
		assertEquals("Plan name doesn't match.", "Plan", fac.cdresult.getPlanName());
	}
}
