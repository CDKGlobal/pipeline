package com.cobalt.bamboo.plugin.pipeline.changelist;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.atlassian.bamboo.author.Author;
import com.atlassian.bamboo.chains.ChainResultsSummary;
import com.atlassian.bamboo.commit.Commit;
import com.atlassian.bamboo.commit.CommitFile;
import com.atlassian.bamboo.resultsummary.ResultsSummary;
import com.cobalt.bamboo.plugin.pipeline.cdresult.Contributor;
import com.cobalt.bamboo.plugin.pipeline.cdresult.ContributorBuilder;

public class ChangeListFactory {

	public static List<Change> buildChangeList(List<ResultsSummary> buildList, 
												ContributorBuilder contributorBuilder) {
		List<Change> changeList = new ArrayList<Change>();
		
		if (buildList != null && buildList.size() > 0) {
			// add changes from the current build
			addChangesToList(changeList, buildList.get(0).getCommits(), 
							buildList.get(0).getBuildNumber(), contributorBuilder);
			
			// check for pipeline completion
			for (int i = 1; i < buildList.size(); i++) {
				ChainResultsSummary currentBuild = (ChainResultsSummary) buildList.get(i);
				
				if (!currentBuild.isContinuable() && currentBuild.isSuccessful()) {
					break;
				}
				
				addChangesToList(changeList, buildList.get(i).getCommits(), 
								currentBuild.getBuildNumber(), contributorBuilder);
			}
		}
		
		return changeList;
	}
	
	protected static void addChangesToList(List<Change> changeList, List<Commit> commits, 
										int buildNumber, ContributorBuilder contributorBuilder) {
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
			
			Set<String> files = new HashSet<String>();
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
