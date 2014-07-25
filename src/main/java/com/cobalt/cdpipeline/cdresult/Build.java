package com.cobalt.cdpipeline.cdresult;

import com.atlassian.bamboo.chains.ChainResultsSummary;

public class Build {
	private ChainResultsSummary buildResult;
	
	/**
	 * Constructs a Build with given parameters
	 * 
	 * @param buildKey
	 * @param buildNumber
	 */
	public Build(ChainResultsSummary buildResult){
		this.buildResult = buildResult;
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
			return buildResult.getBuildKey();
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
	public CDPipelineState getCDPipelineState() {
		if (buildResult == null) {
			return CDPipelineState.CD_NOT_BUILT;
		} else {
			if (buildResult.isSuccessful()) {
				if (buildResult.isContinuable()) {
					return CDPipelineState.CD_MANUALLY_PAUSED;
				} else {
					return CDPipelineState.CD_SUCCESS;
				}
			} else if (buildResult.isFailed()) {
				return CDPipelineState.CD_FAILED;
			} else if (buildResult.isInProgress()) {
				return CDPipelineState.CD_IN_PROGRESS;
			} else if (buildResult.isQueued()) {
				return CDPipelineState.CD_QUEUED;
			} else {
				return CDPipelineState.CD_NOT_BUILT;
			}
		}
	}
}
