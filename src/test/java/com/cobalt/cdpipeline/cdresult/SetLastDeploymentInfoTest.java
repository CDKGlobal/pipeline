package com.cobalt.cdpipeline.cdresult;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.junit.*;

import com.atlassian.bamboo.author.Author;
import com.atlassian.bamboo.chains.ChainResultsSummary;
import com.atlassian.bamboo.commit.Commit;
import com.atlassian.bamboo.resultsummary.ResultsSummary;
import com.google.common.collect.ImmutableList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class SetLastDeploymentInfoTest {

	private final int LIST_SIZE = 5;
	private ChainResultsSummary CS;
	private ChainResultsSummary CNS;
	private ChainResultsSummary NCNS;
	private ChainResultsSummary NCS1;
	private ChainResultsSummary NCS2;
	private ChainResultsSummary fail2;
	private Date day1;
	private Date day2;
	
	
	@Before
	public void setup(){
		day1 = new Date();
		day2 = new Date();
		day1.setDate(day1.getDate() - 1);
		CS = getChainResultsSummary(true, true, "1", day1);
		CNS = getChainResultsSummary(true, false, "2", day1);
		NCNS = getChainResultsSummary(false, false, "3", day1);
		fail2 = getChainResultsSummary(true, false, "6", day2);
		NCS1 = getChainResultsSummary(false, true, "4", day1);
		NCS2 = getChainResultsSummary(false, true, "5", day2);
	}
	
	@Test
	public void testTwoBuildsWithNoDeploymentCS() {
		CDResultFactory cdr = getCDResultFactory(fail2, CS);
		assertEquals("Last Deployment can't be found", null, cdr.cdresult.getLastDeploymentTime());
		assertEquals("Number of Commits should be the commits of all builds", 2, cdr.cdresult.getNumChanges());
		assertEquals("Number of Contributors should be contributors of all builds", 2, cdr.cdresult.getContributors().size());
	}
	
	@Test
	public void testTwoBuildsWithNoDeploymentCNS() {
		CDResultFactory cdr = getCDResultFactory(fail2, CNS);
		assertEquals("Last Deployment can't be found", null, cdr.cdresult.getLastDeploymentTime());
		assertEquals("Number of Commits should be the commits of all builds", 2, cdr.cdresult.getNumChanges());
		assertEquals("Number of Contributors should be contributors of all builds", 2, cdr.cdresult.getContributors().size());
	}
	
	@Test
	public void testTwoBuildsWithNoDeploymentNCNS() {
		CDResultFactory cdr = getCDResultFactory(fail2, NCNS);
		assertEquals("Last Deployment can't be found", null, cdr.cdresult.getLastDeploymentTime());
		assertEquals("Number of Commits should be the commits of all builds", 2, cdr.cdresult.getNumChanges());
		assertEquals("Number of Contributors should be contributors of all builds", 2, cdr.cdresult.getContributors().size());
	}
	
	@Test
	public void testTwoBuildsWithOneDeployment1(){
		CDResultFactory cdr = getCDResultFactory(fail2, NCS1);
		assertEquals("Last Deployment should be the date of first build", day1, cdr.cdresult.getLastDeploymentTime());
		assertEquals("Number of Commits should be the commits of second build", 1, cdr.cdresult.getNumChanges());
		assertEquals("Number of Contributors should be contributors of second build", 1, cdr.cdresult.getContributors().size());
	}
	
	@Test
	public void testTwoBuildsWithNoDeployment2() {
		CDResultFactory cdr = getCDResultFactory(NCS2, CNS);
		assertEquals("Last Deployment can't be found", null, cdr.cdresult.getLastDeploymentTime());
		assertEquals("Number of Commits should be the commits of all builds", 2, cdr.cdresult.getNumChanges());
		assertEquals("Number of Contributors should be contributors of all builds", 2, cdr.cdresult.getContributors().size());
	}
	
	@Test
	public void testTwoBuildsWithTwoDeployment(){
		CDResultFactory cdr = getCDResultFactory(NCS2, NCS1);
		assertEquals("Last Deployment should be the date of first build", day1, cdr.cdresult.getLastDeploymentTime());
		assertEquals("Number of Commits should be the commits of second build", 1, cdr.cdresult.getNumChanges());
		assertEquals("Number of Contributors should be contributors of second build", 1, cdr.cdresult.getContributors().size());
	}
	
	// Deployment Positions: 1 being most current, 5 being oldest
	
	@Test
	public void testFiveBuildsWithTwoDeploymentsAtPos1And2() {
		testFiveBuildsWithTwoDeploymentsAtDifferentPos(1, 2, 2);
	}
	
	@Test
	public void testFiveBuildsWithTwoDeploymentsAtPos1And3() {
		testFiveBuildsWithTwoDeploymentsAtDifferentPos(1, 3, 3);
	}
	
	@Test
	public void testFiveBuildsWithTwoDeploymentsAtPos1And5() {
		testFiveBuildsWithTwoDeploymentsAtDifferentPos(1, 5, 5);
	}
	
	@Test
	public void testFiveBuildsWithTwoDeploymentsAtPos2And4() {
		testFiveBuildsWithTwoDeploymentsAtDifferentPos(2, 4, 2);
	}
	
	@Test
	public void testFiveBuildsWithTwoDeploymentsAtPos2And5() {
		testFiveBuildsWithTwoDeploymentsAtDifferentPos(2, 5, 2);
	}
	
	@Test
	public void testFiveBuildsWithTwoDeploymentsAtPos3And5() {
		testFiveBuildsWithTwoDeploymentsAtDifferentPos(3, 5, 3);
	}
	
	@Test
	public void testFiveBuildsWithThreeDeployments() {
		List<ResultsSummary> buildList = makeNormalSizeBuildList();
		for (int i = 0; i < LIST_SIZE; i++) {
			ChainResultsSummary crs = (ChainResultsSummary) buildList.get(i);
			
			if (i == 0 || i == 2 || i == 4) {  // zero-based index for 1, 3, 5
				// set to deployment condition
				when(crs.isContinuable()).thenReturn(false);
				when(crs.isSuccessful()).thenReturn(true);
			} else {
				when(crs.isContinuable()).thenReturn(true);
				when(crs.isSuccessful()).thenReturn(false);
			}
		}
		
		CDResultFactory fac = new CDResultFactory("Project", "Project - Plan", buildList);
		fac.setLastDeploymentInfo();
		
		assertEquals("Date of last deployment doesn't match.", 
						(new Date(3)).getTime(), fac.cdresult.getLastDeploymentTime().getTime());
		assertEquals("Number of changes since last deployment doesn't match.", 
						3, fac.cdresult.getNumChanges());
		assertEquals("Number of contributors since last deployment doesn't match.", 
						2, fac.cdresult.getContributors().size());
	}
	
	@Test
	public void testFiveBuildsWithAllBeingDeployment() {
		List<ResultsSummary> buildList = makeNormalSizeBuildList();
		
		// set all build to deployment condition
		for (int i = 0; i < LIST_SIZE; i++) {
			ChainResultsSummary crs = (ChainResultsSummary) buildList.get(i);
			when(crs.isContinuable()).thenReturn(false);
			when(crs.isSuccessful()).thenReturn(true);
		}
		
		CDResultFactory fac = new CDResultFactory("Project", "Project - Plan", buildList);
		fac.setLastDeploymentInfo();
		
		assertEquals("Date of last deployment doesn't match.", 
						(new Date(2)).getTime(), fac.cdresult.getLastDeploymentTime().getTime());
		assertEquals("Number of changes since last deployment doesn't match.", 
						1, fac.cdresult.getNumChanges());
		assertEquals("Number of contributors since last deployment doesn't match.", 
						1, fac.cdresult.getContributors().size());
	}
	
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
		
		@Test
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
	
	
	// ========== Private Helper Methods ==========
	
	private void testFiveBuildsWithTwoDeploymentsAtDifferentPos(int pos1, int pos2, int expectedPos) {
		List<ResultsSummary> buildList = makeNormalSizeBuildList();
		setTwoDeployments(buildList, pos1, pos2);
		
		CDResultFactory fac = new CDResultFactory("Project", "Project - Plan", buildList);
		fac.setLastDeploymentInfo();
		
		// Last deployment should be second one in the buildList
		
		int expectedChanges = 0;
		for (int i = 1; i < expectedPos; i++) {
			expectedChanges += i;
		}
		
		assertEquals("Date of last deployment doesn't match.", 
						(new Date(expectedPos)).getTime(), fac.cdresult.getLastDeploymentTime().getTime());
		assertEquals("Number of changes since last deployment doesn't match.", 
						expectedChanges, fac.cdresult.getNumChanges());
		assertEquals("Number of contributors since last deployment doesn't match.", 
						expectedPos - 1, fac.cdresult.getContributors().size());
	}
	
	private List<ResultsSummary> makeNormalSizeBuildList() {
		List<ResultsSummary> buildList = new ArrayList<ResultsSummary>();
		for (int i = 1; i <= LIST_SIZE; i++) {
			ChainResultsSummary crs = mock(ChainResultsSummary.class);
			
			ImmutableList.Builder<Commit> builder = new ImmutableList.Builder<Commit>();
			for (int j = 1; j <= i; j++) {
				Commit c = mock(Commit.class);
				Author a = mock(Author.class);
				when(a.getFullName()).thenReturn("Author" + j);
				when(c.getAuthor()).thenReturn(a);
				builder.add(c);
			}
			
			when(crs.getCommits()).thenReturn(builder.build());
			when(crs.getBuildCompletedDate()).thenReturn(new Date(i));
			
			buildList.add(crs);
		}
		
		return buildList;
	}
	
	// pos1, pos2: the indexes of the deployments to be set at (1 - most current, 5 - oldest)
	private void setTwoDeployments(List<ResultsSummary> list, int pos1, int pos2) {
		pos1--;  // change to zero-based index
		pos2--;  // change to zero-based index
		
		for (int i = 0; i < LIST_SIZE; i++) {
			ChainResultsSummary crs = (ChainResultsSummary) list.get(i);
			
			if (i == pos1 || i == pos2) {
				// set to deployment condition
				when(crs.isContinuable()).thenReturn(false);
				when(crs.isSuccessful()).thenReturn(true);
			} else {
				when(crs.isContinuable()).thenReturn(true);
				when(crs.isSuccessful()).thenReturn(false);
			}
		}
	}
	
	private ChainResultsSummary getChainResultsSummary(boolean cont, boolean succ, String name, Date date){
		ChainResultsSummary result = mock(ChainResultsSummary.class);
		when(result.isContinuable()).thenReturn(cont);
		when(result.isSuccessful()).thenReturn(succ);
		Commit c = mock(Commit.class);
		Author a = mock(Author.class);
		when(a.getFullName()).thenReturn(name);
		when(c.getAuthor()).thenReturn(a);
		ImmutableList<Commit> commits = ImmutableList.of(c);
		when(result.getCommits()).thenReturn(commits);
		when(result.getBuildCompletedDate()).thenReturn(date);
		return result;
	}
	
	private CDResultFactory getCDResultFactory(ResultsSummary rs1, ResultsSummary rs2){
		List<ResultsSummary> builds = new ArrayList<ResultsSummary>();
		builds.add(rs1);
		builds.add(rs2);
		CDResultFactory cdr = new CDResultFactory("test", "test - test", builds);
		cdr.setLastDeploymentInfo();
		return cdr;
	}
	
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
