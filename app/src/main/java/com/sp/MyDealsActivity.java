package com.sp;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.AppEnums.APIType;
import com.adapters.MyDealsListAdapter;
import com.appnetwork.AsyncThread;
import com.async.DownloadingAsync;
import com.appnetwork.ReqRespBean;
import com.appnetwork.WEBAPI;
import com.helper.BMHConstants;
import com.helper.IntentDataObject;
import com.helper.ParamsConstants;
import com.helper.UrlFactory;
import com.interfaces.RecyclerTouchListener;
import com.jsonparser.JsonParser;
import com.model.InventoryRespModel;
import com.utils.Connectivity;
import com.utils.MyDividerItemDecoration;
import com.utils.Utils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class MyDealsActivity extends BaseFragmentActivity implements DownloadingAsync.IDownloadAttachment {
    private final String TAG = MyDealsActivity.class.getSimpleName();
    private Typeface typeface;
    private Toolbar toolbar = null;
    private RecyclerView recyclerView;
    private MyDealsListAdapter myDealsListAdapter;
    private ArrayList<InventoryRespModel.Data> mDealsList;
    private ArrayList<InventoryRespModel.Data> mFilteredList;
    private AsyncThread mAsyncThread = null;
    private final int LOGIN_REQ_CODE_FOR_MY_DEALS = 100;
    RelativeLayout rl_from_date, rl_to_date;
    TextView tv_from_date, tv_from_date_value, tv_to_date, tv_to_date_value;
    private Calendar fromDateCalender, toDateCalender;
    private Calendar currentDateCal;
    int mYear, mMonth, mDay;
    Date currentDate = null;
    private final String dateFormat = Utils.dateFormat;
    private SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
    private String mToDate = "";
    private String mFromDate = "";
    private String file_name;
    private String fileStringUrl;
    File directory = null;
    String savingPath = null;
    LinearLayout linearLayout;

    @Override
    protected String setActionBarTitle() {
        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_deals);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");
        initViews();
        setListeners();
        if (getIntent() != null && getIntent().getSerializableExtra(IntentDataObject.OBJ) instanceof IntentDataObject) {
            toolbar.setTitle(getIntent().getStringExtra(ParamsConstants.TITLE));
          //  IntentDataObject mIntentDataObject = (IntentDataObject) getIntent().getSerializableExtra(IntentDataObject.OBJ);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Connectivity.isConnected(this)) {
            fromDateCalender = Calendar.getInstance();
            toDateCalender = Calendar.getInstance();

            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            currentDateCal.set(Calendar.YEAR, mYear);
            currentDateCal.set(Calendar.MONTH, mMonth);
            currentDateCal.set(Calendar.DAY_OF_MONTH, mDay);

            currentDate = currentDateCal.getTime();
            linearLayout.setVisibility(View.VISIBLE);
            mDealsList = new ArrayList<>();
            mFilteredList = new ArrayList<>();

            if (app.getFromPrefs(BMHConstants.USERID_KEY).isEmpty()) {
                Intent i = new Intent(this, LoginActivity.class);
                i.putExtra(LoginActivity.LOGIN_INTENT, false);// set false if login screen open from intent.
                startActivityForResult(i, LOGIN_REQ_CODE_FOR_MY_DEALS);

            } else {
                getMyDeals();
                myDealsListAdapter = new MyDealsListAdapter(this, null);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 0));

                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(myDealsListAdapter);

                // row click listener
                recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                        fileStringUrl = UrlFactory.IMG_BASEURL + mFilteredList.get(position).getDownload_path();
                        file_name = mFilteredList.get(position).getFile_name();
                        if (!isFileAlreadyDownloaded(file_name, fileStringUrl)) {
                            if (Build.VERSION.SDK_INT >= 23) {
                                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        == PackageManager.PERMISSION_GRANTED) {
                                    new DownloadingAsync(MyDealsActivity.this, file_name, fileStringUrl, directory).execute();
                                } else {
                                    ActivityCompat.requestPermissions(MyDealsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                                }
                            } else {
                                new DownloadingAsync(MyDealsActivity.this, file_name, fileStringUrl, directory).execute();
                            }
                        }
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
            }
        } else {
            linearLayout.setVisibility(View.GONE);
            recyclerView.setBackgroundResource(R.drawable.no_internet);
        }
    }

    boolean isFileAlreadyDownloaded(String mFile, String urlString) {
        if (mFile != null) {
            directory = Utils.createFileDirectory(this);
            savingPath = directory + File.separator + mFile;
            File file = new File(savingPath);
            if (file.exists()) {
                Utils.openAttachment(this, urlString, file);
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void initViews() {
        toolbar = setToolBar();
        setSupportActionBar(toolbar);
        currentDateCal = Calendar.getInstance();

        recyclerView = findViewById(R.id.recycler_view_deals);
        rl_from_date = findViewById(R.id.rl_from_date);
        rl_to_date = findViewById(R.id.rl_to_date);
        tv_from_date = findViewById(R.id.tv_from_date);
        tv_from_date_value = findViewById(R.id.tv_from_date_value);
        tv_to_date = findViewById(R.id.tv_to_date);
        tv_to_date_value = findViewById(R.id.tv_to_date_value);

        tv_from_date.setTypeface(typeface);
        tv_from_date_value.setTypeface(typeface);
        tv_to_date.setTypeface(typeface);
        tv_to_date_value.setTypeface(typeface);

        tv_to_date_value.setText(Utils.getFormattedToDate());
        tv_from_date_value.setText(Utils.getFormattedFromDate(-6));
        linearLayout = findViewById(R.id.ll_filter_root);
    }


    public void setListeners() {
        //  lv_my_transactions.setOnItemClickListener(mOnItemClickListener);
        rl_from_date.setOnClickListener(mOnClickListener);
        rl_to_date.setOnClickListener(mOnClickListener);


    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_from_date:
                    String fromDate = tv_from_date_value.getText().toString();
                    if (!TextUtils.isEmpty(fromDate)) {
                        try {
                            Calendar cal = Utils.getDateFromTextView(formatter, fromDate);
                            mYear = cal.get(Calendar.YEAR);
                            mMonth = cal.get(Calendar.MONTH);
                            mDay = cal.get(Calendar.DAY_OF_MONTH);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    DatePickerDialog fromDateDialog = new DatePickerDialog(MyDealsActivity.this, fromDateChangeListener, mYear, mMonth, mDay);
                    fromDateDialog.show();
                    break;
                case R.id.rl_to_date:
                    String toDate = tv_to_date_value.getText().toString();
                    if (!TextUtils.isEmpty(toDate)) {
                        try {
                            Calendar cal = Utils.getDateFromTextView(formatter, toDate);
                            mYear = cal.get(Calendar.YEAR);
                            mMonth = cal.get(Calendar.MONTH);
                            mDay = cal.get(Calendar.DAY_OF_MONTH);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    DatePickerDialog toDateDialog = new DatePickerDialog(MyDealsActivity.this, toDateChangeListener, mYear, mMonth, mDay);
                    toDateDialog.show();
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_menu, menu);
        MenuItem item = menu.findItem(R.id.action_reset);
        if (!Connectivity.isConnected(this)) {
            item.setVisible(false);
        } else {
            item.setVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_reset:
                resetFilters();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isValidData() {
        return false;
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    private void getMyDeals() {
        ReqRespBean mBean = new ReqRespBean();
        mBean.setApiType(APIType.MY_DEALS);
        mBean.setRequestmethod(WEBAPI.POST);
        mBean.setUrl(WEBAPI.getWEBAPI(APIType.MY_DEALS));
        mBean.setMimeType(WEBAPI.contentTypeFormData);
        StringBuilder mStringBuilder = new StringBuilder("");
        mStringBuilder.append(ParamsConstants.BROKER_CODE);
        mStringBuilder.append("=");
        mStringBuilder.append(app.getFromPrefs(BMHConstants.BROKER_CODE));
        mStringBuilder.append("&builder_id=" + BMHConstants.CURRENT_BUILDER_ID);

        mStringBuilder.append("&from_date=" + mFromDate);
        mStringBuilder.append("&to_date=" + mToDate);

        mBean.setJson(mStringBuilder.toString());
        mBean.setmHandler(mHandler);
        mBean.setCtx(this);
        mAsyncThread = new AsyncThread();
        mAsyncThread.initProgressDialog(MyDealsActivity.this, mOnCancelListener);
        mAsyncThread.execute(mBean);
        mAsyncThread = null;
        //    mLepLogger.info(TAG, "sendLoginRequest():URl:" + mBean.getUrl() + ",Req Json:" + mBean.getJson());
    }


    DialogInterface.OnCancelListener mOnCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            if (mAsyncThread != null) mAsyncThread.cancel(true);
            mAsyncThread = null;
        }
    };


    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.obj == null) {
                return false;
            } else {
                ReqRespBean mBean = (ReqRespBean) msg.obj;
                switch (mBean.getApiType()) {

                    case MY_DEALS:

                        InventoryRespModel inventoryRespModel = (InventoryRespModel) JsonParser.convertJsonToBean(APIType.MY_DEALS, mBean.getJson());
                        if (inventoryRespModel != null) {

                            mToDate = inventoryRespModel.getTodate();
                            mFromDate = inventoryRespModel.getFromdate();
                            tv_to_date_value.setText(mToDate);
                            tv_from_date_value.setText(mFromDate);

                            mDealsList.clear();
                            if (inventoryRespModel.isSuccess() && inventoryRespModel.getData() != null) {
                                mDealsList = inventoryRespModel.getData();
                            } else if (!inventoryRespModel.isSuccess()) {
                                showToast(inventoryRespModel.getMessage());
                            }
                            if (mFilteredList.size() > 0)
                                mFilteredList.clear();

                            for (InventoryRespModel.Data obj : mDealsList) {
                                if (obj.getCategory().equalsIgnoreCase(getString(R.string.category_deals)))
                                    mFilteredList.add(obj);


                            }
                            if (mFilteredList.size() > 0) {
                                myDealsListAdapter = new MyDealsListAdapter(MyDealsActivity.this, mFilteredList);
                                recyclerView.setAdapter(myDealsListAdapter);
                                recyclerView.setBackgroundResource(R.drawable.blank_screen); // to resolve the no data available screen in the background.

                            }

                                else if (mFilteredList.size()==0) {
                                recyclerView.setBackgroundResource(R.drawable.no_deals_available);
                            }
                        } else {
                            showToast(getString(R.string.something_went_wrong));

                        }

                        break;
                    default:

                        break;
                }
            }
            return true;
        }
    });


   /* AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (view.getTag(R.integer.transaction_obj) != null && view.getTag(R.integer.transaction_obj) instanceof MyTransactionsRespModel.Data) {
                MyTransactionsRespModel.Data mData = (MyTransactionsRespModel.Data) view.getTag(R.integer.transaction_obj);
                IntentDataObject mIntentDataObject = new IntentDataObject();
                if (mData.getCustomer_Name() != null && !mData.getCustomer_Name().isEmpty())
                    mIntentDataObject.putData("CustomerInfoEntity Name", ": " + mData.getCustomer_Name());
                if (mData.getCustomer_Mobile_no() != null && !mData.getCustomer_Mobile_no().isEmpty())
                    mIntentDataObject.putData("CustomerInfoEntity Mobile No.", ": " + mData.getCustomer_Mobile_no());
                if (mData.getOrder_no() != null && !mData.getOrder_no().isEmpty())
                    mIntentDataObject.putData("Order Id", ": " + mData.getOrder_no());
                if (mData.getTransaction_no() != null && !mData.getTransaction_no().isEmpty())
                    mIntentDataObject.putData("Transaction No.", ": " + mData.getTransaction_no());
                if (mData.getUnit_no() != null && !mData.getUnit_no().isEmpty())
                    mIntentDataObject.putData("Unit No.", ": " + mData.getUnit_no());
                if (mData.getProject_name() != null && !mData.getProject_name().isEmpty())
                    mIntentDataObject.putData("Project Name", ": " + mData.getProject_name());
                if (mData.getSize() != null && !mData.getSize().isEmpty())
                    mIntentDataObject.putData("Unit Size", ": " + mData.getSize());
                if (mData.getDisplay_name() != null && !mData.getDisplay_name().isEmpty())
                    mIntentDataObject.putData("Unit Display Name", ": " + mData.getDisplay_name());
                if (mData.getTransaction_datetime() != null && !mData.getTransaction_datetime().isEmpty())
                    mIntentDataObject.putData("Date and Time", ": " + mData.getTransaction_datetime());
                if (mData.getTransaction_amount() != null && !mData.getTransaction_amount().isEmpty())
                    mIntentDataObject.putData("Transaction Amount", ": " + mData.getTransaction_amount());
                if (mData.getCoapplicant() != null && !mData.getCoapplicant().isEmpty())
                    mIntentDataObject.putData("Co Applicant Name", ": " + mData.getCoapplicant());
                if (mData.getPayment_plan() != null && !mData.getPayment_plan().isEmpty())
                    mIntentDataObject.putData("Payment Plan", ": " + mData.getPayment_plan());
                if (mData.getCoupen_code() != null && !mData.getCoupen_code().isEmpty())
                    mIntentDataObject.putData("Coupon Code", ": " + mData.getCoupen_code());
                Intent mIntent = new Intent(MyDealsActivity.this, MyTranscationDetailsActivity.class);
                mIntent.putExtra(ParamsConstants.TITLE, "Order Id (" + mData.getOrder_no() + ")");
                mIntent.putExtra(IntentDataObject.OBJ, mIntentDataObject);
                startActivity(mIntent);
            }
        }
    };*/

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQ_CODE_FOR_MY_DEALS && resultCode == Activity.RESULT_OK) {
            if (!app.getFromPrefs(BMHConstants.USERID_KEY).isEmpty()) {
                getMyDeals();
            }
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void resetFilters() {
        mToDate = "";
        mFromDate = "";
        getMyDeals();

        recyclerView.setBackgroundResource(R.drawable.blank_screen);
    }

    DatePickerDialog.OnDateSetListener fromDateChangeListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            SimpleDateFormat sdformat = new SimpleDateFormat(dateFormat, Locale.getDefault());
            Date dateTo = null;
            fromDateCalender.set(Calendar.YEAR, year);
            fromDateCalender.set(Calendar.MONTH, month);
            fromDateCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String dateFrom = null;
            String toDate = null;
            toDateCalender.set(Calendar.YEAR, year);
            toDateCalender.set(Calendar.MONTH, month);
            toDateCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            try {
                dateTo = formatter.parse(tv_to_date_value.getText().toString());
                dateFrom = sdformat.format(toDateCalender.getTime());
                toDate = sdformat.format(dateTo);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            assert dateFrom != null;
            if (fromDateCalender.getTime().before(dateTo) || dateFrom.equalsIgnoreCase(toDate)) {
                CharSequence charSequence = sdformat.format(fromDateCalender.getTime());
                tv_from_date_value.setText(charSequence);
                mFromDate = charSequence.toString();
                getMyDeals();
            } else {
                Toast.makeText(getApplicationContext(), R.string.from_date_toast_message, Toast.LENGTH_SHORT).show();
            }
        }
    };

    DatePickerDialog.OnDateSetListener toDateChangeListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            SimpleDateFormat sdformat = new SimpleDateFormat(dateFormat, Locale.US);
            Date dateFrom = null;
            toDateCalender.set(Calendar.YEAR, year);
            toDateCalender.set(Calendar.MONTH, month);
            toDateCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            try {
                dateFrom = formatter.parse(tv_from_date_value.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (dateFrom.before(toDateCalender.getTime())) {
                CharSequence charSequence = sdformat.format(toDateCalender.getTime());
                tv_to_date_value.setText(charSequence);
                mToDate = charSequence.toString();
                getMyDeals();
            } else {
                Toast.makeText(getApplicationContext(), R.string.to_date_toast_message, Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public void isAttachmentDownloaded(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        myDealsListAdapter.notifyDataSetChanged();
    }
}
