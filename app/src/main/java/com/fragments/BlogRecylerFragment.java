package com.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.AppEnums.APIType;
import com.activities.BlogDetailsActivity;
import com.adapters.BlogRecyclerAdapter;
import com.adapters.RecentPagerAdapter;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.appnetwork.WEBAPI;
import com.bumptech.glide.Glide;
import com.helper.BMHConstants;
import com.helper.ParamsConstants;
import com.helper.UrlFactory;
import com.jsonparser.JsonParser;
import com.model.BlogData;
import com.model.BlogSubscibe;
import com.model.RecentBlogModel;
import com.sp.BMHApplication;
import com.sp.R;
import com.utils.MyDividerItemDecoration;
import com.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlogRecylerFragment extends Fragment implements ViewPager.OnPageChangeListener {
    ArrayList<BlogData> blogDataArrayList;
    RecyclerView.LayoutManager layoutManager;
    BlogRecyclerAdapter mAdapter = null;
    RecyclerView recyclerView;
    ImageButton ib_fab;
    ImageView banner_img;
    //  ProgressDialog dialog;
    BMHApplication app;
    String banner_img_url, banner_name, blog_id, category_id;
    TextView banner_title;
    RecentPagerAdapter mPagerAdapter;
    private int currentPage = 0;
    private int NUM_PAGES_RECENT, NUM_PAGES_POPULAR = 0;
    private ArrayList<RecentBlogModel.Data.Recent> recent;
    private ArrayList<RecentBlogModel.Data.Recent> popular;
    private ViewPager recent_pager, popular_pager;
    private SearchView searchView;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        app = (BMHApplication) getActivity().getApplication();
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_blog_recyler, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.blog_recyclerView);
        ib_fab = (ImageButton) view.findViewById(R.id.ib_floating);
        banner_img = (ImageView) view.findViewById(R.id.iv_banner);
        banner_title = (TextView) view.findViewById(R.id.tv_banner);
        recent_pager = (ViewPager) view.findViewById(R.id.recent_blog_pager);
        popular_pager = (ViewPager) view.findViewById(R.id.popular_blog_pager);
        blogDataArrayList = getArguments().getParcelableArrayList("blog_data");
        banner_img_url = getArguments().getString("banner_img");
        banner_name = getArguments().getString("banner_text");
        blog_id = getArguments().getString("blog_id");
        category_id = getArguments().getString("category_id");

      /*  dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading recent blogs..");
        dialog.show();*/

        getRecentBlog();
        if (!TextUtils.isEmpty(banner_name)) {
            banner_title.setText(banner_name);
        } else {
            banner_title.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(banner_img_url)) {
            Glide.with(this).load(UrlFactory.getShortImageByWidthUrl((int) Utils.dp2px(120, getActivity()),
                    banner_img_url)).into(banner_img);
        } else {
            banner_img.setVisibility(View.GONE);
        }
        banner_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent blogDetails = new Intent(getActivity(), BlogDetailsActivity.class);
                blogDetails.putExtra("blog_id", blog_id);
                startActivity(blogDetails);
            }
        });
        if (blogDataArrayList.size() > 0) {
            mAdapter = new BlogRecyclerAdapter(getActivity(), blogDataArrayList, category_id, null);
            recyclerView.setAdapter(mAdapter);
            recyclerView.addItemDecoration(new MyDividerItemDecoration(Objects.requireNonNull(context), LinearLayoutManager.VERTICAL, 5));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
        }
        ib_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subscribe();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public BlogRecylerFragment newInstance(Bundle bundle) {
        BlogRecylerFragment fragment = new BlogRecylerFragment();
        fragment.setArguments(bundle);
        return fragment;
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
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_brokers_list, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        searchView = (SearchView) search.getActionView();
        searchView.setQueryHint("Search blog");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (blogDataArrayList.size() > 0) {
                    final ArrayList<BlogData> filteredList = filter(blogDataArrayList, s.toLowerCase());
                    mAdapter.setFilter(filteredList);
                }
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
                mAdapter.setFilter(blogDataArrayList);
                return true;
            }
        });
        if (!search.isActionViewExpanded()) {
            if (blogDataArrayList != null) {
                if (mAdapter == null)
                    mAdapter = new BlogRecyclerAdapter(getActivity(), blogDataArrayList, category_id, null);
                mAdapter.setFilter(blogDataArrayList);
            }
        }
    }


    private ArrayList<BlogData> filter(List<BlogData> models, String query) {
        query = query.toLowerCase();
        final ArrayList<BlogData> filteredModelList = new ArrayList<>();
        for (BlogData model : models) {
            final String title = model.getTitle().toLowerCase();
            final String category = model.getCategory_name().toLowerCase();
            final String date = (model.getDate()).toLowerCase();
            //     final String content = (model.getContent()).toLowerCase();
            if (title.contains(query) || category.contains(query) || date.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    public void subscribe() {
        Typeface font = Typeface.createFromAsset(getResources().getAssets(), "fonts/regular.ttf");
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View dialogView = factory.inflate(R.layout.blog_subscribe_layout, null);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        final EditText et_email_subscribe = (EditText) dialogView.findViewById(R.id.et_email_subscribe);
        final TextView tv_banner_name = (TextView) dialogView.findViewById(R.id.make_an_enquiry);
        final Button btnSubscribe = (Button) dialogView.findViewById(R.id.btnSubscribe);
        final ImageButton img_close = (ImageButton) dialogView.findViewById(R.id.img_close);
        et_email_subscribe.setTypeface(font);
        tv_banner_name.setTypeface(font);
        btnSubscribe.setTypeface(font);
        View.OnClickListener dialogViewsClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnSubscribe:
                        String email = et_email_subscribe.getText().toString().trim();
                        if (!TextUtils.isEmpty(email)) {
                            if (!TextUtils.isDigitsOnly(email)) {
                                if (!Utils.isEmailValid(email)) {
                                    app.shakeEdittext(et_email_subscribe);
                                    et_email_subscribe.setSelection(email.length());
                                    et_email_subscribe.requestFocus();
                                    Utils.showToast(getActivity(), "Please enter a valid Email Id");
                                } else {
                                    getSubscribe(email);
                                    dialog.dismiss();
                                }
                            } else {
                                Utils.showToast(getActivity(), "Please enter Valid Email Id");
                            }
                        } else {
                            Utils.showToast(getActivity(), "Please enter Email Id");
                        }
                        break;
                    case R.id.img_close:
                        dialog.dismiss();
                        break;
                }
            }
        };
        img_close.setOnClickListener(dialogViewsClick);
        btnSubscribe.setOnClickListener(dialogViewsClick);
        dialog.setView(dialogView);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
    }

    public void getSubscribe(final String email) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.BLOG_SUBSCRIBE),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        BlogSubscibe blogSubscibe = (BlogSubscibe) JsonParser.convertJsonToBean(APIType.BLOG_SUBSCRIBE, response);
                        if (blogSubscibe != null) {
                            if (blogSubscibe.isSuccess() && blogSubscibe.getMessage() != null) {
                                Utils.showToast(getActivity(), blogSubscibe.getMessage());
                            }
                        } else {
                            Utils.showToast(getActivity(), getString(R.string.something_went_wrong));
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
                params.put("email_id", email);
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

    public void getRecentBlog() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.GET_RECENT_BLOG),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        RecentBlogModel recentBlogModel = (RecentBlogModel) JsonParser.convertJsonToBean(APIType.GET_RECENT_BLOG, response);
                        if (recentBlogModel != null) {
                            if (recentBlogModel.isSuccess() && recentBlogModel.getData() != null) {
/*
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
*/
                                recent = recentBlogModel.getData().getRecent();
                                popular = recentBlogModel.getData().getPopular();
                                mPagerAdapter = new RecentPagerAdapter(getActivity(), recent);
                                recent_pager.setAdapter(mPagerAdapter);
                                mPagerAdapter = new RecentPagerAdapter(getActivity(), popular);
                                popular_pager.setAdapter(mPagerAdapter);
                                NUM_PAGES_RECENT = popular.size();
                                NUM_PAGES_POPULAR = recent.size();
                                final android.os.Handler handler = new android.os.Handler();
                                final Runnable Update = new Runnable() {
                                    public void run() {
                                        if (currentPage == NUM_PAGES_RECENT && currentPage == NUM_PAGES_POPULAR) {
                                            currentPage = 0;
                                        }
                                        recent_pager.setCurrentItem(currentPage++, true);
                                        popular_pager.setCurrentItem(currentPage++, true);
                                    }
                                };
                                Timer swipeTimer = new Timer();
                                swipeTimer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        handler.post(Update);
                                    }
                                }, 3000, 3000);
                            }
                        } else {
                            Utils.showToast(getActivity(), getString(R.string.something_went_wrong));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
/*
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
*/
                        Log.d("Error.Response", "" + error);
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(ParamsConstants.BUILDER_ID, (BMHConstants.CURRENT_BUILDER_ID));
                params.put(ParamsConstants.USER_ID, app.getFromPrefs(BMHConstants.USERID_KEY));
                params.put(ParamsConstants.USER_TYPE, BMHConstants.SALES_PERSON);
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
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
