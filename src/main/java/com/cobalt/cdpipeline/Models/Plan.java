package com.cobalt.cdpipeline.Models;

import java.util.Date;

/**
 * 
 */
public class Plan {
	private String projectName, planName, planKey;
	public Date completedTime, lastUpdate;
	public int changes;
	
	public Contributor[] contributors;
	public Build currentBuild;
	public PipelineStage[] PipelineStages; 
	
	/**
	 * Construct a Plan object 
	 * @param projectName
	 * @param planName
	 * @param planKey
	 */
	public Plan (String projectName, String planName, String planKey) {
		this.projectName = projectName;
		this.planName = planName;
		this.planKey = planKey;
	}
	
	/**
	 * Get project name
	 * @return
	 */
	public String getProjectName() {
		return projectName;
	}
	
	/**
	 * Get plan name
	 * @return
	 */
	public String getPlanName() {
		return planName;
	}
	
	/**
	 * Get plan key
	 * @return
	 */
	public String getPlanKey() {
		return planKey;
	}
	
}
