package com.cobalt.cdpipeline.cdresult;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.atlassian.bamboo.builder.BuildState;


public class CDResultTest {
	Contributor test1, test2, test3;
	Date current;
	
	@Before
	public void setUpContributors(){
		current = new Date();
		test1 = new Contributor("test 1", current);
		test2 = new Contributor("test 2", new Date(current.getTime() - 1000));
		test3 = new Contributor("test 3", new Date(current.getTime() - 10000));
	}
	
	@Test
	public void testGetDaysSinceYesterdayFromCurrent() {
		CDResult cdr = new CDResult("test", "test", "test", "test");
		Date current = new Date();
		Date yest = new Date(current.getTime());
		yest.setDate(current.getDate() - 1);
		cdr.setLastDeploymentTime(yest);
		assertEquals("The days between was not what we expected", 1, cdr.getDaysSinceDeploymentFromCurrent());
	}
	
	@Test
	public void testGetDaysSinceRandomNumberFromCurrent() {
		CDResult cdr = new CDResult("test", "test", "test", "test");
		Date current = new Date();
		Date yest = new Date(current.getTime());
		yest.setDate(current.getDate() - 100);
		cdr.setLastDeploymentTime(yest);
		assertEquals("The days between was not what we expected", 100, cdr.getDaysSinceDeploymentFromCurrent());
	}

	@Test
	public void testGetDaysSinceTodayFromCurrent() {
		CDResult cdr = new CDResult("test", "test", "test", "test");
		Date current = new Date();
		Date yest = new Date(current.getTime());
		yest.setMinutes(current.getMinutes() - 100);
		cdr.setLastDeploymentTime(yest);
		assertEquals("The days between was not what we expected", 0, cdr.getDaysSinceDeploymentFromCurrent());
	}
	
	@Test
	public void testGetDaysSinceFutureFromCurrent() {
		CDResult cdr = new CDResult("test", "test", "test", "test");
		Date current = new Date();
		Date yest = new Date(current.getTime());
		yest.setDate(current.getDate() + 1);
		cdr.setLastDeploymentTime(yest);
		assertEquals("The days between was not what we expected", -1, cdr.getDaysSinceDeploymentFromCurrent());
	}
	
	@Test
	public void testGetDaysSinceNullFromCurrent() {
		CDResult cdr = new CDResult("test", "test", "test", "test");
		assertEquals("The days between was not what we expected", -1, cdr.getDaysSinceDeploymentFromCurrent());
	}
	
	@Test
	public void immutableTestOfgetLastUpdateTime() {
		CDResult cdr = new CDResult("test", "test", "test", "test");
		Date current = new Date();
		cdr.setLastUpdateTime(current);
		Date update = cdr.getLastUpdateTime();
		update.setDate(update.getDate() - 1);
		assertEquals("The update time should not be modified", current, cdr.getLastUpdateTime());
	}
	
	@Test
	public void immutableTestOfsetLastUpdateTime() {
		CDResult cdr = new CDResult("test", "test", "test", "test");
		Date current = new Date();
		cdr.setLastUpdateTime(current);
		current.setDate(current.getDate() - 1);
		Date update = cdr.getLastUpdateTime();
		assertFalse(current.equals(update));
	}
	
	@Test
	public void immutableTestOfgetLastDeploymentTime() {
		CDResult cdr = new CDResult("test", "test", "test", "test");
		Date current = new Date();
		cdr.setLastDeploymentTime(current);
		Date update = cdr.getLastDeploymentTime();
		update.setDate(update.getDate() - 1);
		assertEquals("The update time should not be modified", current, cdr.getLastDeploymentTime());
	}
	
	@Test
	public void immutableTestOfsetLastDeploymentTime() {
		CDResult cdr = new CDResult("test", "test", "test", "test");
		Date current = new Date();
		cdr.setLastDeploymentTime(current);
		current.setDate(current.getDate() - 1);
		Date update = cdr.getLastDeploymentTime();
		assertFalse(current.equals(update));
	}
	
	@Test (expected = UnsupportedOperationException.class)
	public void immutableTestOfGetContributors(){
		CDResult cdr = new CDResult("test", "test", "test", "test");
		Contributor c = new Contributor("test1", new Date());
		cdr.addContributor(c);
		Set<Contributor> cs = cdr.getContributors();
		cs.add(new Contributor("test2", new Date()));
	}
	
	@Test (expected = UnsupportedOperationException.class)
	public void immutableTestOfGetPipelineStages(){
		CDResult cdr = new CDResult("test", "test", "test", "test");
		PipelineStage p = new PipelineStage("test1", BuildState.FAILED);
		cdr.addPipelineStageToList(p);
		List<PipelineStage> ps = cdr.getPipelineStages();
		ps.add(new PipelineStage("test2", BuildState.FAILED));
	}
	
	@Test
	public void identicalContributorsTest(){
		CDResult cdr = new CDResult("test", "test", "test", "test");
		cdr.addContributor(test1);
		Contributor i1 = new Contributor("test 1", current);
		Contributor i2 = new Contributor("test 1", current);
		cdr.addContributor(i1);
		cdr.addContributor(i2);
		Set<Contributor> contributors = cdr.getContributors();
		assertEquals("There should not be increment of number of Contributor", 1, contributors.size());
		for(Contributor c : contributors){
			assertEquals("The number of commits of the identical Contributor should be incremented", 3, c.getNumCommits());
		}
	}
	
	// Test the sorting of Contributors in CDResult.
	@Test
	public void numCommitSortingTestOfContributors(){
		CDResult cdr = new CDResult("test", "test", "test", "test");
		cdr.addContributor(test1);
		cdr.addContributor(test1);
		cdr.addContributor(test1);
		cdr.addContributor(test2);
		cdr.addContributor(test2);
		cdr.addContributor(test3);
		List<Contributor> contributors = cdr.getContributorsSortedByNumCommits();
		assertEquals("There should not be dulplicate identical Contributors", 3, contributors.size());
		assertEquals("The list of Contributors should be sorted by number of commits", test1.getUsername(), contributors.get(0).getUsername());
		assertEquals("The list of Contributors should be sorted by number of commits", test2.getUsername(), contributors.get(1).getUsername());
		assertEquals("The list of Contributors should be sorted by number of commits", test3.getUsername(), contributors.get(2).getUsername());
	}
	
	@Test
	public void lastCommitTimeSortingTestOfContributors(){
		CDResult cdr = new CDResult("test", "test", "test", "test");
		cdr.addContributor(test1);
		cdr.addContributor(test2);
		cdr.addContributor(test3);
		List<Contributor> contributors = cdr.getContributorsSortedByLatestCommit();
		assertEquals("There should not be dulplicate identical Contributors", 3, contributors.size());
		assertEquals("The list of Contributors should be sorted by last commit time", test1.getUsername(), contributors.get(0).getUsername());
		assertEquals("The list of Contributors should be sorted by last commit time", test2.getUsername(), contributors.get(1).getUsername());
		assertEquals("The list of Contributors should be sorted by last commit time", test3.getUsername(), contributors.get(2).getUsername());
	}
	
}
