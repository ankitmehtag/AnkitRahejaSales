package com.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.activities.MyTransactionsActivity;
import com.adapters.MyTransactionsListAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.model.MyTransactionsRespModel;
import com.sp.R;
import com.utils.MyDividerItemDecoration;
import com.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MyTransChannelFragment extends Fragment {

    private static final String TAG = MyTransChannelFragment.class.getSimpleName();
    public static final String TAG_NAME = "CHANNEL";
    private ArrayList<MyTransactionsRespModel.Data> mChannelList = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    public MyTransactionsListAdapter mAdapter = null;
    private Gson gson;
    private Type listType;
    private RecyclerView recyclerView;
    private SearchView searchView;
    Bundle bundle;

    public MyTransChannelFragment newInstance(Bundle bundle) {
        MyTransChannelFragment fragment = new MyTransChannelFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public MyTransChannelFragment() {
        gson = new Gson();
        listType = new TypeToken<ArrayList<MyTransactionsRespModel.Data>>() {
        }.getType();
    }

    private void parseTransactionJson(JSONObject jsonObject) throws JSONException {

        JSONObject mJsonObject = jsonObject.getJSONObject("data");
        JSONArray channelJsonArray = mJsonObject.getJSONArray("channel");
        mChannelList = gson.fromJson(channelJsonArray.toString(), listType);
    }

    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
        if (searchView != null &&
                !searchView.getQuery().toString().isEmpty()) {

            searchView.setIconified(true);
            searchView.setIconified(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_trans_channel, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        try {
            bundle = ((MyTransactionsActivity) getActivity()).masterBundle;
            if (bundle != null) {
                String responseString = bundle.getString("JSON_STRING");
                JSONObject jsonObject = new JSONObject(responseString);
                if (jsonObject != null && jsonObject.getBoolean("success")) {
                    parseTransactionJson(jsonObject);
                } else {
                    Utils.showToast(getActivity(), jsonObject.optString("message"));
                }
            }
        } catch (JSONException e) {
            e.getMessage();
        }

        if (mChannelList.size() > 0) {
            mAdapter = new MyTransactionsListAdapter(getContext(), mChannelList, TAG_NAME);
            recyclerView.setAdapter(mAdapter);
            recyclerView.addItemDecoration(new MyDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL, 5));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
           /* recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {

                    MyTransactionsRespModel.Data mData = mChannelList.get(position);
                    if (mData != null) {
                        Intent intent = new Intent(getContext(), TransactionDetailsActivity.class);
                        intent.putExtra("TAG_NAME", TAG_NAME);
                        intent.putExtra("ChannelTransactionModel", mData);
                        startActivity(intent);
                    }
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));*/
        } else {
            recyclerView.setBackgroundResource(R.drawable.no_channel_transaction_available);
        }
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onPrepareOptionsMenu(menu);
        inflater.inflate(R.menu.menu_my_transactions, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                final List<MyTransactionsRespModel.Data> filteredModelList = filter(mChannelList, s.toLowerCase());
                mAdapter.setFilter(filteredModelList);
                return true;
            }
        });

        search.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                mAdapter.setFilter(mChannelList);
                return true;
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private List<MyTransactionsRespModel.Data> filter(List<MyTransactionsRespModel.Data> models, String query) {
        query = query.toLowerCase();
        final List<MyTransactionsRespModel.Data> filteredModelList = new ArrayList<>();
        for (MyTransactionsRespModel.Data model : models) {
            final String customerName = (model.getCustomer_Name()).toLowerCase();
            final String date = (model.getTransaction_datetime()).toLowerCase();
            final String size = (model.getSize()).toLowerCase();
            final String orderNo = (model.getOrder_no()).toLowerCase();
            final String projectName = (model.getProject_name()).toLowerCase();
            final String unitNo = (model.getUnit_no()).toLowerCase();
            final String displayName = (model.getDisplay_name()).toLowerCase();
            final String paymentMode = (model.getPayment_mode()).toLowerCase();
            final String status = (model.getStatus()).toLowerCase();
          //  final String brokerName = (model.getBroker_name()).toLowerCase();
           // final String brokerCode = (model.getBroker_code()).toLowerCase();
            if (customerName.contains(query) || date.contains(query) || size.contains(query) || orderNo.contains(query) || projectName.contains(query)
                    || unitNo.contains(query) || displayName.contains(query) || paymentMode.contains(query) || status.contains(query)
                     ) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
}
