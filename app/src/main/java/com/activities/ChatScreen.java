package com.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.AppEnums.APIType;
import com.ChatServerModel.CreateChannel;
import com.ChatServerModel.InitialMessageList;
import com.ChatServerModel.RegisterNewUser;
import com.VO.UnitDetailVO;
import com.adapters.ChatRecyclerAdapter;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.appnetwork.AsyncThread;
import com.appnetwork.ReqRespBean;
import com.appnetwork.WEBAPI;
import com.google.firebase.iid.FirebaseInstanceId;
import com.helper.BMHConstants;
import com.helper.ChatConstants;
import com.helper.IntentDataObject;
import com.helper.ParamsConstants;
import com.helper.UrlFactory;
import com.jsonparser.JsonParser;
import com.model.ChatTagsRespModel;
import com.model.GetOfferCodeRespModel;
import com.model.NetworkErrorObject;
import com.model.OfferStatusRespModel;
import com.model.SubmitOfferJsonModel;
import com.sendbird.android.AdminMessage;
import com.sendbird.android.FileMessage;
import com.sp.BMHApplication;
import com.sp.BaseFragmentActivity;
import com.sp.ConnectivityReceiver;
import com.sp.PersonalDetails;
import com.sp.R;
import com.squareup.picasso.Picasso;
import com.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.utils.Utils.createTaggedMessage;

public class ChatScreen extends BaseFragmentActivity implements ConnectivityReceiver.ConnectivityReceiverListener,
        View.OnClickListener {
    private final String TAG = ChatScreen.class.getSimpleName();
    private BMHApplication app;
    public static final String LOGIN_INTENT = "LOGIN_INTENT";
    private NetworkErrorObject mNetworkErrorObject = null;
    private AsyncThread mAsyncThread = null;
    private HashMap<String, String> mData;
    private ImageView iv_unit_img, iv_attachment, iv_camera, iv_send;
    private TextView tv_unit_no, tv_unit_name, tv_details, tv_reserved;
    private ProgressBar progressBar;
    private EditText et_message;
    private LinearLayout ll_tags;
    private HorizontalScrollView hsv;
    UnitDetailVO mUnitDetails;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private ChatRecyclerAdapter mChatAdapter;
    private String userChatId, builderChatId, chatId;
    private static final int STATE_NORMAL = 0;
    private static final int STATE_EDIT = 1;
    private Toolbar toolbar;
    //String payment;
    String mypref = "hello";
    SharedPreferences sharedPreferences;
    private String user_id, channel_url;
    private boolean isUserActive = false;
    private RequestQueue queue;
    private ArrayList<InitialMessageList.Data> mArrayList;
    private TimerTask timerTask;
    private Integer is_reserved;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        app = (BMHApplication) getApplication();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        initViews();
        setTypeface();
        if (getIntent() != null && getIntent().getSerializableExtra(IntentDataObject.OBJ) != null && getIntent().getSerializableExtra(IntentDataObject.OBJ) instanceof IntentDataObject) {
            IntentDataObject mIntentDataObject = (IntentDataObject) getIntent().getSerializableExtra(IntentDataObject.OBJ);
            if (mIntentDataObject.getData() != null && mIntentDataObject.getData() instanceof HashMap) {
                mData = (HashMap<String, String>) mIntentDataObject.getData();
                setUiData(mData);
            } else {
                showToast("Invalid data passing");
            }
        } else {
            showToast(getString(R.string.something_went_wrong));
            finish();
        }
        queue = Volley.newRequestQueue(this);
        getChatTags();
        registerNewUser();
    }

    private void setUiData(HashMap<String, String> mData) {
        if (mData == null) return;
        getSupportActionBar().setTitle(Html.fromHtml(mData.get(ParamsConstants.UNIT_TITLE)));
        tv_unit_name.setText(Html.fromHtml(mData.get(ParamsConstants.UNIT_TITLE)));
        tv_unit_no.setText("Unit No. " + mData.get(ParamsConstants.UNIT_NO));
        is_reserved = Integer.parseInt(mData.get(ParamsConstants.UNIT_RESERVED));
        //if(is_reserved!=null)
        if (is_reserved == 1) {
            tv_reserved.setText("Reserved");
        } else {
            tv_reserved.setVisibility(View.GONE);
        }
        String url = UrlFactory.IMG_BASEURL + mData.get(ParamsConstants.UNIT_IMAGE);//TODO: assign url
        if (url != null && !url.equalsIgnoreCase("")) {
            Picasso.with(this.getApplicationContext()).load(UrlFactory.getShortImageByWidthUrl((int) Utils.dp2px(120, this), url))
                    .placeholder(BMHConstants.PLACE_HOLDER).error(BMHConstants.NO_IMAGE).into(iv_unit_img);
        }

    }

    private void setTypeface() {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");
        //edEmail.setTypeface(font);
        //etPassword.setTypeface(font);
    }

    public void sendData(String coupon_code) {
        Intent intent = getIntent();
        mUnitDetails = intent.getParcelableExtra("unitvo");
        Intent i = new Intent(ChatScreen.this, PersonalDetails.class);
        i.putExtra("unitvo", mUnitDetails);
        i.putExtra("coupon_code", coupon_code);
        i.putExtra("payment_plan", mData.get("payment_plan"));
        startActivity(i);
        //Toast.makeText(this,mUnitDetails.getId(),Toast.LENGTH_LONG).show();
        /*Intent intentCoupan = new Intent(ChatScreen.this, PersonalDetails.class);
        startActivity(intentCoupan);*/

    }

    public void initViews() {
        /*getSupportActionBar().setTitle("Chat");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
        tv_unit_name = findViewById(R.id.tv_unit_name);
        tv_reserved = findViewById(R.id.tv_reserved);
        tv_details = findViewById(R.id.tv_details);
        tv_unit_no = findViewById(R.id.tv_unit_no);
        iv_unit_img = findViewById(R.id.iv_unit_img);
        et_message = findViewById(R.id.et_message);
        /*iv_attachment = (ImageView)findViewById(R.id.iv_attachment);
		iv_camera = (ImageView)findViewById(R.id.iv_camera);*/
        iv_send = findViewById(R.id.iv_send);
        ll_tags = findViewById(R.id.ll_tags);
        hsv = findViewById(R.id.hsv_tags);
        mRecyclerView = findViewById(R.id.lv_chat);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        ll_tags.setVisibility(View.GONE);
        timer = new Timer();
        tv_details.setOnClickListener(this);
        iv_send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_details:
                //if(mUnitCaraouselVO != null && !mUnitCaraouselVO.getSold_status().equalsIgnoreCase("sold")) {
                if (mData != null) {
                    Intent unitDetailsIntent = new Intent(ChatScreen.this, ChatUnitDetails.class);
                    unitDetailsIntent.putExtra(ParamsConstants.BUILDER_ID, BMHConstants.CURRENT_BUILDER_ID);
                    unitDetailsIntent.putExtra("unitId", mData.get(ParamsConstants.UNIT_ID));
                    unitDetailsIntent.putExtra("unitTitle", mData.get(ParamsConstants.UNIT_TITLE));
                    unitDetailsIntent.putExtra("payment_plan", mData.get("payment_plan"));
                    unitDetailsIntent.putExtra("bhkType", mData.get(ParamsConstants.BHK_TYPE));
                    unitDetailsIntent.putExtra("unitType", "");
                    unitDetailsIntent.putExtra("priceSqft", "");
                    startActivity(unitDetailsIntent);
                } else {
                    showToast(getString(R.string.something_went_wrong));
                }
                //}else{
                //	showToast(getString(R.string.unit_reserved));
                //}
                break;
            case R.id.iv_send:
                String message = et_message.getText().toString().trim();
                if (message != null && !message.isEmpty()) {
                    sendUserMessage(message);
                    et_message.setText("");
                } else {
                    showToast(getString(R.string.please_enter_text));
                }
                break;
        }
    }

    public boolean isValidData() {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected String setActionBarTitle() {
        return "Chat";
    }

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
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            showToast("Network available");
            if (mNetworkErrorObject == null || mNetworkErrorObject.getUiEventType() == null || mNetworkErrorObject.getAlertDialog() == null)
                return;
            switch (mNetworkErrorObject.getUiEventType()) {
                case RETRY_LOGIN:
                    break;
            }
            mNetworkErrorObject.getAlertDialog().dismiss();
            mNetworkErrorObject = null;
        } else {
            showToast("Network not available");
        }
    }

    private void getChatTags() {
        if (mData == null) return;
        ReqRespBean mBean = new ReqRespBean();
        mBean.setApiType(APIType.CHAT_TAGS);
        mBean.setRequestmethod(WEBAPI.POST);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.CHAT_TAGS));
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        StringBuilder mStringBuilder = new StringBuilder("");
        mStringBuilder.append(ParamsConstants.USER_ID + "=" + app.getFromPrefs(BMHConstants.USERID_KEY));
        mStringBuilder.append("&" + ParamsConstants.UNIT_ID + "=" + mData.get(ParamsConstants.UNIT_ID));
        mStringBuilder.append("&" + ParamsConstants.BUILDER_ID + "=" + BMHConstants.CURRENT_BUILDER_ID);
        mStringBuilder.append("&" + ParamsConstants.USER_TYPE + "=" + BMHConstants.SALES_PERSON);
        mStringBuilder.append("&" + ParamsConstants.PLAN_ID + "=" + mData.get("payment_plan"));
        mBean.setJson(mStringBuilder.toString());
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        mAsyncThread.initProgressDialog(ChatScreen.this, mOnCancelListener);
        mAsyncThread.execute(mBean);
        mAsyncThread = null;
        //    mLepLogger.info(TAG, "sendLoginRequest():URl:" + mBean.getUrl() + ",Req Json:" + mBean.getJson());
    }

    private void getOfferCode(String desc) {
        ReqRespBean mBean = new ReqRespBean();
        mBean.setApiType(APIType.GET_OFFER_CODE);
        mBean.setRequestmethod(WEBAPI.POST);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.GET_OFFER_CODE));
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        StringBuilder mStringBuilder = new StringBuilder("");
        mStringBuilder.append("builder_chat_id=" + builderChatId);
        mStringBuilder.append("&user_chat_id=" + userChatId);
        mStringBuilder.append("&chatId=" + chatId);
        mStringBuilder.append("&description=" + desc);
        mBean.setJson(mStringBuilder.toString());
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        mAsyncThread.initProgressDialog(ChatScreen.this, mOnCancelListener);
        mAsyncThread.execute(mBean);
        mAsyncThread = null;
    }

    private void getAnotherOfferCode(String desc) {
        ReqRespBean mBean = new ReqRespBean();
        mBean.setApiType(APIType.GET_ANOTHER_OFFER_CODE);
        mBean.setRequestmethod(WEBAPI.POST);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.GET_ANOTHER_OFFER_CODE));
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        StringBuilder mStringBuilder = new StringBuilder("");
        //mStringBuilder.append(ParamsConstants.USER_ID + "="+app.getFromPrefs(BMHConstants.USERID_KEY));
        //mStringBuilder.append("&"+ParamsConstants.UNIT_ID + "=" + mData.get(ParamsConstants.UNIT_ID));
        //mStringBuilder.append("&"+ ParamsConstants.BUILDER_ID+"="+BMHConstants.CURRENT_BUILDER_ID);
        //mStringBuilder.append("&"+ ParamsConstants.USER_TYPE+"="+BMHConstants.CUSTOMER);
        mStringBuilder.append("builder_chat_id=" + builderChatId);
        mStringBuilder.append("&user_chat_id=" + userChatId);
        mStringBuilder.append("&chatId=" + chatId);
        mStringBuilder.append("&description=" + desc);
        mBean.setJson(mStringBuilder.toString());
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        mAsyncThread.initProgressDialog(ChatScreen.this, mOnCancelListener);
        mAsyncThread.execute(mBean);
        mAsyncThread = null;
    }

    private void updateOfferCode(String offerCode, int status) {
        ReqRespBean mBean = new ReqRespBean();
        mBean.setApiType(APIType.UPDATE_OFFER_STATUS);
        mBean.setRequestmethod(WEBAPI.POST);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.UPDATE_OFFER_STATUS));
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        StringBuilder mStringBuilder = new StringBuilder("");
        mStringBuilder.append("offer_code=" + offerCode);
        mStringBuilder.append("&offer_status=" + status);
        mBean.setJson(mStringBuilder.toString());
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        mAsyncThread.initProgressDialog(ChatScreen.this, mOnCancelListener);
        mAsyncThread.execute(mBean);
        mAsyncThread = null;
    }

    DialogInterface.OnCancelListener mOnCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            if (mAsyncThread != null) mAsyncThread.cancel(true);
            mAsyncThread = null;
        }
    };

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.obj == null) {
                return false;
            } else {
                ReqRespBean mBean = (ReqRespBean) msg.obj;
                switch (mBean.getApiType()) {
                    case CHAT_TAGS:
                        ChatTagsRespModel chatTagsRespModel = (ChatTagsRespModel) JsonParser.convertJsonToBean(APIType.CHAT_TAGS, mBean.getJson());
                        if (chatTagsRespModel != null) {
                            if (chatTagsRespModel.isSuccess() && chatTagsRespModel.getData() != null && chatTagsRespModel.getData().getTag() != null) {
                                ll_tags.removeAllViews();
                                LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                mLayoutParams.setMargins((int) Utils.dp2px(5, ChatScreen.this), (int) Utils.dp2px(5, ChatScreen.this), (int) Utils.dp2px(5, ChatScreen.this), (int) Utils.dp2px(5, ChatScreen.this));
                                for (ChatTagsRespModel.Tag tag : chatTagsRespModel.getData().getTag()) {
                                    TextView tagView = (TextView) getLayoutInflater().inflate(R.layout.chat_tag, null);
                                    tagView.setLayoutParams(mLayoutParams);
                                    tagView.setText(tag.getTag_string());
                                    tagView.setTag(R.integer.tag, tag);
                                    tagView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (view.getTag(R.integer.tag) != null && view.getTag(R.integer.tag) instanceof ChatTagsRespModel.Tag) {
                                                ChatTagsRespModel.Tag tag = (ChatTagsRespModel.Tag) view.getTag(R.integer.tag);
                                                //if(tag.getTag_id().equals(ChatConstants.IS_IT_AVAILABLE))
                                                sendUserMessage(createTaggedMessage(tag.getTag_id(), tag.getMessage()));
                                            }
                                        }
                                    });
                                    is_reserved = Integer.valueOf(mData.get(ParamsConstants.UNIT_RESERVED));
                                    if (is_reserved == 1) {
                                        hsv.setVisibility(View.GONE);
                                    } else {
                                        ll_tags.addView(tagView);
                                    }
                                }
                                userChatId = chatTagsRespModel.getData().getUser_chat_id();
                                builderChatId = chatTagsRespModel.getData().getBuilder_chat_id();
                                chatId = chatTagsRespModel.getData().getChat_id();
                                getUserChannel();
                            } else {
                                showToast(chatTagsRespModel.getMessage());
                            }
                        } else {
                            showToast(getString(R.string.something_went_wrong));
                        }
                        break;
                    case GET_OFFER_CODE:
                        GetOfferCodeRespModel getOfferCodeRespModel = (GetOfferCodeRespModel) JsonParser.convertJsonToBean(APIType.GET_OFFER_CODE, mBean.getJson());
                        if (getOfferCodeRespModel != null) {
                            if (getOfferCodeRespModel.isSuccess() && getOfferCodeRespModel.getData() != null) {
                                String descData = JsonParser.convertBeanToJson(new SubmitOfferJsonModel(getOfferCodeRespModel.getData().getOffer_code(), getOfferCodeRespModel.getData().getDescription()));
                                descData = createTaggedMessage(ChatConstants.SUBMIT_OFFER, descData);
                                sendUserMessage(descData);
                            } else {
                                showToast(getOfferCodeRespModel.getMessage());
                            }
                        } else {
                            showToast(getString(R.string.something_went_wrong));
                        }
                        break;
                    case GET_ANOTHER_OFFER_CODE:
                        GetOfferCodeRespModel getAnotherOffer = (GetOfferCodeRespModel) JsonParser.convertJsonToBean(APIType.GET_ANOTHER_OFFER_CODE, mBean.getJson());
                        if (getAnotherOffer != null) {
                            if (getAnotherOffer.isSuccess() && getAnotherOffer.getData() != null) {
                                String descData = JsonParser.convertBeanToJson(new SubmitOfferJsonModel(getAnotherOffer.getData().getOffer_code(), getAnotherOffer.getData().getDescription()));
                                descData = createTaggedMessage(ChatConstants.WHATS_YOUR_OFFER, descData);
                                sendUserMessage(descData);
                            } else {
                                showToast(getAnotherOffer.getMessage());
                            }
                        } else {
                            showToast(getString(R.string.something_went_wrong));
                        }
                        break;
                    case UPDATE_OFFER_STATUS:
                        OfferStatusRespModel offerStatusRespModel = (OfferStatusRespModel) JsonParser.convertJsonToBean(APIType.UPDATE_OFFER_STATUS, mBean.getJson());
                        if (offerStatusRespModel != null) {
                            if (offerStatusRespModel.isSuccess() && offerStatusRespModel.getData() != null) {
                                //String descData = JsonParser.convertBeanToJson(new SubmitOfferJsonModel(getOfferCodeRespModel.getData().getOffer_code(),getOfferCodeRespModel.getData().getDescription()));
                                //descData = Utils.createTaggedMessage(ChatConstants.MAKE_ANOTHER_OFFER,descData);
                                //sendUserMessage(descData);
                                if (offerStatusRespModel.getData().getOffer_status() == ChatRecyclerAdapter.ACCEPT) {
                                    //String data=offerStatusRespModel.getData().getOffer_code()+offerStatusRespModel.getData().getDescription();
                                    String descData = JsonParser.convertBeanToJson(new OfferStatusRespModel.Data(offerStatusRespModel.getData().getOffer_code(), offerStatusRespModel.getData().getCoupon_code(), offerStatusRespModel.getData().getPlan_title(), offerStatusRespModel.getData().getDescription(), offerStatusRespModel.getData().getOffer_status()));
                                    String acceptedMessage = Utils.createTaggedMessage(ChatConstants.ACCEPT_NEW_OFFER, descData);
                                    sendUserMessage(acceptedMessage);
                                } else {
                                    String descData = JsonParser.convertBeanToJson(new OfferStatusRespModel.Data(offerStatusRespModel.getData().getOffer_code(), offerStatusRespModel.getData().getCoupon_code(), offerStatusRespModel.getData().getPlan_title(), offerStatusRespModel.getData().getDescription(), offerStatusRespModel.getData().getOffer_status()));
                                    descData = createTaggedMessage(ChatConstants.REJECT_NEW_OFFER, descData);
                                    sendUserMessage(descData);
                                }
                            } else {
                                showToast(offerStatusRespModel.getMessage());
                            }
                        } else {
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

    public void sendUserMessage(final String text) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.SEND_MESSAGE) + "/" + channel_url + "/messages",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Send Message Response", response);
                        refresh();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", "" + error);
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(ParamsConstants.MESSAGE_TYPE, "MESG");
                params.put(ParamsConstants.USER_ID, user_id);
                params.put(ParamsConstants.MESSAGE, text);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap();
                headers.put("auth", BMHConstants.HEADER);
                // headers.put("apiKey", "xxxxxxxxxxxxxxx");
                return headers;
            }
        };
        BMHApplication.getInstance().addToRequestQueue(stringRequest, "headerRequest");

    }


    private void loadInitialMessageList(int numMessages) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, WEBAPI.getWEBAPI(APIType.LOAD_MESSAGE) + "/" + channel_url + "/messages?message_ts=" + numMessages + "&reader_id=" + user_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("User Channel Response", response);
                        InitialMessageList initialMessageList = (InitialMessageList) JsonParser.convertJsonToBean(APIType.LOAD_MESSAGE, response);
                        if (initialMessageList.getData() != null) {
                            progressBar.setVisibility(View.GONE);
                            ll_tags.setVisibility(View.VISIBLE);
                            mArrayList = initialMessageList.getData();
                            if (mArrayList != null) {
                                ArrayList<InitialMessageList.Data> userMsglist = new ArrayList<>();
                                for (InitialMessageList.Data userMessage : mArrayList) {
                                    if (userMessage instanceof InitialMessageList.Data) {
                                        InitialMessageList.Data userMsg = (InitialMessageList.Data) userMessage;
                                        userMsglist.add(userMessage);
                                    }
                                }
                                mChatAdapter.setMessageList(userMsglist);
                            }
                        } else {
                            showToast(getString(R.string.something_went_wrong));
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", "" + error);
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                //params.put(ParamsConstants.USER_ID, user_id);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap();
                headers.put("auth", BMHConstants.HEADER);
                headers.put("platform", BMHConstants.IOS);
                // headers.put("apiKey", "xxxxxxxxxxxxxxx");
                return headers;
            }
        };
        Log.e("Load Message Request", String.valueOf(stringRequest));
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }

    private void refresh() {
        loadInitialMessageList(ChatConstants.TIMESTAMP);
    }

    private void setUpRecyclerView() {
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mChatAdapter);
        // Load more messages when user reaches the top of the current message list.
    }


    private void setUpChatAdapter() {
        mChatAdapter = new ChatRecyclerAdapter(this, userChatId, builderChatId, chatId, is_reserved);
        mChatAdapter.setOnItemClickListener(new ChatRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onUserMessageItemClick(InitialMessageList.Data message) {

            }

            @Override
            public void onFileMessageItemClick(FileMessage message) {
            }

            @Override
            public void onAdminMessageItemClick(AdminMessage message) {
            }

            @Override
            public void onSubmitOfferClick(String message) {

                getOfferCode(message);
            }

            @Override
            public void onSubmitAnotherOfferClick(String message) {
                getAnotherOfferCode(message);
            }

            @Override
            public void onUpdateOfferClick(String offerCode, int status) {
                updateOfferCode(offerCode, status);
            }

        });

        mChatAdapter.setOnItemLongClickListener(new ChatRecyclerAdapter.OnItemLongClickListener() {
            @Override
            public void onBaseMessageLongClick(final InitialMessageList.Data message, int position) {
            }
        });
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                //refreshing my messages list
                refresh();
            }
        };
        timer.schedule(timerTask, 0, 3 * 1000);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        try {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    //refreshing my messages list
                    refresh();
                }
            };
            timer.schedule(timerTask, 0, 3 * 1000);
        } catch (IllegalStateException e) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        app = (BMHApplication) getApplication();
        BMHApplication.getInstance().setConnectivityListener(this);
        isUserActive = true;
        setUserStatus();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isUserActive = false;
        setUserStatus();
        timer.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isUserActive = false;
        setUserStatus();
        timer.cancel();
    }

    private void registerNewUser() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.REGISTER_NEW_USER),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RegisterNewUserResponse", response);
                        RegisterNewUser registerNewUser = (RegisterNewUser) JsonParser.convertJsonToBean(APIType.REGISTER_NEW_USER, response);
                        if (registerNewUser != null) {
                            isUserActive = true;
                            user_id = registerNewUser.getUser_id();
                            Log.e("USER_ID", user_id);
                        } else {
                            showToast(getString(R.string.something_went_wrong));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", "" + error);
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(ParamsConstants.USER_ID, "SP_" + app.getFromPrefs(BMHConstants.USERID_KEY) + "_" + mData.get(ParamsConstants.UNIT_ID));
                params.put(ParamsConstants.NICKNAME, app.getFromPrefs(BMHConstants.USERNAME_KEY));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap();
                headers.put("auth", BMHConstants.HEADER);
                // headers.put("apiKey", "xxxxxxxxxxxxxxx");
                return headers;
            }
        };
        stringRequest.setShouldCache(false);
        BMHApplication.getInstance().addToRequestQueue(stringRequest, "headerRequest");
    }

    private void getUserChannel() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, WEBAPI.getWEBAPI(APIType.GET_USER_CHANNEL) + "/" + user_id + "?user_chat_id=" + userChatId + "&builder_chat_id=" + builderChatId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Get Channel Response", response);
                        if (response.equalsIgnoreCase("null")) {
                            createUserChannel();
                        } else {
                            CreateChannel existChannel = (CreateChannel) JsonParser.convertJsonToBean(APIType.CREATE_USER_CHANNEL, response);
                            if (existChannel != null) {
                                channel_url = existChannel.getChannel_url();
                                Log.e("Channel Url", channel_url);
                            } else {
                                showToast(getString(R.string.something_went_wrong));
                            }
                        }
                        setUpChatAdapter();
                        setUpRecyclerView();
                        refresh();
                        setUserStatus();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", "" + error);
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                //params.put(ParamsConstants.USER_ID, user_id);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap();
                headers.put("auth", BMHConstants.HEADER);
                // headers.put("apiKey", "xxxxxxxxxxxxxxx");
                return headers;
            }
        };
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
        Log.e("StringRequest", String.valueOf(stringRequest));
        //BMHApplication.getInstance().addToRequestQueue(stringRequest, "headerRequest");
    }

    private void createUserChannel() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.CREATE_USER_CHANNEL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Create Channel Response", response);
                        CreateChannel createChannel = (CreateChannel) JsonParser.convertJsonToBean(APIType.CREATE_USER_CHANNEL, response);
                        if (createChannel != null) {
                            channel_url = createChannel.getChannel_url();
                            refresh();
                            Log.e("USER_ID", user_id);
                        } else {
                            showToast(getString(R.string.something_went_wrong));
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", "" + error);
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(ParamsConstants.USER_CHAT_ID, user_id);
                params.put(ParamsConstants.BUILDER_CHAT_ID, builderChatId);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap();
                headers.put("auth", BMHConstants.HEADER);
                // headers.put("apiKey", "xxxxxxxxxxxxxxx");
                return headers;
            }
        };
        stringRequest.setShouldCache(false);
        BMHApplication.getInstance().addToRequestQueue(stringRequest, "headerRequest");
    }

    private void setUserStatus() {
        final String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.SET_USER_STATE) + "/" + channel_url + "/state",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("State Response", response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", "" + error);
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                if (isUserActive)
                    params.put(ParamsConstants.IS_OPEN, String.valueOf(1));
                else
                    params.put(ParamsConstants.IS_OPEN, String.valueOf(0));

                params.put(ParamsConstants.USER_ID, user_id);
                params.put(ParamsConstants.DEVICE_TOKEN_CHAT_SERVER, FirebaseInstanceId.getInstance().getToken());
                params.put(ParamsConstants.DEVICE_ID_CHAT_SERVER, android_id);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap();
                headers.put("auth", BMHConstants.HEADER);
                // headers.put("apiKey", "xxxxxxxxxxxxxxx");
                return headers;
            }
        };
        Log.e("Status Respone", String.valueOf(stringRequest));
        stringRequest.setShouldCache(false);
        BMHApplication.getInstance().addToRequestQueue(stringRequest, "headerRequest");
    }
}
