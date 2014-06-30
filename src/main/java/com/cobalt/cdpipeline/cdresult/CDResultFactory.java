package com.cobalt.cdpipeline.cdresult;

import java.util.Date;
import java.util.List;

import com.atlassian.bamboo.author.Author;
import com.atlassian.bamboo.chains.ChainResultsSummary;
import com.atlassian.bamboo.chains.ChainStageResult;
import com.atlassian.bamboo.commit.Commit;
import com.atlassian.bamboo.resultsummary.ResultsSummary;

public class CDResultFactory {
	protected CDResult cdresult;
	private List<ResultsSummary> buildList;
	
	/**
	 * Constructs an CDResultFactory object.
	 * 
	 * @param projectName The name of the project this CDResult is associated with.
	 * @param planName The name of the plan this CDResult is associated with. planName
	 *        should be in the format of "[project] - [plan]".
	 * @param buildList The list of build this CDResult is associated with.
	 * @throws IllegalArgumentException if (projectName == null || planName == null || 
	 *         buildList == null || !planName.startWith(projectName + " - "))
	 */
	public CDResultFactory(String projectName, String planName, List<ResultsSummary> buildList) {
		if (projectName == null || planName == null || buildList == null
				|| !planName.startsWith(projectName + " - ")) {
			throw new IllegalArgumentException("Passed in null arguments or invalid plan name."
					+ "(plan name should be in the format of \"[project] - [plan]\"");
		}
		
		// planName is in the format of "[project] - [plan]"
		// Strip planName so that it contains purely the plan's name.
		String strippedPlanName = planName.substring(projectName.length() + 3);
		
		cdresult = new CDResult(projectName, strippedPlanName);
		this.buildList = buildList;
	}
	
	/**
	 * Return a CDResult with project name, plan name, days, changes, contributors info since
	 * last deployment and current build information (name, last update time, pipeline stages
	 * with status) based on the given projectName, planName and all builds when construct.
	 * 
	 * @return
	 */
	public CDResult createCDResult() {
		setLastDeploymentInfo();
		setCurrentBuildInfo();
		
		return cdresult;
	}
	
	/*
	 * Set the lastDeploymentTime, numChanges, and contributors info since last
	 * deployment in the cdresult. 
	 * If there are no builds, cdresult will maintain the default values. 
	 * If there are no last deployment, lastDeploymentTime will be default, and 
	 * changes and contributors will be since the first build.
	 */
	protected void setLastDeploymentInfo() {
		// don't need to set anything if there are no builds at all
		if(buildList == null || buildList.size() <= 0){
			return;
		}
		
		// At this point: at least one build in the build list
		
		int totalChanges = 0;
		
		// add changes and contributors of the first build
		// into cdresult
		totalChanges += buildList.get(0).getCommits().size();
		addAllAuthorsInCommits(buildList.get(0).getCommits());
		
		for (int i = 1; i < buildList.size(); i++) { 
			ChainResultsSummary currentBuild = (ChainResultsSummary) buildList.get(i);	
			
			// check if current build is the last deployment
			if (!currentBuild.isContinuable() && currentBuild.isSuccessful()) {
				cdresult.setLastDeploymentTime(currentBuild.getBuildCompletedDate()); 
				break;
			}
			
			List<Commit> commits = currentBuild.getCommits();
			totalChanges += commits.size();
			addAllAuthorsInCommits(commits);
		}
		
		// set #changes (contributors and date are set in the progress)
		cdresult.setNumChanges(totalChanges);
	}
	
	/*
	 * Set the current build information (build number, build key...), last build
	 * updated time and all pipeline stages.
	 * Current build may be null if there's no builds in build list.
	 * last build updated time may be null if it hasn't finished the first stage.
	 */
	protected void setCurrentBuildInfo() {
		if(buildList != null && buildList.size() > 0){
			
			// get the last build and set current build info.
			ResultsSummary currentResult = buildList.get(0);
			Date lastUpdate = currentResult.getBuildCompletedDate();
			this.cdresult.setLastUpdateTime(lastUpdate);
			String buildKey = currentResult.getBuildKey();
			int buildNum = currentResult.getBuildNumber();
			Build currentBuild = new Build(buildKey, buildNum, lastUpdate);
			this.cdresult.setCurrentBuild(currentBuild);
			
			// set the pipeline stages.
			ChainResultsSummary pipeline = (ChainResultsSummary) currentResult;
			setPipelineStages(pipeline);
		}
	}
	
	/*
	 * Add all contributors of the given commits to the contributors list.
	 */
	protected void addAllAuthorsInCommits(List<Commit> commits) {
		for(Commit c : commits){
			Author author = c.getAuthor();
			Contributor contributor = new Contributor(author.getFullName());
			this.cdresult.addContributor(contributor);
		}
	}
	
	/*
	 * Set the list of PipelineStage in cdresult with the given build result.
	 */
	protected void setPipelineStages(ChainResultsSummary buildResult) {
		cdresult.resetPipelineStagesList();
		List<ChainStageResult> stages = buildResult.getStageResults();

		for (ChainStageResult stageResult : stages) {
			PipelineStage stage = new PipelineStage(stageResult.getName(), stageResult.getState());
			cdresult.addPipelineStageToList(stage);
		}
	}
}
