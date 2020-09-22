package com.VO;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class BaseVO implements Parcelable{
	private boolean success;
	private String Message;
	private String name;
	private String email;
	private String mobile;

	public BaseVO(){

	}
	public BaseVO(JSONObject obj){
		this.setSuccess(obj.optBoolean("success"));
		this.setMessage(obj.optString("message"));
		this.name = obj.optString("name");
		this.email = obj.optString("email");
		this.mobile = obj.optString("mobile");
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
	public String getName() {
		return name;
	}
	public String getEmail() {
		return email;
	}
	public String getMobile() {
		return mobile;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}
	
	public static final Parcelable.Creator<BaseVO> CREATOR = new Parcelable.Creator<BaseVO>() {
		public BaseVO[] newArray(int size) {
			return new BaseVO[size];
		}

		public BaseVO createFromParcel(Parcel in) {
			return new BaseVO(in);
		}
	};
	
	public BaseVO(Parcel in){
	}
}
