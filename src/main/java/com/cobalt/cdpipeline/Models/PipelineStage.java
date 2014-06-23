package com.cobalt.cdpipeline.Models;

public class PipelineStage {
	// states
	public static final int FAILED = 0;
	public static final int UNKNOWN = 1;
	public static final int SUCCESSFUL = 2;
	
	private String stageName;
	private int state;
	
	public PipelineStage(String stageName, String state) {
		this.stageName = stageName;
		
		if (state.equals("Failed"))
			this.state = PipelineStage.FAILED;
		else if (state.equals("Unknown"))
			this.state = PipelineStage.UNKNOWN;
		else
			this.state = PipelineStage.SUCCESSFUL;
	}
	
	public String getStageName() {
		return stageName;
	}
	
	public int getState() {
		return state;
	}
}
