package com.cobalt.cdpipeline.Models;

/**
 * 
 *
 */
public class PipelineStage {
	public static final int FAILED = 0;
	public static final int UNKNOWN = 1;
	public static final int SUCCESSFUL = 2;
	
	private String stageName;
	private int state;
	
	/**
	 * Constructs a PipelineStage object.
	 * 
	 * @param stageName name of this pipeline stage
	 * @param state state of this pipeline stage in the build
	 */
	public PipelineStage(String stageName, String state) {
		this.stageName = stageName;
		
		if (state.equals("Failed"))
			this.state = PipelineStage.FAILED;
		else if (state.equals("Successful"))
			this.state = PipelineStage.SUCCESSFUL;
		else
			this.state = PipelineStage.UNKNOWN;
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
	 * 
	 * @return the state, which can be FAILED, UNKNOWN, or SUCCESSFUL.
	 */
	public int getState() {
		return state;
	}
}
