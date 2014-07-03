package com.cobalt.cdpipeline.cdresult;

import java.util.Date;

public class Build {
	private String buildKey;
	private int buildNumber;
	
	/**
	 * Constructs a Build with given params
	 * @param buildKey
	 * @param buildNumber
	 */
	public Build(String buildKey, int buildNumber){
		this.buildKey = buildKey;
		this.buildNumber = buildNumber;
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

}
