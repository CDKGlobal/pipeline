package com.cobalt.bamboo.plugin.pipeline.cache;

import com.atlassian.bamboo.plan.Plan;
import com.atlassian.bamboo.plan.TopLevelPlan;
import com.atlassian.sal.api.transaction.TransactionCallback;
import com.atlassian.sal.api.transaction.TransactionTemplate;
import com.cobalt.bamboo.plugin.pipeline.domain.model.ProjectReport;
import com.cobalt.bamboo.plugin.pipeline.domain.model.UptimeGrade;
import com.cobalt.bamboo.plugin.pipeline.domain.services.PlanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class CacheManagerImpl implements CacheManager {
    private static final Logger logger = LoggerFactory.getLogger(CacheManagerImpl.class);
    private TransactionTemplate transactionTemplate;
    private WallBoardCache wallBoardCache;
    private PlanService planService;

    private final Object lock = new Object();

    private AtomicBoolean firstLoadDone;

    public CacheManagerImpl() {
        wallBoardCache = new WallBoardCache();
        firstLoadDone = new AtomicBoolean(false);
    }

    /**
     * Put WallBoardData for all plans into the cache. All of existing data (if any)
     * will be replaced.
     */
    public void putAllWallBoardData() {
        transactionTemplate.execute(new TransactionCallback() {

            @Override
            public Object doInTransaction() {

                refreshCache();

                firstLoadDone.compareAndSet(false, true);

                return null;
            }
        });
    }

    /**
     * Update the WallBoardData in the cache for the given plan. If the plan already
     * exists in the cache, the associated WallBoardData will be replaced. If the plan
     * doesn't exist in the cache yet, a new plan will be created with it's new WallBoardData.
     *
     * @param planKey           of the plan to update in the cache
     * @param updateUptimeGrade A boolean to indicate whether to update the UptimeGrade in the cache
     */
    public void updateWallBoardDataForPlan(final String planKey, final boolean updateUptimeGrade) {
        transactionTemplate.execute(new TransactionCallback() {

            @Override
            public Object doInTransaction() {

                ProjectReport projectReport = planService.getCDResultForPlan(planKey);

                UptimeGrade uptimeGrade;
                if (!wallBoardCache.containsPlan(planKey)) {
                    uptimeGrade = planService.getUptimeGradeForPlan(planKey);
                } else {
                    uptimeGrade = wallBoardCache.get(planKey).uptimeGrade;
                    if (updateUptimeGrade) {
                        uptimeGrade.update(projectReport.getCurrentBuild());
                    }
                }


                if (projectReport != null && uptimeGrade != null) {
                    WallBoardData wallBoardData = new WallBoardData(planKey, projectReport, uptimeGrade);
                    wallBoardCache.put(planKey, wallBoardData);
                }

                return null;
            }
        });
    }

    /**
     * Get WallBoardData for all of the plans.
     *
     * @return a list of WallBoardData representing all plans.
     */
    public List<WallBoardData> getAllWallBoardData() {
        if (!firstLoadDone.get()) {
            synchronized (lock) {
                logger.info("Refreshing cache");
                refreshCache();
                firstLoadDone.set(true);
            }
        }

        List<WallBoardData> results = wallBoardCache.getAllWallBoardData();

        return results;
    }

    /**
     * Clear everything in the cache.
     */
    public void clearCache() {
        wallBoardCache.clear();
    }

    /*
     * Refresh the cache
     */
    private void refreshCache() {
        List<TopLevelPlan> plans = planService.getAllPlans();
        Set<String> planKeysSet = new HashSet<String>();

        for (Plan plan : plans) {
            String planKey = plan.getPlanKey().getKey();
            UptimeGrade uptimeGrade = planService.getUptimeGradeForPlan(planKey);
            ProjectReport projectReport = planService.getCDResultForPlan(planKey);
            WallBoardData wallBoardData = new WallBoardData(planKey, projectReport, uptimeGrade);

            planKeysSet.add(planKey);
            wallBoardCache.put(planKey, wallBoardData);
        }

        for (String planKey : wallBoardCache.getAllPlanKeys()) {
            if (!planKeysSet.contains(planKey)) {
                wallBoardCache.removePlan(planKey);
            }
        }

    }

    public TransactionTemplate getTransactionTemplate() {
        return transactionTemplate;
    }

    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    public PlanService getPlanService() {
        return planService;
    }

    public void setPlanService(PlanService planService) {
        this.planService = planService;
    }
}
