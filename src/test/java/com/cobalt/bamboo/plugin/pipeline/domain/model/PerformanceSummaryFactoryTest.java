package com.cobalt.bamboo.plugin.pipeline.domain.model;

import com.atlassian.bamboo.author.Author;
import com.atlassian.bamboo.chains.ChainResultsSummary;
import com.atlassian.bamboo.commit.Commit;
import com.atlassian.bamboo.resultsummary.ResultsSummary;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class PerformanceSummaryFactoryTest {
    private Date day1;
    private Date day2;
    private Date day3;
    private Date day4;
    private Date day5;
    private Date current;
    ContributorBuilder cb;

    @Before
    public void setup() {
        cb = new ContributorBuilder("");
        day1 = new Date();
        day2 = new Date();
        day3 = new Date();
        day4 = new Date();
        day5 = new Date();
        day1.setDate(day1.getDate() - 5);
        day2.setDate(day2.getDate() - 4);
        day3.setDate(day3.getDate() - 3);
        day4.setDate(day4.getDate() - 2);
        day5.setDate(day5.getDate() - 1);
        current = new Date();
    }

    @Test
    public void testNoBuild() {
        PerformanceSummary cdp = PerformanceSummaryCreator.create(new ArrayList<ResultsSummary>(), cb);
        assertEquals("No build, return null", null, cdp);
    }

    @Test
    public void testNoSuccessOneBuild() {
        List<ResultsSummary> buildList = new ArrayList<ResultsSummary>();
        buildList.add(getChainResultsSummary(true, false, "1", 1, day1));
        checkExpected(buildList, 0, -1, -1, new ArrayList<Integer>(), new ArrayList<Integer>());
    }

    @Test
    public void testOneSuccessNoCompletionOneBuild() {
        List<ResultsSummary> buildList = new ArrayList<ResultsSummary>();
        buildList.add(getChainResultsSummary(true, true, "1", 1, day1));
        checkExpected(buildList, 1, -1, -1, new ArrayList<Integer>(), new ArrayList<Integer>());
    }

    @Test
    public void testOneCompletionOneBuild() {
        List<ResultsSummary> buildList = new ArrayList<ResultsSummary>();
        buildList.add(getChainResultsSummary(false, true, "1", 1, day1));
        List<Integer> changes = new ArrayList<Integer>();
        List<Integer> buildNums = new ArrayList<Integer>();
        changes.add(1);
        buildNums.add(1);
        checkExpected(buildList, 1, 1, 0, changes, buildNums);
    }

    @Test
    public void testNoSuccessTwoBuild() {
        List<ResultsSummary> buildList = new ArrayList<ResultsSummary>();
        buildList.add(getChainResultsSummary(true, false, "2", 2, day2));
        buildList.add(getChainResultsSummary(false, false, "1", 1, day1));
        List<Integer> changes = new ArrayList<Integer>();
        List<Integer> buildNums = new ArrayList<Integer>();
        checkExpected(buildList, 0, -1, -1, changes, buildNums);
    }

    @Test
    public void testFailSuccessTwoBuild() {
        List<ResultsSummary> buildList = new ArrayList<ResultsSummary>();
        buildList.add(getChainResultsSummary(true, true, "2", 2, day2));
        buildList.add(getChainResultsSummary(true, false, "1", 1, day1));
        List<Integer> changes = new ArrayList<Integer>();
        List<Integer> buildNums = new ArrayList<Integer>();
        checkExpected(buildList, 0.5, -1, -1, changes, buildNums);
    }

    @Test
    public void testSuccessFailTwoBuild() {
        List<ResultsSummary> buildList = new ArrayList<ResultsSummary>();
        buildList.add(getChainResultsSummary(true, false, "2", 2, day2));
        buildList.add(getChainResultsSummary(true, true, "1", 1, day1));
        List<Integer> changes = new ArrayList<Integer>();
        List<Integer> buildNums = new ArrayList<Integer>();
        checkExpected(buildList, 0.5, -1, -1, changes, buildNums);
    }

    @Test
    public void testTwoSuccessTwoBuild() {
        List<ResultsSummary> buildList = new ArrayList<ResultsSummary>();
        buildList.add(getChainResultsSummary(true, true, "2", 2, day2));
        buildList.add(getChainResultsSummary(true, true, "1", 1, day1));
        List<Integer> changes = new ArrayList<Integer>();
        List<Integer> buildNums = new ArrayList<Integer>();
        checkExpected(buildList, 1, -1, -1, changes, buildNums);
    }

    @Test
    public void testFailCompletionTwoBuild() {
        List<ResultsSummary> buildList = new ArrayList<ResultsSummary>();
        buildList.add(getChainResultsSummary(false, true, "2", 2, day2));
        buildList.add(getChainResultsSummary(true, false, "1", 1, day1));
        List<Integer> changes = new ArrayList<Integer>();
        List<Integer> buildNums = new ArrayList<Integer>();
        changes.add(2);
        buildNums.add(2);
        checkExpected(buildList, 0.5, 2, 1, changes, buildNums);
    }

    @Test
    public void testCompletionFailTwoBuild() {
        List<ResultsSummary> buildList = new ArrayList<ResultsSummary>();
        buildList.add(getChainResultsSummary(true, false, "2", 2, day2));
        buildList.add(getChainResultsSummary(false, true, "1", 1, day1));
        List<Integer> changes = new ArrayList<Integer>();
        List<Integer> buildNums = new ArrayList<Integer>();
        changes.add(1);
        buildNums.add(1);
        checkExpected(buildList, 0.5, 1, 0, changes, buildNums);
    }

    @Test
    public void testSuccessCompletionTwoBuild() {
        List<ResultsSummary> buildList = new ArrayList<ResultsSummary>();
        buildList.add(getChainResultsSummary(false, true, "2", 2, day2));
        buildList.add(getChainResultsSummary(true, true, "1", 1, day1));
        List<Integer> changes = new ArrayList<Integer>();
        List<Integer> buildNums = new ArrayList<Integer>();
        changes.add(2);
        buildNums.add(2);
        checkExpected(buildList, 1, 2, 1, changes, buildNums);
    }

    @Test
    public void testCompletionSuccessTwoBuild() {
        List<ResultsSummary> buildList = new ArrayList<ResultsSummary>();
        buildList.add(getChainResultsSummary(true, true, "2", 2, day2));
        buildList.add(getChainResultsSummary(false, true, "1", 1, day1));
        List<Integer> changes = new ArrayList<Integer>();
        List<Integer> buildNums = new ArrayList<Integer>();
        changes.add(1);
        buildNums.add(1);
        checkExpected(buildList, 1, 1, 0, changes, buildNums);
    }

    @Test
    public void testTwoCompletionTwoBuild() {
        List<ResultsSummary> buildList = new ArrayList<ResultsSummary>();
        buildList.add(getChainResultsSummary(false, true, "2", 2, day2));
        buildList.add(getChainResultsSummary(false, true, "1", 1, day1));
        List<Integer> changes = new ArrayList<Integer>();
        List<Integer> buildNums = new ArrayList<Integer>();
        changes.add(1);
        buildNums.add(2);
        changes.add(1);
        buildNums.add(1);
        checkExpected(buildList, 1, 1, 0.5, changes, buildNums);
    }

    private void checkExpected(List<ResultsSummary> list, double percentage, double changes, double frequency,
                               List<Integer> completionsChanges, List<Integer> buildNumbers) {
        PerformanceSummary cdp = PerformanceSummaryCreator.create(list, cb);
        assertEquals("Success percentage isn't as expected", percentage, cdp.getSuccessPercentage(), 0.0001);
        assertEquals("Average changes isn't as expected", changes, cdp.getAverageChanges(), 0.0001);
        assertEquals("Average frequency isn't as expected", frequency, cdp.getAverageFrequency(), 0.0001);
        List<CompletionStats> completions = cdp.getCompletions();
        assertEquals("Number of completions isn't as expected", buildNumbers.size(), completions.size());
        for (int i = 0; i < completions.size(); i++) {
            assertEquals("Build number of completion " + i + " isn't as expected", (int) buildNumbers.get(i), completions.get(i).getBuildNumber());
            assertEquals("Changes of completion " + i + " isn't as expected", (int) completionsChanges.get(i), completions.get(i).getNumChanges());
        }
    }

    private ChainResultsSummary getChainResultsSummary(boolean cont, boolean succ, String name, int buildNumber, Date date) {
        ChainResultsSummary result = mock(ChainResultsSummary.class);
        when(result.isContinuable()).thenReturn(cont);
        when(result.isSuccessful()).thenReturn(succ);
        Commit c = mock(Commit.class);
        when(c.getComment()).thenReturn("comment");
        Author a = mock(Author.class);
        when(a.getLinkedUserName()).thenReturn(name);
        when(a.getName()).thenReturn(name);
        when(c.getAuthor()).thenReturn(a);
        when(c.getDate()).thenReturn(new Date());
        ImmutableList<Commit> commits = ImmutableList.of(c);
        when(result.getCommits()).thenReturn(commits);
        when(result.getBuildCompletedDate()).thenReturn(date);
        when(result.getBuildNumber()).thenReturn(buildNumber);
        return result;
    }
}
