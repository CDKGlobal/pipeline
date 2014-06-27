package com.cobalt.cdpipeline.Controllers;

import com.atlassian.bamboo.plan.Plan;
import com.atlassian.bamboo.plan.PlanManager;
import com.atlassian.bamboo.plan.TopLevelPlan;
import com.atlassian.bamboo.project.Project;
import com.atlassian.bamboo.project.ProjectManager;
import com.atlassian.bamboo.resultsummary.ResultsSummary;
import com.atlassian.bamboo.resultsummary.ResultsSummaryManager;
import com.cobalt.cdpipeline.cdresult.CDResult;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

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

    /*
    @Test
    public void testDataIsWhatWeExpect() {
        Project project1 = mock(Project.class);
        Project project2 = mock(Project.class);

        when(project1.getName()).thenReturn("Project 1");
        when(project2.getName()).thenReturn("Project 2");

        Set<Project> projects = new HashSet<Project>();
        projects.add(project1);
        projects.add(project2);

        ProjectManager manager = mock(ProjectManager.class);
        when(manager.getAllProjects()).thenReturn(projects);

        //MainManager cut = ;
        int expectedCount = 2;
        int actualCount = manager.getAllProjects().size();
        assertEquals("The count was not what we expected", expectedCount, actualCount);
    }
    */
    
    @Test
    public void testGetCDResultsWithSmallSetAndEmptyBuildListBySize() {
    	Set<Project> projects = createSmallProjectSet();
    	createProjectManager(projects);
    	Map<Project, List<TopLevelPlan>> map = new HashMap<Project, List<TopLevelPlan>>();
    	for (Project pj : projects) {
    		List<TopLevelPlan> plans = createSmallPlanList(pj);
    		map.put(pj, plans);
    	}
    	createPlanManager(projects, map);
    	createEmptyResultsSummaryManager(map);
    	main = new MainManager(projectMgr, planMgr, resultsSumMgr);
    	assertEquals("The count of CDResult list does not match", 4, main.getCDResults().size());
    }
    
    
    // ========== Private Helper Methods =========
    
    // create a set of 2 projects
    private Set<Project> createSmallProjectSet() {
    	Project project1 = mock(Project.class);
    	Project project2 = mock(Project.class);
    	
    	when(project1.getName()).thenReturn("Project 1");
        when(project2.getName()).thenReturn("Project 2");
        
        Set<Project> projects = new HashSet<Project>();
        projects.add(project1);
        projects.add(project2);
        
        return projects;
    }
    
    // create the projectMgr with the given set of projects
    private void createProjectManager(Set<Project> projects) {
    	projectMgr = mock(ProjectManager.class);
    	when(projectMgr.getAllProjects()).thenReturn(projects);
    }
    
    // create a list of 2 plans for the given project
    private List<TopLevelPlan> createSmallPlanList(Project project) {
    	TopLevelPlan plan1 = mock(TopLevelPlan.class);
    	TopLevelPlan plan2 = mock(TopLevelPlan.class);
    	
    	String projName = project.getName();
    	when(plan1.getName()).thenReturn(projName + " - Plan 1");
    	when(plan2.getName()).thenReturn(projName + " - Plan 2");
    	
    	List<TopLevelPlan> plans = new ArrayList<TopLevelPlan>();
    	plans.add(plan1);
    	plans.add(plan2);
    	
    	return plans;
    }
    
    private void createPlanManager(Set<Project> projs, Map<Project, List<TopLevelPlan>> map) {
    	planMgr = mock(PlanManager.class);
    	for (Project p : projs) {
    		when(planMgr.getAllPlansByProject(p, TopLevelPlan.class)).thenReturn(map.get(p));
    	}
    }
    
    private void createEmptyResultsSummaryManager(Map<Project, List<TopLevelPlan>> map) {
    	resultsSumMgr = mock(ResultsSummaryManager.class);
    	for (Project pj : map.keySet()) {
    		for (TopLevelPlan pl : map.get(pj)) {
    			when(resultsSumMgr.getResultSummariesForPlan(pl, 0, 0)).thenReturn(new ArrayList<ResultsSummary>());
    		}
    	}
    }
}