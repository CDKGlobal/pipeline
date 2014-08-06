package com.cobalt.bamboo.plugin.pipeline.cdperformance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class CDPerformance {
	private int totalBuild;
	private int totalSuccess;
	private int numChanges;
	private Date startDate;
	private Date lastCompletionDate;
	private List<CompletionStats> completions;
	private int totalDays;
	
	public CDPerformance(int totalBuild, int totalSuccess, int numChanges, Date startDate, Date lastCompletionDate, List<CompletionStats> completions){
		this.totalBuild = totalBuild;
		this.totalSuccess = totalSuccess;
		this.numChanges = numChanges;
		this.startDate = new Date(startDate.getTime());
		this.lastCompletionDate = new Date(lastCompletionDate.getTime());
		this.completions = new ArrayList<CompletionStats>(completions);
		this.totalDays = Math.round((lastCompletionDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
	}
	
	/**
	 * Return success percentage calculated by total successes / total builds
	 * @return success percentage of this performance statistics
	 */
	public double getSuccessPercentage(){
		return totalSuccess * 1.0 / totalBuild;
	}
	
	/**
	 * Return average changes from the creation of this plan to the recent completion,
	 * calculated by total changes / total completions
	 * @return average changes between completions
	 */
	public double getAverageChanges() {
		return numChanges * 1.0 / completions.size();
	}
	
	/**
	 * Return average frequency of completion from the creation of this plan to the
	 * recent completion in days, calculated by total days / total completions
	 * @return average days between completions
	 */
	public double getAverageFrequency() {
		return totalDays * 1.0 / completions.size();
	}
	
	/**
	 * Return all completions of this performance statistics
	 * @return list of completions of this performance statistics
	 */
	public List<CompletionStats> getCompletions() {
		return Collections.unmodifiableList(completions);
	}
}
