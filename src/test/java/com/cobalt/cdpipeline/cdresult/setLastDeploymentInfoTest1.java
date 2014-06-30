package com.cobalt.cdpipeline.cdresult;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import com.atlassian.bamboo.author.Author;
import com.atlassian.bamboo.chains.ChainResultsSummary;
import com.atlassian.bamboo.commit.Commit;
import com.atlassian.bamboo.resultsummary.ResultsSummary;
import com.google.common.collect.ImmutableList;

public class setLastDeploymentInfoTest1 {
	
	// no build 
	@Test 
	public void testNoBuild() {
		List<ResultsSummary> emptyBuildList = new ArrayList<ResultsSummary>();
		CDResultFactory cdfac = new CDResultFactory("project", "project - plan", emptyBuildList);		
		cdfac.setLastDeploymentInfo();
		
		assertEquals(0, cdfac.cdresult.getNumChanges());
		assertEquals(new HashSet<Contributor>(), cdfac.cdresult.getContributors());
		assertEquals(null, cdfac.cdresult.getLastDeploymentTime());
	}
		

	// 1 build (succ/fail). No last deployment
	@Test
	public void testOneBuildSuccessful() {
		List<ResultsSummary> buildList = createBuildList(1, 0);
		
		CDResultFactory cdfac = new CDResultFactory("project", "project - plan", buildList);		
		cdfac.setLastDeploymentInfo();
		
		assertEquals(1, cdfac.cdresult.getNumChanges());		
		assertEquals(1, cdfac.cdresult.getContributors().size());
		assertEquals(null, cdfac.cdresult.getLastDeploymentTime());	
	}
	
	public void testOneBuildFailed() {
		List<ResultsSummary> buildList = createBuildList(1, -1);
		CDResultFactory cdfac = new CDResultFactory("project", "project - plan", buildList);		
		cdfac.setLastDeploymentInfo();
		
		assertEquals(1, cdfac.cdresult.getNumChanges());		
		assertEquals(1, cdfac.cdresult.getContributors().size());
		assertEquals(null, cdfac.cdresult.getLastDeploymentTime());	
	}
	
	
	// 5 builds (first 2 cases)
	@Test
	public void testFiveBuildsNoDeployment() {
		// N N N N N        (Is deployment? Y/N)
		List<ResultsSummary> buildList = createBuildList(5, -1);
		CDResultFactory cdfac = new CDResultFactory("project", "project - plan", buildList);		
		cdfac.setLastDeploymentInfo();
		
		assertEquals(15, cdfac.cdresult.getNumChanges());		
		assertEquals(5, cdfac.cdresult.getContributors().size());
		assertEquals(null, cdfac.cdresult.getLastDeploymentTime());	
	}
	
	@Test
	public void testFiveBuildsOneNewestDeployment() {
		// N N N N Y
		List<ResultsSummary> buildList = createBuildList(5, 0);
		CDResultFactory cdfac = new CDResultFactory("project", "project - plan", buildList);		
		cdfac.setLastDeploymentInfo();
		
		assertEquals(15, cdfac.cdresult.getNumChanges());		
		assertEquals(5, cdfac.cdresult.getContributors().size());
		assertEquals(null, cdfac.cdresult.getLastDeploymentTime());	
	}
	
	@Test
	public void testFiveBuildsSecondNewestDeployment() {
		// N N N Y N
		List<ResultsSummary> buildList = createBuildList(5, 1);
		CDResultFactory cdfac = new CDResultFactory("project", "project - plan", buildList);		
		cdfac.setLastDeploymentInfo();
		
		assertEquals(1, cdfac.cdresult.getNumChanges());		
		assertEquals(1, cdfac.cdresult.getContributors().size());
		assertEquals(new Date(2014, 1, 1, 1, 1, 1), cdfac.cdresult.getLastDeploymentTime());
	}
	
	@Test
	public void testFiveBuildsMidDeployment() {
		// N N Y N N
		List<ResultsSummary> buildList = createBuildList(5, 2);
		CDResultFactory cdfac = new CDResultFactory("project", "project - plan", buildList);		
		cdfac.setLastDeploymentInfo();
		
		assertEquals(3, cdfac.cdresult.getNumChanges());		
		assertEquals(2, cdfac.cdresult.getContributors().size());
		assertEquals(new Date(2014, 1, 1, 1, 1, 1), cdfac.cdresult.getLastDeploymentTime());	
	}
	
	@Test
	public void testFiveBuildsOldestDeployment() {
		// Y N N N N 
		List<ResultsSummary> buildList = createBuildList(5, 4);
		CDResultFactory cdfac = new CDResultFactory("project", "project - plan", buildList);		
		cdfac.setLastDeploymentInfo();
		
		assertEquals(10, cdfac.cdresult.getNumChanges());		
		assertEquals(4, cdfac.cdresult.getContributors().size());
		assertEquals(new Date(2014, 1, 1, 1, 1, 1), cdfac.cdresult.getLastDeploymentTime());	
	}
	
	////////////////////////////////////////////
	// private helper methods
	
	// create a list of commits, each w/ its authors' full name
	private ImmutableList<Commit> createCommitList3(int numCommits) {
		ImmutableList.Builder<Commit> commits = new ImmutableList.Builder <Commit>();
		for (int i = 0; i < numCommits; i++) {
			Author author = mock(Author.class);
	    	when(author.getFullName()).thenReturn("author" + i);    	
			Commit commit = mock(Commit.class);
	    	when(commit.getAuthor()).thenReturn(author);	    	
	    	commits.add(commit);
		}
		return commits.build();
	}
	
	// create a build that is a deployment (!C & S)
	private ResultsSummary createDeploymentBuild() {
		ChainResultsSummary build = mock(ChainResultsSummary.class);
		// set its deployment condition 
		when(build.isContinuable()).thenReturn(false);
		when(build.isSuccessful()).thenReturn(true);
		when(build.getBuildCompletedDate()).thenReturn(new Date(2014, 1, 1, 1, 1, 1));
		
		return build;
	}
	
	// create a build that is not a deployment 
	// randomly assign one of the 3 failure cases to it (C & S, C & !S, !C & !S)
	private ResultsSummary createNonDeploymentBuild() {
		ChainResultsSummary build = mock(ChainResultsSummary.class);
		Random r = new Random();
		int failCaseNum = r.nextInt(3); // randomly choose one of the failure cases
		
		// set its deployment condition 
		if (failCaseNum == 0) { // C & S
			when(build.isContinuable()).thenReturn(true);
			when(build.isSuccessful()).thenReturn(true);
		} else if (failCaseNum == 1) { // C & !S
			when(build.isContinuable()).thenReturn(true);
			when(build.isSuccessful()).thenReturn(false);
		} else { // !C & !S
			when(build.isContinuable()).thenReturn(false);
			when(build.isSuccessful()).thenReturn(false);
		}
		
		return build;
	}
	
	// create a list of builds with the specified position of deployment (-1 if no deployment)
	private List<ResultsSummary> createBuildList(int numBuilds, int deploymentIndex) {
		List<ResultsSummary> buildList = new ArrayList<ResultsSummary>();
		for (int i = 0; i < numBuilds; i++) {
			// create a deployment/non-deployment build
			ResultsSummary build;	
			if (i == deploymentIndex) {
				build = createDeploymentBuild();
			} else {
				build = createNonDeploymentBuild();
			}		
			// add the build to the list
			buildList.add(build);

			// associated the build with a list of commits
			ImmutableList<Commit> commits = createCommitList3(i + 1);	 // add 1	
			when(build.getCommits()).thenReturn(commits);
		}
		return buildList;
	}
	
}
