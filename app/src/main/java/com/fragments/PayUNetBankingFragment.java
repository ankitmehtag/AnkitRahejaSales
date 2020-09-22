package com.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.AppEnums.APIType;
import com.adapters.CustomSpinnerAdapter;
import com.appnetwork.WEBAPI;
import com.sp.BMHApplication;
import com.sp.PayUPaymentProcessActivity;
import com.sp.PaymentStatusActivity;
import com.sp.PaymentsActivity;
import com.sp.ProjectsListActivity;
import com.sp.R;
import com.helper.BMHConstants;
import com.helper.IntentDataObject;
import com.helper.SdkUIConstants;
import com.jsonparser.JsonParser;
import com.model.PayUResponseModel;
import com.model.UpdatePersonalDetailRespModel;
import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Model.PayuHashes;
import com.payu.india.Model.PostData;
import com.payu.india.Payu.Payu;
import com.payu.india.Payu.PayuConstants;
import com.payu.india.Payu.PayuErrors;
import com.payu.india.Payu.PayuUtils;
import com.payu.india.PostParams.PaymentPostParams;
import com.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Naresh on 27-Apr-17.
 */

public class PayUNetBankingFragment extends Fragment {


    private Spinner sp_bank;
    private Button btn_make_payment;
    private PayuUtils payuUtils;
    private  View view;
    private UpdatePersonalDetailRespModel mUpdatePersonalDetailRespModel;
    private String bankCode = "";
    private BMHApplication app;


    public PayUNetBankingFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void sortBankList(){
        if(mUpdatePersonalDetailRespModel.getData() == null || mUpdatePersonalDetailRespModel.getData().getNetbanking_info() == null)return;
        Collections.sort(mUpdatePersonalDetailRespModel.getData().getNetbanking_info() , new Comparator<UpdatePersonalDetailRespModel.NetbankingInfo>() {
            @Override
            public int compare(UpdatePersonalDetailRespModel.NetbankingInfo lhs, UpdatePersonalDetailRespModel.NetbankingInfo rhs) {
                    return Integer.compare(Utils.toInt(rhs.getIndex()),Utils.toInt(lhs.getIndex()));
               }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_payu_net_banking, container, false);
        app = (BMHApplication) getActivity().getApplication();
        payuUtils = new PayuUtils();

        initViews();
        setListeners();

        if(getActivity() instanceof PayUPaymentProcessActivity){
            mUpdatePersonalDetailRespModel = ((PayUPaymentProcessActivity)getActivity()).mUpdatePersonalDetailRespModel;
            sortBankList();
            if(mUpdatePersonalDetailRespModel != null) {
                ArrayList<String> bankData = new ArrayList<>();
                for (UpdatePersonalDetailRespModel.NetbankingInfo info : mUpdatePersonalDetailRespModel.getData().getNetbanking_info()) {
                    bankData.add(info.getBank_name());
                }
                CustomSpinnerAdapter adapterNoofParkings = new CustomSpinnerAdapter(getActivity(), R.layout.textview_spinner, bankData);
                sp_bank.setAdapter(adapterNoofParkings);
            }
        }
        return view;
    }


    private void setListeners() {
        btn_make_payment.setOnClickListener(mOnClickListener);
        sp_bank.setOnItemSelectedListener(mOnItemSelectedListener);
    }

    private void initViews(){
        if(view == null)return;
        sp_bank = (Spinner) view.findViewById(R.id.sp_bank);
        btn_make_payment = (Button)view.findViewById(R.id.btn_make_payment);

    }
    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_make_payment:
                    getParams();
                    break;
            }
        }
    };

    AdapterView.OnItemSelectedListener mOnItemSelectedListener  = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            bankCode = mUpdatePersonalDetailRespModel.getData().getNetbanking_info().get(position).getBank_code();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private void getParams(){
        if(mUpdatePersonalDetailRespModel == null || mUpdatePersonalDetailRespModel.getData() == null)return;
        String UDF =""; // User Defined Fields, you can put any data here.
        PaymentParams mPaymentParams = new PaymentParams();
        mPaymentParams.setKey(SdkUIConstants.KEY);
        mPaymentParams.setAmount(mUpdatePersonalDetailRespModel.getData().getService_provider_information().getAmount());
        mPaymentParams.setProductInfo(mUpdatePersonalDetailRespModel.getData().getService_provider_information().getProduct_info());
        mPaymentParams.setFirstName(mUpdatePersonalDetailRespModel.getData().getUser_information().getFirst_name());
        mPaymentParams.setLastName(mUpdatePersonalDetailRespModel.getData().getUser_information().getLast_name());
        mPaymentParams.setPg(PayuConstants.NB);
        mPaymentParams.setEmail(mUpdatePersonalDetailRespModel.getData().getUser_information().getEmail());
        mPaymentParams.setTxnId(mUpdatePersonalDetailRespModel.getData().getTransaction_info().getTransaction_id());
        mPaymentParams.setZipCode(mUpdatePersonalDetailRespModel.getData().getUser_information().getZipcode());
        if(app != null && app.getFromPrefs(BMHConstants.USERID_KEY) == null || app.getFromPrefs(BMHConstants.USERID_KEY).length() == 0) {
            mPaymentParams.setUdf1(app.getFromPrefs(BMHConstants.USERID_KEY));
        }else{
            mPaymentParams.setUdf1(UDF);
        }
       // mPaymentParams.setUdf2(SdkUIConstants.PAYMENT_MODE);
        mPaymentParams.setUdf1(UDF);
        mPaymentParams.setUdf2(UDF);
        mPaymentParams.setUdf3(UDF);
        mPaymentParams.setUdf4(UDF);
        mPaymentParams.setUdf5(UDF);
        mPaymentParams.setPhone(mUpdatePersonalDetailRespModel.getData().getUser_information().getPhone_number());
        if(BMHConstants.IS_STAGING) {
            mPaymentParams.setSurl(SdkUIConstants.S_URL);
            mPaymentParams.setFurl(SdkUIConstants.F_URL);
        }else{
            mPaymentParams.setSurl(WEBAPI.getWEBAPI(APIType.PAYU_SUCCESS));
            mPaymentParams.setFurl(WEBAPI.getWEBAPI(APIType.PAYU_FAILURE));
        }
        //generate hashes or get them from server
        PayuHashes hashes = new PayuHashes();//getHashesFromServer();
        hashes.setPaymentHash(mUpdatePersonalDetailRespModel.getData().getTransaction_info().getHash());
        mPaymentParams.setHash(hashes.getPaymentHash());
        mPaymentParams.setBankCode(bankCode);

        try {
            Payu.setInstance(getActivity());
            PostData postData = new PaymentPostParams(mPaymentParams, PayuConstants.NB).getPaymentPostParams();
            if (postData.getCode() == PayuErrors.NO_ERROR) {
                PayuConfig payuConfig = new PayuConfig();
                if(BMHConstants.IS_STAGING) {
                    payuConfig.setEnvironment(PayuConstants.STAGING_ENV);
                }else{
                    payuConfig.setEnvironment(PayuConstants.PRODUCTION_ENV);
                }
                payuConfig.setData(postData.getResult());
                Intent intent = new Intent(getActivity(),PaymentsActivity.class);
                intent.putExtra(PayuConstants.PAYU_CONFIG, payuConfig);
                intent.putExtra(PayuConstants.PAYMENT_PARAMS, mPaymentParams);
                intent.putExtra(PayuConstants.PAYU_HASHES, hashes);
                startActivityForResult(intent, PayuConstants.PAYU_REQUEST_CODE);
            } else {
                // something went wrong
                Toast.makeText(getActivity(),postData.getResult(), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == PayuConstants.PAYU_REQUEST_CODE) {
            if (data != null) {
                String payUResponse = data.getStringExtra("payu_response");
                // String merchantResponse = data.getStringExtra("result");
                if(payUResponse != null){
                    PayUResponseModel payUResponseModel = (PayUResponseModel) JsonParser.convertJsonToBean(APIType.PAYU_RESPONSE,payUResponse);
                    if(payUResponseModel != null){
                        if(payUResponseModel.getStatus().equalsIgnoreCase("success") && payUResponseModel.getSurl() != null && !payUResponseModel.getSurl().isEmpty()){
                            sentIntent(payUResponseModel.getSurl(),payUResponseModel);
                            getActivity().finish();
                        }else if(payUResponseModel.getStatus().equalsIgnoreCase("failure") && payUResponseModel.getFurl() != null && !payUResponseModel.getFurl().isEmpty()){
                            sentIntent(payUResponseModel.getFurl(),payUResponseModel);
                            getActivity().finish();
                        }else{
                            showToast(getString(R.string.transaction_canceled));
                            gotToHome();
                        }

                    }else{
                        showToast(getString(R.string.transaction_canceled));
                        gotToHome();
                    }
                }else{
                    showToast(getString(R.string.transaction_canceled));
                    gotToHome();
                }
            } else {
                showToast(getString(R.string.transaction_canceled));
                gotToHome();
            }
        }
    }

    private void sentIntent(String url,PayUResponseModel model){
       /* model = new PayUResponseModel();
        model.setStatus("success");
        //model.setStatus("failure");
        model.setTxnid("139204");
        model.setMode("DC");
        model.setAmount("30,000");
        model.setFirstname("Naresh Kumar");
        model.setEmail("naresh@builder.com");
        model.setProductinfo("3939-Alaknanda");*/

        if(model == null || url == null)return;
        IntentDataObject mIntentDataObject = new IntentDataObject();
        mIntentDataObject.putData("PaymentMode","Net banking");
        mIntentDataObject.putData("Payment Status",makeFisrtCharCaps(model.getStatus()));
        mIntentDataObject.putData("Transaction No",makeFisrtCharCaps(model.getTxnid()));
        mIntentDataObject.putData("Transaction Date",makeFisrtCharCaps(model.getAddedon()));
        if(model.getMode().equalsIgnoreCase("CC")){
            mIntentDataObject.putData("Payment Mode","Credit Card");
        }else if(model.getMode().equalsIgnoreCase("DC")){
            mIntentDataObject.putData("Payment Mode","Debit Card");
        }else if(model.getMode().equalsIgnoreCase("NB")){
            mIntentDataObject.putData("Payment Mode","Net Banking");
        }else{
            mIntentDataObject.putData("Payment Mode",makeFisrtCharCaps(model.getMode()));
        }
        mIntentDataObject.putData("Amount",model.getAmount());
        mIntentDataObject.putData("CustomerInfoEntity Name",makeFisrtCharCaps(model.getFirstname()));
        mIntentDataObject.putData("CustomerInfoEntity Email",model.getEmail());
        mIntentDataObject.putData("Unit Details",makeFisrtCharCaps(model.getProductinfo()));
    //    mIntentDataObject.putData(getString(R.string.txt_payment_plan_key), model.getPayment_plan());
    //    mIntentDataObject.putData(getString(R.string.txt_payment_plan_desc_key), mTargetInfo.getPaymentPlanDesc());
        //Intent mIntent = new Intent(getActivity(),PayUResponseWebViewActivity.class);
        Intent mIntent = new Intent(getActivity(),PaymentStatusActivity.class);
        mIntent.putExtra("URL",url);
        mIntent.putExtra(IntentDataObject.OBJ,mIntentDataObject);
        startActivity(mIntent);
    }

    private String makeFisrtCharCaps(String str){
        if(str == null || str.isEmpty())return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private void showToast(String msg){
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }

    private void gotToHome(){
        Intent intent = new Intent(getActivity(), ProjectsListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    /*private void sortLocationList(){
        if(locationsListAdapter == null || locationsListAdapter.arrLocation == null)return;
        Collections.sort(locationsListAdapter.arrLocation, new Comparator<LocationVO>() {
            @Override
            public int compare(LocationVO lhs, LocationVO rhs) {
                if(pos == 0){
                    return String.compare(Utils.toFloat(lhs.getAvgPsfLocation()),Utils.toFloat(rhs.getAvgPsfLocation()));
                }else if(pos == 1){
                    return Float.compare(Utils.toFloat(rhs.getAvgPsfLocation()),Utils.toFloat(lhs.getAvgPsfLocation()));
                }else if(pos == 2){
                    return Integer.compare(Utils.toInt(rhs.getAvg_rating()),Utils.toInt(lhs.getAvg_rating()));
                }else if(pos == 3){
                    return Integer.compare(Utils.toInt(rhs.getInfra()),Utils.toInt(lhs.getInfra()));
                }else if(pos == 4){
                    return Integer.compare(Utils.toInt(rhs.getNeeds()),Utils.toInt(lhs.getNeeds()));
                }else if(pos == 5){
                    return Integer.compare(Utils.toInt(rhs.getLifeStyle()),Utils.toInt(lhs.getLifeStyle()));
                }else if(pos == 6){
                    return Float.compare(Utils.toFloat(rhs.getReturnsval()),Utils.toFloat(lhs.getReturnsval()));
                    //return rhs.getReturnsval().compareTo(lhs.getReturnsval());
                }
                return rhs.getName().compareTo(lhs.getName());
            }
        });
        locationsListAdapter.notifyDataSetChanged();
    }*/
}
