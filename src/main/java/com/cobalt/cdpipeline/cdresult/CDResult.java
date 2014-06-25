package com.cobalt.cdpipeline.cdresult;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 */
public class CDResult {
	private String projectName, planName;
	private Date lastDeploymentTime, lastUpdate;
	private int numChanges;
	
	private Set<Contributor> contributors;
	private Build currentBuild;
	private List<PipelineStage> pipelineStages; 
	
	/**
	 * Construct a Plan object 
	 * @param projectName
	 * @param planName
	 * @param planKey
	 */
	public CDResult(String projectName, String planName) {
		this.projectName = projectName;
		this.planName = planName;
		
		contributors = new HashSet<Contributor>();
		pipelineStages = new ArrayList<PipelineStage>();
	}
	
	/**
	 * Set lastDeploymentTime to the given date.
	 * Scope identifier left out intentionally for package protection.
	 * 
	 * @param lastDeployment
	 */
	void setLastDeploymentTime(Date lastDeployment) {
		this.lastDeploymentTime = lastDeployment;
	}
	
	/**
	 * Set lastUpdate time to the given date.
	 * Scope identifier left out intentionally for package protection.
	 * 
	 * @param lastUpdate
	 */
	void setLastUpdateTime(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	/**
	 * Set number of changes to the given number.
	 * Scope identifier left out intentionally for package protection.
	 * 
	 * @param numChanges
	 */
	void setNumChanges(int numChanges) {
		this.numChanges = numChanges;
	}
	
	/**
	 * Add given contributor to contributors set.
	 * Scope identifier left out intentionally for package protection.
	 * 
	 * @param contributor
	 */
	void addContributorToSet(Contributor contributor) {
		contributors.add(contributor);
	}
	
	/**
	 * Add given PipelineStage to the pipelineStages list.
	 * Scope identifier left out intentionally for package protection.
	 * 
	 * @param stage
	 */
	void addPipelineStageToList(PipelineStage stage) {
		pipelineStages.add(stage);
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
