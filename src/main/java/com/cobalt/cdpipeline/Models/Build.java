package com.cobalt.cdpipeline.Models;

import java.util.Date;

public class Build {
	private String buildKey;
	private int buildNumber;
	private Date completeTime;
	
	/**
	 * Constructs a Build with given params
	 * @param buildKey
	 * @param buildNumber
	 * @param completeTime
	 */
	public Build(String buildKey, int buildNumber, Date completeTime){
		this.buildKey = buildKey;
		this.buildNumber = buildNumber;
		this.completeTime = completeTime;
	}

	/**
	 * Returns the build key of this build
	 * @return build key
	 */
	public String getBuildKey() {
		return buildKey;
	}

	/**
	 * Returns the build number of this build
	 * @return build number
	 */
	public int getBuildNumber() {
		return buildNumber;
	}

	/**
	 * Returns the completed time of this build
	 * @return completed time
	 */
	public Date getCompleteTime() {
		return completeTime;
	}

}
