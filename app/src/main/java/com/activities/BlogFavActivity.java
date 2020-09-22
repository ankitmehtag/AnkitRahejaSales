package com.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.AppEnums.APIType;
import com.adapters.BlogRecyclerAdapter;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.appnetwork.WEBAPI;
import com.helper.BMHConstants;
import com.helper.ParamsConstants;
import com.interfaces.RecyclerTouchListener;
import com.jsonparser.JsonParser;
import com.model.FavBlogModel;
import com.sp.BMHApplication;
import com.sp.R;
import com.utils.Connectivity;
import com.utils.MyDividerItemDecoration;
import com.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BlogFavActivity extends AppCompatActivity {
    RecyclerView.LayoutManager layoutManager;
    BlogRecyclerAdapter mAdapter = null;
    RecyclerView recyclerView;
    ArrayList<FavBlogModel.Data> blogDataArrayList;
    ProgressDialog dialog;
    private BMHApplication app;
    Toolbar toolbar;
    View no_blog_found;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_fav);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.txt_read_later);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        app = (BMHApplication) getApplication();
        recyclerView = findViewById(R.id.fav_blog_recyclerView);
        no_blog_found = (View) findViewById(R.id.no_blog_found);
        if (Connectivity.isConnected(this)) {
            getFavBlog();
            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    FavBlogModel.Data mData = blogDataArrayList.get(position);
                    if (mData != null) {
                        Intent blogDetails = new Intent(BlogFavActivity.this, BlogDetailsActivity.class);
                        blogDetails.putExtra("blog_id", mData.getBlog_id());
                        startActivity(blogDetails);
                    } else {
                        Utils.showToast(BlogFavActivity.this, getString(R.string.something_went_wrong));
                    }
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));
        } else {
            recyclerView.setBackgroundResource(R.drawable.no_internet);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getFavBlog();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void getFavBlog() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.GET_FAV_BLOG),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        FavBlogModel blogData = (FavBlogModel) JsonParser.convertJsonToBean(APIType.GET_FAV_BLOG, response);
                        if (blogData != null) {
                            if (blogData.isSuccess() && blogData.getData() != null) {
                                blogDataArrayList = blogData.getData();
                                mAdapter = new BlogRecyclerAdapter(BlogFavActivity.this, null, null, blogDataArrayList);
                                recyclerView.setAdapter(mAdapter);
                                recyclerView.addItemDecoration(new MyDividerItemDecoration(BlogFavActivity.this, LinearLayoutManager.VERTICAL, 5));
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                layoutManager = new LinearLayoutManager(BlogFavActivity.this);
                                recyclerView.setLayoutManager(layoutManager);
                            } else {
                                no_blog_found.setVisibility(View.VISIBLE);
                                recyclerView.setAdapter(null);
                            }
                        } else {
                            Utils.showToast(BlogFavActivity.this, getString(R.string.something_went_wrong));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", "" + error);
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", app.getFromPrefs(BMHConstants.USERID_KEY));
                params.put("user_type", BMHConstants.SALES_PERSON);
                params.put(ParamsConstants.BUILDER_ID, (BMHConstants.CURRENT_BUILDER_ID));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap();
                // headers.put("apiKey", "xxxxxxxxxxxxxxx");
                return headers;
            }
        };
        stringRequest.setShouldCache(false);
        BMHApplication.getInstance().addToRequestQueue(stringRequest, "headerRequest");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
