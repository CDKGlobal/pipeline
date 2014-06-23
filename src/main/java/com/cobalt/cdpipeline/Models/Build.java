package com.cobalt.cdpipeline.Models;

import java.util.Date;

public class Build {
	private String buildKey;
	private int buildNumber;
	private Date completeTime;
	
	public Build(String buildKey, int buildNumber, Date completeTime){
		this.buildKey = buildKey;
		this.buildNumber = buildNumber;
		this.completeTime = completeTime;
	}

	public String getBuildKey() {
		return buildKey;
	}

	public int getBuildNumber() {
		return buildNumber;
	}

	public Date getCompleteTime() {
		return completeTime;
	}

}
