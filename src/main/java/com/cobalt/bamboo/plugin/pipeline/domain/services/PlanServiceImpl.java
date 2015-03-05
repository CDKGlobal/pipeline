package com.cobalt.bamboo.plugin.pipeline.domain.services;

import com.atlassian.bamboo.plan.*;
import com.atlassian.bamboo.resultsummary.ResultsSummary;
import com.atlassian.bamboo.resultsummary.ResultsSummaryManager;
import com.cobalt.bamboo.plugin.pipeline.domain.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The main controller of CDPipeline Plugin Project that handles getting the
 * results needed for displaying.
 */
public class PlanServiceImpl implements PlanService {
    private PlanManager planManager;
    private ResultsSummaryManager resultsSummaryManager;
    private PlanExecutionManager planExecutionManager;
    private ContributorBuilder contributorBuilder;

    public PlanServiceImpl() {
        String jiraBaseUrl = "";
        this.contributorBuilder = new ContributorBuilder(jiraBaseUrl);
    }

    @Override
    public List<ProjectReport> getCDResults() {
        List<ProjectReport> resultList = new ArrayList<ProjectReport>();

        List<TopLevelPlan> plans = planManager.getAllPlans(TopLevelPlan.class);
        for (Plan plan : plans) {

            ProjectReport result = ProjectReportFactory.createCDResult(plan, contributorBuilder, planExecutionManager, resultsSummaryManager);
            resultList.add(result);
        }

        return resultList;
    }

    @Override
    public List<Change> getChangeListForPlan(String planKey) {
        PlanKey planKeyObj = PlanKeys.getPlanKey(planKey);
        Plan plan = planManager.getPlanByKey(planKeyObj);

        if (plan == null) {
            return null;
        }

        List<ResultsSummary> buildList = resultsSummaryManager.getResultSummariesForPlan(plan, 0, 0);

        return ChangeListFactory.buildChangeList(buildList, contributorBuilder);
    }

    @Override
    public PerformanceSummary getPerformanceStatsForPlan(String planKey) {
        PlanKey planKeyObj = PlanKeys.getPlanKey(planKey);
        Plan plan = planManager.getPlanByKey(planKeyObj);

        if (plan == null) {
            return null;
        }

        List<ResultsSummary> buildList = resultsSummaryManager.getResultSummariesForPlan(plan, 0, 0);

        return PerformanceSummaryCreator.create(buildList, contributorBuilder);
    }

    @Override
    public UptimeGrade getUptimeGradeForPlan(String planKey) {
        PlanKey planKeyObj = PlanKeys.getPlanKey(planKey);
        Plan plan = planManager.getPlanByKey(planKeyObj);

        if (plan == null) {
            return null;
        }

        List<ResultsSummary> buildList = resultsSummaryManager.getResultSummariesForPlan(plan, 0, 0);

        return UptimeGradeFactory.createUptimeGrade(buildList);
    }

    @Override
    public ProjectReport getCDResultForPlan(String planKey) {
        PlanKey planKeyObj = PlanKeys.getPlanKey(planKey);
        Plan plan = planManager.getPlanByKey(planKeyObj);

        if (plan == null) {
            return null;
        }

        return ProjectReportFactory.createCDResult(plan, contributorBuilder, planExecutionManager, resultsSummaryManager);
    }

    @Override
    public List<TopLevelPlan> getAllPlans() {
        return planManager.getAllPlans(TopLevelPlan.class);
    }

    public PlanManager getPlanManager() {
        return planManager;
    }

    public void setPlanManager(PlanManager planManager) {
        this.planManager = planManager;
    }

    public ResultsSummaryManager getResultsSummaryManager() {
        return resultsSummaryManager;
    }

    public void setResultsSummaryManager(ResultsSummaryManager resultsSummaryManager) {
        this.resultsSummaryManager = resultsSummaryManager;
    }

    public PlanExecutionManager getPlanExecutionManager() {
        return planExecutionManager;
    }

    public void setPlanExecutionManager(PlanExecutionManager planExecutionManager) {
        this.planExecutionManager = planExecutionManager;
    }

    public ContributorBuilder getContributorBuilder() {
        return contributorBuilder;
    }

    public void setContributorBuilder(ContributorBuilder contributorBuilder) {
        this.contributorBuilder = contributorBuilder;
    }
}
