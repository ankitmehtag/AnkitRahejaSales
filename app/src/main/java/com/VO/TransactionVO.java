package com.VO;

import org.json.JSONObject;

public class TransactionVO extends BaseVO{
	
	private String post_url;
	private String order_id;
	private String encRequest;
	private String access_code;
	private String merchant_id;

	public String getMerchant_id() {
		return merchant_id;
	}

	public void setMerchant_id(String merchant_id) {
		this.merchant_id = merchant_id;
	}

	public TransactionVO(JSONObject obj){
		super(obj);
		
		setPost_url(obj.optString("post_url"));
		setOrder_id(obj.optString("order_id"));
		setEncRequest(obj.optString("encRequest"));
		setAccess_code(obj.optString("access_code"));
		setMerchant_id(obj.optString("merchant_id"));
	}

	public String getPost_url() {
		return post_url;
	}

	public void setPost_url(String post_url) {
		this.post_url = post_url;
	}

	public String getEncRequest() {
		return encRequest;
	}

	public void setEncRequest(String encRequest) {
		this.encRequest = encRequest;
	}

	public String getAccess_code() {
		return access_code;
	}

	public void setAccess_code(String access_code) {
		this.access_code = access_code;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	
}
