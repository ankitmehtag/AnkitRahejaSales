package com.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.AppEnums.UIEventType;
import com.VO.TandCVO;
import com.VO.UnitDetailVO;
import com.activities.ChatScreen;
import com.appnetwork.AsyncThread;
import com.helper.IntentDataObject;
import com.helper.ParamsConstants;
import com.sp.BMHApplication;
import com.sp.ConnectivityReceiver;
import com.sp.PayTokenActivity;
import com.sp.PaymentProccessActivity;
import com.sp.PersonalDetails;
import com.sp.R;
import com.model.NetworkErrorObject;
import com.utils.Utils;

import java.util.HashMap;

public class TermsFragment extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {
    private FragmentActivity fragmentActivity;
    private BMHApplication app;
    private WebView web;
    private Button btn_bmh_tnc, btn_builder_tnc, btn_accept, btn_chat_with_dev;
    // PageVO basevo;

    TandCVO basevo;
    UnitDetailVO unitDetailsVo;
    private AsyncThread mAsyncThread = null;
    private HashMap<String, String> mData;
    private NetworkErrorObject mNetworkErrorObject = null;
    private String urlString = "";
    private String dataUrlString = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        unitDetailsVo = ((PaymentProccessActivity) getActivity()).unitDetailVO;
        View view = inflater.inflate(R.layout.terms, container, false);
        app = (BMHApplication) fragmentActivity.getApplication();
        btn_bmh_tnc = (Button) view.findViewById(R.id.btn_bmh_tnc);
        btn_builder_tnc = (Button) view.findViewById(R.id.btn_builder_tnc);
        btn_builder_tnc.setVisibility(View.GONE);
        btn_accept = (Button) view.findViewById(R.id.btn_accept);
        btn_chat_with_dev = (Button) view.findViewById(R.id.btn_chat_with_dev);
        web = (WebView) view.findViewById(R.id.webViewTerms);
        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                urlString = url;
                web = view;
                if (ConnectivityReceiver.isConnected()) {
                    //TODO: network call
                    web.loadUrl(urlString);
                } else {
                    mNetworkErrorObject = Utils.showNetworkErrorDialog(getActivity(), UIEventType.RETRY_LOAD_URL,
                            new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (ConnectivityReceiver.isConnected()) {
                                        //TODO: network call
                                        web.loadUrl(urlString);
                                        mNetworkErrorObject.getAlertDialog().dismiss();
                                        mNetworkErrorObject = null;
                                    } else {
                                        Utils.showToast(getActivity(), getString(R.string.check_your_internet_connection));
                                    }
                                }
                            });
                }
                return true;
            }
        });
        btn_bmh_tnc.setOnClickListener(mOnClickListener);
        btn_builder_tnc.setOnClickListener(mOnClickListener);
        btn_accept.setOnClickListener(mOnClickListener);
        btn_chat_with_dev.setOnClickListener(mOnClickListener);

        btn_builder_tnc.performClick();
        if (unitDetailsVo.getBuilder_term_condition() == null || unitDetailsVo.getBuilder_term_condition().isEmpty()
                || unitDetailsVo.getBuilder_term_condition().equalsIgnoreCase("NA") || unitDetailsVo.getBuilder_term_condition().equalsIgnoreCase("N/A")) {
            btn_builder_tnc.setVisibility(View.GONE);
        } else {
            btn_builder_tnc.setVisibility(View.VISIBLE);
        }
        return view;
    }

    private void tncBtnState(View btn) {
        btn_bmh_tnc.setSelected(false);
        btn_builder_tnc.setSelected(false);
        //if(btn != null)btn.setSelected(true);

    }

    OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_bmh_tnc:
                    dataUrlString = unitDetailsVo.getTerm_condition();
                    if (ConnectivityReceiver.isConnected()) {
                        //TODO: network call
                        web.loadData(dataUrlString, "text/html", "UTF-8");
                    } else {
                        mNetworkErrorObject = Utils.showNetworkErrorDialog(getActivity(), UIEventType.RETRY_DATA_URL,
                                new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (ConnectivityReceiver.isConnected()) {
                                            //TODO: network call
                                            web.loadData(dataUrlString, "text/html", "UTF-8");
                                            mNetworkErrorObject.getAlertDialog().dismiss();
                                            mNetworkErrorObject = null;
                                        } else {
                                            Utils.showToast(getActivity(), getString(R.string.check_your_internet_connection));
                                        }
                                    }
                                });
                    }
                    tncBtnState(btn_bmh_tnc);
                    break;
                case R.id.btn_builder_tnc:
                    dataUrlString = unitDetailsVo.getBuilder_term_condition();
                    tncBtnState(btn_builder_tnc);
                    if (ConnectivityReceiver.isConnected()) {
                        //TODO: network call
                        web.loadData(dataUrlString, "text/html", "UTF-8");
                    } else {
                        mNetworkErrorObject = Utils.showNetworkErrorDialog(getActivity(), UIEventType.RETRY_DATA_URL,
                                new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (ConnectivityReceiver.isConnected()) {
                                            //TODO: network call
                                            web.loadData(dataUrlString, "text/html", "UTF-8");
                                            mNetworkErrorObject.getAlertDialog().dismiss();
                                            mNetworkErrorObject = null;
                                        } else {
                                            Utils.showToast(getActivity(), getString(R.string.check_your_internet_connection));
                                        }
                                    }
                                });
                    }

                    break;
                case R.id.btn_accept:
                    if (unitDetailsVo != null && unitDetailsVo.isIsformavailable()) {
                        if (fragmentActivity instanceof PaymentProccessActivity) {
                            ((PaymentProccessActivity) fragmentActivity).pager.setCurrentItem(2, true);

                        } else if (fragmentActivity instanceof PayTokenActivity) {

                        }
                    } else {
                        String sPaymentPlan = ((PaymentProccessActivity) getActivity()).getPaymentPlan;
                        Intent i = new Intent(getActivity(), PersonalDetails.class);
                        i.putExtra("unitvo", unitDetailsVo);
                        i.putExtra(ParamsConstants.PAYMENT_PLAN, sPaymentPlan);
                        startActivity(i);
                    }
                    break;
                case R.id.btn_chat_with_dev:
                    String sPaymentPlan = ((PaymentProccessActivity) getActivity()).getPaymentPlan;
                    IntentDataObject mIntentDataObject = new IntentDataObject();
                    //mIntentDataObject.setObj(mData);
                    mIntentDataObject.putData(ParamsConstants.UNIT_ID, unitDetailsVo.getId());
                    mIntentDataObject.putData(ParamsConstants.UNIT_NO, unitDetailsVo.getunitNo());
                    mIntentDataObject.putData(ParamsConstants.UNIT_RESERVED, String.valueOf(0));
                    Log.e("Unit No", unitDetailsVo.getunitNo());
                    mIntentDataObject.putData("payment_plan", sPaymentPlan);
                    mIntentDataObject.putData(ParamsConstants.UNIT_TITLE, unitDetailsVo.getProject_name());
                    mIntentDataObject.putData(ParamsConstants.BHK_TYPE, unitDetailsVo.getUnitFloor());
                    mIntentDataObject.putData(ParamsConstants.UNIT_IMAGE, unitDetailsVo.getUnit_image());
                    Intent mIntent = new Intent(getActivity(), ChatScreen.class);
                    mIntent.putExtra(IntentDataObject.OBJ, mIntentDataObject);
                    mIntent.putExtra("unitvo", unitDetailsVo);
                    startActivity(mIntent);
                    break;

            }
        }
    };
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
            return true;
        }
    });

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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//		if (isVisibleToUser) {
//			if (basevo == null) {
//				startTermsTask();
//			}
//		}
    }

    @Override
    public void onResume() {
        super.onResume();
        BMHApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            if (mNetworkErrorObject == null || mNetworkErrorObject.getUiEventType() == null || mNetworkErrorObject.getAlertDialog() == null)
                return;
            switch (mNetworkErrorObject.getUiEventType()) {
                case RETRY_LOAD_URL:
                    web.loadUrl(urlString);
                    break;
                case RETRY_DATA_URL:
                    web.loadData(dataUrlString, "text/html", "UTF-8");
                    break;

            }
            mNetworkErrorObject.getAlertDialog().dismiss();
            mNetworkErrorObject = null;
        }
    }
}
