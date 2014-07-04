package com.cobalt.cdpipeline.cdresult;

import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.atlassian.bamboo.author.Author;
import com.atlassian.bamboo.commit.Commit;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class AddAllAuthorsInCommitsTest {

	private static final int COMMIT_LIST_SIZE = 10; // >= 3
	CDResult cdresult; 
	ContributorBuilder cb = new ContributorBuilder("testHostname", "testUsername", "testPassword");

	@Before
	public void setup() {
		cdresult = new CDResult("project", "project - plan", "project", "plan");		
	}
	
	@Test
	public void testNoCommit() {
		List<Commit> commits = createCommitListWithoutAuthors(0);
    	CDResultFactory.addAllAuthorsInCommits(cdresult, commits, cb);
    	assertEquals("0 commit should have 0 contributor", 0, cdresult.getContributors().size());   
	}
		
	@Test
	public void testOneCommit() {
		List<Commit> commits = createCommitListWithoutAuthors(1);
		List<Author> authors = createAuthorListWithNames(1);
		for (int i = 0; i < 1; i++) {
	    	when(commits.get(i).getAuthor()).thenReturn(authors.get(i));
		}
		CDResultFactory.addAllAuthorsInCommits(cdresult, commits, cb);
    	assertEquals("1 commit and 1 contributor", 1, cdresult.getContributors().size());  
	}
		
	@Test
	public void testManyCommitsUniqueAuthors() {
		// create lists of commits and their corresponding authors
		List<Commit> commits = createCommitListWithoutAuthors(COMMIT_LIST_SIZE);
		List<Author> authors = createAuthorListWithNames(10);
		// assign unique authors to each commit
		for (int i = 0; i < COMMIT_LIST_SIZE; i++) {
	    	when(commits.get(i).getAuthor()).thenReturn(authors.get(i));
		}

		CDResultFactory.addAllAuthorsInCommits(cdresult, commits, cb);
    	// check that all unique authors are counted
    	assertEquals("10 commits, each w/ unique contributor", COMMIT_LIST_SIZE, cdresult.getContributors().size());    	
	}
	
	@Test
	public void testMultipleCommitsDuplicateAuthors() {
		List<Commit> commits = createCommitListWithoutAuthors(COMMIT_LIST_SIZE);
		List<Author> authors = createAuthorListWithNames(3);			
		for (int i = 0; i < COMMIT_LIST_SIZE; i++) {
	    	when(commits.get(i).getAuthor()).thenReturn(authors.get(i % 3));
	    	when(commits.get(i).getDate()).thenReturn(new Date());
		}
		
		CDResultFactory.addAllAuthorsInCommits(cdresult, commits, cb);
    	// check the duplicate authors are only counted once
    	assertEquals("10 commits w/ duplicate contributors", 3, cdresult.getContributors().size()); 	
	}
	
	
	// private helper methods
	
	/*
	 * Create a list of commits w/o assigning them the authors
	 */
	private List<Commit> createCommitListWithoutAuthors(int numCommits) {
		List<Commit> commits = new ArrayList<Commit>();
		for (int i = 0; i < numCommits; i++) {
			commits.add(mock(Commit.class));
		}
		return commits;
	}

	/*
	 * Create a list of authors w/o assigning them the names
	 */
	private List<Author> createAuthorListWithNames(int numAuthors) {
		List<Author> authors = new ArrayList<Author>();
		for (int i = 0; i < numAuthors; i++) {
			authors.add(mock(Author.class));
		}
		for (int i = 0; i < numAuthors; i++) {
	    	when(authors.get(i).getFullName()).thenReturn("author" + i);
		}
		return authors;
	}
}
