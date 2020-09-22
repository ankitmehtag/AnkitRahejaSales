package com.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.exception.BMHException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class ContentLoader {

	private final static int TIMEOUT= 15*1000;
	private static final String JSON_CONTENT_TYPE = "application/json";
	
	public static final String getJsonFromUrl(final String url)
			throws BMHException {
		final byte[] json = getContentFromServer(url, JSON_CONTENT_TYPE);
		if (json != null)
			return new String(json);
		else
			return "";
	}

	/*
	 * Post method for API call
	 */
	public static final String getJsonUsingPost(final String url, String params)
			throws BMHException {

		final byte[] json = getPostContentFromServer(url, JSON_CONTENT_TYPE, params);
		if (json != null)
			return new String(json);
		return null;
	}

	private static final byte[] getContentFromServer(String url,final String contentType) throws BMHException {

		HttpURLConnection urlConnection = null;
		byte[] byteResponse = null;
		InputStream io = null;

		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		try {
			URL jsonUrl = new URL(url);
			urlConnection = (HttpURLConnection) jsonUrl.openConnection();
			urlConnection.setConnectTimeout(TIMEOUT);
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("User-Agent","Android");
			
			
//			urlConnection.setRequestProperty("TK-API-KEY",
//					UrlFactory.TK_API_KEY);
//			urlConnection.setDoOutput(true);
			urlConnection.connect();
			int responseCode = urlConnection.getResponseCode();
			
			System.out.println("Response Code: " + responseCode);
			
			if(responseCode== HttpURLConnection.HTTP_OK){
				io = urlConnection.getInputStream();
				int ch;
				while ((ch = io.read()) != -1) {
					bo.write(ch);
				}
				byteResponse = bo.toByteArray();
			}
		} catch (IOException e) {
			 e.printStackTrace();
			throw new BMHException(e.getMessage());
		} catch (Exception e) {
			 e.printStackTrace();
			throw new BMHException(e.getMessage());
		}
		finally {
			try {
				bo.close();
				bo = null;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				if (io != null) {
					io.close();
					io = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (urlConnection != null){
				urlConnection.disconnect();
				urlConnection = null;
			}
		}
		return byteResponse;
	}

	/*
	 * Post request to API
	 */

 private static final byte[] getPostContentFromServer(final String url,final String contentType,final String params)throws BMHException {
		
		HttpURLConnection urlConnection = null;
		byte[] byteResponse = null;
		InputStream io = null;

		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		try {
			URL jsonUrl = new URL(url);
			urlConnection = (HttpURLConnection) jsonUrl.openConnection();
			urlConnection.setRequestMethod("POST");
			urlConnection.setRequestProperty("User-Agent",	"Android");
			urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
//			urlConnection.setRequestProperty("TK-API-KEY",
//					UrlFactory.TK_API_KEY);
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			urlConnection.connect();
			
			 DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
	         wr.writeBytes(params);
	         wr.flush();
	         wr.close();
	         int responseCode = urlConnection.getResponseCode();
	         if(responseCode== HttpURLConnection.HTTP_OK){
				io = urlConnection.getInputStream();
				int ch;
				while ((ch = io.read()) != -1) {
					bo.write(ch);
				}
				byteResponse = bo.toByteArray();
	         }

//		} catch (ConnectionNotFoundException e) {
//			// e.printStackTrace();
//			throw new TastyKhanaException(e.getMessage());
//
		} catch (IOException e) {
			 e.printStackTrace();
			throw new BMHException(e.getMessage());
		} catch (Exception e) {
			 e.printStackTrace();
			throw new BMHException(e.getMessage());
		}
		finally {
			try {
				bo.close();
				bo = null;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				if (io != null) {
					io.close();
					io = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (urlConnection != null){
				urlConnection.disconnect();
				urlConnection = null;
			}
		}
		return byteResponse;
	}
	
	/**
	 * Method used to get the image bitmap object by downloading the image from server using URL
	 * @param imageUrl 
	 * @return Bitmap object to be set on {@link ImageView}
	 * @throws BMHException
	 */
	public static Bitmap getImage(String imageUrl) throws BMHException{
		Bitmap bmp = null;
		URL url;
		try {
				url = new URL(imageUrl);
				URLConnection urlConnection = url.openConnection();
				if(urlConnection!=null){
					InputStream is = urlConnection.getInputStream();
					if(is!=null){
						bmp = BitmapFactory.decodeStream(is);
					}
				}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new BMHException(e.getMessage());
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new BMHException(e.getMessage());
		}
		return bmp;
	}
	
	public boolean getImage(String iconUrl, String filePathToSave) throws BMHException{
		boolean isImageSaved  = false;
		URL url;
		try {
				int byteread = 0;
				url = new URL(iconUrl);
				URLConnection urlConnection = url.openConnection();
				if(urlConnection!=null){
					InputStream is = urlConnection.getInputStream();
					FileOutputStream fs = new FileOutputStream(filePathToSave);
					byte[] buffer = new byte[1444];
			        while ((byteread = is.read(buffer)) != -1) {
			            fs.write(buffer, 0, byteread);
			        }
			        fs.close();
			        is.close();
				}
				isImageSaved = true;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new BMHException(e.getMessage());
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new BMHException(e.getMessage());
		}
		return isImageSaved;
	}
	
	
	public static String UploadImage(final Bundle fileInfoBundle, final String url,String filepath) throws BMHException {
		DataOutputStream dos = null;
		// Open a HTTP connection to the URL
		
		HttpURLConnection conn = null;
		try {
			System.out.println("hh server hit ="+url);
			conn = (HttpURLConnection) new URL(url).openConnection();
			
			conn.setRequestProperty("User-Agent", System.getProperties().getProperty("http.agent"));
			conn.setRequestMethod("POST");
			String strBoundary = "3i2ndDfv2rTHiSisAbouNdArYfORhtTPEefj3q2f";
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="	+ strBoundary);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setChunkedStreamingMode(1024);
			conn.setRequestProperty("Connection", "Keep-Alive");
			String endLine = "\r\n";
			
			 int bytesRead, bytesAvailable, bufferSize;
			    byte[] buffer;
			    int maxBufferSize = 1 * 1024;
			
			try {
				conn.connect();
				dos = new DataOutputStream(conn.getOutputStream());
				dos.writeBytes("--" + strBoundary + endLine);
				 FileInputStream fileInputStream = new FileInputStream(new File(
						 filepath));

				for (String key : fileInfoBundle.keySet()) {
					// write the clip id in the output stream.
					// if (fileInfoBundle.getByteArray(key) != null) {
					if (key.equals("image")) {
						dos.writeBytes("Content-Disposition: form-data; name=\"prop_images\"; filename=\"thumb.png\""	+ endLine);
						dos.writeBytes("Content-Type: application/octet-stream"	+ endLine + endLine);
						//dos.write(fileInfoBundle.getByteArray(key));
						bytesAvailable = fileInputStream.available();
				        bufferSize = Math.min(bytesAvailable, maxBufferSize);
				        buffer = new byte[bufferSize];

				        // Read file
				        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				        Log.e("Image length", bytesAvailable + "");
				        try {
				            while (bytesRead > 0) {
				                try {
				                	dos.write(buffer, 0, bufferSize);
				                } catch (OutOfMemoryError e) {
				                    e.printStackTrace();
				               
				                   
				                }
				                bytesAvailable = fileInputStream.available();
				                bufferSize = Math.min(bytesAvailable, maxBufferSize);
				                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				            }
				        } catch (Exception e) {
				            e.printStackTrace();
				           
				        }finally{
				        	fileInputStream.close();
				        }
									
						
						dos.writeBytes(endLine + "--" + strBoundary + endLine);
					} else {
						dos.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n"  + fileInfoBundle.getString(key));
						dos.writeBytes(endLine + "--" + strBoundary + endLine);
					}
				}
				// int serverResponseCode = conn.getResponseCode();
				// String serverResponseMessage = conn.getResponseMessage();
				// System.out.println("hh Upload file to server"+"HTTP Response is : "
				// + serverResponseMessage + ": " + serverResponseCode);
				dos.flush();
				dos.close();
			} catch (MalformedURLException ex) {
				ex.printStackTrace();
				//Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				dos.close();
				
			}
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			return receiveResponse(conn);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static String receiveResponse(HttpURLConnection conn)
			throws IOException {
		StringBuilder response = new StringBuilder();
		 int status = conn.getResponseCode();
		 if (status == HttpURLConnection.HTTP_OK) {
	            BufferedReader reader = new BufferedReader(new InputStreamReader( conn.getInputStream()));
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                response.append(line);
	            }
	            reader.close();
	            conn.disconnect();
	        } else {
	            throw new IOException("Server returned non-OK status: " + status);
	        }
	 
	        return response.toString();
	}
	
	public static  byte[] getPostContentFromServer(final String url,final String params) throws Exception {
        System.out.println("hh Register on Sercer: " + url);
		HttpURLConnection urlConnection = null;
		byte[] byteResponse = null;
		InputStream io = null;

		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		try {
			URL jsonUrl = new URL(url);
			urlConnection = (HttpURLConnection) jsonUrl.openConnection();
			urlConnection.setRequestMethod("POST");
			urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			urlConnection.connect();

			DataOutputStream wr = new DataOutputStream(	urlConnection.getOutputStream());
			wr.writeBytes(params);
			wr.flush();
			wr.close();
			int responseCode = urlConnection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				io = urlConnection.getInputStream();
				int ch;
				while ((ch = io.read()) != -1) {
					bo.write(ch);
				}
				byteResponse = bo.toByteArray();
				String res = new String(byteResponse);
				System.out.println("hh Response from Server: " +  res);
			}

		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			try {
				bo.close();
				bo = null;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				if (io != null) {
					io.close();
					io = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (urlConnection != null) {
				urlConnection.disconnect();
				urlConnection = null;
			}
		}
		return byteResponse;
	}
	
}
