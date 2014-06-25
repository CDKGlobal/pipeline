package com.cobalt.cdpipeline.cdresult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;;

/**
 * 
 */
public class CDResult {
	private String projectName, planName;
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
	public CDResult (String projectName, String planName) {
		contributors = new ArrayList<Contributor>();
		pipelineStages = new ArrayList<PipelineStage>();

		this.projectName = projectName;
		this.planName = planName;
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
}
