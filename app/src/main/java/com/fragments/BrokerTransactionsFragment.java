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
import android.widget.ListView;
import android.widget.Toast;

import com.AppEnums.APIType;
import com.adapters.MyTransactionsListAdapter;
import com.appnetwork.AsyncThread;
import com.appnetwork.ReqRespBean;
import com.appnetwork.WEBAPI;
import com.helper.BMHConstants;
import com.helper.ParamsConstants;
import com.jsonparser.JsonParser;
import com.model.MyTransactionsRespModel;
import com.model.NetworkErrorObject;
import com.payu.india.Payu.PayuConstants;
import com.sp.BMHApplication;
import com.sp.ConnectivityReceiver;
import com.sp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class BrokerTransactionsFragment extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {

    private final String TAG = BrokerTransactionsFragment.class.getSimpleName();
    private View view;
    private BMHApplication app;
    private NetworkErrorObject mNetworkErrorObject = null;
    private ListView lv_leads;
    private String brokerId, brokerCode;
    private AsyncThread mAsyncThread = null;
    private MyTransactionsListAdapter myTransactionsListAdapter;

    public static Fragment getInstance(String brokerId, String brokerCode) {
        BrokerTransactionsFragment brokerTransactionsFragment = new BrokerTransactionsFragment();
        Bundle args = new Bundle();
        args.putString("brokerId", brokerId);
        args.putString("brokerCode", brokerCode);
        brokerTransactionsFragment.setArguments(args);
        return brokerTransactionsFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        brokerId = this.getArguments().getString("brokerId");
        brokerCode = this.getArguments().getString("brokerCode");
        getBrokerTransactionData(brokerCode);
        view = inflater.inflate(R.layout.fragment_my_transaction, container, false);
        app = (BMHApplication) getActivity().getApplication();
        initViews();
        setListeners();
        return view;
    }

    private void setListeners() {

    }

    private void initViews() {
        if (view == null) return;
        lv_leads = (ListView) view.findViewById(R.id.lv_leads);
    }

    private void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == PayuConstants.PAYU_REQUEST_CODE) {

        }
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_make_payment:
                    break;
            }
        }
    };

    private boolean isValidData() {
        return true;
    }


    private void getBrokerTransactionData(String brokerCode) {
        ReqRespBean mBean = new ReqRespBean();
        mBean.setApiType(APIType.BROKER_TRANSACTIONS);
        mBean.setRequestmethod(WEBAPI.POST);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.BROKER_TRANSACTIONS));
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        StringBuilder mStringBuilder = new StringBuilder("");
        mStringBuilder.append(ParamsConstants.BROKER_CODE);
        mStringBuilder.append("=");
        mStringBuilder.append(brokerCode);
        mStringBuilder.append("&builder_id=" + BMHConstants.CURRENT_BUILDER_ID);
        mBean.setJson(mStringBuilder.toString());
        mBean.setmHandler(mHandler);
        mBean.setCtx(getActivity());
        mAsyncThread = new AsyncThread();
        mAsyncThread.initProgressDialog(getActivity(), mOnCancelListener);
        mAsyncThread.execute(mBean);
        mAsyncThread = null;
        //    mLepLogger.info(TAG, "sendLoginRequest():URl:" + mBean.getUrl() + ",Req Json:" + mBean.getJson());
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
                    case BROKER_TRANSACTIONS:
                        MyTransactionsRespModel myTransactionsRespModel = (MyTransactionsRespModel) JsonParser.convertJsonToBean(APIType.BROKER_TRANSACTIONS, mBean.getJson());
                        if (myTransactionsRespModel != null) {
                            if (myTransactionsRespModel.isSuccess() && myTransactionsRespModel.getData() != null) {
                                myTransactionsListAdapter = new MyTransactionsListAdapter(getActivity(), myTransactionsRespModel.getData(), TAG);
                                //   lv_leads.setAdapter(myTransactionsListAdapter);
                            } else if (!myTransactionsRespModel.isSuccess()) {
                                showToast(myTransactionsRespModel.getMessage());
                                //TODO: show empty data
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


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            if (mNetworkErrorObject == null || mNetworkErrorObject.getUiEventType() == null || mNetworkErrorObject.getAlertDialog() == null)
                return;
            switch (mNetworkErrorObject.getUiEventType()) {
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
