package com.model;

import com.VO.BaseVO;
import com.exception.BMHException;
import com.helper.ContentLoader;
import com.helper.UrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginModel {
	
	public BaseVO Register(String name, String email, String mobileNo, String password) throws BMHException{
		BaseVO basevo = null;
		String url = UrlFactory.getRegisterUrl();
		String params = "first_name="+name+"&email="+email+"&mobile="+mobileNo+"&password="+password;
		String response=ContentLoader.getJsonUsingPost(url, params);
		System.out.println("hs serverHit= "+url);
		System.out.println("hs params= "+params);
		 if(response==null){
			 System.out.println("hs response null");
			 return null;
		 }
		 System.out.println("hs response= "+response);
		 try {
			JSONObject jsonObj= new JSONObject(response);
			if(jsonObj != null){
				basevo = new BaseVO(jsonObj);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		 return basevo;
	}
	
	/*public LoginVO Login(String email, String password) throws BMHException{
		LoginVO basevo = null;
		String url = UrlFactory.getLoginUrl();
		String params = "email="+email+"&password="+password;
		String response=ContentLoader.getJsonUsingPost(url, params);
		System.out.println("hs serverHit= "+url);
		System.out.println("hs params= "+params);
		 if(response==null){
			 System.out.println("hs response null");
			 return null;
		 }
		 System.out.println("hs response= "+response);
		 try {
			JSONObject jsonObj= new JSONObject(response);
			if(jsonObj != null){
				basevo = new LoginVO(jsonObj);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		 return basevo;
	}*/
	/*public LoginVO LoginWithFb(final String fb_id,final String email, String username) throws BMHException{
		LoginVO basevo = null;
		String url = UrlFactory.getThirdPartyLoginUrl();
		String params = "email="+email+"&fb_id="+fb_id+"&username="+username;
		String response=ContentLoader.getJsonUsingPost(url, params);
		System.out.println("hs serverHit= "+url);
		System.out.println("hs params= "+params);
		 if(response==null){
			 System.out.println("hs response null");
			 return null;
		 }
		 System.out.println("hs response= "+response);
		 try {
			JSONObject jsonObj= new JSONObject(response);
			if(jsonObj != null){
				basevo = new LoginVO(jsonObj);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		 return basevo;
	}
*/
//	public BaseVO PersonalDetails(String first_name, String last_name ,String email, String mobile, 
//			String dob ,String coapplicant, String pan, String address, String city,
//			String state , String country , String zip ) throws BMHException{
//		BaseVO basevo = null;
//		String url = UrlFactory.getPersonDetails();
//		String params = "first_name="+first_name+"last_name"+last_name+ "&email="+email+"&mobile="+mobile+"&dob="+dob
//				+"coapplicant="+coapplicant+"pan="+pan+"address="+address+"city="+city+"state="+state+"country="+country
//				+"zip="+zip;
//		String response=ContentLoader.getJsonUsingPost(url, params);
//		System.out.println("hs serverHit= "+url);
//		System.out.println("hs params= "+params);
//		 if(response==null){
//			 System.out.println("hs response null");
//			 return null;
//		 }
//		 System.out.println("hs response= "+response);
//		 try {
//			JSONObject jsonObj= new JSONObject(response);
//			if(jsonObj != null){
//				basevo = new BaseVO(jsonObj);
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		 return basevo;
//	}


}
