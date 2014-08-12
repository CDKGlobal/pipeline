package com.cobalt.bamboo.plugin.pipeline.Controllers;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.atlassian.sal.api.transaction.TransactionCallback;
import com.atlassian.sal.api.transaction.TransactionTemplate;
import com.cobalt.bamboo.plugin.pipeline.cache.CDResultsCache;
import com.cobalt.bamboo.plugin.pipeline.cdresult.CDResult;

public class CacheManagerImpl implements CacheManager {
	private final MainManager mainManager;
	private TransactionTemplate transactionTemplate;
	private ReadWriteLock lock;
	CDResultsCache cdResultsCache;
	
	/**
	 * Constructs a CacheManager object.
	 * 
	 * @param mainManager The MainManager to get CDResults from.
	 * @param transactionTemplate The TransactionTemplate to allow access to the Bamboo
	 *                            database from outside of a web request.
	 */
	public CacheManagerImpl(MainManager mainManager, TransactionTemplate transactionTemplate){
		this.mainManager = mainManager;
		this.transactionTemplate = transactionTemplate;
		lock = new ReentrantReadWriteLock();
		cdResultsCache = new CDResultsCache();
	}

	@Override
	public void putAllCDResult() {
		lock.writeLock().lock();
		transactionTemplate.execute(new TransactionCallback() {
			
			@Override
			public Object doInTransaction() {
				unlockedCacheRefresh();
				return null;
			}
		});
		
		lock.writeLock().unlock();
	}
	
	@Override
	public void updateCDResultForPlan(final String planKey) {
		lock.writeLock().lock();
		
		if (cdResultsCache.isEmpty()) {
			transactionTemplate.execute(new TransactionCallback() {
				
				@Override
				public Object doInTransaction() {
					unlockedCacheRefresh();
					return null;
				}
			});
		} else {
			transactionTemplate.execute(new TransactionCallback() {
				
				@Override
				public Object doInTransaction() {
					CDResult cdResult = mainManager.getCDResultForPlan(planKey);
					if (cdResult != null) {
						cdResultsCache.put(planKey, cdResult);
					}
					return null;
				}
			});
		}
		
		lock.writeLock().unlock();
	}

	@Override
	public List<CDResult> getAllCDResult() {
		// to avoid write-lock first causing reading to be waiting for each other
		lock.readLock().lock();
		if(!cdResultsCache.isEmpty()){
			List<CDResult> results = cdResultsCache.getAllCDResults();
			lock.readLock().unlock();
			return results;
		}
		lock.readLock().unlock();
		
		lock.writeLock().lock();
		if (cdResultsCache.isEmpty()) {
			unlockedCacheRefresh();
		}
		lock.writeLock().unlock();

		lock.readLock().lock();
		List<CDResult> results = cdResultsCache.getAllCDResults();
		lock.readLock().unlock();
		
		return results;
	}

	@Override
	public void clearCache() {
		lock.writeLock().lock();
		cdResultsCache.clear();
		lock.writeLock().unlock();
	}

	private void unlockedCacheRefresh() {
		// create a new temporary cache to avoid locking the cache for too long
		this.cdResultsCache.clear();
		List<CDResult> cdResults = mainManager.getCDResults();
		for (CDResult cdResult : cdResults) {
			this.cdResultsCache.put(cdResult.getPlanKey(), cdResult);
		}
	}
}
