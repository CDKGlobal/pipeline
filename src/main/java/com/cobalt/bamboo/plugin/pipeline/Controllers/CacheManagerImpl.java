package com.cobalt.bamboo.plugin.pipeline.Controllers;

import java.util.List;

import com.atlassian.bamboo.plan.PlanKey;
import com.cobalt.bamboo.plugin.pipeline.cdresult.CDResult;

public class CacheManagerImpl implements CacheManager {
	private final MainManager mainManager;
	// TODO cache field
	
	public CacheManagerImpl(MainManager mainManager){
		this.mainManager = mainManager;
		
		// put all data from mainManager.getCDResult() into the cache (first load)
	}
	
	@Override
	public List<CDResult> getCDResults() { // TODO: change it!
		return mainManager.getCDResults();
	}

	@Override
	public void putAllCDResult() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void putCDResultForPlan(PlanKey planKey, CDResult cdresult) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<CDResult> getAllCDResult() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearCache() {
		// TODO Auto-generated method stub
		
	}

}
