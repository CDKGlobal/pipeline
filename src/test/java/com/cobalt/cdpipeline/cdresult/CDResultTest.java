package com.cobalt.cdpipeline.cdresult;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.atlassian.bamboo.builder.BuildState;


public class CDResultTest {
	
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
	
}
