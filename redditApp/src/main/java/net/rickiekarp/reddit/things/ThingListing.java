package net.rickiekarp.reddit.things;

public class ThingListing {
	private String kind;
	private ThingInfo data;
	
	public void setKind(String kind) {
		this.kind = kind;
	}
	public String getKind() {
		return kind;
	}
	
	public void setData(ThingInfo data) {
		this.data = data;
	}
	public ThingInfo getData() {
		return data;
	}
}
