package com.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.AppEnums.APIType;
import com.activities.BrokerDetailsActivity;
import com.activities.TransactionDetailsActivity;
import com.adapters.MyTransactionsListAdapter;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.appnetwork.WEBAPI;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.helper.ParamsConstants;
import com.interfaces.RecyclerTouchListener;
import com.model.MyTransactionsRespModel;
import com.model.NetworkErrorObject;
import com.sp.BMHApplication;
import com.sp.ConnectivityReceiver;
import com.sp.R;
import com.utils.MyDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TransactionsFragment extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {

    private String mBrokerCode;
    private String mTabName;
    private static final String TAG = TransactionsFragment.class.getSimpleName();
    private ArrayList<MyTransactionsRespModel.Data> mTransactionsList = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private MyTransactionsListAdapter mAdapter = null;
    private Gson gson;
    private Type listType;
    private NetworkErrorObject mNetworkErrorObject = null;
    private Context mContext;

    public TransactionsFragment() {
        gson = new Gson();
        listType = new TypeToken<ArrayList<MyTransactionsRespModel.Data>>() {
        }.getType();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBrokerCode = ((BrokerDetailsActivity) mContext).brokerCode;
        mTabName = getString(R.string.tab_transactions);
        if (!TextUtils.isEmpty(mBrokerCode))
            getTransactionsList(mBrokerCode, mTabName);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mContext == null) {
            mContext = getActivity();
        }
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

    private void parseTransactionJson(JSONObject jsonObject) throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        mTransactionsList = gson.fromJson(jsonArray.toString(), listType);
    }

    private void getTransactionsList(final String mBrokerCode, final String tabName) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.BROKER_TRANSACTIONS),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            if (response != null) {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getBoolean("success") && jsonObject != null) {
                                    parseTransactionJson(jsonObject);
                                }
                                if (mTransactionsList.size() > 0) {
                                    mAdapter = new MyTransactionsListAdapter(mContext, mTransactionsList, tabName);
                                    recyclerView.setAdapter(mAdapter);
                                    if (mContext != null)
                                        recyclerView.addItemDecoration(new MyDividerItemDecoration(Objects.requireNonNull(mContext), LinearLayoutManager.VERTICAL, 5));
                                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                                    layoutManager = new LinearLayoutManager(mContext);
                                    recyclerView.setLayoutManager(layoutManager);

                                    recyclerView.addOnItemTouchListener(new RecyclerTouchListener(mContext, recyclerView, new RecyclerTouchListener.ClickListener() {
                                        @Override
                                        public void onClick(View view, int position) {
                                            MyTransactionsRespModel.Data mData = mTransactionsList.get(position);
                                            if (mData != null) {
                                                Intent intent = new Intent(getContext(), TransactionDetailsActivity.class);
                                                intent.putExtra("TAG_NAME", tabName);
                                                intent.putExtra("ChannelTransactionModel", mData);
                                                startActivity(intent);
                                            }
                                        }

                                        @Override
                                        public void onLongClick(View view, int position) {

                                        }
                                    }));
                                }else {
                                    recyclerView.setBackgroundResource(R.drawable.no_transaction_by_brokers);
                                }
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
                params.put(ParamsConstants.BROKER_CODE, mBrokerCode);
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
        View rootView = inflater.inflate(R.layout.fragment_my_transactions, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        return rootView;
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
