package com.sp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.AppEnums.APIType;
import com.AppEnums.LocalityType;
import com.adapters.LocalityListAdapter;
import com.appnetwork.AsyncThread;
import com.appnetwork.ReqRespBean;
import com.appnetwork.WEBAPI;
import com.helper.BMHConstants;
import com.helper.IntentDataObject;
import com.helper.ParamsConstants;
import com.jsonparser.JsonParser;
import com.model.LocalitySearchRespModel;
import com.utils.LocalityData;
import com.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;

public class LocalitySearchActivity extends BaseFragmentActivity implements OnClickListener {

    private DatabaseHandler mDatabaseHandler;
    private ListView lv_locality;
    private EditText et_locality;
    private ImageButton ib_back;
    private AsyncThread mAsyncThread = null;
    private LocalityListAdapter mLocalityListAdapter = null;
    private ArrayList<LocalityData> mList = new ArrayList<>();
    private String cityId = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locality_search);
        initViews();
        setListeners();
        setFonts();
        mDatabaseHandler = new DatabaseHandler(this);
        cityId = app.getFromPrefs(BMHConstants.CITYID);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        LocalityData[] list = mDatabaseHandler.getAllContacts(Integer.valueOf(cityId));
        if (list != null && list.length > 0) {
            mList = new ArrayList<LocalityData>(Arrays.asList(list));
            for (LocalityData temp : mList) {
                temp.setLocalityType(LocalityType.RECENT.value);
            }
            mList.get(0).setHeader(LocalityListAdapter.RECENT_SEARCH);
        }
        mLocalityListAdapter = new LocalityListAdapter(LocalitySearchActivity.this, mList, listItemClick);
        lv_locality.setAdapter(mLocalityListAdapter);
        getLocality();
    }

    private void setFonts(){
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");
        et_locality.setTypeface(font);
    }

    private void setListeners() {
        ib_back.setOnClickListener(mOnClickListener);
        et_locality.addTextChangedListener(mTextWatcher);
    }

    private void initViews() {
        lv_locality = (ListView) findViewById(R.id.lv_locality);
        et_locality = (EditText) findViewById(R.id.et_locality);
        ib_back = (ImageButton)findViewById(R.id.ib_back);
    }


    public void getLocality() {
        ReqRespBean mBean = new ReqRespBean();
        mBean.setApiType(APIType.LOCALITY_SEARCH);
        mBean.setRequestmethod(WEBAPI.POST);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.LOCALITY_SEARCH));
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        mBean.setJson("city_id=" + cityId);
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
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

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            mLocalityListAdapter.getFilter().filter(s);
        }
    };

    OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ib_back:
                    onBackPressed();
                    break;
            }

        }
    };

    OnClickListener listItemClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getTag(R.integer.locality_item) != null && v.getTag(R.integer.locality_item) instanceof LocalityData) {
                LocalityData model = (LocalityData) v.getTag(R.integer.locality_item);
                //Duplicate object
               /* LocalityData model = new LocalityData();
                model.setId(mLocalityLocalityData.getId());
                model.setBuildername(mLocalityLocalityData.getBuildername());
                model.setLocation_Name(mLocalityLocalityData.getLocation_name());
                model.setSubtitle(mLocalityLocalityData.getSubtitle());
                model.setTitle(mLocalityLocalityData.getTitle());*/
                model.setCity(cityId);
                mDatabaseHandler.addContact(model, Integer.parseInt(cityId));
                if (model.getSubtitle().equalsIgnoreCase(LocalityType.PROJECT.value)) {
                    IntentDataObject mIntentDataObject = new IntentDataObject();
                    mIntentDataObject.putData(ParamsConstants.ID,model.getId());
                    mIntentDataObject.putData(ParamsConstants.TYPE,ParamsConstants.BUY);
                    Intent mIntent = new Intent(LocalitySearchActivity.this, ProjectDetailActivity.class);
                    mIntent.putExtra(IntentDataObject.OBJ, mIntentDataObject);
                    startActivity(mIntent);
                    finish();
                }else{
                    Intent output = new Intent();
                    IntentDataObject mIntentDataObject = new IntentDataObject();
                    mIntentDataObject.setObj(model);
                    output.putExtra("obj",mIntentDataObject);
                    setResult(RESULT_OK, output);
                    finish();
                }
            }

            Utils.hideKeyboard(LocalitySearchActivity.this);
        }
    };


    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.obj == null) {
                //showConfirmAlert("","Unable to connect Server.Please try later");
                // showToast("Server error");
                return false;
            } else {
                ReqRespBean mBean = (ReqRespBean) msg.obj;
                switch (mBean.getApiType()) {
                    case LOCALITY_SEARCH:
                        LocalitySearchRespModel localitySearchRespModel = (LocalitySearchRespModel) JsonParser.convertJsonToBean(APIType.LOCALITY_SEARCH, mBean.getJson());
                        if (localitySearchRespModel != null && localitySearchRespModel.getSuccess() == 1) {
                            if (localitySearchRespModel.getData() != null) {
                                filterData(localitySearchRespModel.getData());
                                mLocalityListAdapter.setData(mList);
                                mLocalityListAdapter.getFilter().filter("");

                            } else {

                            }
                        } else {
                            //TODO:
                        }
                        break;
                    default:
                        break;
                }
            }
            return true;
        }
    });


    private void filterData(ArrayList<LocalityData> dataList) {
        ArrayList<LocalityData> locality = new ArrayList<>();
        ArrayList<LocalityData> subLocality = new ArrayList<>();
        ArrayList<LocalityData> project = new ArrayList<>();
        ArrayList<LocalityData> builder = new ArrayList<>();
        for (LocalityData data : dataList) {
            if (data.getSubtitle().equalsIgnoreCase(LocalityType.SUBLOCATION.value)) {
                data.setHeader("");
                data.setLocalityType(LocalityType.SUBLOCATION.value);
                subLocality.add(data);
            } else if (data.getSubtitle().equalsIgnoreCase(LocalityType.LOCATION.value)) {
                data.setHeader("");
                data.setLocalityType(LocalityType.LOCATION.value);
                locality.add(data);
            } else if (data.getSubtitle().equalsIgnoreCase(LocalityType.BUILDER.value)) {
                data.setHeader("");
                data.setLocalityType(LocalityType.BUILDER.value);
                builder.add(data);
            } else if (data.getSubtitle().equalsIgnoreCase(LocalityType.PROJECT.value)) {
                data.setHeader("");
                data.setLocalityType(LocalityType.PROJECT.value);
                project.add(data);
            }
        }
        if (locality.size() > 0) {
            locality.get(0).setHeader(LocalityListAdapter.LOCALITY);
            mList.addAll(locality);
        }
        if (subLocality.size() > 0) {
            subLocality.get(0).setHeader(LocalityListAdapter.SUB_LOCALITY);
            mList.addAll(subLocality);
        }
        if (builder.size() > 0) {
            builder.get(0).setHeader(LocalityListAdapter.BUILDER);
            mList.addAll(builder);
        }
        if (project.size() > 0) {
            project.get(0).setHeader(LocalityListAdapter.PROJECT);
            mList.addAll(project);
        }

    }

    @Override
    protected String setActionBarTitle() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }
    @Override
    public void onResume() {
        super.onResume();
    }

}