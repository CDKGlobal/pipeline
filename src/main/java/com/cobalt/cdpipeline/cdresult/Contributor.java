package com.cobalt.cdpipeline.cdresult;

/**
 * 
 */
public class Contributor {
	private String username;
	
	/**
	 * Constructs a Contributor object.
	 * 
	 * @param username of the contributor
	 */
	public Contributor(String username) {
		this.username = username;
	}
	
	/**
	 * Gets the username of this.
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	
	// TODO override equals
}
