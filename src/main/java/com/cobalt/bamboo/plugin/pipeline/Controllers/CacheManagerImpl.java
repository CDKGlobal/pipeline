package com.cobalt.bamboo.plugin.pipeline.Controllers;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.atlassian.sal.api.transaction.TransactionCallback;
import com.atlassian.sal.api.transaction.TransactionTemplate;
import com.cobalt.bamboo.plugin.pipeline.cache.WallBoardCache;
import com.cobalt.bamboo.plugin.pipeline.cache.WallBoardData;
import com.cobalt.bamboo.plugin.pipeline.cdperformance.UptimeGrade;
import com.cobalt.bamboo.plugin.pipeline.cdresult.CDResult;

public class CacheManagerImpl implements CacheManager {
	private final MainManager mainManager;
	private TransactionTemplate transactionTemplate;
//	private ReadWriteLock lock;
	WallBoardCache wallBoardCache;
	
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
//		lock = new ReentrantReadWriteLock();
		wallBoardCache = new WallBoardCache();
	}

	@Override
	public void putAllWallBoardData() {
//		lock.writeLock().lock();
		transactionTemplate.execute(new TransactionCallback() {
			
			@Override
			public Object doInTransaction() {
//				unlockedCacheRefresh();
				return null;
			}
		});
		
//		lock.writeLock().unlock();
	}
	
	@Override
	public void updateWallBoardDataForPlan(final String planKey, final boolean updateUptimeGrade) {
//		lock.writeLock().lock();
		
		if (wallBoardCache.isEmpty()) {
			transactionTemplate.execute(new TransactionCallback() {
				
				@Override
				public Object doInTransaction() {
//					unlockedCacheRefresh();
					return null;
				}
			});
		} else {
			transactionTemplate.execute(new TransactionCallback() {
				
				@Override
				public Object doInTransaction() {
					CDResult cdResult = mainManager.getCDResultForPlan(planKey);
					
					UptimeGrade uptimeGrade = wallBoardCache.get(planKey).uptimeGrade;
					if (updateUptimeGrade) {
						uptimeGrade.update(cdResult.getCurrentBuild());
					}
					
					if (cdResult != null && uptimeGrade != null) {
						WallBoardData wallBoardData = new WallBoardData(planKey, cdResult, uptimeGrade);
						wallBoardCache.put(planKey, wallBoardData);
					}
					return null;
				}
			});
		}
		
//		lock.writeLock().unlock();
	}

	@Override
	public List<WallBoardData> getAllWallBoardData() {
		// to avoid write-lock first causing reading to be waiting for each other
//		lock.readLock().lock();
		if(!wallBoardCache.isEmpty()){
			List<WallBoardData> results = wallBoardCache.getAllWallBoardData();
//			lock.readLock().unlock();
			return results;
		}
//		lock.readLock().unlock();
		
//		lock.writeLock().lock();
		if (wallBoardCache.isEmpty()) {
			unlockedCacheRefresh();
		}
//		lock.writeLock().unlock();

//		lock.readLock().lock();
		List<WallBoardData> results = wallBoardCache.getAllWallBoardData();
//		lock.readLock().unlock();
		
		return results;
	}

	@Override
	public void clearCache() {
//		lock.writeLock().lock();
		wallBoardCache.clear();
//		lock.writeLock().unlock();
	}

	private void unlockedCacheRefresh() {
		this.wallBoardCache.clear();
		
		List<CDResult> cdResults = mainManager.getCDResults();
		
		for (CDResult cdResult : cdResults) {
			String planKey = cdResult.getPlanKey();
			UptimeGrade uptimeGrade = mainManager.getUptimeGradeForPlan(planKey);
			
			WallBoardData wallBoardData = new WallBoardData(planKey, cdResult, uptimeGrade);
			
			this.wallBoardCache.put(planKey, wallBoardData);
		}
	}
}
