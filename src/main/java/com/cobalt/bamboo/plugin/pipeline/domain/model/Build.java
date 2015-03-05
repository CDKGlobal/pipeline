package com.cobalt.bamboo.plugin.pipeline.domain.model;

import com.atlassian.bamboo.chains.ChainResultsSummary;
import com.atlassian.bamboo.progressbar.ProgressBar;

import java.util.Date;

public class Build {
	private ChainResultsSummary buildResult;
	private ProgressBar progressBar;

	/**
	 * Constructs a Build object
	 * 
	 * @param buildResult from Bamboo to construct off of
	 * @param progressBar for builds that are currently building
	 */
	public Build(ChainResultsSummary buildResult, ProgressBar progressBar){
		this.buildResult = buildResult;
		this.progressBar = progressBar;
	}

	/**
	 * Returns the build key of this build.
	 * Returns null when there are no build yet.
	 * 
	 * @return build key
	 */
	public String getBuildKey() {
		if (buildResult == null) {
			return null;
		} else {
			return buildResult.getBuildResultKey();
		}
	}

	/**
	 * Returns the build number of this build.
	 * Returns -1 when there are no build yet.
	 * 
	 * @return build number
	 */
	public int getBuildNumber() {
		if (buildResult == null) {
			return -1;
		} else {
			return buildResult.getBuildNumber();
		}
	}

	/**
	 * Get the CDPipelineState based on the state of buildResult, but only
	 * includes the states that are to the interest of our users, of this pipeline stage.
	 * 
	 * @return the CDPipelineState, which can be CD_SUCCESS, CD_FAILED, CD_IN_PROGRESS, 
	 * 			CD_NOT_BUILT, CD_MANUALLY_PAUSED
	 */
    public PipelineState getCDPipelineState() {
        if (buildResult == null) {
            return PipelineState.CD_NOT_BUILT;
        } else {
            if (buildResult.isSuccessful()) {
				if (buildResult.isContinuable()) {
                    return PipelineState.CD_MANUALLY_PAUSED;
                } else {
                    return PipelineState.CD_SUCCESS;
                }
            } else if (buildResult.isFailed()) {
                return PipelineState.CD_FAILED;
            } else if (buildResult.isInProgress() || getPercentageCompleted() > 0) {
                return PipelineState.CD_IN_PROGRESS;
            } else if (buildResult.isQueued()) {
                return PipelineState.CD_QUEUED;
            } else {
                return PipelineState.CD_NOT_BUILT;
            }
        }
	}
	
	/**
	 * Get the percentage completed if this Build is currently building.
	 * 
	 * @return percentage completed in double format if this Build is currently building,
	 *         -1 otherwise.
	 */
	public double getPercentageCompleted() {
		if (progressBar != null) {
			return progressBar.getPercentageCompleted();
		}
		return -1;
	}
	
	/**
	 * Get the time remaining if this Build is currently building.
	 * 
	 * @return time remaining in pretty string format if this Build is currently building,
	 *         null otherwise.
	 */
	public String getTimeRemaining() {
		if (progressBar != null) {
			return progressBar.getPrettyTimeRemaining(false);
		}
		return null;
	}
	
	/**
	 * Get the completed date of this build.
	 * 
	 * @return the completed Date of this build.
	 */
	public Date getBuildCompletedDate() {
		if(buildResult == null){
			return null;
		}
		return buildResult.getBuildCompletedDate();
	}
	
	/**
	 * Return whether this build is successful.
	 * 
	 * @return true if this build is successful, false otherwise.
	 */
	public boolean isSuccessful() {
		if(buildResult == null){
			return false;
		}
		return buildResult.isSuccessful();
	}
}
