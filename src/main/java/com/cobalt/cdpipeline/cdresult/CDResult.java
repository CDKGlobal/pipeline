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
	 * Construct a CDResult Object.
	 * Access modifier left out intentionally for package protection.
	 * 
	 * @param projectName
	 * @param planName
	 */
	CDResult(String projectName, String planName) {
		this.projectName = projectName;
		this.planName = planName;
		
		contributors = new HashSet<Contributor>();
		pipelineStages = new ArrayList<PipelineStage>();
	}
	
	/**
	 * Set lastDeploymentTime to the given date.
	 * Access modifier left out intentionally for package protection.
	 * 
	 * @param lastDeployment
	 */
	void setLastDeploymentTime(Date lastDeployment) {
		this.lastDeploymentTime = lastDeployment;
	}
	
	/**
	 * Return last deployment date and time.
	 * Return null if there's no deployment yet.
	 * @return
	 */
	public Date getLastDeploymentTime(){
		return this.lastDeploymentTime;
	}
	
	/**
	 * Set lastUpdate time to the given date.
	 * Access modifier left out intentionally for package protection.
	 * 
	 * @param lastUpdate
	 */
	void setLastUpdateTime(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	/**
	 * Return last update date and time
	 * @return
	 */
	public Date getLastUpdateTime(){
		return this.lastUpdate;
	}
	
	/**
	 * Set number of changes to the given number.
	 * Access modifier left out intentionally for package protection.
	 * 
	 * @param numChanges
	 */
	void setNumChanges(int numChanges) {
		this.numChanges = numChanges;
	}
	
	/**
	 * Return changes(commits) since last deployment
	 * @return
	 */
	public int getNumChanges(){
		return this.numChanges;
	}
	
	/**
	 * Add given contributor to contributors set.
	 * Access modifier left out intentionally for package protection.
	 * 
	 * @param contributor
	 */
	void addContributor(Contributor contributor) {
		contributors.add(contributor);
	}
	
	/**
	 * Return all contributors since last deployment
	 * @return
	 */
	public Set<Contributor> getContributors(){
		return this.contributors;
	}
	
	/**
	 * Add given PipelineStage to the pipelineStages list.
	 * Access modifier left out intentionally for package protection.
	 * 
	 * @param stage
	 */
	void addPipelineStageToList(PipelineStage stage) {
		pipelineStages.add(stage);
	}
	
	/**
	 * Return all pipeline stages of the current build.
	 * @return
	 */
	public List<PipelineStage> getPipelineStages(){
		return this.pipelineStages;
	}
	
	/**
	 * Set current build information with the given Build
	 * @param current
	 */
	void setCurrentBuild(Build current){
		this.currentBuild = current;
	}
	
	/**
	 * Return current build information.
	 * @return
	 */
	public Build getCurrentBuild(){
		return this.currentBuild;
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
