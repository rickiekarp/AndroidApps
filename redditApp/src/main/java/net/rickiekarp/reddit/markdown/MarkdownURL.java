package net.rickiekarp.reddit.markdown;

import java.io.Serializable;

public class MarkdownURL implements Comparable<MarkdownURL>, Serializable {
	static final long serialVersionUID = 29;
	
	public int startOffset;
	public String url;
	public String anchorText;
	
	public MarkdownURL(int startOffset, String url, String anchorText) {
		this.startOffset = startOffset;
		this.url = url;
		this.anchorText = anchorText;
	}
	
	public int compareTo(MarkdownURL other) {
		if (startOffset < other.startOffset)
			return -1;
		else if (startOffset > other.startOffset)
			return 1;
		return 0;
	}
}
