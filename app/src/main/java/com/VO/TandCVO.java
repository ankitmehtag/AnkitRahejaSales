package com.VO;

import org.json.JSONObject;

public class TandCVO extends BaseVO{

//	term_condition: "<p><strong>See Application form</strong></p> "
//		booking_amt: "0"
//		eauction_token: "0"
//		type: "Buy"
	
	private String term_condition;
	private String booking_amt;
	private String eauction_token;
	private String type;
	
	public TandCVO(JSONObject obj) {
		super(obj);
		
		JSONObject data = obj.optJSONObject("data");
		
		setBooking_amt(data.optString("booking_amt"));
		setEauction_token(data.optString("eauction_token"));
		setTerm_condition(data.optString("term_condition"));
		setType(data.optString("type"));
		
	}



	public String getTerm_condition() {
		return term_condition;
	}



	public void setTerm_condition(String term_condition) {
		this.term_condition = term_condition;
	}



	public String getBooking_amt() {
		return booking_amt;
	}



	public void setBooking_amt(String booking_amt) {
		this.booking_amt = booking_amt;
	}



	public String getEauction_token() {
		return eauction_token;
	}



	public void setEauction_token(String eauction_token) {
		this.eauction_token = eauction_token;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}

	
	
}
