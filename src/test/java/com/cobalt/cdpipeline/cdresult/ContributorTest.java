package com.cobalt.cdpipeline.cdresult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.cobalt.cdpipeline.cdresult.Contributor;

public class ContributorTest {
	Contributor super1, super2, super3;
    	
    @Before
    public void setup() {
        super1 = new Contributor("super pipe");
        super2 = new Contributor("Super Pipe");
        super3 = new Contributor("SUPER PIPE");
    }
    
	// test constructor

	@Test
	public void test_constuctor() {
		new Contributor("Like Boss");
	}
	
	// test equals()
	
	@Test
	public void test_equals_same_name() {
        Contributor superfake = new Contributor("super pipe");
		assertTrue(super1.equals(superfake));
	}
	
	@Test
	public void test_equals_unique_names() {
        Contributor anotherUser = new Contributor("super pie");
		assertFalse(super1.equals(anotherUser));
	}
	
	@Test
	public void test_unique_names_same_chars() {
        assertFalse(super1.equals(super2));
        assertFalse(super1.equals(super3));
	}
	
	// test hashcode()
	
    @Test
	public void test_hash_same_name() {
        Contributor superfake = new Contributor("super pipe");
		assertEquals(super1, superfake);
	}
	
	@Test
	public void test_hash_unique_names() {
        Contributor anotherUser = new Contributor("old pie");
        assertTrue(super1 != anotherUser);

	}
	
	@Test
	public void test_hash_names_same_chars() {
        assertTrue(super1 != super2);
        assertTrue(super1 != super3);
	}
}
