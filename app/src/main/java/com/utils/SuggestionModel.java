package com.utils;

public class SuggestionModel {

	private LocalityData[] localityData;

	private String success;

	public LocalityData[] getLocalityData() {
		return localityData;
	}

	public void setLocalityData(LocalityData[] localityData) {
		this.localityData = localityData;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	@Override
	public String toString() {
		return "ClassPojo [localityData = " + localityData + ", success = " + success + "]";
	}

//	public class LocalityData {
//		private String id;
//
//		private String title;
//
//		private String subtitle;
//
//		public String getId() {
//			return id;
//		}
//
//		public void setId(String id) {
//			this.id = id;
//		}
//
//		public String getTitle() {
//			return title;
//		}
//
//		public void setTitle(String title) {
//			this.title = title;
//		}
//
//		public String getSubtitle() {
//			return subtitle;
//		}
//
//		public void setSubtitle(String subtitle) {
//			this.subtitle = subtitle;
//		}
//
//		@Override
//		public String toString() {
//			return "ClassPojo [id = " + id + ", title = " + title	+ ", subtitle = " + subtitle + "]";
//		}
//	}

}