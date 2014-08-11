package com.cobalt.bamboo.plugin.pipeline.Controllers;

import java.util.List;

import com.atlassian.bamboo.plan.PlanKey;
import com.cobalt.bamboo.plugin.pipeline.cdresult.CDResult;

public interface CacheManager {

	/**
	 * Get all the results needed for displaying the CDPipeline table.
	 * 
	 * @return a list of CDResults, where each CDResult represents one row.
	 *         See CDResults for more details.
	 */
	public List<CDResult> getCDResults();
	
	/**
	 * Put CDResult for all plans into the cache. All of existing data (if any)
	 * will be replaced.
	 */
	public void putAllCDResult();
	
	/**
	 * Put the given CDResult for the given plan into the cache. If the plan already
	 * exists in the cache, the associated CDResult will be replaced. If the plan
	 * doesn't exist in the cache yet, a new plan will be created with the given CDResult.
	 * 
	 * @param planKey of the plan to put into cache
	 * @param cdresult of the plan to put into cache
	 */
	public void putCDResultForPlan(PlanKey planKey, CDResult cdresult);
	
	/**
	 * Get CDResult for all of the plans.
	 * 
	 * @return a list of CDResult representing all plans.
	 */
	public List<CDResult> getAllCDResult();
	
	/**
	 * Clear everything in the cache.
	 */
	public void clearCache();
}
