package com.model;

public class UserModel {
	private Data data;

	private String success;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}
	public class Data {
		private String first_name;
		private String last_name;
		private String email;
		private String mobile;
		private String dob;
		private String coapplicant;
		private String pan_no;
		private String address;
		private String nationality;
		private String city;
		private String state;
		private String zip;
		private String country;
		private String throughAgent;

		public String getCoapplicant() {
			return coapplicant;
		}

		public void setCoapplicant(String coapplicant) {
			this.coapplicant = coapplicant;
		}

		public String getFirst_name() {
			return first_name;
		}

		public void setFirst_name(String first_name) {
			this.first_name = first_name;
		}

		public String getLast_name() {
			return last_name;
		}

		public void setLast_name(String last_name) {
			this.last_name = last_name;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}

		public String getDob() {
			return dob;
		}

		public void setDob(String dob) {
			this.dob = dob;
		}

		public String getPan_no() {
			return pan_no;
		}

		public void setPan_no(String pan_no) {
			this.pan_no = pan_no;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getNationality() {
			return nationality;
		}

		public void setNationality(String nationality) {
			this.nationality = nationality;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getZip() {
			return zip;
		}

		public void setZip(String zip) {
			this.zip = zip;
		}

		public String getCountry() {
			return country;
		}

		public void setCountry(String country) {
			this.country = country;
		}

		public String getThroughAgent() {
			return throughAgent;
		}

		public void setThroughAgent(String throughAgent) {
			this.throughAgent = throughAgent;
		}
	}

}
