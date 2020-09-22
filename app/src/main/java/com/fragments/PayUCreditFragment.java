package com.fragments;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.AppEnums.APIType;
import com.AppEnums.UIEventType;
import com.appnetwork.WEBAPI;
import com.helper.ParamsConstants;
import com.sp.BMHApplication;
import com.sp.ConnectivityReceiver;
import com.sp.PayUPaymentProcessActivity;
import com.sp.PaymentStatusActivity;
import com.sp.PaymentsActivity;
import com.sp.ProjectsListActivity;
import com.sp.R;
import com.helper.BMHConstants;
import com.helper.IntentDataObject;
import com.helper.SdkUIConstants;
import com.jsonparser.JsonParser;
import com.model.NetworkErrorObject;
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
import com.utils.PayUErrorCodes;
import com.utils.Utils;

import butterknife.ButterKnife;
import butterknife.OnTextChanged;


/**
 * A simple {@link Fragment} subclass.
 */
public class PayUCreditFragment extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {

    private final String TAG = PayUCreditFragment.class.getSimpleName();
    private View view;
    private EditText cardNumberEditText, cardNameEditText, cardCVCEditText, cardDateEditText;
    private Button btn_make_payment;
    private ImageView ivCard;
    private String issuer;
    private PayuUtils payuUtils;
    //TODO Below are mandatory params for hash genetation
    private UpdatePersonalDetailRespModel mUpdatePersonalDetailRespModel;

    private static int CARD_NUMBER_TOTAL_SYMBOLS = 19; // size of pattern 0000-0000-0000-0000
    private static int CARD_NUMBER_TOTAL_DIGITS = 16; // max numbers of digits in pattern: 0000 x 4
    private static final int CARD_NUMBER_DIVIDER_MODULO = 5; // means divider position is every 5th symbol beginning with 1
    private static final int CARD_NUMBER_DIVIDER_POSITION = CARD_NUMBER_DIVIDER_MODULO - 1; // means divider position is every 4th symbol beginning with 0
    private static final char CARD_NUMBER_DIVIDER = '-';

    private static final int CARD_DATE_TOTAL_SYMBOLS = 7; // size of pattern MM/YYYY
    private static final int CARD_DATE_TOTAL_DIGITS = 6; // max numbers of digits in pattern: MM + YYYY
    private static final int CARD_DATE_DIVIDER_MODULO = 3; // means divider position is every 3rd symbol beginning with 1
    private static final int CARD_DATE_DIVIDER_POSITION = CARD_DATE_DIVIDER_MODULO - 1; // means divider position is every 2nd symbol beginning with 0
    private static final char CARD_DATE_DIVIDER = '/';

    private static int CARD_CVC_TOTAL_SYMBOLS = 4;

    private BMHApplication app;
    private NetworkErrorObject mNetworkErrorObject = null;

    public PayUCreditFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() instanceof PayUPaymentProcessActivity) {
            mUpdatePersonalDetailRespModel = ((PayUPaymentProcessActivity) getActivity()).mUpdatePersonalDetailRespModel;
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_credit_debit, container, false);
        app = (BMHApplication) getActivity().getApplication();
        initViews();
        setListeners();
        payuUtils = new PayuUtils();
        ButterKnife.bind(this, view);
        return view;
    }

    private void setListeners() {
        btn_make_payment.setOnClickListener(mOnClickListener);
        cardNumberEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    if (!isCardValid()) {
                        ivCard.setImageResource(R.drawable.error_icon);
                    } else {
                        if (issuer != null && !issuer.isEmpty()) {
                            ivCard.setImageResource(getIssuerImage(issuer));
                        }
                    }
                }
            }
        });
    }

    private void initViews() {
        if (view == null) return;
        cardNumberEditText = (EditText) view.findViewById(R.id.cardNumberEditText);
        cardNameEditText = (EditText) view.findViewById(R.id.cardNameEditText);
        cardCVCEditText = (EditText) view.findViewById(R.id.cardCVCEditText);
        cardDateEditText = (EditText) view.findViewById(R.id.cardDateEditText);
        btn_make_payment = (Button) view.findViewById(R.id.btn_make_payment);
        ivCard = (ImageView) view.findViewById(R.id.ivCard);

    }

    private int getIssuerImage(String issuer) {
        switch (issuer) {
            case PayuConstants.VISA:
                return R.drawable.logo_visa;
            case PayuConstants.LASER:
                return R.drawable.laser;
            case PayuConstants.DISCOVER:
                return R.drawable.discover;
            case PayuConstants.MAES:
                return R.drawable.mas_icon;
            case PayuConstants.MAST:
                return R.drawable.mc_icon;
            case PayuConstants.AMEX:
                return R.drawable.amex;
            case PayuConstants.DINR:
                return R.drawable.diner;
            case PayuConstants.JCB:
                return R.drawable.jcb;
            case PayuConstants.SMAE:
                return R.drawable.maestro;
            case PayuConstants.RUPAY:
                return R.drawable.rupay;
            default:
                return R.drawable.ic_card_number;
        }
    }

    private void getParams() {
        if (mUpdatePersonalDetailRespModel == null || mUpdatePersonalDetailRespModel.getData() == null)
            return;
        String UDF = ""; // User Defined Fields, you can put any data here.
        PaymentParams mPaymentParams = new PaymentParams();
        mPaymentParams.setKey(SdkUIConstants.KEY);
        mPaymentParams.setAmount(mUpdatePersonalDetailRespModel.getData().getService_provider_information().getAmount());
        mPaymentParams.setProductInfo(mUpdatePersonalDetailRespModel.getData().getService_provider_information().getProduct_info());
        mPaymentParams.setFirstName(mUpdatePersonalDetailRespModel.getData().getUser_information().getFirst_name());
        mPaymentParams.setLastName(mUpdatePersonalDetailRespModel.getData().getUser_information().getLast_name());
        mPaymentParams.setPg(PayuConstants.CC);
        mPaymentParams.setEmail(mUpdatePersonalDetailRespModel.getData().getUser_information().getEmail());
        mPaymentParams.setTxnId(mUpdatePersonalDetailRespModel.getData().getTransaction_info().getTransaction_id());
        mPaymentParams.setZipCode(mUpdatePersonalDetailRespModel.getData().getUser_information().getZipcode());
        if (app != null && app.getFromPrefs(BMHConstants.USERID_KEY) == null
                || app.getFromPrefs(BMHConstants.USERID_KEY).length() == 0) {
            mPaymentParams.setUdf1(app.getFromPrefs(BMHConstants.USERID_KEY));
        } else {
            mPaymentParams.setUdf1(UDF);
        }
        // mPaymentParams.setUdf2(SdkUIConstants.PAYMENT_MODE);
        mPaymentParams.setUdf1(UDF);
        mPaymentParams.setUdf2(UDF);
        mPaymentParams.setUdf3(UDF);
        mPaymentParams.setUdf4(UDF);
        mPaymentParams.setUdf5(UDF);
        mPaymentParams.setPhone(mUpdatePersonalDetailRespModel.getData().getUser_information().getPhone_number());
        if (BMHConstants.IS_STAGING) {
            mPaymentParams.setSurl(SdkUIConstants.S_URL);
            mPaymentParams.setFurl(SdkUIConstants.F_URL);
            Log.i(TAG, "S_URL:" + SdkUIConstants.S_URL);
            Log.i(TAG, "F_URL:" + SdkUIConstants.F_URL);
        } else {
            mPaymentParams.setSurl(WEBAPI.getWEBAPI(APIType.PAYU_SUCCESS));
            mPaymentParams.setFurl(WEBAPI.getWEBAPI(APIType.PAYU_FAILURE));
            Log.i(TAG, "S_URL:" + WEBAPI.getWEBAPI(APIType.PAYU_SUCCESS));
            Log.i(TAG, "F_URL:" + WEBAPI.getWEBAPI(APIType.PAYU_FAILURE));
        }
        //generate hashes or get them from server
        PayuHashes hashes = new PayuHashes();//getHashesFromServer();
        hashes.setPaymentHash(mUpdatePersonalDetailRespModel.getData().getTransaction_info().getHash());
        mPaymentParams.setHash(hashes.getPaymentHash());
        Log.i(TAG, "KeyHashes:" + hashes.getPaymentHash());

        String cardNumber = cardNumberEditText.getText().toString().trim().replaceAll("[^0-9]", "");
        String cardDate = cardDateEditText.getText().toString().trim().replaceAll("[^0-9]", "");
        String cvv = cardCVCEditText.getText().toString().trim();
        String cardPersonName = cardNameEditText.getText().toString().trim();

        mPaymentParams.setCardNumber(cardNumber);
        mPaymentParams.setCardName("");
        mPaymentParams.setNameOnCard(cardPersonName);
        mPaymentParams.setCvv(cvv);
        if (cardDate.toString().trim().length() == CARD_DATE_TOTAL_DIGITS) {
            mPaymentParams.setExpiryMonth(cardDate.substring(0, 2));// MM
            mPaymentParams.setExpiryYear(cardDate.substring(2));// YYYY
        }

        Log.i(TAG, "Key:" + SdkUIConstants.KEY);
        /*mPaymentParams.setCardNumber("5123 4567 8901 2346");
        mPaymentParams.setCardName("HHH");
        mPaymentParams.setNameOnCard("Naresh Gupta");
        mPaymentParams.setExpiryMonth("05");// MM
        mPaymentParams.setExpiryYear("2017");// YYYY
        mPaymentParams.setCvv("123");*/

        try {
            Payu.setInstance(getActivity());
            PostData postData = new PaymentPostParams(mPaymentParams, PayuConstants.CC).getPaymentPostParams();
            if (postData.getCode() == PayuErrors.NO_ERROR) {
                // launch webview
                PayuConfig payuConfig = new PayuConfig();
                if (BMHConstants.IS_STAGING) {
                    payuConfig.setEnvironment(PayuConstants.STAGING_ENV);
                    Log.i(TAG, "Environment:" + PayuConstants.STAGING_ENV);
                } else {
                    payuConfig.setEnvironment(PayuConstants.PRODUCTION_ENV);
                    Log.i(TAG, "Environment:" + PayuConstants.PRODUCTION_ENV);
                }

                payuConfig.setData(postData.getResult());
                Intent intent = new Intent(getActivity(), PaymentsActivity.class);
                intent.putExtra(PayuConstants.PAYU_CONFIG, payuConfig);
                intent.putExtra(PayuConstants.PAYMENT_PARAMS, mPaymentParams);
                intent.putExtra(PayuConstants.PAYU_HASHES, hashes);
                startActivityForResult(intent, PayuConstants.PAYU_REQUEST_CODE);
            } else {
                // something went wrong
                String errorMsg = PayUErrorCodes.getErrorMsg(postData.getCode()).isEmpty() ? postData.getResult() : PayUErrorCodes.getErrorMsg(postData.getCode());
                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }


    private void gotToHome() {
        Intent intent = new Intent(getActivity(), ProjectsListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == PayuConstants.PAYU_REQUEST_CODE) {
            if (data != null) {
                String payUResponse = data.getStringExtra("payu_response");
                // String merchantResponse = data.getStringExtra("result");
                if (payUResponse != null) {
                    PayUResponseModel payUResponseModel = (PayUResponseModel) JsonParser.convertJsonToBean(APIType.PAYU_RESPONSE, payUResponse);
                    if (payUResponseModel != null) {
                        if (payUResponseModel.getStatus().equalsIgnoreCase("success") && payUResponseModel.getSurl() != null && !payUResponseModel.getSurl().isEmpty()) {
                            sentIntent(payUResponseModel.getSurl(), payUResponseModel);
                            getActivity().finish();
                        } else if (payUResponseModel.getStatus().equalsIgnoreCase("failure") && payUResponseModel.getFurl() != null && !payUResponseModel.getFurl().isEmpty()) {
                            sentIntent(payUResponseModel.getFurl(), payUResponseModel);
                            getActivity().finish();
                        } else {
                            showToast(getString(R.string.transaction_canceled));
                            gotToHome();
                        }

                    } else {
                        showToast(getString(R.string.transaction_canceled));
                        gotToHome();
                    }
                } else {
                    showToast(getString(R.string.transaction_canceled));
                    gotToHome();
                }
            } else {
                showToast(getString(R.string.transaction_canceled));
                gotToHome();
            }
        }
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_make_payment:
                    if (isValidData()) {
                        if (ConnectivityReceiver.isConnected()) {
                            getParams();
                        } else {
                            mNetworkErrorObject = Utils.showNetworkErrorDialog(getActivity(), UIEventType.RETRY_MAKE_PAYMENT, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (ConnectivityReceiver.isConnected()) {
                                        getParams();
                                    } else {
                                        Utils.showToast(getActivity(), getString(R.string.check_your_internet_connection));
                                    }
                                }
                            });
                        }
                    }

                    break;
            }
        }
    };

    private boolean isValidData() {
        if (cardNumberEditText.getText().toString().isEmpty()) {
            showToast("Please enter card number");
            return false;
        } else if (cardNameEditText.getText().toString().isEmpty()) {
            showToast("Please enter name on card");
            return false;
        } else if (cardDateEditText.getText().toString().isEmpty()) {
            showToast("Please enter expiry date");
            return false;
        } else if (cardCVCEditText.getText().toString().isEmpty()) {
            showToast("Please enter CVV");
            return false;
        }
        return true;
    }


    @OnTextChanged(value = R.id.cardNumberEditText, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void onCardNumberTextChanged(Editable s) {
        if (!isInputCorrect(s, CARD_NUMBER_TOTAL_SYMBOLS, CARD_NUMBER_DIVIDER_MODULO, CARD_NUMBER_DIVIDER)) {
            s.replace(0, s.length(), concatString(getDigitArray(s, CARD_NUMBER_TOTAL_DIGITS), CARD_NUMBER_DIVIDER_POSITION, CARD_NUMBER_DIVIDER));
        }
        if (s.toString().replace(" ", "").replace("-", "").length() > 5) {// to confirm rupay card we need min 6 digit.
            if (issuer == null || issuer.isEmpty()) {
                issuer = payuUtils.getIssuer(s.toString().replace(" ", "").replace("-", ""));
            }
            ivCard.setImageResource(getIssuerImage(issuer));
            setCardDigitInputType(issuer);
        } else {
            ivCard.setImageResource(R.drawable.ic_card_number);
            issuer = null;
        }
    }

    @OnTextChanged(value = R.id.cardDateEditText, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void onCardDateTextChanged(Editable s) {
        if (!isDateInputCorrect(s, CARD_DATE_TOTAL_SYMBOLS, CARD_DATE_DIVIDER_MODULO, CARD_DATE_DIVIDER)) {
            s.replace(0, s.length(), dateConcatString(getDigitArray(s, CARD_DATE_TOTAL_DIGITS), CARD_DATE_DIVIDER_POSITION, CARD_DATE_DIVIDER));
        }
    }

    @OnTextChanged(value = R.id.cardCVCEditText, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void onCardCVCTextChanged(Editable s) {
        if (s.length() > CARD_CVC_TOTAL_SYMBOLS) {
            s.delete(CARD_CVC_TOTAL_SYMBOLS, s.length());
        }
    }

    private boolean isInputCorrect(Editable s, int size, int dividerPosition, char divider) {
        boolean isCorrect = s.length() <= size;
        for (int i = 0; i < s.length(); i++) {
            if (i > 0 && (i + 1) % dividerPosition == 0) {
                isCorrect &= divider == s.charAt(i);
            } else {
                isCorrect &= Character.isDigit(s.charAt(i));
            }
        }
        return isCorrect;
    }

    private String concatString(char[] digits, int dividerPosition, char divider) {
        final StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < digits.length; i++) {
            if (digits[i] != 0) {
                formatted.append(digits[i]);
                if ((i > 0) && (i < (digits.length - 1)) && (((i + 1) % dividerPosition) == 0)) {
                    formatted.append(divider);
                }
            }
        }

        return formatted.toString();
    }


    private boolean isDateInputCorrect(Editable s, int size, int dividerPosition, char divider) {
        boolean isCorrect = s.length() <= size;
        for (int i = 0; i < s.length(); i++) {
            if (i > 0 && (i + 1 == dividerPosition)) {
                isCorrect &= divider == s.charAt(i);
            } else {
                isCorrect &= Character.isDigit(s.charAt(i));
            }
        }
        return isCorrect;
    }

    private String dateConcatString(char[] digits, int dividerPosition, char divider) {
        final StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < digits.length; i++) {
            if (digits[i] != 0) {
                formatted.append(digits[i]);
                if ((i > 0) && (i < (digits.length - 1)) && (((i + 1 == dividerPosition)))) {
                    formatted.append(divider);
                }
            }
        }

        return formatted.toString();
    }

    private char[] getDigitArray(final Editable s, final int size) {
        char[] digits = new char[size];
        int index = 0;
        for (int i = 0; i < s.length() && index < size; i++) {
            char current = s.charAt(i);
            if (Character.isDigit(current)) {
                digits[index] = current;
                index++;
            }
        }
        return digits;
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            if (mNetworkErrorObject == null || mNetworkErrorObject.getUiEventType() == null || mNetworkErrorObject.getAlertDialog() == null)
                return;
            switch (mNetworkErrorObject.getUiEventType()) {
                case RETRY_MAKE_PAYMENT:
                    getParams();
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

    private void sentIntent(String url, PayUResponseModel model) {
       /* model = new PayUResponseModel();
        model.setStatus("success");
        //model.setStatus("failure");
        model.setTxnid("139204");
        model.setAddedon("2017-05-08 10:59:19");
        model.setMode("DC");
        model.setAmount("30,000");
        model.setFirstname("Naresh Kumar");
        model.setEmail("naresh@builder.com");
        model.setProductinfo("3939-Alaknanda");*/

        if (model == null || url == null) return;
        IntentDataObject mIntentDataObject = new IntentDataObject();
        // mIntentDataObject.putData("PaymentMode","CC/DC");
        String str = model.getProductinfo();
        String[] arr = str.split("-");
        mIntentDataObject.putData(getString(R.string.txt_order_no), mUpdatePersonalDetailRespModel.getData().getTransaction_info().getOrder_id());
        if (arr.length == 3) {
            mIntentDataObject.putData(getString(R.string.txt_unit_no), arr[0] + "-" + arr[1]);
            mIntentDataObject.putData(getString(R.string.txt_project_name), arr[2]);
        } else {
            mIntentDataObject.putData(getString(R.string.txt_unit_no), arr[0]);
            mIntentDataObject.putData(getString(R.string.txt_project_name), arr[1]);
        }
        mIntentDataObject.putData(getString(R.string.payment_status), makeFisrtCharCaps(model.getStatus()));

        if (model.getMode().equalsIgnoreCase("CC")) {
            mIntentDataObject.putData(getString(R.string.txt_payment_mode), getString(R.string.txt_credit_card));
        } else if (model.getMode().equalsIgnoreCase("DC")) {
            mIntentDataObject.putData(getString(R.string.txt_payment_mode), getString(R.string.txt_debit_card));
        } else if (model.getMode().equalsIgnoreCase("NB")) {
            mIntentDataObject.putData(getString(R.string.txt_payment_mode), getString(R.string.txt_net_banking));
        } else {
            mIntentDataObject.putData(getString(R.string.txt_payment_mode), makeFisrtCharCaps(model.getMode()));
        }
        mIntentDataObject.putData(getString(R.string.txt_cheque_no), makeFisrtCharCaps(model.getId()));
        mIntentDataObject.putData(getString(R.string.txt_cheque_amount), model.getAmount());
        mIntentDataObject.putData(getString(R.string.txt_cheque_date), makeFisrtCharCaps(model.getAddedon()));
        mIntentDataObject.putData(getString(R.string.txt_customer_name), makeFisrtCharCaps(model.getFirstname()));
        mIntentDataObject.putData(getString(R.string.txt_customer_email), model.getEmail());
        mIntentDataObject.putData(getString(R.string.txt_customer_mobile_no), model.getPhone());
        mIntentDataObject.putData(getString(R.string.txt_pan_aadhar_no), mUpdatePersonalDetailRespModel.getData().getUser_information().getPan_no());
        mIntentDataObject.putData(getString(R.string.txt_coupon_code), app.getFromPrefs(ParamsConstants.COUPON_CODE));
        // mIntentDataObject.putData("Unit Details",makeFisrtCharCaps(model.getProductinfo()));
        //Intent mIntent = new Intent(getActivity(),PayUResponseWebViewActivity.class);
        Intent mIntent = new Intent(getActivity(), PaymentStatusActivity.class);
        mIntent.putExtra("URL", url);
        mIntent.putExtra(IntentDataObject.OBJ, mIntentDataObject);
        mIntent.putExtra(TAG, TAG);
        startActivity(mIntent);
    }

    private String makeFisrtCharCaps(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private void setCardDigitInputType(String issuer) {
        if (issuer == null) return;
        if (issuer == "AMEX") {
            CARD_CVC_TOTAL_SYMBOLS = 4;
            cardCVCEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(CARD_CVC_TOTAL_SYMBOLS)});
        } else {
            CARD_CVC_TOTAL_SYMBOLS = 3;
            cardCVCEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(CARD_CVC_TOTAL_SYMBOLS)});
        }
        if (issuer == "SMAE" || issuer == "MAES") {
            CARD_NUMBER_TOTAL_SYMBOLS = 23;
            CARD_NUMBER_TOTAL_DIGITS = 20;
            cardNumberEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(23)});
            //  cardLength = 23;
        } else if (issuer == "AMEX") {
            CARD_NUMBER_TOTAL_SYMBOLS = 18;
            CARD_NUMBER_TOTAL_DIGITS = 16;
            cardNumberEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(18)});
            // cardLength = 18;
        } else if (issuer == "DINR") {
            CARD_NUMBER_TOTAL_SYMBOLS = 17;
            CARD_NUMBER_TOTAL_DIGITS = 16;
            cardNumberEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(17)});
            // cardLength = 17;
        } else {
            CARD_NUMBER_TOTAL_SYMBOLS = 19;
            CARD_NUMBER_TOTAL_DIGITS = 16;
            cardNumberEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(19)});
            //cardLength = 20;
        }
    }

    public boolean isCardValid() {
        if (!(payuUtils.validateCardNumber(cardNumberEditText.getText().toString().replace(" ", "").replace("-", ""))) && cardNumberEditText.length() > 0) {
            return false;
        } else {
            return true;
        }
    }
}
