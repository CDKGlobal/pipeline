package com.cobalt.cdpipeline.cdresult;

import java.util.Date;

public class Contributor {
	private String username;
	private int numCommits;
	private Date lastCommit;
	private String fullname;
	private String pictureUrl;
	private String profilePageUrl;
	
	/**
	 * Constructs a Contributor object.
	 * 
	 * @param username of the contributor
	 */
	public Contributor(String username, Date commitTime, String fullname, String pictureUrl, String profilePageUrl) {
		this.username = username;
		this.numCommits = 1;
		this.lastCommit = commitTime;
		this.fullname = fullname;
		this.pictureUrl = pictureUrl;
		this.profilePageUrl = profilePageUrl;
		
	}
	
	/**
	 * Gets the username of this contributor.
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Gets the number of commits of this contributor after the last deployment.
	 * @return the number of commits
	 */
	public int getNumCommits(){
		return numCommits;
	}
	
	/**
	 * Gets the most recent commit time of this contributor.
	 * @return the date and time of last commit
	 */
	public Date getLastCommitTime(){
		return lastCommit;
	}
	
	/**
	 * Updates the last commit time if the given time is more recent.
	 * @param commitTime a commit time of this contributor
	 */
	void updateLastCommitTime(Date commitTime){
		if(commitTime.compareTo(lastCommit) > 0){
			lastCommit = commitTime;
		}
	}
	
	/**
	 * Increments the number of commits by 1.
	 */
	void incrementNumCommits(){
		numCommits++;
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
