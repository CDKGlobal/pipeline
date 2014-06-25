package com.cobalt.cdpipeline.cdresult;

import java.util.HashSet;
import java.util.Set;
import java.util.List;

import com.atlassian.bamboo.chains.ChainResultsSummary;
import com.atlassian.bamboo.commit.Commit;
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
		Set<Contributor> contributors = new HashSet<Contributor>();
		
		// TODO
	}
	
	private void setCurrentBuildInfo() {
		// TODO
	}
	
	private void addAllAuthorsInCommits(List<Commit> commits, Set<Contributor> contributors) {
		// TODO
	}
	
	private void setPipelineStages(ChainResultsSummary pipeline) {
		// TODO
	}
}
