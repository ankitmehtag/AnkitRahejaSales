package com.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.AppEnums.APIType;
import com.activities.BrokerDetailsActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.appnetwork.WEBAPI;
import com.helper.ParamsConstants;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.model.NetworkErrorObject;
import com.model.PersonalInfoModel;
import com.sp.BMHApplication;
import com.sp.ConnectivityReceiver;
import com.sp.R;
import com.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PersonalInfoFragment extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {

    private Context mContext;
    private String mBrokerId;
    private static final String TAG = PersonalInfoFragment.class.getSimpleName();
    private NetworkErrorObject mNetworkErrorObject = null;
    CircularImageView imageView;
    TextView tvAddress;
    TextView tvContactNo;
    EditText tvEmailId;
    TextView tvBusinessType;
    TextView tvReraNo;
    TextView tvPanCard;
    TextView tvGSTNo;
    TextView tvDocumentStatus;
    LinearLayout layout_tv_gst_no, div_layout_tv_gst_no, div_layout_tv_rera_no, layout_tv_rera_no;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBrokerId = ((BrokerDetailsActivity) mContext).brokerId;
        if (!TextUtils.isEmpty(mBrokerId))
            getPersonalInfo(mBrokerId);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mContext == null) {
            mContext = getActivity();
        }
    }

    private void getPersonalInfo(final String mBrokerId) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.GET_BROKER_PROFILE_INFO),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response != null) {
                                PersonalInfoModel infoModel = null;
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getBoolean("success") && jsonObject != null)
                                    infoModel = new PersonalInfoModel(jsonObject.getJSONObject("data"));
                              //  String url = UrlFactory.IMG_BASEURL + infoModel.getUser_image();
                                if (infoModel != null) {
                                   /* Glide.with(BMHApplication.getInstance())
                                            .load(url)
                                            .error(R.drawable.ic_profile_circle)
                                            .into(imageView);*/
                                    Utils.imageUpdate(mContext, imageView, infoModel.getUser_image());
                                    tvAddress.setText(getString(R.string.colon, infoModel.getAddress()));
                                    tvContactNo.setText(getString(R.string.colon, infoModel.getMobile_no()));
                                    tvEmailId.setText(getString(R.string.colon, infoModel.getEmail_id()));
                                    tvBusinessType.setText(getString(R.string.colon, infoModel.getBusiness_type()));
                                    if (TextUtils.isEmpty(infoModel.getRera_no()) || infoModel.getRera_no().equalsIgnoreCase("")
                                            || infoModel.getRera_no().equalsIgnoreCase("NA")) {
                                        div_layout_tv_rera_no.setVisibility(View.GONE);
                                        layout_tv_rera_no.setVisibility(View.GONE);
                                    } else {
                                        tvReraNo.setText(getString(R.string.colon, infoModel.getRera_no()));
                                    }
                                    tvPanCard.setText(getString(R.string.colon, infoModel.getPan_no()));
                                    if (TextUtils.isEmpty(infoModel.getGst_no()) || infoModel.getGst_no().equalsIgnoreCase("")
                                            || infoModel.getGst_no().equalsIgnoreCase("NA")) {
                                        layout_tv_gst_no.setVisibility(View.GONE);
                                        div_layout_tv_gst_no.setVisibility(View.GONE);
                                    } else {
                                        tvGSTNo.setText(getString(R.string.colon, infoModel.getGst_no()));
                                    }
                                    tvDocumentStatus.setText(getString(R.string.colon, infoModel.getDocument_status()));

                                }/* else
                                    Toast.makeText(getActivity(), R.string.txt_no_data_found, Toast.LENGTH_SHORT).show();*/
                            } else {
                                Toast.makeText(mContext, R.string.txt_some_thing_went, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "" + error);
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(ParamsConstants.BROKER_ID, mBrokerId);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap();
                headers.put("Content-Type", WEBAPI.contentTypeFormData);
                return headers;
            }
        };
        stringRequest.setShouldCache(true);
        BMHApplication.getInstance().addToRequestQueue(stringRequest, "headerRequest");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_info, container, false);
        imageView = view.findViewById(R.id.iv_user_img);
        tvAddress = view.findViewById(R.id.tv_address_val);
        tvContactNo = view.findViewById(R.id.tv_contact_no_val);
        tvEmailId = view.findViewById(R.id.tv_email_id_val);
        tvBusinessType = view.findViewById(R.id.tv_business_type_val);
        tvReraNo = view.findViewById(R.id.tv_rera_no_val);
        tvPanCard = view.findViewById(R.id.tv_pan_card_val);
        tvGSTNo = view.findViewById(R.id.tv_gst_no_val);
        tvDocumentStatus = view.findViewById(R.id.tv_document_status_val);
        layout_tv_gst_no = view.findViewById(R.id.layout_tv_gst_no);
        div_layout_tv_gst_no = view.findViewById(R.id.div_layout_tv_gst_no);
        div_layout_tv_rera_no = view.findViewById(R.id.div_layout_tv_rera_no);
        layout_tv_rera_no = view.findViewById(R.id.layout_tv_rera_no);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

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
}
