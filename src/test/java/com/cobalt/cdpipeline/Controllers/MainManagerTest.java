package com.cobalt.cdpipeline.Controllers;

import com.atlassian.bamboo.plan.PlanManager;
import com.atlassian.bamboo.plan.TopLevelPlan;
import com.atlassian.bamboo.project.Project;
import com.atlassian.bamboo.project.ProjectManager;
import com.atlassian.bamboo.resultsummary.ResultsSummary;
import com.atlassian.bamboo.resultsummary.ResultsSummaryManager;
import com.cobalt.cdpipeline.cdresult.CDResult;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MainManagerTest {
	private MainManager main;
	private ProjectManager projectMgr;
	private PlanManager planMgr;
	private ResultsSummaryManager resultsSumMgr;
	
	
    @Test(expected = IllegalArgumentException.class)
    public void testGetCDResultsWithNullArguments() {
        main = new MainManager(null, null, null);
        List<CDResult> results = main.getCDResults();
    }
    
    // The following tests only test the project-and-plan logic in
    // GetCDResults using the size of the result list. 
    // Empty build lists are used as the mock up data.
    
    // no project
    @Test
    public void testGetCDResultWithNoProjectBySize() {
    	testWithEmptyBuildListBySize(0, 0, 0);
    }
    
    // 1 project, no plans
    @Test
    public void testGetCDResultWithOneProjectNoPlanBySize() {
    	testWithEmptyBuildListBySize(1, 0, 0);
    }
    
    // 3 projects, each with no plans
    @Test
    public void testGetCDResultWithSmallProjectSetAndNoPlansBySize() {
    	testWithEmptyBuildListBySize(3, 0, 0);
    }
    
    // 20 projects, each with no plans
    @Test
    public void testGetCDResultWithBiggerProjectSetAndNoPlansBySize() {
    	testWithEmptyBuildListBySize(20, 0, 0);
    }
    
    // 1 project, 1 plan
    @Test
    public void testGetCDResultWithOneProjectAndOnePlanBySize() {
    	testWithEmptyBuildListBySize(1, 1, 1);
    }
    
    // 5 projects, each with 1 plan
    @Test
    public void testGetCDResultWithSmallProjectSetAndOnePlanBySize() {
    	testWithEmptyBuildListBySize(5, 1, 5);
    }
    
    // 25 projects, each with 1 plan
    @Test
    public void testGetCDResultWithBiggerProjectSetAndOnePlanBySize() {
    	testWithEmptyBuildListBySize(25, 1, 25);
    }
    
    // 1 projects, each with 5 plans
    @Test
    public void testGetCDResultsWithOneProjectAndSmallPlanListBySize() {
    	testWithEmptyBuildListBySize(1, 5, 5);
    }
    
    // 4 projects, each with 5 plans
    @Test
    public void testGetCDResultsWithSmallProjectSetAndSmallPlanListBySize() {
    	testWithEmptyBuildListBySize(4, 5, 20);
    }
    
    // 20 projects, each with 5 plans
    @Test
    public void testGetCDResultsWithBiggerProjectSetAndSmallPlanListBySize() {
    	testWithEmptyBuildListBySize(20, 5, 100);
    }
    
    // 1 projects, each with 30 plans
    @Test
    public void testGetCDResultsWithOneProjectAndBiggerPlanListBySize() {
    	testWithEmptyBuildListBySize(1, 30, 30);
    }
    
    // 5 projects, each with 30 plans
    @Test
    public void testGetCDResultsWithSmallProjectSetAndBiggerPlanListBySize() {
    	testWithEmptyBuildListBySize(5, 30, 150);
    }
    
    // 20 projects, each with 30 plans
    @Test
    public void testGetCDResultsWithBiggerProjectSetAndBiggerPlanListBySize() {
    	testWithEmptyBuildListBySize(20, 30, 600);
    }
    
    
    // ========== Private Helper Methods =========
    
    // test for different project-plan sizes with empty build list
    private void testWithEmptyBuildListBySize(int numProjects, int numPlans, int expected) {
    	Set<Project> projects = createNProjects(numProjects);
    	createProjectManager(projects);
    	Map<Project, List<TopLevelPlan>> map = new HashMap<Project, List<TopLevelPlan>>();
    	for (Project pj : projects) {
    		List<TopLevelPlan> plans = createSmallPlanList(pj, numPlans);
    		map.put(pj, plans);
    	}
    	createPlanManager(projects, map);
    	createEmptyResultsSummaryManager(map);
    	main = new MainManager(projectMgr, planMgr, resultsSumMgr);
    	assertEquals("The count of CDResult list does not match", expected, main.getCDResults().size());
    }
    
    // create a set of N projects
    private Set<Project> createNProjects(int N) {
    	Set<Project> projects = new HashSet<Project>();
    	for (int i = 0; i < N; i++) {
    		Project p = mock(Project.class);
    		when(p.getName()).thenReturn("Project " + i);
    		projects.add(p);
    	}
        
        return projects;
    }
    
    // create the projectMgr with the given set of projects
    private void createProjectManager(Set<Project> projects) {
    	projectMgr = mock(ProjectManager.class);
    	when(projectMgr.getAllProjects()).thenReturn(projects);
    }
    
    // create a list of N plans for the given project
    private List<TopLevelPlan> createSmallPlanList(Project project, int N) {
    	List<TopLevelPlan> plans = new ArrayList<TopLevelPlan>();
		String projName = project.getName();
    	for (int i = 0; i < N; i++) {
    		TopLevelPlan plan = mock(TopLevelPlan.class);
        	when(plan.getName()).thenReturn(projName + " - Plan " + i);
        	plans.add(plan);
    	}
    	
    	return plans;
    }
    
    // create the planMgr for the given project-plan-map
    private void createPlanManager(Set<Project> projs, Map<Project, List<TopLevelPlan>> map) {
    	planMgr = mock(PlanManager.class);
    	for (Project p : projs) {
    		when(planMgr.getAllPlansByProject(p, TopLevelPlan.class)).thenReturn(map.get(p));
    	}
    }
    
    // create the resultsSumMgr on the given project-plan-map that returns empty build lists
    private void createEmptyResultsSummaryManager(Map<Project, List<TopLevelPlan>> map) {
    	resultsSumMgr = mock(ResultsSummaryManager.class);
    	for (Project pj : map.keySet()) {
    		for (TopLevelPlan pl : map.get(pj)) {
    			when(resultsSumMgr.getResultSummariesForPlan(pl, 0, 0)).thenReturn(new ArrayList<ResultsSummary>());
    		}
    	}
    }
}