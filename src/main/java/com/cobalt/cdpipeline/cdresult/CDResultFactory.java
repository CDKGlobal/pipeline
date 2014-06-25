package com.cobalt.cdpipeline.cdresult;

import java.util.List;

import com.atlassian.bamboo.resultsummary.ResultsSummary;

public class CDResultFactory {
	private CDResult cdresult;
	private List<ResultsSummary> buildList;
	
	public CDResultFactory(String projectName, String planName, List<ResultsSummary> buildList) {
		cdresult = new CDResult(projectName, planName);
		this.buildList = buildList;
	}
	
	public CDResult createCDResult() {
		// TODO
		
		return cdresult;
	}
}
