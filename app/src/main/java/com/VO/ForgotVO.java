package com.VO;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class ForgotVO implements Parcelable{
	
	private String  email;
	
	public ForgotVO(JSONObject o) {
		setEmail(o.optString("email"));
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub

		dest.writeString(getEmail());

	}
	
	public static final Parcelable.Creator<ForgotVO> CREATOR = new Parcelable.Creator<ForgotVO>() {
		public ForgotVO[] newArray(int size) {
			return new ForgotVO[size];
		}

		public ForgotVO createFromParcel(Parcel in) {
			return new ForgotVO(in);
		}
	};
	

	private ForgotVO(Parcel in) {
		setEmail(in.readString());
		

	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}


}
