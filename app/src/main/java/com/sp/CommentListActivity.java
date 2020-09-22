package com.sp;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.AppEnums.APIType;
import com.VO.AllCommentsVO;
import com.adapters.CommentsListAdapter;
import com.appnetwork.AsyncThread;
import com.appnetwork.ReqRespBean;
import com.appnetwork.WEBAPI;
import com.helper.IntentDataObject;
import com.helper.ParamsConstants;
import com.jsonparser.JsonParser;

import java.util.HashMap;

public class CommentListActivity extends BaseFragmentActivity {

	private Activity ctx = this;
	private String projectId;
	private ListView list;
	private IntentDataObject mIntentDataObject = null;
	private  AsyncThread mAsyncThread = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment_list);

		final Toolbar toolbar = setToolBar();
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		list = (ListView) findViewById(R.id.listViewComments);
		if(getIntent() != null && getIntent().getSerializableExtra(IntentDataObject.OBJ) != null && getIntent().getSerializableExtra(IntentDataObject.OBJ) instanceof IntentDataObject){
			mIntentDataObject = (IntentDataObject) getIntent().getSerializableExtra(IntentDataObject.OBJ);
			if(mIntentDataObject != null && mIntentDataObject.getData() != null){
				HashMap<String,String> searchParams = mIntentDataObject.getData();
				StringBuilder mStringBuilder = new StringBuilder("");
				mStringBuilder.append(ParamsConstants.ID);
				mStringBuilder.append("=");
				mStringBuilder.append(searchParams.get(ParamsConstants.ID));
				mStringBuilder.append("&");
				mStringBuilder.append(ParamsConstants.COMMENT_TYPE);
				mStringBuilder.append("=");
				mStringBuilder.append(searchParams.get(ParamsConstants.COMMENT_TYPE));
				getAllComment(mStringBuilder.toString());
			}else{
				finish();
			}
		}else{
			finish();
		}
	}


//	ImageView img_fav = (ImageView)findViewById(R.id.imageViewFav);

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.comment_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		switch (id) {
			case R.id.action_settings:

				break;
			case android.R.id.home:
				finish();
				break;
		}
		return super.onOptionsItemSelected(item);
	}



	@Override
	protected String setActionBarTitle() {
		return "All Comments";
	}

	private void getAllComment(String params) {
		ReqRespBean mBean = new ReqRespBean();
		mBean.setApiType(APIType.GET_ALL_COMMENT);
		mBean.setRequestmethod(WEBAPI.POST);
		mBean.setUrl(WEBAPI.getWEBAPI(APIType.GET_ALL_COMMENT));
		mBean.setMimeType(WEBAPI.contentTypeFormData);
		if(params != null && params.length() > 0) mBean.setJson(params);
		mBean.setmHandler(mHandler);
		mBean.setCtx(this);
		mAsyncThread = new AsyncThread();
		mAsyncThread.initProgressDialog(CommentListActivity.this,mOnCancelListener);
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
				//TODO: Error
				return false;
			} else {
				ReqRespBean mBean = (ReqRespBean) msg.obj;
				switch (mBean.getApiType()) {
					case GET_ALL_COMMENT:
						if(mBean.getJson() != null) {
							AllCommentsVO mAllCommentsVO = (AllCommentsVO) JsonParser.convertJsonToBean(APIType.GET_ALL_COMMENT,mBean.getJson() );
							if(mAllCommentsVO != null && mAllCommentsVO.getData() != null){
								list.setAdapter(new CommentsListAdapter(ctx, mAllCommentsVO.getData()));
							}else{
								showToast(getString(R.string.unable_to_connect_server));
							}
						}else{
							showToast(getString(R.string.unable_to_connect_server));
						}
						break;
					default:
						break;
				}
			}
			return true;
		}
	});


}
