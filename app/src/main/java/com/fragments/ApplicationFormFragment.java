package com.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.VO.BaseVO;
import com.VO.TandCVO;
import com.VO.UnitDetailVO;
import com.sp.BMHApplication;
import com.sp.CustomAsyncTask;
import com.sp.CustomAsyncTask.AsyncListner;
import com.sp.PaymentProccessActivity;
import com.sp.PaymentWebviewActivity;
import com.sp.R;
import com.exception.BMHException;
import com.helper.BMHConstants;
import com.model.PropertyModel;
import com.utils.ImageLoadingUtils;
import com.utils.StringUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ApplicationFormFragment extends Fragment{

	private PaymentProccessActivity fragmentActivity;
	private BMHApplication app;
//	PageVO basevo;
	private final int SELECT_PHOTO=457;
	private final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE=456;

	private static final String CAMERA_DIR = "/dcim/";
	String mCurrentPhotoPath;
	TandCVO basevo;
	ImageView imgPhoto, imgPan, imgAddProof, imgClicked;
	String photoPath= "", addProofPath= "", panCardPath = "";
	private HashMap<String, String> map;
	private HashMap<String, String> mapImage;
	private ArrayList<String> imgPaths;
	private ArrayList<String> imgTypes;
	private String uploadingImagePath;
	private int position = 0;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_application_form,container, false);
		app = (BMHApplication) fragmentActivity.getApplication();
		map = new HashMap<String, String>();
		mapImage = new HashMap<String, String>();
		
		final EditText edName = (EditText) view.findViewById(R.id.edApplicantName);
		final EditText edCoappName = (EditText) view.findViewById(R.id.edCoappName);
		final EditText edRelationWithCoappli = (EditText) view.findViewById(R.id.edRelationWithCoappli);
		final EditText edResidentOf = (EditText) view.findViewById(R.id.edResidentOf);
		final EditText edPinCode = (EditText) view.findViewById(R.id.edPinCode);
		final EditText edTelephoneNo = (EditText) view.findViewById(R.id.edTelephoneNo);
		final EditText edMobile = (EditText) view.findViewById(R.id.edMobile);
		final EditText edEmail = (EditText) view.findViewById(R.id.et_email);
		final EditText edNationality = (EditText) view.findViewById(R.id.edNationality);
		final EditText edPanno = (EditText) view.findViewById(R.id.edPanno);
		final EditText edAddress = (EditText) view.findViewById(R.id.edAddress);
		
		LinearLayout llRadios = (LinearLayout) view.findViewById(R.id.llRadios);
		final LinearLayout llPhoto = (LinearLayout) view.findViewById(R.id.llPhoto);
		final LinearLayout llPAN = (LinearLayout) view.findViewById(R.id.llPAN);
		final LinearLayout llAdd = (LinearLayout) view.findViewById(R.id.llAdd);
		
		final RadioGroup rGroup = (RadioGroup) view.findViewById(R.id.radioGroup1);
		Button btnSelectPhoto = (Button) view.findViewById(R.id.btnSelectPhoto);
		Button btnSelectPan = (Button) view.findViewById(R.id.btnSelectPan);
		Button btnSelectAddProof = (Button) view.findViewById(R.id.btnSelectAddProof);
		Button submit = (Button) view.findViewById(R.id.buttonSubmit);
		
		imgPhoto = (ImageView) view.findViewById(R.id.imgPhoto);
		imgPan = (ImageView) view.findViewById(R.id.imgPan);
		imgAddProof = (ImageView) view.findViewById(R.id.imgAddProof);

		UnitDetailVO vo = ((PaymentProccessActivity) getActivity()).unitDetailVO;
		ArrayList<String> arrFields = vo.getFormFields();
		
		for (int i = 0; i < arrFields.size(); i++) {
			String field = arrFields.get(i);
			if(field.equalsIgnoreCase("First / Applicant")){
				edName.setVisibility(View.VISIBLE);
			}else if(field.equalsIgnoreCase("Co-applicant name")){
				edCoappName.setVisibility(View.VISIBLE);
			}else if(field.equalsIgnoreCase("Relatives")){
				edRelationWithCoappli.setVisibility(View.VISIBLE);
			}else if(field.equalsIgnoreCase("Resident of")){
				edResidentOf.setVisibility(View.VISIBLE);
			}else if(field.equalsIgnoreCase("PIN Code")){
				edPinCode.setVisibility(View.VISIBLE);
			}else if(field.equalsIgnoreCase("Telephone No")){
				edTelephoneNo.setVisibility(View.VISIBLE);
			}else if(field.equalsIgnoreCase("Mobile No")){
				edMobile.setVisibility(View.VISIBLE);
			}else if(field.equalsIgnoreCase("E Mail")){
				edEmail.setVisibility(View.VISIBLE);
			}else if(field.equalsIgnoreCase("National")){
				edNationality.setVisibility(View.VISIBLE);
			}else if(field.equalsIgnoreCase("PAN")){
				edPanno.setVisibility(View.VISIBLE);
			}else if(field.equalsIgnoreCase("Resident Status")){
				llRadios.setVisibility(View.VISIBLE);
			}else if(field.equalsIgnoreCase("Address for Correspondence")){
				edAddress.setVisibility(View.VISIBLE);
			}else if(field.equalsIgnoreCase("Photograph")){
				llPhoto.setVisibility(View.VISIBLE);
			}else if(field.equalsIgnoreCase("Pan Card")){
				llPAN.setVisibility(View.VISIBLE);
			}else if(field.equalsIgnoreCase("Address Proff")){
				llAdd.setVisibility(View.VISIBLE);
			}

		}
		
		btnSelectPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				imgClicked = imgPhoto;
				showChooserDialog();
			}
		});
		btnSelectPan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				imgClicked = imgPan;
				showChooserDialog();
			}
		});
		btnSelectAddProof.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				imgClicked = imgAddProof;
				showChooserDialog();
			}
		});
		
		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String name = edName.getText().toString().trim();
				String coappName = edCoappName.getText().toString().trim();
				String relation = edRelationWithCoappli.getText().toString().trim();
				String residance = edResidentOf.getText().toString().trim();
				String pincode = edPinCode.getText().toString().trim();
				String telephone = edTelephoneNo.getText().toString().trim();
				String mobile = edMobile.getText().toString().trim();
				String email = edEmail.getText().toString().trim();
				String nationality = edNationality.getText().toString().trim();
				String panNo = edPanno.getText().toString().trim();
				String address = edAddress.getText().toString().trim();
				String residentStatus="";
				switch (rGroup.getCheckedRadioButtonId()) {
				case R.id.radioResidantIndian:
					residentStatus="Residence";
					break;
				case R.id.radioNonResidentIndian:
					residentStatus="Non Resident Indian";
					break;
				case R.id.radioPersonofIndianOrigin:
					residentStatus="Person of Indian Origin";
					break;
				case R.id.radioNonResident:
					residentStatus="Non Resident";
					break;
				default:
					break;
				}
				
				if(edName.getVisibility()== View.VISIBLE && name.isEmpty()){
					app.showToastAtCenter(fragmentActivity, "Enter applicant name.");
					edName.requestFocus();
				}else if(edCoappName.getVisibility()== View.VISIBLE && coappName.isEmpty()){
					app.showToastAtCenter(fragmentActivity, "Enter co-applicant name.");
				}else if(edRelationWithCoappli.getVisibility()== View.VISIBLE && relation.isEmpty()){
					app.showToastAtCenter(fragmentActivity, "Enter relation with co-applicant.");
					edRelationWithCoappli.requestFocus();
				}else if(edResidentOf.getVisibility()== View.VISIBLE && residance.isEmpty()){
					app.showToastAtCenter(fragmentActivity, "Enter residing city.");
					edResidentOf.requestFocus();
				}else if(edPinCode.getVisibility()== View.VISIBLE && pincode.isEmpty()){
					app.showToastAtCenter(fragmentActivity, "Enter pin code.");
					edPinCode.requestFocus();
				}else if(edTelephoneNo.getVisibility()== View.VISIBLE && telephone.isEmpty()){
					app.showToastAtCenter(fragmentActivity, "Enter telephone no.");
					edTelephoneNo.requestFocus();
				}else if(edMobile.getVisibility()== View.VISIBLE && mobile.isEmpty()){
					app.showToastAtCenter(fragmentActivity, "Ente mobile no.");
					edMobile.requestFocus();
				}else if(edEmail.getVisibility()== View.VISIBLE && email.isEmpty()){
					app.showToastAtCenter(fragmentActivity, "Enter email.");
					edEmail.requestFocus();
				}else if(edEmail.getVisibility()== View.VISIBLE && !StringUtil.validEmail(email)){
					app.showToastAtCenter(fragmentActivity, "Enter valid email");
					edEmail.requestFocus();
				}else if(edNationality.getVisibility()== View.VISIBLE && nationality.isEmpty()){
					app.showToastAtCenter(fragmentActivity, "Enter nationality.");
					edNationality.requestFocus();
				}else if(edPanno.getVisibility()== View.VISIBLE && panNo.isEmpty()){
					app.showToastAtCenter(fragmentActivity, "Enter PAN no.");
					edPanno.requestFocus();
				}else if(edAddress.getVisibility()== View.VISIBLE && address.isEmpty()){
					app.showToastAtCenter(fragmentActivity, "Enter address.");
					edAddress.requestFocus();
				}else if(llPhoto.getVisibility() == View.VISIBLE && photoPath.isEmpty()){
					app.showToastAtCenter(fragmentActivity, "Select photo image.");
				}else if(llPAN.getVisibility() == View.VISIBLE && panCardPath.isEmpty()){
					app.showToastAtCenter(fragmentActivity, "Select PAN card image.");
				}else if(llAdd.getVisibility() == View.VISIBLE && addProofPath.isEmpty()){
					app.showToastAtCenter(fragmentActivity, "Select address proof image.");
				}
				else{
					// hit the api to post this form on server. first submit the data and get the id then submit the images on that id
//					app.showToastAtCenter(fragmentActivity, "Hit api");
					imgPaths = new ArrayList<String>(); 
					imgTypes = new ArrayList<String>();
					if(llPhoto.getVisibility() == View.VISIBLE ){
						imgPaths.add(photoPath);
						imgTypes.add("Photograph");
					}
					if(llPAN.getVisibility() == View.VISIBLE ){
						imgPaths.add(panCardPath);
						imgTypes.add("Pan_Card");
					}
					if(llAdd.getVisibility() == View.VISIBLE ){
						imgPaths.add(addProofPath);
						imgTypes.add("Address_Proof");
					}
					if(imgPaths.size()>0){
						uploadingImagePath = imgPaths.get(position);	
					}
					map.put("first_applicant", name);
					map.put("co_applicant", coappName);
					map.put("relation", relation);
					map.put("residentof", residance);
					map.put("pincode", pincode);
					map.put("telephone", telephone);
					map.put("mobile_no", mobile);
					map.put("email", email);
					map.put("nationality", nationality);
					map.put("pan_num", panNo);
					map.put("address", address);
					map.put("resident_status", residentStatus);
					
					map.put("relative_radio", "");
					
					map.put("user_id", app.getFromPrefs(BMHConstants.USERID_KEY));
					
					startSubmitStringFields();
					
				}
			}
		});
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	public PaymentProccessActivity getFragmentActivity() {
		return fragmentActivity;
	}

	public void setFragmentActivity(PaymentProccessActivity fragmentActivity) {
		this.fragmentActivity = fragmentActivity;
	}
	
	
	private void startSubmitImageTask() {
		String messageLoading = "Loading...";
		if(imgTypes.get(position).equalsIgnoreCase("Photograph")){
			messageLoading = "Uploading Photograph...";
		}else if(imgTypes.get(position).equalsIgnoreCase("Pan_Card")){
			messageLoading = "Uploading Pan card...";
		}else if(imgTypes.get(position).equalsIgnoreCase("Address_Proof")){
			messageLoading = "Uploading Address Proof...";
		}
		CustomAsyncTask loadingTask = new CustomAsyncTask(fragmentActivity, new AsyncListner(){
			BaseVO base;
					@Override
					public void OnBackgroundTaskCompleted() {
						if(base == null){
							showToast(getString(R.string.unable_to_connect_server));
						}else{
							if(base.isSuccess()){
								if(position<imgPaths.size()-1){
									position++;
									uploadingImagePath = imgPaths.get(position);
									startSubmitImageTask();
								}else{
									UnitDetailVO vo = getArguments().getParcelable("unitvo");
									Intent i = new Intent(getActivity(), PaymentWebviewActivity.class);
									i.putExtra("unitvo", vo);
									startActivity(i);
								}
								
							}else{
								showToast(base.getMessage());
							}
						}
					}
					@Override
					public void DoBackgroundTask(String[] url) {
						mapImage.put("image_type", imgTypes.get(position));
						PropertyModel model = new PropertyModel();
						base = model.doFileUpload(mapImage, uploadingImagePath);
					}
					@Override
					public void OnPreExec() {
						
					}
				});
		
		loadingTask.setLogingMsg(messageLoading);
		loadingTask.execute("");
	}
	
	private void startSubmitStringFields() {
		CustomAsyncTask loadingTask = new CustomAsyncTask(fragmentActivity, new AsyncListner(){
			BaseVO vo;
					@Override
					public void OnBackgroundTaskCompleted() {
						if(vo == null){
							showToast(getString(R.string.unable_to_connect_server));
						}else{
							if(vo.isSuccess()){
								// now hit the api for uploading images.
								//
								mapImage.put("user_id", app.getFromPrefs(BMHConstants.USERID_KEY));
								startSubmitImageTask();
							}else{
								showToast(vo.getMessage());
							}
						}
					}
					@Override
					public void DoBackgroundTask(String[] url) {
						PropertyModel model = new PropertyModel();
						try {
							
							vo = model.submitFormFields(map);
						} catch (BMHException e) {
							e.printStackTrace();
						}
					}
					@Override
					public void OnPreExec() {
						
					}
				});
		loadingTask.execute("");
	}
	
//	@Override
//	public void setUserVisibleHint(boolean isVisibleToUser) {
//		super.setUserVisibleHint(isVisibleToUser);
//		if(isVisibleToUser){
//			if(basevo == null){
////				startTermsTask();
//			}
//		}
//	}
	
	private void showChooserDialog() {

		final Dialog dialogr = new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
		dialogr.setContentView(R.layout.custom_dialog);
		dialogr.setCancelable(true);

		// set the custom dialog components - text, image and button
		ImageView gallery = (ImageView) dialogr.findViewById(R.id.galleryimage);
		gallery.setImageResource(R.drawable.gallery_icon);
		ImageView camera = (ImageView) dialogr.findViewById(R.id.cameraimage);
		camera.setImageResource(R.drawable.cmeraicon);

		gallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

				startActivityForResult(i, SELECT_PHOTO);
				dialogr.dismiss();
				dialogr.hide();
			}
		});

		camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// give the image a name so we can store it in the phone's
				// default location
				Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//				File f = null;
				try {
//					f = setUpPhotoFile();
					
					String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
					String imageFileName = "bmh" + timeStamp + "_";
					File albumF = getAlbumDir();
					File imageF = File.createTempFile(imageFileName, "bmh", albumF);
					
					
					mCurrentPhotoPath = imageF.getAbsolutePath();
					takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageF));
				} catch (IOException e) {
					e.printStackTrace();
//					f = null;
//					mCurrentPhotoPath = null;
					
				}
				
				startActivityForResult(takePictureIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
				dialogr.dismiss();
				dialogr.hide();
			}
		});
		dialogr.show();
	}
	
	private File getAlbumDir() {
		File storageDir = null;

		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
					storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"CameraPicture");
				} else {
					storageDir = new File (Environment.getExternalStorageDirectory()+ CAMERA_DIR+ "CameraPicture");
				}

		if (storageDir != null) {
		if (! storageDir.mkdirs()) {
		if (! storageDir.exists()){
//		Log.d("CameraSample", "failed to getInstance directory");
		return null;
		}
		}
		}

		} else {
//		Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
		}

		return storageDir;
		}
	
	
	
	public void onActivityResult(final int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(data== null){
			System.out.println("hh data null");
		}
		
		if( resultCode == Activity.RESULT_OK )
		{
			if( requestCode == SELECT_PHOTO ){
				
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getActivity().getContentResolver().query(selectedImage,	filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String imagePath1 = cursor.getString(columnIndex);
				System.out.println("hh image path ="+imagePath1);
				
				File f =new File(compressImage(imagePath1, 1));
				Bitmap myBitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
				 
				imgClicked.setImageBitmap(myBitmap);
				
				if(imgClicked == imgPhoto){
					photoPath = f.getAbsolutePath();
				}else if(imgClicked == imgPan){
					panCardPath = f.getAbsolutePath();
				}else if(imgClicked == imgAddProof){
					addProofPath = f.getAbsolutePath();
				} 
				
			}else if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
				
		       System.out.println("hh image path = "+mCurrentPhotoPath);
		       
		        File f =new File(compressImage(mCurrentPhotoPath, 0));
				Bitmap myBitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
				 
				imgClicked.setImageBitmap(myBitmap);
				
				if(imgClicked == imgPhoto){
					photoPath = f.getAbsolutePath();
				}else if(imgClicked == imgPan){
					panCardPath = f.getAbsolutePath();
				}else if(imgClicked == imgAddProof){
					addProofPath = f.getAbsolutePath();
				} 
		    }
		}    
	}
	
	
	public String compressImage(String imageUri , int flag) {
		String filePath;
		if(flag ==1)
		{
			filePath = getRealPathFromURI(imageUri);
		}
		
		filePath = imageUri;
		Bitmap scaledBitmap = null;
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;						
		Bitmap bmp = BitmapFactory.decodeFile(filePath,options);
		
		int actualHeight = options.outHeight;
		int actualWidth = options.outWidth;
		float maxHeight = 816.0f;
		float maxWidth = 612.0f;
		float imgRatio = actualWidth / actualHeight;
		float maxRatio = maxWidth / maxHeight;

		if (actualHeight > maxHeight || actualWidth > maxWidth) {
			if (imgRatio < maxRatio) {
				imgRatio = maxHeight / actualHeight;
				actualWidth = (int) (imgRatio * actualWidth);
				actualHeight = (int) maxHeight;
			} else if (imgRatio > maxRatio) {
				imgRatio = maxWidth / actualWidth;
				actualHeight = (int) (imgRatio * actualHeight);
				actualWidth = (int) maxWidth;
			} else {
				actualHeight = (int) maxHeight;
				actualWidth = (int) maxWidth;     
				
			}
		}
		
		ImageLoadingUtils utils = new ImageLoadingUtils(getActivity());
		options.inSampleSize = utils.calculateInSampleSize(options, actualWidth, actualHeight);
		options.inJustDecodeBounds = false;
		options.inDither = false;
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inTempStorage = new byte[16*1024];
			
		try{	
			bmp = BitmapFactory.decodeFile(filePath,options);
		}
		catch(OutOfMemoryError exception){
			exception.printStackTrace();
			
		}
		try{
			scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
		}
		catch(OutOfMemoryError exception){
			exception.printStackTrace();
		}
						
		float ratioX = actualWidth / (float) options.outWidth;
		float ratioY = actualHeight / (float)options.outHeight;
		float middleX = actualWidth / 2.0f;
		float middleY = actualHeight / 2.0f;
			
		Matrix scaleMatrix = new Matrix();
		scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

		Canvas canvas = new Canvas(scaledBitmap);
		canvas.setMatrix(scaleMatrix);
		canvas.drawBitmap(bmp, middleX - bmp.getWidth()/2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

						
		ExifInterface exif;
		try {
			exif = new ExifInterface(filePath);
		
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
			Log.d("EXIF", "Exif: " + orientation);
			Matrix matrix = new Matrix();
			if (orientation == 6) {
				matrix.postRotate(90);
				Log.d("EXIF", "Exif: " + orientation);
			} else if (orientation == 3) {
				matrix.postRotate(180);
				Log.d("EXIF", "Exif: " + orientation);
			} else if (orientation == 8) {
				matrix.postRotate(270);
				Log.d("EXIF", "Exif: " + orientation);
			}
			scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileOutputStream out = null;
		String filename = getFilename();
		try {
			out = new FileOutputStream(filename);
			scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return filename;

	}

	public String getFilename() {
		File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
		if (!file.exists()) {
		file.mkdirs();
		}
		String uriSting = (file.getAbsolutePath() + "/"+ System.currentTimeMillis() + ".jpg");
		return uriSting;
		}

		private String getRealPathFromURI(String contentURI) {
		Uri contentUri = Uri.parse(contentURI);
		Cursor cursor = getActivity().getContentResolver().query(contentUri, null, null, null, null);
		if (cursor == null) {
		return contentUri.getPath();
		} else {
		cursor.moveToFirst();
		int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
		return cursor.getString(idx);
		}

		}


	protected void showToast(String msg){
		Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
	}

}
