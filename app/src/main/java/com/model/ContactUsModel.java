package com.model;

import com.exception.BMHException;
import com.helper.ContentLoader;

import org.json.JSONException;
import org.json.JSONObject;

public class ContactUsModel {
	public JSONObject send(String url,String param){
		
		System.out.println("hs serverHit= " + url);
		System.out.println("hs value= " + param);
		String response = null;
		try {
			response = ContentLoader.getJsonUsingPost(url, param);
		} catch (BMHException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (response == null) {
			System.out.println("hs response null");
			return null;
		}
		System.out.println("hs response= " + response);
		JSONObject jsonObj = null;
		try {
			jsonObj = new JSONObject(response);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObj;
	}
}



//package com.model;
//
//import com.model.UserModel.LocalityData;
//
//public class ContactUsModel {
//
//	private LocalityData data;
//
//	private String success;
//
//	public LocalityData getLocalityData() {
//		return data;
//	}
//
//	public void setLocalityData(LocalityData data) {
//		this.data = data;
//	}
//
//	public String getSuccess() {
//		return success;
//	}
//
//	public void setSuccess(String success) {
//		this.success = success;
//	}
//
//	@Override
//	public String toString() {
//		return "ClassPojo [data = " + data + ", success = " + success + "]";
//	}
//
//	public class LocalityData {
//
//		private String firstname;
//		private String lastname;
//		private String email;
//		private String contactno;
//		private String message;
//
//		public String getfirstname() {
//			return firstname;
//		}
//
//		public void setfirstname(String firstname) {
//			this.firstname = firstname;
//		}
//
//		public String getlastname() {
//			return lastname;
//		}
//
//		public void setlastname(String lastname) {
//			this.lastname = lastname;
//		}
//
//		public String getemail() {
//			return email;
//		}
//
//		public void setemail() {
//			this.email = email;
//		}
//
//		public String getcontactno() {
//			return contactno;
//		}
//
//		public void setcontactno() {
//			this.contactno = contactno;
//		}
//
//		public String getmessage() {
//			return message;
//		}
//
//		public void setmessage() {
//			this.message = message;
//		}
//
//		@Override
//		public String toString() {
//			return "ClassPojo [firstname = " + firstname + ", lastname = " + lastname + ", email = " + email
//					+ ", contactno = " + contactno + ", message = " + message + "]";
//		}
//
//	}
//
//}
