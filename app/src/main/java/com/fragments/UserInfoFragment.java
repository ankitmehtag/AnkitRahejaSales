package com.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.AppEnums.APIType;
import com.appnetwork.AsyncThread;
import com.appnetwork.ReqRespBean;
import com.appnetwork.WEBAPI;
import com.helper.ParamsConstants;
import com.jsonparser.JsonParser;
import com.model.BrokerProfileInfoRespModel;
import com.model.NetworkErrorObject;
import com.payu.india.Payu.PayuConstants;
import com.sp.BMHApplication;
import com.sp.ConnectivityReceiver;
import com.sp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserInfoFragment extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {

    private final String TAG = UserInfoFragment.class.getSimpleName();
    private View view;
    private BMHApplication app;
    private NetworkErrorObject mNetworkErrorObject = null;
    private LinearLayout ll_user_info;
    private AsyncThread mAsyncThread = null;
    private String brokerId,brokerCode;



    public static Fragment getInstance(String brokerId,String brokerCode) {
        UserInfoFragment userInfoFragment = new UserInfoFragment();
        Bundle args = new Bundle();
        args.putString("brokerId", brokerId);
        args.putString("brokerCode", brokerCode);
        userInfoFragment.setArguments(args);
        return userInfoFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        brokerId = this.getArguments().getString("brokerId");
        brokerCode = this.getArguments().getString("brokerCode");
        view = inflater.inflate(R.layout.fragment_user_info, container, false);
        app = (BMHApplication) getActivity().getApplication();
        initViews();
        setListeners();
        getUserInfo(brokerId);
        return view;
    }

    private void setListeners() {

    }

    private void initViews(){
        if(view == null)return;
        ll_user_info = (LinearLayout) view.findViewById(R.id.ll_user_info);

    }

    private void showToast(String msg){
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }

    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == PayuConstants.PAYU_REQUEST_CODE) {

        }
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_make_payment:
                    break;
            }
        }
    };

    private boolean isValidData(){
        return true;
    }


    private void getUserInfo(String brokerId) {
        ReqRespBean mBean = new ReqRespBean();
        mBean.setApiType(APIType.GET_BROKER_PROFILE_INFO);
        mBean.setRequestmethod(WEBAPI.POST);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.GET_BROKER_PROFILE_INFO));
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        StringBuilder mStringBuilder = new StringBuilder("");
        mStringBuilder.append(ParamsConstants.BROKER_ID + "=" + brokerId);
       // mStringBuilder.append("&" + ParamsConstants.BUILDER_ID + "=" + BMHConstants.CURRENT_BUILDER_ID);
        mBean.setJson(mStringBuilder.toString());
        mBean.setmHandler(mHandler);
        mBean.setCtx(getActivity());
        mAsyncThread = new AsyncThread();
        mAsyncThread.initProgressDialog(getActivity(),mOnCancelListener);
        mAsyncThread.execute(mBean);
        mAsyncThread = null;
        //    mLepLogger.info(TAG, "sendLoginRequest():URl:" + mBean.getUrl() + ",Req Json:" + mBean.getJson());
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
            if (msg.obj == null) {return false;
            } else {
                ReqRespBean mBean = (ReqRespBean) msg.obj;
                switch (mBean.getApiType()) {
                    case GET_BROKER_PROFILE_INFO:
                        BrokerProfileInfoRespModel brokerProfileInfoRespModel = (BrokerProfileInfoRespModel) JsonParser.convertJsonToBean(APIType.GET_BROKER_PROFILE_INFO,mBean.getJson());
                        if(brokerProfileInfoRespModel != null ){
                            if (brokerProfileInfoRespModel.isSuccess() && brokerProfileInfoRespModel.getData() != null) {
                            setUserInfo(brokerProfileInfoRespModel.getData());
                            }else if(!brokerProfileInfoRespModel.isSuccess()){
                                showToast(brokerProfileInfoRespModel.getMessage());
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

    private void setUserInfo(BrokerProfileInfoRespModel.Data userInfo) {
        if(userInfo == null)return;
        ll_user_info.removeAllViews();
        LayoutInflater mLayoutInflater = LayoutInflater.from(getActivity());
        View address = mLayoutInflater.inflate(R.layout.user_info_row,null);
        TextView tv_address_title = (TextView) address.findViewById(R.id.tv_title);
        TextView tv_address_detail = (TextView)address.findViewById(R.id.tv_detail);
        tv_address_title.setText("Address");
        tv_address_detail.setText(": "+userInfo.getAddress());

        View contactNo = mLayoutInflater.inflate(R.layout.user_info_row,null);
        TextView tv_contactNoTitle = (TextView) contactNo.findViewById(R.id.tv_title);
        TextView tv_contactNo = (TextView)contactNo.findViewById(R.id.tv_detail);
        tv_contactNoTitle.setText("Contact Number");
        tv_contactNo.setText(": "+userInfo.getMobile_no());

        View emailView = mLayoutInflater.inflate(R.layout.user_info_row,null);
        TextView emailTitle = (TextView) emailView.findViewById(R.id.tv_title);
        TextView emailValue = (TextView)emailView.findViewById(R.id.tv_detail);
        emailTitle.setText("Email Id");
        emailValue.setText(":"+userInfo.getEmail_id());

        View businessTypeView = mLayoutInflater.inflate(R.layout.user_info_row,null);
        TextView businessTypeTitle = (TextView) businessTypeView.findViewById(R.id.tv_title);
        TextView businessTypeValue = (TextView)businessTypeView.findViewById(R.id.tv_detail);
        businessTypeTitle.setText("Business Type");
        businessTypeValue.setText(": "+userInfo.getBroker_type());

        View reraView = mLayoutInflater.inflate(R.layout.user_info_row,null);
        TextView reraViewTitle = (TextView) reraView.findViewById(R.id.tv_title);
        TextView reraViewValue = (TextView)reraView.findViewById(R.id.tv_detail);
        reraViewTitle.setText("Rera Number");
        reraViewValue.setText(": "+userInfo.getRera_no());

        View panView = mLayoutInflater.inflate(R.layout.user_info_row,null);
        TextView panViewTitle = (TextView) panView.findViewById(R.id.tv_title);
        TextView panViewValue = (TextView)panView.findViewById(R.id.tv_detail);
        panViewTitle.setText("PAN Number");
        panViewValue.setText(": "+userInfo.getPan_no());

        View gstView = mLayoutInflater.inflate(R.layout.user_info_row,null);
        TextView gstViewTitle = (TextView) gstView.findViewById(R.id.tv_title);
        TextView gstViewValue = (TextView)gstView.findViewById(R.id.tv_detail);
        gstViewTitle.setText("GST Number");
        gstViewValue.setText(": "+userInfo.getPan_no());

        View documentStatusView = mLayoutInflater.inflate(R.layout.user_info_row,null);
        TextView documentStatusTitle = (TextView) documentStatusView.findViewById(R.id.tv_title);
        TextView documentStatusValue = (TextView)documentStatusView.findViewById(R.id.tv_detail);
        documentStatusTitle.setText("Document Status");
        documentStatusValue.setText(": "+userInfo.getDocument_status());

        ll_user_info.addView(address);
        ll_user_info.addView(contactNo);
        ll_user_info.addView(emailView);
        ll_user_info.addView(businessTypeView);
        ll_user_info.addView(reraView);
        ll_user_info.addView(panView);
        ll_user_info.addView(gstView);
        ll_user_info.addView(documentStatusView);
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if(isConnected){
            if(mNetworkErrorObject == null || mNetworkErrorObject.getUiEventType() == null || mNetworkErrorObject.getAlertDialog() == null)return;
            switch (mNetworkErrorObject.getUiEventType()){
                case RETRY_MAKE_PAYMENT:
                    break;

            }
            mNetworkErrorObject.getAlertDialog().dismiss();
            mNetworkErrorObject = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        BMHApplication.getInstance().setConnectivityListener(this);
    }
}
