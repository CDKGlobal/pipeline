package com.cobalt.bamboo.plugin.pipeline.cdresult;

import java.util.Date;
import java.util.List;

public class Change {
	private String authorFullName;
	private String authorPictureUrl;
	private int buildNumber;
	private String comment;
	private Date date;
	private String timeElapsed;
	private List<String> files;
	private String revisionNumber;
	
	
	/**
	 * Construct a Change object
	 * @param author			the name of the contributor
	 * @param authorPictureUrl	the link to the contributor's profile page
	 * @param buildNumber		the build number
	 * @param comment			the comment on the change
	 * @param date				the date of the change
	 * @param files				the files associated with the change
	 * @param revisionNumber	the revision number of the change
	 */
	public Change(String authorFullName, String authorPictureUrl, int buildNumber, String comment,
					Date date, List<String> files, String revisionNumber) {
        this.authorFullName = authorFullName;
		this.authorPictureUrl = authorPictureUrl;
		this.buildNumber = buildNumber;
		this.comment = comment;
		this.date = date;
		this.files = files;
		this.revisionNumber = revisionNumber;
	}

	/**
	 * Get the full name of the contributor
	 * @return the full name of the contributor
	 */
	public String getAuthorFullName() {
		return authorFullName;	
	}
	
	/**
	 * Get the link to the contributor's profile page
	 * @return the link to the contributor's profile page
	 */
	public String getAuthorPictureUrl() {
		return authorPictureUrl;
	}
	
	/**
	 * Get the build number
	 * @return the build number
	 */
	public int getBuildNumber() {
		return buildNumber;
	}
	
	/**
	 * Get the comment on the change
	 * @return the comment on the change
	 */
	public String getComment() {
		return comment;
	}
	
	/**
	 * Get the date of the change
	 * @return the date of the change
	 */
	public String getDateFormatted() {
		return date.toString();
	}
	
	/**
	 * Get the time elapsed since the change:
	 * - "XYZ" minute/hour/day/month/year ago if the change was made within the same hour/day/month/year/...
	 * - "Just now" if the change was made within the same minute
	 */
	public String getTimeElapsed() {
		Date today = new Date();
		int minuteDiff = today.getMinutes() - date.getMinutes();
		int hourDiff = today.getHours() - date.getHours();
		int dayDiff = today.getDate() - date.getDate();
		int monthDiff = today.getMonth() - date.getMonth();
		int yearDiff = today.getYear() - date.getYear();
			
		if (yearDiff == 0 && monthDiff == 0 && dayDiff == 0 && hourDiff == 0 && minuteDiff == 0) {
			timeElapsed = "Just now";
		} else if (yearDiff == 0 && monthDiff == 0 && dayDiff == 0 && hourDiff == 0) {
			timeElapsed = String.format("%d minute%s ago", minuteDiff, minuteDiff > 1 ? "s" : "");
		} else if (yearDiff == 0 && monthDiff == 0 && dayDiff == 0) {
			timeElapsed = String.format("%d hour%s ago", hourDiff, hourDiff > 1 ? "s" : "");
		} else if (yearDiff == 0 && monthDiff == 0) {
			timeElapsed = String.format("%d day%s ago", dayDiff, dayDiff > 1 ? "s" : "");
		} else if (yearDiff == 0) {
			timeElapsed = String.format("%d month%s ago", monthDiff, monthDiff > 1 ? "s" : "");
		} else {
			timeElapsed = String.format("%d year%s ago", yearDiff, yearDiff > 1 ? "s" : "");
		}
		
		return timeElapsed;
	}
	
	/**
	 * Get the files associated with the change
	 * @return 	the files associated with the change
	 */
	public List<String> getFiles() {
		return files;
	}
	
	/**
	 * Get the revision number of the change
	 * @return the revision number of the change
	 */
	public String getRevisionNumber() {
		return revisionNumber;
	}
}
