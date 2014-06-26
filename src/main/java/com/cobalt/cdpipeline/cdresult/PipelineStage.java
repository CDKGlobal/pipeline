package com.cobalt.cdpipeline.cdresult;

import com.atlassian.bamboo.builder.BuildState;

/**
 * Represents a single stage in the pipeline.
 */
public class PipelineStage {
	public static final BuildState STATE_FAILED = BuildState.FAILED;
	public static final BuildState STATE_UNKNOWN = BuildState.UNKNOWN;
	public static final BuildState STATE_SUCCESSFUL = BuildState.SUCCESS;
	
	private String stageName;
	private BuildState state;
	
	/**
	 * Constructs a PipelineStage object.
	 * 
	 * @param stageName name of this pipeline stage
	 * @param state state of this pipeline stage in the build
	 */
	public PipelineStage(String stageName, BuildState state) {
		this.stageName = stageName;
		this.state = state;
	}
	
	/**
	 * Gets the stage name of this pipeline stage
	 * 
	 * @return the stageName
	 */
	public String getStageName() {
		return stageName;
	}
	
	/**
	 * Gets the state of this pipeline stage.
	 * To get String representation of the state, use toString().
	 * To determine the state, compare state to STATE_FAILED, STATE_UNKNOWN,
	 * or STATE_SUCCESSFUL.
	 * 
	 * @return the state, which can be STATE_FAILED, STATE_UNKNOWN, or STATE_SUCCESSFUL.
	 */
	public BuildState getState() {
		return state;
	}
}
