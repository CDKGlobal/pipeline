package com.cobalt.cdpipeline.cdresult;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

import com.atlassian.bamboo.author.Author;
import com.atlassian.bamboo.chains.ChainResultsSummary;
import com.atlassian.bamboo.chains.ChainStageResult;
import com.atlassian.bamboo.commit.Commit;
import com.atlassian.bamboo.resultsummary.BuildResultsSummary;
import com.atlassian.bamboo.resultsummary.ResultsSummary;

public class CDResultFactory {
	private CDResult cdresult;
	private List<ResultsSummary> buildList;
	
	public CDResultFactory(String projectName, String planName, List<ResultsSummary> buildList) {
		cdresult = new CDResult(projectName, planName);
		this.buildList = buildList;
	}
	
	public CDResult createCDResult() {
		setLastDeploymentInfo();
		setCurrentBuildInfo();
		
		return cdresult;
	}
	
	private void setLastDeploymentInfo() {
		int totalChanges = 0;		
		// Find the last completed build in buildList
		int totalBuilds = buildList.size();
		int currBuildNum = 1; 	// skip the curr build
		ResultsSummary currBuild = null;
		cdresult.setLastDeploymentTime(null);
		
		while (currBuildNum < totalBuilds) { 
			currBuild = buildList.get(currBuildNum);	
			// check completed
			ChainResultsSummary crs = (ChainResultsSummary) currBuild;
			if (!crs.isContinuable() && crs.isSuccessful()) {
				cdresult.setLastDeploymentTime(currBuild.getBuildCompletedDate()); 
				break;
			}		
			List<Commit> commits = currBuild.getCommits();
			int changesInCurrBuild = currBuild.getCommits().size();			
			// update the 2 counts
			totalChanges += changesInCurrBuild;
			addAllAuthorsInCommits(commits);
			
			currBuildNum++;
		}			
		// set #changes (contributors and date are set in the progress)
		cdresult.setNumChanges(totalChanges + buildList.get(0).getCommits().size());	
	}
	
	private void setCurrentBuildInfo() {
		ResultsSummary currentResult = buildList.get(0);
		Date lastUpdate = currentResult.getBuildCompletedDate();
		this.cdresult.setLastUpdateTime(lastUpdate);
		String buildKey = currentResult.getBuildKey();
		int buildNum = currentResult.getBuildNumber();
		Build currentBuild = new Build(buildKey, buildNum, lastUpdate);
		this.cdresult.setCurrentBuild(currentBuild);
		
		ChainResultsSummary pipeline = (ChainResultsSummary) currentResult;
		setPipelineStages(pipeline);
	}
	
	private void addAllAuthorsInCommits(List<Commit> commits) {
		for(Commit c : commits){
			Author author = c.getAuthor();
			Contributor contributor = new Contributor(author.getFullName());
			this.cdresult.addContributor(contributor);
		}
	}
	
	/*
	 * Set the list of PipelineStage in cdresult with the given build result.
	 */
	private void setPipelineStages(ChainResultsSummary buildResult) {
		List<ChainStageResult> stages = buildResult.getStageResults();

		for (ChainStageResult stageResult : stages) {
			PipelineStage stage = new PipelineStage(stageResult.getName(), stageResult.getState());
			cdresult.addPipelineStageToList(stage);
		}
	}
}
