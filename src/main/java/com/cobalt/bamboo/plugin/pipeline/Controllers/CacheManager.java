package com.cobalt.bamboo.plugin.pipeline.Controllers;

import java.util.List;

import com.cobalt.bamboo.plugin.pipeline.cdresult.CDResult;

/**
 * CacheManager manages all activities for cache.
 */
public interface CacheManager {
	
	/**
	 * Put CDResult for all plans into the cache. All of existing data (if any)
	 * will be replaced.
	 */
	public void putAllCDResult();
	
	/**
	 * Update the CDResult in the cache for the given plan. If the plan already
	 * exists in the cache, the associated CDResult will be replaced. If the plan
	 * doesn't exist in the cache yet, a new plan will be created with it's new CDResult.
	 * 
	 * @param planKey of the plan to update in the cache
	 */
	public void updateCDResultForPlan(String planKey);
	
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
