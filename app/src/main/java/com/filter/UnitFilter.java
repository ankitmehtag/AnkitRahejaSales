package com.filter;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sp.BaseFragmentActivity;
import com.sp.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.helper.IntentDataObject;
import com.helper.ParamsConstants;
import com.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class UnitFilter extends BaseFragmentActivity {

    private String sort_value = "";
    private Button btn_unit_price, btn_unit_floor;
    String filer_type = "";
    private int minFloor = -1;
    private int maxFloor = -1;
    private List<Button> floorButtons = null;
    private LinearLayout buttonContainer = null;
    private GoogleApiClient client;
    private Button apply_unit_filter;
    private Toolbar toolbar;
    private Button popupBtnLToH, popupBtnHToL;
    private Typeface fond;
    private int MAX_FLOOR, SELECTED_MIN_FLOOR, SELECTED_MAX_FLOOR = 0;
    private IntentDataObject mIntentDataObject;

    @Override
    protected String setActionBarTitle() {
        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unit_filter);
        fond = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");
        MAX_FLOOR = getIntent().getExtras().getInt("totalFloors");
        initView();
        setTypeface();
        setListeners();

        if (getIntent() != null && getIntent().getSerializableExtra(IntentDataObject.OBJ) != null
                && getIntent().getSerializableExtra(IntentDataObject.OBJ) instanceof IntentDataObject) {
            mIntentDataObject = (IntentDataObject) getIntent().getSerializableExtra(IntentDataObject.OBJ);
            if (mIntentDataObject.getData().get(ParamsConstants.MINFLOOR) != null) {
                SELECTED_MIN_FLOOR = Utils.toInt(mIntentDataObject.getData().get(ParamsConstants.MINFLOOR));
                if (mIntentDataObject.getData().get(ParamsConstants.MINFLOOR) != null)
                    minFloor = Utils.toInt(mIntentDataObject.getData().get(ParamsConstants.MINFLOOR));
                if (mIntentDataObject.getData().get(ParamsConstants.MAXFLOOR) != null)
                    maxFloor = Utils.toInt(mIntentDataObject.getData().get(ParamsConstants.MAXFLOOR));
                if (floorButtons != null) {
                    for (Button btn : floorButtons) {
                        int minVal = (int) btn.getTag();
                        if (SELECTED_MIN_FLOOR == minVal) {
                            btn.setSelected(true);
                            btn.setBackgroundColor(Color.parseColor("#17a3f2"));
                            //Color should not changed code is above

                        }

                    }

                }
                     }

            if (mIntentDataObject.getData().get(ParamsConstants.MAXFLOOR) != null) {
                SELECTED_MAX_FLOOR = Utils.toInt(mIntentDataObject.getData().get(ParamsConstants.MAXFLOOR));
            }
            if (mIntentDataObject.getData().get(ParamsConstants.PRICE) != null) {
                if (mIntentDataObject.getData().get(ParamsConstants.PRICE).equalsIgnoreCase(ParamsConstants.PRICE_LTOH)) {
                    sort_value = mIntentDataObject.getData().get(ParamsConstants.PRICE);
                    setPriceState(sort_value);
                } else if (mIntentDataObject.getData().get(ParamsConstants.PRICE).equalsIgnoreCase(ParamsConstants.PRICE_HTOL)) {
                    sort_value = mIntentDataObject.getData().get(ParamsConstants.PRICE);
                    setPriceState(sort_value);
                }
            }
        }

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initView() {
        toolbar = setToolBar();
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.filter));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btn_unit_price = (Button) findViewById(R.id.btn_unit_price);
        btn_unit_floor = (Button) findViewById(R.id.btn_unit_floor);
        apply_unit_filter = (Button) findViewById(R.id.apply_unit_filter);

        buttonContainer = (LinearLayout) findViewById(R.id.unit_floor);
        floorButtons = new ArrayList<Button>();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.LEFT;

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        buttonParams.leftMargin = 5;
        buttonParams.rightMargin = 5;
        buttonParams.topMargin = 5;
        buttonParams.bottomMargin = 5;
        buttonParams.gravity = Gravity.NO_GRAVITY;
        int tenLimitRange = MAX_FLOOR;
        if (MAX_FLOOR > 100) {
            tenLimitRange = 100;
        }
        LinearLayout fourButtonLayout = null;
        int buttonCount = 0;
        for (int i = 1; i <= tenLimitRange; i = i + 10) {
            Button floorBtn = new Button(this);
            floorBtn.setTag(i);
            int labelLast = i + 9;
            if (labelLast > MAX_FLOOR) {
                labelLast = MAX_FLOOR;
            }
            floorBtn.setText(i + " - " + (labelLast));
            floorBtn.setTypeface(fond);
            floorBtn.setBackgroundColor(Color.parseColor("#FFFFFF"));
            if (buttonCount % 3 == 0) {
                fourButtonLayout = new LinearLayout(this);
                buttonContainer.addView(fourButtonLayout, params);
            }
            fourButtonLayout.addView(floorBtn, buttonParams);
            floorBtn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    onFloorBtnClicked(v);
                }
            });
            buttonCount++;
            floorButtons.add(floorBtn);
        }
        int start = 1;
        if (MAX_FLOOR > 100) {
            start = 101;

            for (int i = start; i < MAX_FLOOR; i = i + 20) {
                Button floorBtn = new Button(this);
                floorBtn.setTag(i);
                int labelLast = i + 19;
                if (labelLast > MAX_FLOOR) {
                    labelLast = MAX_FLOOR;
                }
                floorBtn.setText(i + " - " + (labelLast));
                floorBtn.setTypeface(fond);
                floorBtn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                if (buttonCount % 3 == 0) {
                    fourButtonLayout = new LinearLayout(this);
                    buttonContainer.addView(fourButtonLayout, params);
                }
                fourButtonLayout.addView(floorBtn, buttonParams);
                buttonCount++;
                floorBtn.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        onFloorBtnClicked(v);
                    }
                });

                floorButtons.add(floorBtn);
            }
        }

    }

    private void setListeners() {
        apply_unit_filter.setOnClickListener(mOnClickListener);
        btn_unit_price.setOnClickListener(mOnClickListener);
        btn_unit_floor.setOnClickListener(mOnClickListener);
    }

    private void setTypeface() {
        TextView tv_sort = (TextView) findViewById(R.id.sort_text);
        TextView tv_filter = (TextView) findViewById(R.id.tv_filter_by_text);
        TextView tv_floors = (TextView) findViewById(R.id.tv_showing_floors);
        tv_sort.setTypeface(fond);
        tv_filter.setTypeface(fond);
        tv_floors.setTypeface(fond);
        btn_unit_price.setTypeface(fond);
        btn_unit_floor.setTypeface(fond);
        apply_unit_filter.setTypeface(fond);
    }

    OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_unit_price:
                    v.setSelected(true);
                    //btn_unit_floor.setSelected(false);
                    showPopupLowHigh();
                    //enableFloorButton(false);
                    break;
                case R.id.btn_unit_floor:
                    //v.setSelected(true);
                    //btn_unit_price.setSelected(false);
                    //enableFloorButton(true);
                    break;
                case R.id.apply_unit_filter:
                    IntentDataObject mIntentDataObject = new IntentDataObject();
                    if (sort_value != null && !sort_value.isEmpty()) {
                        mIntentDataObject.putData(ParamsConstants.PRICE, String.valueOf(sort_value));
                    }
                    if (minFloor > -1 && maxFloor > -1) {
                        mIntentDataObject.putData(ParamsConstants.MINFLOOR, String.valueOf(minFloor));
                        mIntentDataObject.putData(ParamsConstants.MAXFLOOR, String.valueOf(maxFloor));
                    }
                    Intent intent = getIntent();
                    intent.putExtra(IntentDataObject.OBJ, mIntentDataObject);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
            }
        }
    };

    private void onFloorBtnClicked(View view) {
        int selectedIndex = -1;

        for (int i = 0; i < floorButtons.size(); i++) {
            floorButtons.get(i).setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            if (floorButtons.get(i) == (Button) view) {
                selectedIndex = i;
            }
        }
        int range = (selectedIndex % 10);
        minFloor = range * 10 + 1;
        maxFloor = (range + 1) * 10;

        view.setSelected(true);
        view.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_color));
    }

    private void enableFloorButton(Boolean enable) {
        for (int i = 0; i < floorButtons.size(); i++) {
			/*floorButtons.get(i).setEnabled(enable);
			floorButtons.get(i).setClickable(enable);*/
            floorButtons.get(i).setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            //floorButtons.get(i).setSelected(false);
        }
    }


    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("UnitFilter Page")
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (client != null) {
            client.connect();
            AppIndex.AppIndexApi.start(client, getIndexApiAction());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (client != null) {
            AppIndex.AppIndexApi.end(client, getIndexApiAction());
            client.disconnect();
        }
    }

    private void showPopupLowHigh() {
        View layout = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.high_low_layout, null);
        ;
        final PopupWindow pw = new PopupWindow(layout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, false);
        pw.setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        pw.setOutsideTouchable(true);
        final Button btnLtoH = (Button) layout.findViewById(R.id.btnLtoH);
        final Button btnHtoL = (Button) layout.findViewById(R.id.btnHtoL);
        final Button btnCancel = (Button) layout.findViewById(R.id.btnCancel);
        btnLtoH.setTypeface(fond);
        btnHtoL.setTypeface(fond);
        btnCancel.setTypeface(fond);

        btnLtoH.setOnClickListener(new OnClickListener() {
            public void onClick(View vv) {
                sort_value = "price_ltoh";
                btnLtoH.setSelected(true);
                btnHtoL.setSelected(false);
                setPriceState(sort_value);
                pw.dismiss();

            }
        });

        btnHtoL.setOnClickListener(new OnClickListener() {
            public void onClick(View vv) {
                sort_value = "price_htol";
                btnHtoL.setSelected(true);
                btnLtoH.setSelected(false);
                setPriceState(sort_value);
                pw.dismiss();
            }
        });
        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_unit_price.setText(getString(R.string.unit_price));
                sort_value = "";
                btn_unit_price.setSelected(false);
                pw.dismiss();
            }
        });
        pw.setOutsideTouchable(true);
        pw.showAsDropDown(btn_unit_price);
    }

    private void setPriceState(String type) {
        if (type == null) return;
        if (type.equalsIgnoreCase("price_htol")) {
            //btnHtoL.setSelected(true);
            //btnLtoH.setSelected(false);
            btn_unit_price.setText(getString(R.string.high_to_low));
            btn_unit_price.setSelected(true);
        } else if (type.equalsIgnoreCase("price_ltoh")) {
            sort_value = "price_ltoh";
            //btnLtoH.setSelected(true);
            //btnHtoL.setSelected(false);
            btn_unit_price.setText(getString(R.string.low_to_high));
            btn_unit_price.setSelected(true);
        }
    }

    private void resetFilter() {
        btn_unit_price.setSelected(false);
        //btn_unit_floor.setSelected(false);
        btn_unit_price.setText(getString(R.string.unit_price));
        sort_value = "";
        maxFloor = -1;
        minFloor = -1;
        enableFloorButton(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.unit_filter_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_reset:
                resetFilter();
                break;
        }
        return true;
    }


}
