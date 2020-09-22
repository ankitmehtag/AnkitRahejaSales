package com.VO;

import java.io.Serializable;

public class MediaGellaryVO implements Serializable {

	private String url;
	private int type;
	private int subtype;
	private String image_name;
	private String media_count;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getSubtype() {
		return subtype;
	}

	public void setSubtype(int subtype) {
		this.subtype = subtype;
	}

	public String getImage_name() {
		return image_name;
	}

	public void setImage_name(String image_name) {
		this.image_name = image_name;
	}

	public String getMedia_count() {
		return media_count;
	}

	public void setMedia_count(String media_count) {
		this.media_count = media_count;
	}
}
