package net.rickiekarp.reddit.things;

public class ListingData {
	private ThingListing[] children;
	private String after;
	private String before;
	private String modhash;
	
	public void setAfter(String after) {
		this.after = after;
	}
	public String getAfter() {
		return after;
	}
	
	public void setBefore(String before) {
		this.before = before;
	}
	public String getBefore() {
		return before;
	}
	
	public void setModhash(String modhash) {
		this.modhash = modhash;
	}
	public String getModhash() {
		return modhash;
	}
	
	public void setChildren(ThingListing[] children) {
		this.children = children;
	}
	public ThingListing[] getChildren() {
		return children;
	}
}