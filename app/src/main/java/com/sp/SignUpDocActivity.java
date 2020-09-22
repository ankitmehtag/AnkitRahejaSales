package com.sp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.AppEnums.APIType;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.appnetwork.AsyncThread;
import com.appnetwork.ReqRespBean;
import com.appnetwork.WEBAPI;
import com.helper.BMHConstants;
import com.helper.IntentDataObject;
import com.helper.ParamsConstants;
import com.jsonparser.JsonParser;
import com.model.BaseRespModel;
import com.model.NetworkErrorObject;
import com.model.UploadRegistrationDoc;
import com.utils.Utils;
import com.views.CustomProgressDialog;

import java.io.File;

public class SignUpDocActivity extends BaseFragmentActivity implements ConnectivityReceiver.ConnectivityReceiverListener  {

	private final String TAG = SignUpDocActivity.class.getSimpleName();
	private BMHApplication app;
	private Activity ctx = SignUpDocActivity.this;
	private NetworkErrorObject mNetworkErrorObject = null;
	private CustomProgressDialog progressDialog;
	private AsyncThread mAsyncThread = null;
	private Toolbar toolbar;

	//private Bitmap bitmap;
	protected static final int CAMERA_REQ_DOC1 = 1,CAMERA_REQ_DOC2 = 2,CAMERA_REQ_DOC3 = 3;
	protected static final int GALLERY_REQ_DOC1 = 21, GALLERY_REQ_DOC2 = 22, GALLERY_REQ_DOC3 = 23;
	private String doc1Name = "doc1.jpg",doc2Name = "doc2.jpg",doc3Name = "doc3.jpg";
	private String selectedDoc1Path,selectedDoc2Path,selectedDoc3Path;
	private SimpleMultiPartRequest smrDoc1,smrDoc2,smrDoc3;
	private int percentage1,percentage2,percentage3;
	private LinearLayout ll_doc_root;
	private String broker_id = "",broker_code = "";
	private int userType = -1;
	private LayoutInflater mLayoutInflater = null;
	private View doc1View, doc2View, doc3View;
	//private final int DOC_TYPE_1 = 1,DOC_TYPE_2 = 2,DOC_TYPE_3 = 3;
	private final String ID_PROOF_GST_DOC = "1",PAN_CARD_DOC = "2", RERA_DOC = "3";
	//private int CUR_DOC_TYPE = DOC_TYPE_1;
	private Button btn_done;
	private TextView tv_header_title;
	private static final int PERMISSION_CALLBACK_CONSTANT = 100;

	String[] permissionsRequired = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
	private boolean sentToSettings = false;
	private static final int REQUEST_PERMISSION_SETTING = 101;
	private SharedPreferences permissionStatus;




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup_doc);
		app = (BMHApplication) getApplication();
		permissionStatus = getSharedPreferences("permissionStatus",MODE_PRIVATE);
		initViews();
		setListeners();
		setTypeface();
		mLayoutInflater = LayoutInflater.from(this);
		if(getIntent() != null && getIntent().getSerializableExtra(IntentDataObject.OBJ) != null
				&& getIntent().getSerializableExtra(IntentDataObject.OBJ) instanceof IntentDataObject){
			IntentDataObject mIntentDataObject = (IntentDataObject) getIntent().getSerializableExtra(IntentDataObject.OBJ);
			if(mIntentDataObject.getMap() != null) {
				broker_id = mIntentDataObject.getData().get(ParamsConstants.BROKER_ID);
				broker_code = mIntentDataObject.getData().get(ParamsConstants.BROKER_CODE);
				userType = Utils.toInt(mIntentDataObject.getData().get(ParamsConstants.TYPE));
				if(userType == RegisterActivity.BROKER_FIRM || userType == RegisterActivity.EMPLOYEE_OF_BROKER_FIRM || userType == RegisterActivity.INDIVIDUAL){
					addDoc();
				}
				if(userType == RegisterActivity.BROKER_FIRM){
					tv_header_title.setText("Documents(BF)");
				}else if(userType == RegisterActivity.EMPLOYEE_OF_BROKER_FIRM){
					tv_header_title.setText("Documents(Emp)");
				}else if(userType == RegisterActivity.INDIVIDUAL){
					tv_header_title.setText("Documents(I)");
				}else{
					//Do nothing show error
				}
			}
		}
		getPermission();
	}// End of Oncreate

	private void setTypeface() {
		Typeface font = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");
	}

	private void initViews(){
		toolbar = setToolBar();
		toolbar.setTitle("Sign Up");
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		ll_doc_root = (LinearLayout)findViewById(R.id.ll_doc_root);
		btn_done = (Button)findViewById(R.id.btn_done);
		tv_header_title = (TextView)findViewById(R.id.tv_header_title);
	}
	private void setListeners() {
		btn_done.setOnClickListener(mOnClickListener);
	}

	@Override
	protected void onResume() {
		super.onResume();
		app = (BMHApplication) getApplication();
		BMHApplication.getInstance().setConnectivityListener(this);
	}


	OnClickListener mOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()){
				case R.id.btn_done:
					if(userType == RegisterActivity.BROKER_FIRM){
						if(selectedDoc1Path == null || selectedDoc1Path.isEmpty() || selectedDoc2Path == null || selectedDoc2Path.isEmpty() || selectedDoc3Path == null || selectedDoc3Path.isEmpty()){
							showToast("Please upload all the documents");
						}else{
							signUpDone();
						}
					}else if(userType == RegisterActivity.EMPLOYEE_OF_BROKER_FIRM){
						if(selectedDoc1Path == null || selectedDoc1Path.isEmpty() || selectedDoc2Path == null || selectedDoc2Path.isEmpty()){
							showToast("Please upload all the documents");
						}else{
							signUpDone();
						}
					}else if(userType == RegisterActivity.INDIVIDUAL){
						if(selectedDoc1Path == null || selectedDoc1Path.isEmpty() || selectedDoc2Path == null || selectedDoc2Path.isEmpty() || selectedDoc3Path == null || selectedDoc3Path.isEmpty()){
							showToast("Please upload all the documents");
						}else{
							signUpDone();
						}
					}else{
						showToast("Invalid User type");
					}
					break;
			}
		}
	};
	public boolean isValidData() {
		return true;
	}


	public static boolean checkSpecialCharacter(String name) {
		for (int i = 0; i < name.length(); ++i) {
			char ch = name.charAt(i);
			if (!Character.isLetter(ch) && !(ch == ' ')) {
				return true;
			}
		}
		return false;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
			case android.R.id.home:
				onBackPressed();
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	// ================================= Facebook login

	public boolean getConnectivityStatus() {
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo info = connManager.getActiveNetworkInfo();
		if (info != null)
			if (info.isConnected()) {
				return true;
			} else {
				return false;
			}
		else
			return false;
	}





	@Override
	public void onBackPressed() {
		if(smrDoc1 != null)smrDoc1.cancel();
		if(smrDoc2 != null)smrDoc2.cancel();
		if(smrDoc3 != null)smrDoc3.cancel();
		super.onBackPressed();
	}

	@Override
	protected String setActionBarTitle() {
		return "Login";
	}

	@Override
	public void onNetworkConnectionChanged(boolean isConnected) {
		if(isConnected) {
			showToast("Network available");
		}
	}

	private void signUpDone(){
		ReqRespBean mBean = new ReqRespBean();
		mBean.setApiType(APIType.SIGNUP_DONE);
		mBean.setRequestmethod(WEBAPI.POST);
		mBean.setUrl(WEBAPI.getWEBAPI(APIType.SIGNUP_DONE));
		mBean.setMimeType(WEBAPI.contentTypeFormData);
		StringBuilder mStringBuilder = new StringBuilder("");
		mStringBuilder.append("broker_id=" + broker_id);
		mBean.setJson(mStringBuilder.toString());
		mBean.setmHandler(mHandler);
		mBean.setCtx(this);
		mAsyncThread = new AsyncThread();
		mAsyncThread.initProgressDialog(this,mOnCancelListener);
		mAsyncThread.execute(mBean);
		mAsyncThread = null;
	}
	DialogInterface.OnCancelListener mOnCancelListener = new DialogInterface.OnCancelListener() {
		@Override
		public void onCancel(DialogInterface dialog) {
			if(mAsyncThread != null)mAsyncThread.cancel(true);
			mAsyncThread = null;
		}
	};

	Handler mHandler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			if (msg.obj == null) {
				if(progressDialog != null)progressDialog.dismiss();
				return false;
			} else {
				ReqRespBean mBean = (ReqRespBean) msg.obj;
				switch (mBean.getApiType()) {
					case SIGNUP_DONE:
						BaseRespModel  baseRespModel = (BaseRespModel) JsonParser.convertJsonToBean(APIType.SIGNUP_DONE,mBean.getJson());
						if(baseRespModel != null){
							if(baseRespModel.isSuccess()){
								doneAlert("Message","Your account will be activated after document verification.");
							}else{
								//doneAlert("Message","Your account will be activated after document verification.");
								showToast(baseRespModel.getMessage());
							}
						}else{
							showToast(getString(R.string.something_went_wrong));
						}
						break;
					default:
						break;
				}
			}
			return true;
		}
	});



	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == CAMERA_REQ_DOC1) {
			if(resultCode == RESULT_OK && doc1View != null) {
				doc1View.findViewById(R.id.iv_success).setVisibility(View.GONE);
				setCapturedImage(CAMERA_REQ_DOC1);
			}
		}else if (requestCode == CAMERA_REQ_DOC2) {
			if(resultCode == RESULT_OK && doc2View != null) {
				doc2View.findViewById(R.id.iv_success).setVisibility(View.GONE);
				setCapturedImage(CAMERA_REQ_DOC2);
			}
		}else if (requestCode == CAMERA_REQ_DOC3) {
			if(resultCode == RESULT_OK && doc3View != null) {
				doc3View.findViewById(R.id.iv_success).setVisibility(View.GONE);
				setCapturedImage(CAMERA_REQ_DOC3);
			}
		}else if (requestCode == GALLERY_REQ_DOC1) {
			if(resultCode == RESULT_OK && doc1View != null) {
				doc1View.findViewById(R.id.iv_success).setVisibility(View.GONE);
				setChooseFile(data, GALLERY_REQ_DOC1);
			}
		}else if (requestCode == GALLERY_REQ_DOC2) {
			if(resultCode == RESULT_OK && doc1View != null) {
				doc2View.findViewById(R.id.iv_success).setVisibility(View.GONE);
				setChooseFile(data, GALLERY_REQ_DOC2);
			}
		}else if (requestCode == GALLERY_REQ_DOC3) {
			if(resultCode == RESULT_OK && doc1View != null) {
				doc3View.findViewById(R.id.iv_success).setVisibility(View.GONE);
				setChooseFile(data, GALLERY_REQ_DOC3);
			}
		} else if (requestCode == REQUEST_PERMISSION_SETTING) {
			if (ActivityCompat.checkSelfPermission(this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
				//proceedAfterPermission();
			}
		}

	}
	private String getPath(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
		Cursor cursor = loader.loadInBackground();
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String result = cursor.getString(column_index);
		cursor.close();
		return result;
	}


	private void imageUploadDoc1() {
		if(selectedDoc1Path == null || selectedDoc1Path.isEmpty()){
			showToast("Please select document");
			return;
		}
		smrDoc1 = new SimpleMultiPartRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.UPLOAD_REGISTRATION_DOC),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.i("ResponseDoc1", response);
						if(doc1View != null){
							((Button) doc1View.findViewById(R.id.btn_upload)).setEnabled(true);
							((TextView)doc1View.findViewById(R.id.tv_percentage)).setVisibility(View.GONE);
						}
						if(response != null){
							UploadRegistrationDoc mUploadRegistrationDoc = (UploadRegistrationDoc) JsonParser.convertJsonToBean(APIType.UPLOAD_REGISTRATION_DOC,response);
							if(mUploadRegistrationDoc != null && mUploadRegistrationDoc.isSuccess()) {
								showToast(mUploadRegistrationDoc.getMessage());
								((ImageView) doc1View.findViewById(R.id.iv_success)).setVisibility(View.VISIBLE);
							}else if(mUploadRegistrationDoc != null && !mUploadRegistrationDoc.isSuccess()){
								showToast(mUploadRegistrationDoc.getMessage());
							}else{
								showToast(getString(R.string.something_went_wrong));
							}
						}
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if(doc1View != null){
					((Button) doc1View.findViewById(R.id.btn_upload)).setEnabled(true);
					((TextView)doc1View.findViewById(R.id.tv_percentage)).setVisibility(View.GONE);
				}
				if(error != null && error.getMessage() != null){
					//int respCode =  error.networkResponse.statusCode;
					Log.e(TAG,error.getMessage());
					showToast(getString(R.string.something_went_wrong) + error.getMessage());
				}else{
					showToast(getString(R.string.something_went_wrong));
				}
				//btn_make_payment.setEnabled(true);
			}
		});

		smrDoc1.setOnProgressListener(mProgressListenerDoc1);
		String userid = app.getFromPrefs(BMHConstants.USERID_KEY);
		if(doc1View != null){
			smrDoc1.addFile("doc_image", selectedDoc1Path);
			smrDoc1.addStringParam("user_id",userid);
			smrDoc1.addStringParam(ParamsConstants.DOC_TYPE,ID_PROOF_GST_DOC);
			smrDoc1.addStringParam(ParamsConstants.BROKER_ID,broker_id);
			smrDoc1.addStringParam(ParamsConstants.BUILDER_ID,BMHConstants.CURRENT_BUILDER_ID);
			((Button) doc1View.findViewById(R.id.btn_upload)).setEnabled(false);
			((TextView)doc1View.findViewById(R.id.tv_percentage)).setVisibility(View.GONE);


		}
		BMHApplication.getInstance().addToRequestQueue(smrDoc1);
	}

	Response.ProgressListener mProgressListenerDoc1 = new Response.ProgressListener() {

		@Override
		public void onProgress(long transferredBytes, long totalSize) {
			percentage1 = (int) ((transferredBytes /  ((float)totalSize)) * 100);
			if(percentage1 >= 0 && percentage1 <= 100){
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						ProgressBar progressBar = null;
						TextView tv_percentage = null;
						//showToast("Progress"+percentage1 + "%");
						if(doc1View != null){
							progressBar = (ProgressBar) doc1View.findViewById(R.id.pb_progress);
							tv_percentage = (TextView)doc1View.findViewById(R.id.tv_percentage);
							if (progressBar.getVisibility() == View.GONE) progressBar.setVisibility(View.VISIBLE);
							if (tv_percentage.getVisibility() == View.GONE) tv_percentage.setVisibility(View.VISIBLE);
							progressBar.setProgress(percentage1);
							tv_percentage.setText(percentage1 + "%");
							if (percentage1 == 100) {
								progressBar.setVisibility(View.GONE);
								tv_percentage.setText("Please wait...");
							}
						}

					}
				});
			}
		}
	};


	private void imageUploadDoc2() {
		if (selectedDoc2Path == null || selectedDoc2Path.isEmpty()){
			showToast("Please select document");
			return;
		}
		smrDoc2 = new SimpleMultiPartRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.UPLOAD_REGISTRATION_DOC),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.i("Response", response);
						if(doc2View != null){
							((Button) doc2View.findViewById(R.id.btn_upload)).setEnabled(true);
							((TextView)doc2View.findViewById(R.id.tv_percentage)).setVisibility(View.GONE);
						}
						if (response != null) {
							UploadRegistrationDoc mUploadRegistrationDoc = (UploadRegistrationDoc) JsonParser.convertJsonToBean(APIType.UPLOAD_REGISTRATION_DOC, response);
							if (mUploadRegistrationDoc != null && mUploadRegistrationDoc.isSuccess()) {
								showToast(mUploadRegistrationDoc.getMessage());
								((ImageView) doc2View.findViewById(R.id.iv_success)).setVisibility(View.VISIBLE);
							} else if (mUploadRegistrationDoc != null && !mUploadRegistrationDoc.isSuccess()) {
								showToast(mUploadRegistrationDoc.getMessage());
							} else {
								showToast(getString(R.string.something_went_wrong));
							}
						}
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if(doc2View != null){
					((Button) doc2View.findViewById(R.id.btn_upload)).setEnabled(true);
					((TextView)doc2View.findViewById(R.id.tv_percentage)).setVisibility(View.GONE);

				}
				if (error != null && error.getMessage() != null) {
					//int respCode =  error.networkResponse.statusCode;
					Log.e(TAG, error.getMessage());
					showToast(getString(R.string.something_went_wrong) + error.getMessage());
				} else {
					showToast(getString(R.string.something_went_wrong));
				}
				//btn_make_payment.setEnabled(true);
			}
		});

		smrDoc2.setOnProgressListener(mProgressListenerDoc2);
		String userid = app.getFromPrefs(BMHConstants.USERID_KEY);
		if (doc2View != null) {
			smrDoc2.addFile("doc_image", selectedDoc2Path);
			smrDoc2.addStringParam("user_id", userid);
			smrDoc2.addStringParam(ParamsConstants.DOC_TYPE, PAN_CARD_DOC);
			smrDoc2.addStringParam(ParamsConstants.BROKER_ID, broker_id);
			smrDoc2.addStringParam(ParamsConstants.BUILDER_ID, BMHConstants.CURRENT_BUILDER_ID);
			BMHApplication.getInstance().addToRequestQueue(smrDoc2);
			((Button) doc2View.findViewById(R.id.btn_upload)).setEnabled(false);
			((TextView)doc2View.findViewById(R.id.tv_percentage)).setVisibility(View.GONE);

		}
	}

	Response.ProgressListener mProgressListenerDoc2 = new Response.ProgressListener() {

		@Override
		public void onProgress(long transferredBytes, long totalSize) {
			percentage2 = (int) ((transferredBytes /  ((float)totalSize)) * 100);
			if(percentage2 >= 0 && percentage2 <= 100){
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						ProgressBar progressBar = null;
						TextView tv_percentage;
						//showToast("Progress"+percentage2 + "%");
						if(doc2View != null){
							progressBar = (ProgressBar) doc2View.findViewById(R.id.pb_progress);
							tv_percentage = (TextView)doc2View.findViewById(R.id.tv_percentage);
							if (progressBar.getVisibility() == View.GONE) progressBar.setVisibility(View.VISIBLE);
							if (tv_percentage.getVisibility() == View.GONE)tv_percentage.setVisibility(View.VISIBLE);
							tv_percentage.setText(percentage2+"%");
							progressBar.setProgress(percentage2);
							if (percentage2 == 100) {
								progressBar.setVisibility(View.GONE);
								tv_percentage.setText("Please wait...");
							}
						}
					}
				});
			}
		}

	};



	private void imageUploadDoc3() {
		if (selectedDoc3Path == null || selectedDoc3Path.isEmpty()){
			showToast("Please select document");
			return;
		}
		smrDoc3 = new SimpleMultiPartRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.UPLOAD_REGISTRATION_DOC),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						if(doc3View != null){
							((Button) doc3View.findViewById(R.id.btn_upload)).setEnabled(true);
							((TextView)doc3View.findViewById(R.id.tv_percentage)).setVisibility(View.GONE);
						}
						Log.i("Response", response);
						if (response != null) {
							UploadRegistrationDoc mUploadRegistrationDoc = (UploadRegistrationDoc) JsonParser.convertJsonToBean(APIType.UPLOAD_REGISTRATION_DOC, response);
							if (mUploadRegistrationDoc != null && mUploadRegistrationDoc.isSuccess()) {
								showToast(mUploadRegistrationDoc.getMessage());
								((ImageView) doc3View.findViewById(R.id.iv_success)).setVisibility(View.VISIBLE);
							} else if (mUploadRegistrationDoc != null && !mUploadRegistrationDoc.isSuccess()) {
								showToast(mUploadRegistrationDoc.getMessage());
							} else {
								showToast(getString(R.string.something_went_wrong));
							}
						}
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if(doc3View != null){
					((Button) doc3View.findViewById(R.id.btn_upload)).setEnabled(true);
					((TextView)doc3View.findViewById(R.id.tv_percentage)).setVisibility(View.GONE);
				}
				if (error != null && error.getMessage() != null) {
					//int respCode =  error.networkResponse.statusCode;
					Log.e(TAG, error.getMessage());
					showToast(getString(R.string.something_went_wrong) + error.getMessage());
				} else {
					showToast(getString(R.string.something_went_wrong));
				}
				//btn_make_payment.setEnabled(true);
			}
		});

		smrDoc3.setOnProgressListener(mProgressListenerDoc3);
		String userid = app.getFromPrefs(BMHConstants.USERID_KEY);
		if(doc3View != null){
			smrDoc3.addFile("doc_image", selectedDoc3Path);
			smrDoc3.addStringParam("user_id",userid);
			smrDoc3.addStringParam(ParamsConstants.DOC_TYPE,RERA_DOC);
			smrDoc3.addStringParam(ParamsConstants.BROKER_ID,broker_id);
			smrDoc3.addStringParam(ParamsConstants.BUILDER_ID,BMHConstants.CURRENT_BUILDER_ID);
			BMHApplication.getInstance().addToRequestQueue(smrDoc3);
			((Button) doc3View.findViewById(R.id.btn_upload)).setEnabled(false);
			((TextView)doc3View.findViewById(R.id.tv_percentage)).setVisibility(View.GONE);
		}
	}

	Response.ProgressListener mProgressListenerDoc3 = new Response.ProgressListener() {

		@Override
		public void onProgress(long transferredBytes, long totalSize) {
			percentage3 = (int) ((transferredBytes /  ((float)totalSize)) * 100);
			if(percentage3 >= 0 && percentage3 <= 100){
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						ProgressBar progressBar = null;
						TextView tv_percentage;
						if(doc3View != null){
							progressBar = (ProgressBar) doc3View.findViewById(R.id.pb_progress);
							tv_percentage = (TextView)doc3View.findViewById(R.id.tv_percentage);
							if (progressBar.getVisibility() == View.GONE) progressBar.setVisibility(View.VISIBLE);
							if (tv_percentage.getVisibility() == View.GONE)tv_percentage.setVisibility(View.VISIBLE);
							tv_percentage.setText(percentage3+"%");
							progressBar.setProgress(percentage3);
							if (percentage3 == 100) {
								progressBar.setVisibility(View.GONE);
								tv_percentage.setText("Please wait...");
							}
						}
					}
				});
			}
		}

	};

	private String getImageName(String str){
		return "image_"+str+".jpg";
	}

	@Override
	public void onPause() {
		if (mAsyncThread != null)
			mAsyncThread.cancel(true);
		super.onPause();

	}


	private void capturePicture(int cameraReqCode) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		String docName = "";
		if(cameraReqCode == CAMERA_REQ_DOC1)docName = doc1Name;
		if(cameraReqCode == CAMERA_REQ_DOC2)docName = doc2Name;
		if(cameraReqCode == CAMERA_REQ_DOC3)docName = doc3Name;
		File f = new File(android.os.Environment.getExternalStorageDirectory(), docName);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		startActivityForResult(intent, cameraReqCode);
	}

	private void chooseFile(int galeryReqCode){
		Intent pictureActionIntent = null;
		pictureActionIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(pictureActionIntent, galeryReqCode);
	}

	private void addDoc(){
		if(userType == RegisterActivity.BROKER_FIRM){
			//Add doc 1
			doc1View = mLayoutInflater.inflate(R.layout.upload_registeration_doc,null);
			ll_doc_root.addView(doc1View);
			TextView doc1_tv_title = (TextView) doc1View.findViewById(R.id.tv_title);
			doc1_tv_title.setText("GST Registration document");
			ImageView doc1_iv_capture_img = (ImageView) doc1View.findViewById(R.id.iv_capture_img);
			doc1_iv_capture_img.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					capturePicture(CAMERA_REQ_DOC1);
				}
			});
			ImageView doc1_iv_choose_file = (ImageView) doc1View.findViewById(R.id.iv_choose_file);
			doc1_iv_choose_file.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					chooseFile(GALLERY_REQ_DOC1);
				}
			});
			//ImageView doc1_iv_perv = (ImageView)doc1View.findViewById(R.id.iv_perv);
			Button doc1_btn_upload = (Button) doc1View.findViewById(R.id.btn_upload);
			doc1_btn_upload.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					imageUploadDoc1();
				}
			});
			// add doc 2
			doc2View = mLayoutInflater.inflate(R.layout.upload_registeration_doc,null);
			ll_doc_root.addView(doc2View);
			TextView doc2_tv_title = (TextView) doc2View.findViewById(R.id.tv_title);
			doc2_tv_title.setText("Firm PAN Card");
			ImageView doc2_iv_capture_img = (ImageView) doc2View.findViewById(R.id.iv_capture_img);
			doc2_iv_capture_img.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					capturePicture(CAMERA_REQ_DOC2);
				}
			});
			ImageView doc2_iv_choose_file = (ImageView) doc2View.findViewById(R.id.iv_choose_file);
			doc2_iv_choose_file.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					chooseFile(GALLERY_REQ_DOC2);
				}
			});
			//ImageView doc2_iv_perv = (ImageView)doc2View.findViewById(R.id.iv_perv);
			Button doc2_btn_upload = (Button) doc2View.findViewById(R.id.btn_upload);
			doc2_btn_upload.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					imageUploadDoc2();
				}
			});

			// add doc 3
			doc3View = mLayoutInflater.inflate(R.layout.upload_registeration_doc,null);
			ll_doc_root.addView(doc3View);
			TextView doc3_tv_title = (TextView) doc3View.findViewById(R.id.tv_title);
			doc3_tv_title.setText("RERA Registration Document");
			ImageView doc3_iv_capture_img = (ImageView) doc3View.findViewById(R.id.iv_capture_img);
			doc3_iv_capture_img.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					capturePicture(CAMERA_REQ_DOC3);
				}
			});
			ImageView doc3_iv_choose_file = (ImageView) doc3View.findViewById(R.id.iv_choose_file);
			doc3_iv_choose_file.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					chooseFile(GALLERY_REQ_DOC3);
				}
			});
			//ImageView doc3_iv_perv = (ImageView)doc3View.findViewById(R.id.iv_perv);
			Button doc3_btn_upload = (Button) doc3View.findViewById(R.id.btn_upload);
			doc3_btn_upload.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					imageUploadDoc3();
				}
			});

		}else if(userType == RegisterActivity.EMPLOYEE_OF_BROKER_FIRM){
			//Add doc 1
			doc1View = mLayoutInflater.inflate(R.layout.upload_registeration_doc,null);
			ll_doc_root.addView(doc1View);
			TextView doc1_tv_title = (TextView) doc1View.findViewById(R.id.tv_title);
			doc1_tv_title.setText("Aadhar Card/Passport/Driving Licence");
			ImageView doc1_iv_capture_img = (ImageView) doc1View.findViewById(R.id.iv_capture_img);
			doc1_iv_capture_img.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					capturePicture(CAMERA_REQ_DOC1);
				}
			});
			ImageView doc1_iv_choose_file = (ImageView) doc1View.findViewById(R.id.iv_choose_file);
			doc1_iv_choose_file.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					chooseFile(GALLERY_REQ_DOC1);
				}
			});
			//ImageView doc1_iv_perv = (ImageView)doc1View.findViewById(R.id.iv_perv);
			Button doc1_btn_upload = (Button) doc1View.findViewById(R.id.btn_upload);
			doc1_btn_upload.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					imageUploadDoc1();
				}
			});
			// add doc 2
			doc2View = mLayoutInflater.inflate(R.layout.upload_registeration_doc,null);
			ll_doc_root.addView(doc2View);
			TextView doc2_tv_title = (TextView) doc2View.findViewById(R.id.tv_title);
			doc2_tv_title.setText("PAN Card");
			ImageView doc2_iv_capture_img = (ImageView) doc2View.findViewById(R.id.iv_capture_img);
			doc2_iv_capture_img.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					capturePicture(CAMERA_REQ_DOC2);
				}
			});
			ImageView doc2_iv_choose_file = (ImageView) doc2View.findViewById(R.id.iv_choose_file);
			doc2_iv_choose_file.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					chooseFile(GALLERY_REQ_DOC2);
				}
			});
			//ImageView doc2_iv_perv = (ImageView)doc2View.findViewById(R.id.iv_perv);
			Button doc2_btn_upload = (Button) doc2View.findViewById(R.id.btn_upload);
			doc2_btn_upload.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					imageUploadDoc2();
				}
			});

		}else if(userType == RegisterActivity.INDIVIDUAL){
			//Add doc 1
			doc1View = mLayoutInflater.inflate(R.layout.upload_registeration_doc,null);
			ll_doc_root.addView(doc1View);
			TextView doc1_tv_title = (TextView) doc1View.findViewById(R.id.tv_title);
			doc1_tv_title.setText("Aadhar Card/Passport/Driving Licence");
			ImageView doc1_iv_capture_img = (ImageView) doc1View.findViewById(R.id.iv_capture_img);
			doc1_iv_capture_img.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					capturePicture(CAMERA_REQ_DOC1);
				}
			});
			ImageView doc1_iv_choose_file = (ImageView) doc1View.findViewById(R.id.iv_choose_file);
			doc1_iv_choose_file.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					chooseFile(GALLERY_REQ_DOC1);
				}
			});
			//ImageView doc1_iv_perv = (ImageView)doc1View.findViewById(R.id.iv_perv);
			Button doc1_btn_upload = (Button) doc1View.findViewById(R.id.btn_upload);
			doc1_btn_upload.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					imageUploadDoc1();
				}
			});
			// add doc 2
			doc2View = mLayoutInflater.inflate(R.layout.upload_registeration_doc,null);
			ll_doc_root.addView(doc2View);
			TextView doc2_tv_title = (TextView) doc2View.findViewById(R.id.tv_title);
			doc2_tv_title.setText("PAN Card");
			ImageView doc2_iv_capture_img = (ImageView) doc2View.findViewById(R.id.iv_capture_img);
			doc2_iv_capture_img.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					capturePicture(CAMERA_REQ_DOC2);
				}
			});
			ImageView doc2_iv_choose_file = (ImageView) doc2View.findViewById(R.id.iv_choose_file);
			doc2_iv_choose_file.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					chooseFile(GALLERY_REQ_DOC2);
				}
			});
			//ImageView doc2_iv_perv = (ImageView)doc2View.findViewById(R.id.iv_perv);
			Button doc2_btn_upload = (Button) doc2View.findViewById(R.id.btn_upload);
			doc2_btn_upload.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					imageUploadDoc2();
				}
			});

			// add doc 3
			doc3View = mLayoutInflater.inflate(R.layout.upload_registeration_doc,null);
			ll_doc_root.addView(doc3View);
			TextView doc3_tv_title = (TextView) doc3View.findViewById(R.id.tv_title);
			doc3_tv_title.setText("RERA Registration Document");
			ImageView doc3_iv_capture_img = (ImageView) doc3View.findViewById(R.id.iv_capture_img);
			doc3_iv_capture_img.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					capturePicture(CAMERA_REQ_DOC3);
				}
			});
			ImageView doc3_iv_choose_file = (ImageView) doc3View.findViewById(R.id.iv_choose_file);
			doc3_iv_choose_file.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					chooseFile(GALLERY_REQ_DOC3);
				}
			});
			//ImageView doc3_iv_perv = (ImageView)doc3View.findViewById(R.id.iv_perv);
			Button doc3_btn_upload = (Button) doc3View.findViewById(R.id.btn_upload);
			doc3_btn_upload.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					imageUploadDoc3();
				}
			});
		}
	}// End of add doc


	private void doneAlert(String title,String message) {
		AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
		myAlertDialog.setTitle(title);
		myAlertDialog.setMessage(message);

		myAlertDialog.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.dismiss();
						//Intent mIntent = new Intent(SignUpDocActivity.this,LoginActivity.class);
						setResult(RESULT_OK, getIntent());
						//startActivity(mIntent);
						finish();
					}
				});
		myAlertDialog.show();
	}


	private void setCapturedImage(int cameraReqCode){
		if(cameraReqCode == -1)return;
		File f = new File(Environment.getExternalStorageDirectory().toString());
		for (File temp : f.listFiles()) {
			String docName = "";
			if(cameraReqCode == CAMERA_REQ_DOC1)docName = doc1Name;
			if(cameraReqCode == CAMERA_REQ_DOC2)docName = doc2Name;
			if(cameraReqCode == CAMERA_REQ_DOC3)docName = doc3Name;
			if (temp.getName().equals(docName)) {
				f = temp;
				if(cameraReqCode == CAMERA_REQ_DOC1)selectedDoc1Path = f.getAbsolutePath();
				if(cameraReqCode == CAMERA_REQ_DOC2)selectedDoc2Path = f.getAbsolutePath();
				if(cameraReqCode == CAMERA_REQ_DOC3)selectedDoc3Path = f.getAbsolutePath();
				break;
			}
		}if (!f.exists()) {
			showToast("Error while capturing image");
			return;
		}
		try {
			Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
			bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true);
			try {
				int rotate = 0;
				ExifInterface exif = new ExifInterface(f.getAbsolutePath());
				int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
				switch (orientation) {
					case ExifInterface.ORIENTATION_ROTATE_270:rotate = 270;break;
					case ExifInterface.ORIENTATION_ROTATE_180:rotate = 180;break;
					case ExifInterface.ORIENTATION_ROTATE_90:rotate = 90;break;
				}
				Matrix matrix = new Matrix();
				matrix.postRotate(rotate);
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				View mView = null;
				if(cameraReqCode == CAMERA_REQ_DOC1)mView = doc1View;
				if(cameraReqCode == CAMERA_REQ_DOC2)mView = doc2View;
				if(cameraReqCode == CAMERA_REQ_DOC3)mView = doc3View;
				if(mView != null) ((ImageView)mView.findViewById(R.id.iv_preview)).setImageBitmap(bitmap);
			} catch (Exception e) {e.printStackTrace();}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}// end of setCapture Image


	private void setChooseFile(Intent data, int galleryReqCode){
		if (data != null) {
			Uri selectedImage = data.getData();
			//if(type == 22)selectedDoc1Path = getPath(selectedImage);
			String[] filePath = { MediaStore.Images.Media.DATA };
			Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
			c.moveToFirst();
			if(galleryReqCode == GALLERY_REQ_DOC1 && doc1View != null){
				selectedDoc1Path = c.getString(c.getColumnIndex(filePath[0]));
				c.close();
				if (selectedDoc1Path != null) {
					Bitmap bitmap = BitmapFactory.decodeFile(selectedDoc1Path); // load
					bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, false);
					((ImageView) doc1View.findViewById(R.id.iv_preview)).setImageBitmap(bitmap);
				}
			}else if(galleryReqCode == GALLERY_REQ_DOC2 && doc2View != null){
				selectedDoc2Path = c.getString(c.getColumnIndex(filePath[0]));
				c.close();
				if (selectedDoc2Path != null) {
					Bitmap bitmap = BitmapFactory.decodeFile(selectedDoc2Path); // load
					bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, false);
					((ImageView) doc2View.findViewById(R.id.iv_preview)).setImageBitmap(bitmap);
				}
			}else if(galleryReqCode == GALLERY_REQ_DOC3 && doc3View != null){
				selectedDoc3Path = c.getString(c.getColumnIndex(filePath[0]));
				c.close();
				if (selectedDoc3Path != null) {
					Bitmap bitmap = BitmapFactory.decodeFile(selectedDoc3Path); // load
					bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, false);
					((ImageView) doc3View.findViewById(R.id.iv_preview)).setImageBitmap(bitmap);
				}
			}
		} else {
			showToast("Canceled");
		}
	}


	private void getPermission() {
		if(ActivityCompat.checkSelfPermission(this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
				|| ActivityCompat.checkSelfPermission(this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
				|| ActivityCompat.checkSelfPermission(this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED){
			if(ActivityCompat.shouldShowRequestPermissionRationale(this,permissionsRequired[0])
					|| ActivityCompat.shouldShowRequestPermissionRationale(this,permissionsRequired[1])
					|| ActivityCompat.shouldShowRequestPermissionRationale(this,permissionsRequired[2])){
				//Show Information about why you need the permission
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Need Multiple Permissions");
				builder.setMessage("This app needs Camera and Storage permissions.");
				builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						ActivityCompat.requestPermissions(SignUpDocActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
					}
				});
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				builder.show();
			} else if (permissionStatus.getBoolean(permissionsRequired[0],false)) {
				//Previously Permission Request was cancelled with 'Dont Ask Again',
				// Redirect to Settings after showing Information about why you need the permission
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Need Multiple Permissions");
				builder.setMessage("This app needs Camera and Location permissions.");
				builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						sentToSettings = true;
						Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
						Uri uri = Uri.fromParts("package", getPackageName(), null);
						intent.setData(uri);
						startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
						showToast("Go to Permissions to Grant  Camera and Storage");
					}
				});
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				builder.show();
			}  else {
				//just request the permission
				ActivityCompat.requestPermissions(this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
			}
			SharedPreferences.Editor editor = permissionStatus.edit();
			editor.putBoolean(permissionsRequired[0],true);
			editor.commit();
		} else {
			//You already have the permission, just go ahead.
			//proceedAfterPermission();
		}

	}// end of getPermission()


	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if(requestCode == PERMISSION_CALLBACK_CONSTANT){
			//check if all permissions are granted
			boolean allgranted = false;
			for(int i=0;i<grantResults.length;i++){
				if(grantResults[i]==PackageManager.PERMISSION_GRANTED){
					allgranted = true;
				} else {
					allgranted = false;
					break;
				}
			}

			if(allgranted){
				//proceedAfterPermission();
			} else if(ActivityCompat.shouldShowRequestPermissionRationale(this,permissionsRequired[0])
					|| ActivityCompat.shouldShowRequestPermissionRationale(this,permissionsRequired[1])
					|| ActivityCompat.shouldShowRequestPermissionRationale(this,permissionsRequired[2])){
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Need Multiple Permissions");
				builder.setMessage("This app needs Camera and Storage permissions.");
				builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						showToast("This app needs Camera and Storage permissions.");
						finish();
						//ActivityCompat.requestPermissions(SignUpDocActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
					}
				});
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				builder.show();
			} else {
				showToast("Unable to get Permission");
			}
		}
	}


}
