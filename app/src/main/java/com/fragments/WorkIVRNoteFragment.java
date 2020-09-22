package com.fragments;


import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.activities.IVRHistory;
import com.adapters.AsmHistoryAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.model.AsmHistoryModel;
import com.sp.R;
import com.utils.MyDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

public class WorkIVRNoteFragment extends Fragment {

    public static final String TAG = HistoryNotesFragment.class.getSimpleName();
    private ArrayList<AsmHistoryModel> mHistoryList;
    private RecyclerView.LayoutManager layoutManager;
    public AsmHistoryAdapter mAdapter = null;
    private RecyclerView recyclerView;
    private Gson gson;
    private Type listType;
    Bundle bundle;
    private Context context;

    public WorkIVRNoteFragment() {
        gson = new Gson();
        listType = new TypeToken<ArrayList<AsmHistoryModel>>() {
        }.getType();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    private void parseJson(JSONObject jsonObject) throws JSONException {
        if (mHistoryList != null && mHistoryList.size() > 0)
            mHistoryList.clear();
        JSONObject mJsonObject = jsonObject.getJSONObject("data");
        JSONArray workArray = mJsonObject.optJSONArray("notes");
        if (workArray != null)
            mHistoryList = gson.fromJson(workArray.toString(), listType);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            bundle = ((IVRHistory) getActivity()).mBundle;
            String responseString = bundle.getString("JSON_STRING");
            JSONObject jsonObject = new JSONObject(responseString);
            if (jsonObject.getBoolean("success") && jsonObject != null) {
                parseJson(jsonObject);
            }
        } catch (JSONException e) {
            e.getMessage();
        }
        mAdapter = new AsmHistoryAdapter(getContext(), mHistoryList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new MyDividerItemDecoration(Objects.requireNonNull(context), LinearLayoutManager.VERTICAL, 5));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_transactions, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        return rootView;
    }
}
