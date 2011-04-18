package org.emonocot.model.description;

import org.apache.commons.lang.ObjectUtils;



public class TextContent extends Content {
	
	private String content;

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}
	
	@Override
	public boolean equals(Object other) {
		if(!super.equals(other)) {
			return false;
		}
		TextContent content = (TextContent)other;
	    return ObjectUtils.equals(this.content,content.content);
	}
}
