package com.cobalt.bamboo.plugin.pipeline.Controllers;

import java.util.List;

import com.cobalt.bamboo.plugin.pipeline.cdresult.CDResult;

public class CacheManagerImpl implements CacheManager {
	private final MainManager mainManager;
	
	public CacheManagerImpl(MainManager mainManager){
		this.mainManager = mainManager;
	}
	
	@Override
	public List<CDResult> getCDResults() { // TODO: change it!
		return mainManager.getCDResults();
	}

}
