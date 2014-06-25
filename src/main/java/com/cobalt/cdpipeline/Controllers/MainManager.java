package com.cobalt.cdpipeline.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.atlassian.bamboo.plan.Plan;
import com.atlassian.bamboo.plan.PlanManager;
import com.atlassian.bamboo.plan.TopLevelPlan;
import com.atlassian.bamboo.project.Project;
import com.atlassian.bamboo.project.ProjectManager;
import com.atlassian.bamboo.resultsummary.ResultsSummary;
import com.atlassian.bamboo.resultsummary.ResultsSummaryManager;
import com.cobalt.cdpipeline.Models.CDResult;

public class MainManager {
	private ProjectManager projectManager;
	private PlanManager planManager;
	private ResultsSummaryManager resultsSummaryManager;
	
	public MainManager(ProjectManager projectManager, PlanManager planManager, 
			                       ResultsSummaryManager resultsSummaryManager) {
		this.projectManager = projectManager;
		this.planManager = planManager;
		this.resultsSummaryManager = resultsSummaryManager;
	}
	
	public List<CDResult> getCDResults() {
		List<CDResult> resultList = new ArrayList<CDResult>();
		
		Set<Project> projects = projectManager.getAllProjects();
		for (Project project : projects) {
			String projectName = project.getName();
			
			List<TopLevelPlan> plans = planManager.getAllPlansByProject(project, TopLevelPlan.class);
			for (Plan plan : plans) {
				String planName = plan.getName();
				List<ResultsSummary> resultSummaryList = resultsSummaryManager.getResultSummariesForPlan(plan, 0, 0);
				
				CDResultFactory factory = new CDResultFactory();
				CDResult result = factory.createCDResult(projectName, planName, resultSummaryList);
				resultList.add(result);
			}
		}
		
		return resultList;
	}
}
