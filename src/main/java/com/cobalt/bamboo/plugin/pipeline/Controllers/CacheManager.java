package com.cobalt.bamboo.plugin.pipeline.Controllers;

import java.util.List;

import com.cobalt.bamboo.plugin.pipeline.cdresult.CDResult;

public interface CacheManager {

	/**
	 * Get all the results needed for displaying the CDPipeline table.
	 * 
	 * @return a list of CDResults, where each CDResult represents one row.
	 *         See CDResults for more details.
	 */
	public List<CDResult> getCDResults();
}
