package com.sp;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.AppEnums.APIType;
import com.adapters.IvrLeadAdapter;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.appnetwork.WEBAPI;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.helper.ParamsConstants;
import com.model.IvrLeadModel;
import com.model.Projects;
import com.utils.Connectivity;
import com.utils.MyDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.helper.BMHConstants.*;


public class IVRLeads extends AppCompatActivity {
    public static String TAG = IVRLeads.class.getSimpleName();
    private Typeface font;
    private final int LOGIN_REQ_CODE_FOR_BROKER_LIST = 100;
    private ArrayList<IvrLeadModel> mIvrList = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private IvrLeadAdapter mAdapter = null;
    private SearchView searchView;
    private ProgressDialog dialog;
    private BMHApplication app;
    private Gson gson;
    private Type listType,projectListType;
    private int hasExtCallPermission;
    private List<String> permissions = new ArrayList<String>();
    private ArrayList<Projects> projectsMasterList=new ArrayList<>();
    private String mobileNo;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ivr_leads);
        font = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");
        initViews();
        if (Connectivity.isConnected(this)) {
            if (app.getFromPrefs(USERID_KEY).isEmpty()) {
                Intent i = new Intent(this, LoginActivity.class);
                i.putExtra(LoginActivity.LOGIN_INTENT, false);// set false if login screen open from intent.
                startActivityForResult(i, LOGIN_REQ_CODE_FOR_BROKER_LIST);

            } else {
                getBrokersData();
            }
        } else {

            recyclerView.setBackgroundResource(R.drawable.no_internet);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.txt_Ivr);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        app = (BMHApplication) getApplication();
        gson = new Gson();
        listType = new TypeToken<ArrayList<IvrLeadModel>>() {
        }.getType();
        projectListType = new TypeToken<ArrayList<Projects>>() {
        }.getType();
        recyclerView = findViewById(R.id.recyclerView_ivr);

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_brokers_list, menu);
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
                if (mIvrList.size() > 0) {
                    final ArrayList<IvrLeadModel> filteredList = filter(mIvrList, s.toLowerCase());
                    //   if (filteredList.size() > 0)
                    mAdapter.setFilter(filteredList);
                }
                return true;
            }
        });

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        if (mIvrList.size() > 0)
                            mAdapter.setFilter(mIvrList);
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        return true;
                    }
                });
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.search:
                SearchView searchView = (android.widget.SearchView) item.getActionView();
                searchView.setIconified(false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<IvrLeadModel> filter(List<IvrLeadModel> models, String query) {
        query = query.toLowerCase();
        final ArrayList<IvrLeadModel> filteredModelList = new ArrayList<>();
        for (IvrLeadModel model : models) {
            final String mobile_no = (model.getmobile_no()).toLowerCase();
            final String campaign_title = (model.getcampaign_title()).toLowerCase();
            final String datetime = (model.getdatetime()).toLowerCase();
            final String dialstatus = (model.getdialstatus()).toLowerCase();
            if (mobile_no.contains(query) || campaign_title.contains(query) || datetime.contains(query) || dialstatus.contains(query) ) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    public void actionCall(String mobileNo) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        String contactNo = "tel:" + mobileNo;
        callIntent.setData(Uri.parse(contactNo));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
    }

    public boolean checkPermissions(String mobile_no) {
        mobileNo = mobile_no;
        hasExtCallPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE);
        return hasExtCallPermission == PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestPermissions() {
        if (hasExtCallPermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.CALL_PHONE);
        }
        if (!permissions.isEmpty()) {
            requestPermissions(permissions.toArray(new String[permissions.size()]), PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        actionCall(mobileNo);
                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        Log.d("Permissions", "Permission Denied: " + permissions[i]);
                    }
                }
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    private void getBrokersData() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        if (!isFinishing())

            dialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.IVR_LEADS),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        try {
                            if (response != null) {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getBoolean("success") && jsonObject != null) {
                                    parseTransactionJson(jsonObject);
                                }
                                if (mIvrList.size() > 0) {
                                    mAdapter = new IvrLeadAdapter(IVRLeads.this, mIvrList,projectsMasterList);
                                    recyclerView.setAdapter(mAdapter);
                                    recyclerView.addItemDecoration(new MyDividerItemDecoration(IVRLeads.this, LinearLayoutManager.VERTICAL, 5));
                                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                                    layoutManager = new LinearLayoutManager(IVRLeads.this);
                                    recyclerView.setLayoutManager(layoutManager);
                                } else {
                                    recyclerView.setBackgroundResource(R.drawable.no_ivr_leads);
                                }
                            } else {
                                Toast.makeText(IVRLeads.this, R.string.txt_some_thing_went, Toast.LENGTH_SHORT).show();
                            }
                        }
                            catch (JSONException e) {

                        }
                    }
                },


                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Log.d(TAG, "" + error);
                    }
                }
        ) {



            @Override
            protected Map<String, String> getParams()
                       {
                Map<String, String> params = new HashMap<>();
                params.put(ParamsConstants.EMAIL, app.getFromPrefs(USER_EMAIL));
                params.put(ParamsConstants.USER_ID, app.getFromPrefs(USERID_KEY));
                params.put(ParamsConstants.USER_DESIGNATION, app.getFromPrefs(USER_DESIGNATION));
                params.put(ParamsConstants.BUILDER_ID, CURRENT_BUILDER_ID);
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

    private void parseTransactionJson(JSONObject jsonObject) throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        JSONArray projectJsonArray = jsonObject.getJSONArray("projects");
        mIvrList = gson.fromJson(jsonArray.toString(), listType);
        projectsMasterList = gson.fromJson(projectJsonArray.toString(), projectListType);
    }

/*    AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if (view.getTag(R.integer.broker_data) != null && view.getTag(R.integer.broker_data) instanceof BrokersRespDataModel.Data) {
                BrokersRespDataModel.Data mData = (BrokersRespDataModel.Data) view.getTag(R.integer.broker_data);
                IntentDataObject mIntentDataObject = new IntentDataObject();
                mIntentDataObject.putData(ParamsConstants.BROKER_CODE, mData.getBroker_code());
                mIntentDataObject.putData(ParamsConstants.BROKER_ID, mData.getBroker_id());
                Intent mIntent = new Intent(BrokersListActivity.this, LeadsTransactionProileTabsActivity.class);
                mIntent.putExtra(ParamsConstants.TITLE, mData.getFirst_name() + " " + mData.getLast_name());
                mIntent.putExtra(IntentDataObject.OBJ, mIntentDataObject);
                startActivity(mIntent);
            }
        }
    };*/

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQ_CODE_FOR_BROKER_LIST && resultCode == Activity.RESULT_OK) {
            if (!app.getFromPrefs(USERID_KEY).isEmpty() ) {
                getBrokersData();
            }

        } else {
            finish();
        }
    }


}
