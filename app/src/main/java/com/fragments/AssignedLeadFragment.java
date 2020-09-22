package com.fragments;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.adapters.AssignedLeadAdapter;
import com.model.NetworkErrorObject;
import com.payu.india.Payu.PayuConstants;
import com.sp.BMHApplication;
import com.sp.ConnectivityReceiver;
import com.sp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AssignedLeadFragment extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {

    private final String TAG = AssignedLeadFragment.class.getSimpleName();
    private View view;
    private BMHApplication app;
    private NetworkErrorObject mNetworkErrorObject = null;
    private ListView lv_leads;
    private String brokerId,brokerCode;


    public static Fragment getInstance(String brokerId,String brokerCode) {
        AssignedLeadFragment mAssignedLeadFragment = new AssignedLeadFragment();
        Bundle args = new Bundle();
        args.putString("brokerId", brokerId);
        args.putString("brokerCode", brokerCode);
        mAssignedLeadFragment.setArguments(args);
        return mAssignedLeadFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        brokerId = this.getArguments().getString("brokerId");
        brokerCode = this.getArguments().getString("brokerCode");
        view = inflater.inflate(R.layout.fragment_assigned_lead, container, false);
        app = (BMHApplication) getActivity().getApplication();
        initViews();
        setListeners();
        return view;
    }

    private void setListeners() {

    }

    private void initViews(){
        if(view == null)return;
        lv_leads = (ListView)view.findViewById(R.id.lv_leads);
        lv_leads.setAdapter(new AssignedLeadAdapter(getActivity(),null));
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
