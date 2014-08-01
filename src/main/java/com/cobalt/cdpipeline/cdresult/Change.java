package com.cobalt.cdpipeline.cdresult;

import java.util.Date;

public class Change {
	private String author;
	private int buildNumber;
	private String comment;
	private Date date;
	
	public Change(String author, int buildNumber, String comment, Date date) {
		this.author = author;
		this.comment = comment;
		this.date = date;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public int getBuildNumber() {
		return buildNumber;
	}
	
	public String getComment() {
		return comment;
	}
	
	public Date getDateFormatted() {
		return new Date(date.getTime());
	}
	
}
