package com.cobalt.cdpipeline.cdresult;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

import com.atlassian.bamboo.author.Author;
import com.atlassian.bamboo.chains.ChainResultsSummary;
import com.atlassian.bamboo.commit.Commit;
import com.atlassian.bamboo.resultsummary.BuildResultsSummary;
import com.atlassian.bamboo.resultsummary.ResultsSummary;

public class CDResultFactory {
	private CDResult cdresult;
	private List<BuildResultsSummary> buildList;
	
	public CDResultFactory(String projectName, String planName, List<BuildResultsSummary> buildList) {
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
		Set<Contributor> contributors = new HashSet<Contributor>();
		
		// TODO
	}
	
	private void setCurrentBuildInfo() {
		BuildResultsSummary currentResult = buildList.get(0);
		Date lastUpdate = currentResult.getBuildCompletedDate();
		this.cdresult.setLastUpdateTime(lastUpdate);
		String buildKey = currentResult.getBuildKey();
		int buildNum = currentResult.getBuildNumber();
		Build currentBuild = new Build(buildKey, buildNum, lastUpdate);
		this.cdresult.setCurrentBuild(currentBuild);
		ChainResultsSummary pipeline = currentResult.getChainResultsSummary();
		setPipelineStages(pipeline);
	}
	
	private void addAllAuthorsInCommits(List<Commit> commits) {
		for(Commit c : commits){
			Author author = c.getAuthor();
			Contributor contributor = new Contributor(author.getFullName());
			this.cdresult.addContributor(contributor);
		}
	}
	
	private void setPipelineStages(ChainResultsSummary pipeline) {
		// TODO
	}
}
