package net.rickiekarp.reddit.things;

public class Listing {
	private String kind;
	private ListingData data;
	
	public Listing() {}
	
	public Listing(String stuff) {
		kind = null;
		data = null;
	}
	
	public void setKind(String kind) {
		this.kind = kind;
	}
	public String getKind() {
		return kind;
	}
	
	public void setData(ListingData data) {
		this.data = data;
	}
	public ListingData getData() {
		return data;
	}
}