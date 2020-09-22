package com.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.AppEnums.APIType;
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
import com.model.BlogDetails;
import com.model.BlogSubscibe;
import com.sp.BMHApplication;
import com.sp.BaseFragmentActivity;
import com.sp.LoginActivity;
import com.sp.R;
import com.utils.PicassoImageGetter;
import com.utils.Utils;
import com.utils.WordUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class BlogDetailsActivity extends BaseFragmentActivity {
    private String blogId, categoryId, share_url, title, is_note, url;
    ProgressDialog dialog;
    TextView tv_title, tv_author, tv_date_time, tv_description;
    ImageView iv_blog_image;
    Toolbar mTopToolbar;
    TextToSpeech description_to_speech;
    private Spannable description;
    private Spanned category;
    private boolean isChecked = false;
    private boolean isNoted;
    EditText et_subscribe;
    Button btnSubscribe;
    private static final String DRAWABLE = "drawable";
    Context context;
    private MenuItem checkableNote, checkable;
    BlogDetails blogDetails;
    private final int LOGIN_REQ_CODE = 452;
    /**
     * Regex pattern that looks for embedded images of the format: [img src=imageName/]
     */
    public static final String PATTERN = "\\Q[img src=\\E([a-zA-Z0-9_]+?)\\Q/]\\E";

    @Override
    protected String setActionBarTitle() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_details);
        blogId = getIntent().getStringExtra("blog_id");
        categoryId = getIntent().getStringExtra("category_id");
        mTopToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mTopToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        et_subscribe = findViewById(R.id.et_email_subscribe);
        btnSubscribe = findViewById(R.id.btnSubscribe);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.show();
        description_to_speech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    description_to_speech.setLanguage(Locale.US);
                }
            }
        });
        tv_title = (TextView) findViewById(R.id.tv_blog_details_title);
        tv_author = (TextView) findViewById(R.id.tv_blog_details_author);
        tv_date_time = (TextView) findViewById(R.id.tv_blog_details_date_time);
        tv_description = (TextView) findViewById(R.id.tv_blog_details_description);
        iv_blog_image = (ImageView) findViewById(R.id.iv_blog_details_image);
        getBlogDetails();
        iv_blog_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bigViewBlogImage();
            }
        });
        btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = et_subscribe.getText().toString().trim();
                if (!TextUtils.isDigitsOnly(email)) {
                    if (!Utils.isEmailValid(email)) {
                        app.shakeEdittext(et_subscribe);
                        et_subscribe.setSelection(email.length());
                        et_subscribe.requestFocus();
                        Utils.showToast(BlogDetailsActivity.this, "Please enter a valid Email Id");
                    } else {
                        getSubscribe(email);
                        et_subscribe.setText("");
                    }
                }
            }
        });
    }

    private void bigViewBlogImage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.big_blog_image, null);
        dialogBuilder.setView(dialogView);

        ImageView big_blog_image = (ImageView) dialogView.findViewById(R.id.big_blog_image);
        if (url != null && !url.equalsIgnoreCase("")) {
            Glide.with(BlogDetailsActivity.this).load(UrlFactory.IMG_BASEURL + url).
                    placeholder(BMHConstants.PLACE_HOLDER).error(BMHConstants.NO_IMAGE).into(big_blog_image);
        }
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

    }

    private void getBlogDetails() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.BLOG_DETAILS),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        blogDetails = (BlogDetails) JsonParser.convertJsonToBean(APIType.BLOG_DETAILS, response);
                        if (blogDetails != null) {
                            if (blogDetails.isSuccess() && blogDetails.getData() != null) {
                                title = blogDetails.getData().getTitle();
                                tv_title.setText(WordUtils.capitalize(title.toLowerCase()));
                                category = Html.fromHtml(blogDetails.getData().getCategory_name());
                                Objects.requireNonNull(getSupportActionBar()).setTitle(category);
                                tv_author.setText(getString(R.string.author) + ": " + blogDetails.getData().getAuthor());
                                tv_date_time.setText(blogDetails.getData().getDate());
                                //description = (Spannable) Html.fromHtml(blogDetails.getData().getContent());
                                PicassoImageGetter imageGetter = new PicassoImageGetter(tv_description, getApplicationContext());
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                    description = (Spannable) Html.fromHtml(blogDetails.getData().getContent(), Html.FROM_HTML_MODE_LEGACY, imageGetter, null);
                                } else {
                                    description = (Spannable) Html.fromHtml(blogDetails.getData().getContent(), imageGetter, null);
                                }
                                tv_description.setText(description);
                                share_url = blogDetails.getData().getBlog_url();
                                is_note = blogDetails.getData().getIs_note();
                                if (is_note.equalsIgnoreCase("0")) {
                                    checkableNote.setIcon(R.drawable.ic_note);
                                    isNoted = false;
                                } else {
                                    checkableNote.setIcon(R.drawable.ic_note_unfav);
                                    isNoted = true;
                                }
                                url = blogDetails.getData().getImage();//TODO: assign url
                                if (url != null && !url.equalsIgnoreCase("")) {
                                    Glide.with(BlogDetailsActivity.this).load(UrlFactory.IMG_BASEURL + url)
                                            .placeholder(BMHConstants.PLACE_HOLDER).error(BMHConstants.NO_IMAGE).into(iv_blog_image);
                                }
                                updateReadStatus();
                                app.saveTabIntoPrefs("RECENT_BLOG_CATEGORY", categoryId);

                            } else {
                                try {
                                    showToast(new JSONObject(response).optString("message"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            showToast(getString(R.string.something_went_wrong));
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
                params.put(ParamsConstants.BLOG_ID, blogId);
                params.put(ParamsConstants.USER_ID, app.getFromPrefs(BMHConstants.USERID_KEY));
                params.put(ParamsConstants.USER_TYPE, BMHConstants.SALES_PERSON);
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

    private void updateReadStatus() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.READ_BLOG_DETAILS),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response != null) {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.optBoolean("success")) {
                                    Log.d("Response", "" + jsonObject.optString("message"));
                                }
                            }
                        } catch (JSONException e) {
                            e.getMessage();
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
                params.put(ParamsConstants.BUILDER_ID, (BMHConstants.CURRENT_BUILDER_ID));
                params.put(ParamsConstants.USER_ID, app.getFromPrefs(BMHConstants.USERID_KEY));
                params.put(ParamsConstants.USER_TYPE, BMHConstants.SALES_PERSON);
                params.put(ParamsConstants.BLOG_ID, blogId);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap();
                return headers;
            }
        };
        stringRequest.setShouldCache(false);
        BMHApplication.getInstance().addToRequestQueue(stringRequest, "blogReadStatusRequest");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.blog_details_menu_items, menu);
        checkable = menu.findItem(R.id.action_text_to_speech);
        checkable.setChecked(isChecked);
        checkableNote = menu.findItem(R.id.action_add_to_note);
        checkableNote.setChecked(isNoted);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        switch (id) {
            case R.id.action_add_to_note:
                if (app.getFromPrefs(BMHConstants.USERID_KEY) != null && app.getFromPrefs(BMHConstants.USERID_KEY).length() > 0) {
                    if (isNoted == true) {
                        isNoted = false;
                        item.setChecked(isNoted);
                        item.setIcon(R.drawable.ic_note);
                        addToNote("0");
                        return true;
                    } else {
                        isNoted = true;
                        item.setChecked(isNoted);
                        item.setIcon(R.drawable.ic_note_unfav);
                        addToNote("1");
                        return false;
                    }
                } else {
                    Intent i = new Intent(this, LoginActivity.class);
                    i.putExtra(LoginActivity.LOGIN_INTENT, false);// set false if login screen open from intent.
                    i.putExtra("blog_id", blogId);
                    startActivityForResult(i, LOGIN_REQ_CODE);
                }
                break;
            case R.id.action_text_to_speech:
                if (!isChecked) {
                    isChecked = true;
                    item.setChecked(isChecked);
                    item.setIcon(R.drawable.ic_speak);
                    description_to_speech.speak(blogDetails.getData().getTitle() + "..." + "" + "" + Html.fromHtml(blogDetails.getData().getContent()), TextToSpeech.QUEUE_FLUSH, null);
                    return true;
                } else {
                    if (description_to_speech != null) {
                        description_to_speech.stop();
                    }
                    isChecked = false;
                    item.setChecked(isChecked);
                    item.setIcon(R.drawable.ic_speak_off);
                    return false;
                }
            case R.id.action_share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, share_url);
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out this blog!");
                startActivity(Intent.createChooser(intent, "Share"));
                return true;
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQ_CODE && resultCode == Activity.RESULT_OK) {
            if (!app.getFromPrefs(BMHConstants.USERID_KEY).isEmpty()) {
                getBlogDetails();
            }
        } else {
            Utils.showToast(this, getString(R.string.something_went_wrong));
        }
    }

    @Override
    protected void onPause() {
        if (description_to_speech != null) {
            description_to_speech.stop();
            checkable.setIcon(R.drawable.ic_speak_off);
            isChecked = false;
        }
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void getSubscribe(final String email) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.BLOG_SUBSCRIBE),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        BlogSubscibe blogSubscibe = (BlogSubscibe) JsonParser.convertJsonToBean(APIType.BLOG_SUBSCRIBE, response);
                        if (blogSubscibe != null) {
                            if (blogSubscibe.isSuccess() && blogSubscibe.getMessage() != null) {
                                Utils.showToast(BlogDetailsActivity.this, blogSubscibe.getMessage());
                            }
                        } else {
                            Utils.showToast(BlogDetailsActivity.this, getString(R.string.something_went_wrong));
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

    public void addToNote(final String addOrRemove) {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.BLOG_ADD_TO_NOTE),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {
                                showToast(jsonObject.optString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                params.put("status", addOrRemove);
                params.put("blog_id", blogId);
                params.put("user_id", app.getFromPrefs(BMHConstants.USERID_KEY));
                params.put("user_type", BMHConstants.SALES_PERSON);
                params.put(ParamsConstants.BUILDER_ID, BMHConstants.CURRENT_BUILDER_ID);
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
}
