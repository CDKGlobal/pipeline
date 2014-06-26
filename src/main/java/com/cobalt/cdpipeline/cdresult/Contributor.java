package com.cobalt.cdpipeline.cdresult;

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
	 * Gets the username of this contributor.
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	
	@Override
	public boolean equals(Object o){
		if (o instanceof Contributor){
			Contributor other = (Contributor) o;
			if(other.username.equals(this.username)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return this.username.hashCode();
	}
	
}
