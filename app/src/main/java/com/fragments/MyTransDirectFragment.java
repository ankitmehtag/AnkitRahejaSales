package com.fragments;


import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.view.MenuItemCompat;
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
import java.util.Objects;

public class MyTransDirectFragment extends Fragment {
    public static final String TAG = MyTransChannelFragment.class.getSimpleName();
    public static final String TAG_NAME = "DIRECT";
    private ArrayList<MyTransactionsRespModel.Data> mDirectList = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    public MyTransactionsListAdapter mAdapter = null;
    private Gson gson;
    private Type listType;
    private RecyclerView recyclerView;
    private SearchView searchView;
    Bundle bundle;
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    public MyTransDirectFragment() {
        gson = new Gson();
        listType = new TypeToken<ArrayList<MyTransactionsRespModel.Data>>() {
        }.getType();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void parseTransactionJson(JSONObject jsonObject) throws JSONException {
        JSONObject mJsonObject = jsonObject.getJSONObject("data");
        JSONArray directJsonArray = mJsonObject.getJSONArray("direct");
        mDirectList = gson.fromJson(directJsonArray.toString(), listType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_transactions, container, false);
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
        if (mDirectList.size() > 0) {
            mAdapter = new MyTransactionsListAdapter(getContext(), mDirectList, TAG_NAME);
            recyclerView.setAdapter(mAdapter);
            recyclerView.addItemDecoration(new MyDividerItemDecoration(Objects.requireNonNull(context), LinearLayoutManager.VERTICAL, 5));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);

           /* recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    MyTransactionsRespModel.Data mData = mDirectList.get(position);
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
            recyclerView.setBackgroundResource(R.drawable.no_transaction_available);
        }
        return rootView;
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onPrepareOptionsMenu(menu);
        inflater.inflate(R.menu.menu_my_transactions, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                final List<MyTransactionsRespModel.Data> filteredModelList = filter(mDirectList, s.toLowerCase());
                mAdapter.setFilter(filteredModelList);
                return true;
            }
        });

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        mAdapter.setFilter(mDirectList);
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
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

            if (customerName.contains(query) || date.contains(query) || size.contains(query) || orderNo.contains(query) || projectName.contains(query)
                    || unitNo.contains(query) || displayName.contains(query) || paymentMode.contains(query) || status.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
}
