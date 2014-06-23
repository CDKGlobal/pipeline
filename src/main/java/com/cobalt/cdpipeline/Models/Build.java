package com.cobalt.cdpipeline.Models;

import org.joda.time.DateTime;

public class Build {
	private String buildKey;
	private int buildNumber;
	private DateTime completeTime;
	
	public Build(String buildKey, int buildNumber, DateTime completeTime){
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

	public DateTime getCompleteTime() {
		return completeTime;
	}

}
