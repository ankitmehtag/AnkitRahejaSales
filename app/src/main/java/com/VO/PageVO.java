package com.VO;

import org.json.JSONObject;

public class PageVO extends BaseVO{
	private String page;
	private String content;

	public PageVO(JSONObject obj) {
		super(obj);
		JSONObject objdata = obj.optJSONObject("data");
		if(objdata!= null){
			setContent(objdata.optString("content"));
		}
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
