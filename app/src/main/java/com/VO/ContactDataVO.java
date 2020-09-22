package com.VO;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class ContactDataVO implements Parcelable {

	private  String address;
	private String phone;
	private String email1;
	private String email2;

	public ContactDataVO(JSONObject o) {
		setAddress(o.optString("address"));
		setPhone(o.optString("phone"));
		setEmail_one(o.optString("email1"));
		setEmail_second(o.optString("email2"));

	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail_one() {
		return email1;
	}

	public void setEmail_one(String email1) {
		this.email1 = email1;
	}

	public String getEmail_second() {
		return email2;
	}

	public void setEmail_second(String email2) {
		this.email2 = email2;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub

		dest.writeString(getAddress());
		dest.writeString(getPhone());
		dest.writeString(getEmail_one());
		dest.writeString(getEmail_second());

	}
	
	public static final Parcelable.Creator<ContactDataVO> CREATOR = new Parcelable.Creator<ContactDataVO>() {
		public ContactDataVO[] newArray(int size) {
			return new ContactDataVO[size];
		}

		public ContactDataVO createFromParcel(Parcel in) {
			return new ContactDataVO(in);
		}
	};
	
	
	

	private ContactDataVO(Parcel in) {
		setAddress(in.readString());
		setPhone(in.readString());
		setEmail_one(in.readString());
		setEmail_second(in.readString());

	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

}
