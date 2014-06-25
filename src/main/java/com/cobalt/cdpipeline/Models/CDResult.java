package com.cobalt.cdpipeline.Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;;

/**
 * 
 */
public class CDResult {
	private String projectName, planName, planKey;
	public Date completedTime, lastUpdate;
	public int changes;
	
	public List<Contributor> contributors;
	public Build currentBuild;
	public List<PipelineStage> pipelineStages; 
	
	/**
	 * Construct a Plan object 
	 * @param projectName
	 * @param planName
	 * @param planKey
	 */
	public CDResult (String projectName, String planName, String planKey) {
		contributors = new ArrayList<Contributor>();
		pipelineStages = new ArrayList<PipelineStage>();

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
