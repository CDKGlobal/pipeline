package com.cobalt.bamboo.plugin.pipeline.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cobalt.bamboo.plugin.pipeline.cdresult.CDResult;

public class CDResultsCache {
	private Map<String, CDResult> cache;
	
	public CDResultsCache() {
		cache = new HashMap<String, CDResult>();
	}
	
	public void put(String planKey, CDResult cdResult) {
		cache.put(planKey, cdResult);
	}
	
	public boolean isEmpty() {
		return cache.isEmpty();
	}
	
	public List<CDResult> getAllCDResults() {
		List<CDResult> resultList = new ArrayList<CDResult>();
		for (String planKey : cache.keySet()) {
			resultList.add(cache.get(planKey));
		}
		return resultList;
	}
	
	public void clear() {
		cache.clear();
	}
}
