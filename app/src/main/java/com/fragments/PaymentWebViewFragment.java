package com.fragments;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.AppEnums.APIType;
import com.AppEnums.UIEventType;
import com.VO.BaseVO;
import com.VO.TransactionVO;
import com.VO.UnitDetailVO;
import com.appnetwork.WEBAPI;
import com.sp.BMHApplication;
import com.sp.ConnectivityReceiver;
import com.sp.CustomAsyncTask;
import com.sp.CustomAsyncTask.AsyncListner;
import com.sp.PaymentWebviewActivity;
import com.sp.R;
import com.exception.BMHException;
import com.helper.BMHConstants;
import com.model.NetworkErrorObject;
import com.model.PropertyModel;
import com.utils.Utils;

public class PaymentWebViewFragment extends BaseFragment implements ConnectivityReceiver.ConnectivityReceiverListener {

	private FragmentActivity fragmentActivity;
	private WebView webview;
	private TransactionVO transactionVo;
	private BMHApplication app;
	private UnitDetailVO vo;
	private static String TAG = PaymentWebViewFragment.class.getSimpleName();
	private LinearLayout llprogress;
	private Button btn_done;
	private NetworkErrorObject mNetworkErrorObject = null;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.payment_webview,container, false);
		llprogress = (LinearLayout) view.findViewById(R.id.ll_webprogress);
		btn_done = (Button)view.findViewById(R.id.btn_done);
		btn_done.setOnClickListener(mOnClickListener);
		final ProgressBar pb = (ProgressBar) view.findViewById(R.id.progressbarweb);
		Bundle b = getArguments();
		app = (BMHApplication) getActivity().getApplication();
		vo = getArguments().getParcelable("unitvo");
		webview = (WebView) view.findViewById(R.id.webView1);
		homeButtonState(false);
		WebSettings settings = webview.getSettings();
		String unitId = vo.getId();

		webview.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				pb.setProgress(progress);
			}
		});
		if (unitId != null && unitId.length()>0) {
			getTransactionData();
		}else{
			showToast(getString(R.string.something_went_wrong));
		}
		webview.getSettings().setJavaScriptEnabled(true);
		webview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
		webview.setWebViewClient(new WebViewClient(){
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(webview, url);
				llprogress.setVisibility(View.GONE);
				Log.i(TAG,"onPageFinished()");
				if(url.equalsIgnoreCase(WEBAPI.getWEBAPI(APIType.PAYMENT_REDIRECT)) && transactionVo!=null && transactionVo.getOrder_id()!=null && transactionVo.getOrder_id().length()>0){
					if(ConnectivityReceiver.isConnected()){
						//TODO: network call
						getTransactionStatus();
					}else{
						mNetworkErrorObject = Utils.showNetworkErrorDialog(getActivity(), UIEventType.RETRY_TRANSACTION_STATUS,
								new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										if(ConnectivityReceiver.isConnected()){
											//TODO: network call
											getTransactionStatus();
											mNetworkErrorObject.getAlertDialog().dismiss();
											mNetworkErrorObject = null;
										}else{
											Utils.showToast(getActivity(),getString(R.string.check_your_internet_connection));
										}
									}
								});
					}
				}
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				Toast.makeText(getActivity(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				llprogress.setVisibility(View.VISIBLE);
			}
		});

		return view;
	}

	class MyJavaScriptInterface {
		@JavascriptInterface
		public void processHTML(String html) {
			// process the html as needed by the app
			String status = null;
			if(html.indexOf("Failure")!=-1){
				status = "Transaction Declined!";
			}else if(html.indexOf("Success")!=-1){
				status = "Transaction Successful!";
			}else if(html.indexOf("Aborted")!=-1){
				status = "Transaction Cancelled!";
			}else{
				status = "Status Not Known!";
			}
			showToast(status);
			//TODO
		}
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	public FragmentActivity getFragmentActivity() {
		return fragmentActivity;
	}

	public void setFragmentActivity(FragmentActivity fragmentActivity) {
		this.fragmentActivity = fragmentActivity;
	}

	private void getTransactionData() {
		CustomAsyncTask loadingTask = new CustomAsyncTask(fragmentActivity, new AsyncListner(){

			@Override
			public void OnBackgroundTaskCompleted() {
				if(transactionVo == null){
					showToast(getString(R.string.unable_to_connect_server));
				}else{
					if(transactionVo.isSuccess()){
						String urlData = transactionVo.getPost_url()
								+"&order_id="+transactionVo.getOrder_id()
								+"&encRequest="+transactionVo.getEncRequest()
								+"&access_code="+transactionVo.getAccess_code()
								+"&merchant_id="+transactionVo.getMerchant_id();
						try {
							webview.loadUrl(urlData);
						} catch (Exception e) {
							showToast("Exception occured while opening webview.");
						}

					}else{
						showToast(getString(R.string.something_went_wrong));
					}
				}
			}
			@Override
			public void DoBackgroundTask(String[] url) {
				PropertyModel model = new PropertyModel();
				try {
					String uesrid = app.getFromPrefs(BMHConstants.USERID_KEY);
					transactionVo = model.getTransactionData(uesrid, vo.getId());
				} catch (BMHException e) {
					e.printStackTrace();
				}
			}
			@Override
			public void OnPreExec() {
			}
		});
		loadingTask.dontShowProgressDialog();
		loadingTask.execute("");
	}

	private void getTransactionStatus() {
		CustomAsyncTask loadingTask = new CustomAsyncTask(fragmentActivity, new AsyncListner(){
			BaseVO vo;
			@Override
			public void OnBackgroundTaskCompleted() {
				if(vo == null){
					showToast(getString(R.string.unable_to_connect_server));
				}else{
					if(vo.isSuccess()){
						//TODO:
						/*Intent mIntent = new Intent(getActivity(), PaymentStatusActivity.class);
						IntentDataObject mIntentDataObject = new IntentDataObject();
						mIntentDataObject.putData("Status","Success");
						mIntentDataObject.putData("Payment Id: ","373747");
						mIntent.putExtra(IntentDataObject.OBJ,mIntentDataObject);
						startActivity(mIntent);
						getActivity().finish();*/
						homeButtonState(true);

					}else{
						//	app.showSnackBar(fragmentActivity, vo.getMessage());
						// show the a new thank you screen with transaction failure message
						homeButtonState(false);
					}
				}
			}
			@Override
			public void DoBackgroundTask(String[] url) {
				PropertyModel model = new PropertyModel();
				try {
					vo = model.getTransactionStatus(app.getFromPrefs(BMHConstants.USERID_KEY),transactionVo.getOrder_id());
				} catch (BMHException e) {
					e.printStackTrace();
				}
			}
			@Override
			public void OnPreExec() {
			}
		});
		loadingTask.dontShowProgressDialog();
		loadingTask.execute("");
	}

	/*private class myWebClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
			System.out.println("hh over ride url="+urlNewString);
			return true;
		}
		@Override
		public void onPageStarted(WebView view, String url, Bitmap icon) {
			System.out.println("hh onPageStarted ="+url);
			llprogress.setVisibility(View.VISIBLE);
//       	mProgressDialog = ProgressDialog.show(fragmentActivity, "","Loading..."+"\t\t\t\t\t", true, false);
//			mProgressDialog.setCanceledOnTouchOutside(false);
//			mProgressDialog.setCancelable(false);
		}
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(webview, url);
//       	if(mProgressDialog != null && mProgressDialog.isShowing())
//				mProgressDialog.dismiss();

			llprogress.setVisibility(View.GONE);
			Log.i(TAG,"onPageFinished()");
			//String redirectUrl = "http://bookmyhouse.com/php_integration_kit/redirecturl.php";
			if(url.equalsIgnoreCase(WEBAPI.getWEBAPI(APIType.PAYMENT_REDIRECT)) && transactionVo!=null && transactionVo.getOrder_id()!=null && transactionVo.getOrder_id().length()>0){
				getTransactionStatus();
			}
		}
		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			showToast("Payment error:"+description);
		}
	}
*/
	@Override
	public String getTagText() {
		return TAG;
	}

	@Override
	public boolean onBackPressed() {
		return false;
	}

	/*private void showToast(String msg){
		Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
	}*/

	View.OnClickListener mOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()){
				case R.id.btn_done:
					if(getActivity() instanceof PaymentWebviewActivity){
						((PaymentWebviewActivity)getActivity()).gotToHome();
					}
					break;
			}
		}
	};

	private void homeButtonState(boolean isEnable){
		RelativeLayout.LayoutParams mLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
		int marginTop = (int)Utils.dp2px(5,getActivity());
		int marginBottom = 0;
		if(isEnable){
			marginBottom = (int)Utils.dp2px(50,getActivity());
			btn_done.setVisibility(View.VISIBLE);
		}else{
			marginBottom = 0;
			btn_done.setVisibility(View.GONE);
		}
		mLayoutParams.setMargins(0,marginTop,0,marginBottom);
		webview.setLayoutParams(mLayoutParams);
	}

	@Override
	public void onResume() {
		super.onResume();
		BMHApplication.getInstance().setConnectivityListener(this);
	}

	@Override
	public void onNetworkConnectionChanged(boolean isConnected) {
		if(isConnected){
			if(mNetworkErrorObject == null || mNetworkErrorObject.getUiEventType() == null || mNetworkErrorObject.getAlertDialog() == null)return;
			switch (mNetworkErrorObject.getUiEventType()){
				case RETRY_TRANSACTION_STATUS:
					getTransactionStatus();
					break;
			}
			mNetworkErrorObject.getAlertDialog().dismiss();
			mNetworkErrorObject = null;
		}
	}

	protected void showToast(String msg){
		Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
	}

}
