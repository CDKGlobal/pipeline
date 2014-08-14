package com.cobalt.bamboo.plugin.pipeline.cdperformance;

import java.util.Date;
import java.util.List;

import com.atlassian.bamboo.resultsummary.ResultsSummary;

public class UptimeGradeFactory {

	/**
	 * Return a UptimeGrade generated based on the given buildList, ignoring all
	 * builds that in queue but never started yet.
	 * @param buildList to calculate the uptime grade with
	 * @return UptimeGrade based on the given buildList. Return null if the given
	 *         buildList is null or it's empty.
	 */
	public static UptimeGrade createUptimeGrade(List<ResultsSummary> buildList) {
		if(buildList == null || buildList.size() <= 0){
			return null;
		}
		
		// Set startDate to be the first build completed time.
		// If it's not completed, use the queued time.
		Date startDate = buildList.get(buildList.size() - 1).getBuildCompletedDate();
		if(startDate == null){
			startDate = buildList.get(buildList.size() - 1).getQueueTime();
		}
		
		// Find the most recent completed build.
		Date currentBuildDate = null;
		boolean currentBuildState = false;
		int currentBuildNum = 0;
		while(currentBuildDate == null){
			currentBuildDate = buildList.get(currentBuildNum).getBuildCompletedDate();
			currentBuildState = buildList.get(currentBuildNum).isSuccessful();
			currentBuildNum++;
		}
		
		// Calculate the uptime starting from the current build.
		long totalUptime = 0;
		long lastBuildTime = buildList.get(currentBuildNum).getBuildCompletedDate().getTime();
		for(int i = currentBuildNum; i < buildList.size(); i++){
			ResultsSummary currentBuild = buildList.get(i);
			Date currentBuildCompletedDate = currentBuild.getBuildCompletedDate();
			if(currentBuildCompletedDate != null){
				long currentBuildTime = currentBuild.getBuildCompletedDate().getTime();
				if(currentBuild.isSuccessful()){
					totalUptime += lastBuildTime - currentBuildTime;
				}
				lastBuildTime = currentBuildTime;
			}
		}
		
		return new UptimeGrade(startDate, totalUptime, currentBuildState, currentBuildDate);
	}
}
