package com.cobalt.bamboo.plugin.pipeline.Controllers;

import java.util.ArrayList;
import java.util.List;

import com.atlassian.bamboo.applinks.JiraApplinksService;
import com.atlassian.bamboo.author.Author;
import com.atlassian.bamboo.chains.ChainResultsSummary;
import com.atlassian.bamboo.commit.Commit;
import com.atlassian.bamboo.commit.CommitFile;
import com.atlassian.bamboo.plan.Plan;
import com.atlassian.bamboo.plan.PlanExecutionManager;
import com.atlassian.bamboo.plan.PlanKey;
import com.atlassian.bamboo.plan.PlanKeys;
import com.atlassian.bamboo.plan.PlanManager;
import com.atlassian.bamboo.plan.TopLevelPlan;
import com.atlassian.bamboo.project.Project;
import com.atlassian.bamboo.resultsummary.ResultsSummary;
import com.atlassian.bamboo.resultsummary.ResultsSummaryManager;
import com.cobalt.bamboo.plugin.pipeline.cdresult.CDResult;
import com.cobalt.bamboo.plugin.pipeline.cdresult.CDResultFactory;
import com.cobalt.bamboo.plugin.pipeline.cdresult.Change;
import com.cobalt.bamboo.plugin.pipeline.cdresult.Contributor;
import com.cobalt.bamboo.plugin.pipeline.cdresult.ContributorBuilder;

/**
 * The main controller of CDPipeline Plugin Project that handles getting the
 * results needed for displaying.
 */
public class MainManager {
	private PlanManager planManager;
	private ResultsSummaryManager resultsSummaryManager;
	private PlanExecutionManager planExecutionManager;
	private ContributorBuilder contributorBuilder;
	
	/**
	 * Constructs a MainManager object.
	 * 
	 * @param planManager The PlanMananger (within Bamboo) to get information about plans.
	 * @param resultsSummaryManager The ResultsSummaryManager (within Bamboo) to get information
	 *                              about builds.
	 * @param jiraApplinksService The JiraApplinksService (within Bamboo) to get information
	 *                            about the application link to Jira.
	 * @param planExecutionManager The PlanExecutionManager to get information about a currently
	 *                             building build.
	 */
	public MainManager(PlanManager planManager, 
			           ResultsSummaryManager resultsSummaryManager,
			           JiraApplinksService jiraApplinksService, 
			           PlanExecutionManager planExecutionManager) {
        if (planManager == null || resultsSummaryManager == null || planExecutionManager == null) {
            throw new IllegalArgumentException("Null arguments not allowed");
        }
        
		this.planManager = planManager;
		this.resultsSummaryManager = resultsSummaryManager;
		this.planExecutionManager = planExecutionManager;
		this.contributorBuilder = new ContributorBuilder(jiraApplinksService);
	}
	
	/**
	 * Get all the results needed for displaying the CDPipeline table.
	 * 
	 * @return a list of CDResults, where each CDResult represents one row.
	 *         See CDResults for more details.
	 */
	public List<CDResult> getCDResults() {
		List<CDResult> resultList = new ArrayList<CDResult>();
		
		List<TopLevelPlan> plans = planManager.getAllPlans(TopLevelPlan.class);
		for (Plan plan : plans) {
			String planName = plan.getName();
			String planKey = plan.getKey();
			
			Project project = plan.getProject();
			String projectName = project.getName();
			String projectKey = project.getKey();
			
			List<ResultsSummary> buildList = resultsSummaryManager.getResultSummariesForPlan(plan, 0, 0);
			
			CDResult result = CDResultFactory.createCDResult(projectName, planName, projectKey, planKey, 
															buildList, contributorBuilder, planExecutionManager);
			resultList.add(result);
		}
		
		return resultList;
	}
	
	public List<Change> getChangeListForPlan(String planKey) {
		List<Change> changeList = new ArrayList<Change>();
		
		PlanKey planKeyObj = PlanKeys.getPlanKey(planKey); 
		Plan plan = planManager.getPlanByKey(planKeyObj);
		if (plan == null) {
			return null;
		}
		List<ResultsSummary> buildList = resultsSummaryManager.getResultSummariesForPlan(plan, 0, 0);
		if (buildList != null && buildList.size() > 0) {
			addChangesToList(changeList, buildList.get(0).getCommits(), buildList.get(0).getBuildNumber());
			
			for (int i = 1; i < buildList.size(); i++) {
				ChainResultsSummary currentBuild = (ChainResultsSummary) buildList.get(i);
				
				if (!currentBuild.isContinuable() && currentBuild.isSuccessful()) {
					break;
				}
				
				addChangesToList(changeList, buildList.get(i).getCommits(), currentBuild.getBuildNumber());
			}
		}
		
		return changeList;
	}
	
	private void addChangesToList(List<Change> changeList, List<Commit> commits, int buildNumber) {
		for (Commit commit : commits) {
			Author author = commit.getAuthor();
			String username = author.getLinkedUserName();
			if (username == null) {
				username = author.getName();
			}
			
			Contributor contributor = contributorBuilder.createContributor(username, commit.getDate(), author.getFullName());
			
			// a list of changes
			String comment = commit.getComment();
			int importInfo = comment.indexOf("Imported from Git"); // TODO too specific
			if (importInfo != -1) {
				comment = comment.substring(0, importInfo);
			}
			
			List<String> files = new ArrayList<String>();
			List<CommitFile> commitFiles = commit.getFiles();
			String revisionNum = "";
			for (CommitFile commitFile : commitFiles) {
				String filename = commitFile.getCleanName();
				files.add(filename.substring(filename.lastIndexOf("/") + 1)); // remove the path
				revisionNum = commitFile.getRevision();			
			}
			
			Change change = new Change(author.getFullName(), contributor.getPictureUrl(), buildNumber, comment, commit.getDate(), files, revisionNum);
			
			changeList.add(change);
		}
	}
}
