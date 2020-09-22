package com.sp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.AppEnums.GalleryType;
import com.AppEnums.MediaType;
import com.adapters.GalleryAdapter;
import com.fragments.GalleryFragment;
import com.google.android.youtube.player.YouTubeIntents;
import com.helper.BMHConstants;
import com.helper.IntentDataObject;
import com.helper.ParamsConstants;
import com.helper.UrlFactory;
import com.interfaces.FragmentClickListener;
import com.model.GalleryRespData;
import com.squareup.picasso.Picasso;
import com.utils.Utils;

import java.util.ArrayList;

public class GalleryView extends BaseFragmentActivity implements FragmentClickListener {

    private TabLayout tab_layout = null;
    private ArrayList<GalleryRespData.Data> mGalleryData = null;
    private GalleryAdapter mGalleryAdapter = null;
    private ViewPager pager_full_img;
    private TextView tv_count;
    private Toolbar toolbar = null;
    private ArrayList<String> tabs;
    private ArrayList<GalleryRespData.Data> currentTabDataList = null;
    private LinearLayout ll_thumbnail_container = null;
    private HorizontalScrollView hs_thumbnail;
    private ScrollView scrv_thumbnail;
    private static int curTabPos = 0;
    private static int curSelectedItemPos = 0;
    private MediaType CUR_MEDIA_TYPE = MediaType.PHOTO;
    private GalleryType CUR_GALLERY_TYPE = GalleryType.IMAGE;
    private String title, subTitle = "";
    private int orientation;

    @Override
    protected String setActionBarTitle() {
        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getString(R.string.gallery);
        if (getIntent() != null && getIntent().getSerializableExtra(IntentDataObject.OBJ) != null && getIntent().getSerializableExtra(IntentDataObject.OBJ) instanceof IntentDataObject) {
            IntentDataObject obj = (IntentDataObject) getIntent().getSerializableExtra(IntentDataObject.OBJ);
            if (obj.getObj() != null) {
                mGalleryData = (ArrayList<GalleryRespData.Data>) obj.getObj();
                if (obj.getData() != null && obj.getData().get(ParamsConstants.TITLE) != null) {
                    title = obj.getData().get(ParamsConstants.TITLE);
                }
                if (obj.getData() != null && obj.getData().get(ParamsConstants.SUB_TITLE) != null) {
                    subTitle = obj.getData().get(ParamsConstants.SUB_TITLE);
                }
            }
        }

        orientation = getScreenOrientation();
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.gallery_view);
            initViewsForLandscape();
            setListenersForLandscape();
            createGalleryTabsForLandscape();
            defaultStateForLandscape();
            toolbar.setTitle(title);
            toolbar.setSubtitle(subTitle);

        } else {
            setContentView(R.layout.gallery_view);
            initViewsForPortrait();
            setListenersForPortrait();
            createGalleryTabsForPortrait();
            defaultStateForPortrait();
            toolbar.setTitle(title);
            toolbar.setSubtitle(subTitle);
        }
    }

    public int getScreenOrientation() {
        return getResources().getConfiguration().orientation;

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

      /*  if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }*/
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("CURRENT_TAB_POS", curTabPos);

        //TODO:
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        curTabPos = savedInstanceState.getInt("CURRENT_TAB_POS");
        if (tab_layout.getTabCount() > 0 && tab_layout.getTabAt(curTabPos) != null) {
            tab_layout.getTabAt(curTabPos).select();
        }
    }

    private void defaultStateForPortrait() {
        if (tabs != null && tabs.size() > 0) {
            CUR_GALLERY_TYPE = GalleryType.getEnum(tabs.get(0));
            updateUiForPortrait(CUR_GALLERY_TYPE);
            setGalleryCount(CUR_GALLERY_TYPE, CUR_MEDIA_TYPE, curSelectedItemPos);
        }
    }

    private void defaultStateForLandscape() {
        if (tabs != null && tabs.size() > 0) {
            CUR_GALLERY_TYPE = GalleryType.getEnum(tabs.get(0));
            updateUiForLandscape(CUR_GALLERY_TYPE);
            setGalleryCount(CUR_GALLERY_TYPE, CUR_MEDIA_TYPE, curSelectedItemPos);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initViewsForPortrait() {
        //multiViewPager = (MultiViewPager) findViewById(R.id.pager_thumbnail);
        pager_full_img = findViewById(R.id.pager_full_img);
        tab_layout = findViewById(R.id.tab_layout);
        tab_layout.setSelectedTabIndicatorColor(getResources().getColor(R.color.blue_color));
        tv_count = findViewById(R.id.tv_count);
        toolbar = setToolBar();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(getString(R.string.select_project));
        hs_thumbnail = findViewById(R.id.hs_thumbnail);
        ll_thumbnail_container = findViewById(R.id.ll_thumbnail_container);
    }

    private void initViewsForLandscape() {
        //multiViewPager = (MultiViewPager) findViewById(R.id.pager_thumbnail);
        pager_full_img = findViewById(R.id.pager_full_img);
        tab_layout = findViewById(R.id.tab_layout);
        tab_layout.setSelectedTabIndicatorColor(getResources().getColor(R.color.blue_color));
        tv_count = findViewById(R.id.tv_count);
        toolbar = setToolBar();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(getString(R.string.select_project));
        scrv_thumbnail = findViewById(R.id.scrv_thumbnail);
        ll_thumbnail_container = findViewById(R.id.ll_thumbnail_container);
    }

    private void setListenersForPortrait() {
        //multiViewPager.addOnPageChangeListener(multiViewPagerOnPageChangeListener);
        pager_full_img.addOnPageChangeListener(imgPageChangeForPortrait);
        tab_layout.setOnTabSelectedListener(mOnTabSelectedListenerForPortrait);
    }

    private void setListenersForLandscape() {
        //multiViewPager.addOnPageChangeListener(multiViewPagerOnPageChangeListener);
        pager_full_img.addOnPageChangeListener(imgPageChangeForLandscape);
        tab_layout.setOnTabSelectedListener(mOnTabSelectedListenerForLandscape);
    }

    private void createGalleryTabsForPortrait() {
        if (mGalleryData != null) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(this);
            tabs = new ArrayList<>();
            for (GalleryRespData.Data data : mGalleryData) {
                if (!tabs.contains(data.getGallary_type())) {
                    tabs.add(data.getGallary_type());
                    View mView = mLayoutInflater.inflate(R.layout.gallery_tab_view, null);
                    TextView tv_title = mView.findViewById(R.id.tv_title);
                    ImageView iv_icon = mView.findViewById(R.id.iv_icon);

                    if (GalleryType.IMAGE == GalleryType.getEnum(data.getGallary_type())) {
                        CUR_GALLERY_TYPE = GalleryType.getEnum(data.getGallary_type());
                        tv_title.setText("Image");
                        iv_icon.setBackgroundResource(R.drawable.gallery_photo_tab_state);
                        tab_layout.addTab(tab_layout.newTab().setCustomView(mView).setTag(GalleryType.IMAGE), true);
                    } else if (GalleryType.VIDEO == GalleryType.getEnum(data.getGallary_type())) {
                        CUR_GALLERY_TYPE = GalleryType.getEnum(data.getGallary_type());
                        tv_title.setText("Video");
                        iv_icon.setBackgroundResource(R.drawable.gallery_video_tab_state);
                        tab_layout.addTab(tab_layout.newTab().setCustomView(mView).setTag(GalleryType.VIDEO));
                    } else if (GalleryType.CONSTRUCTION_UPDATE == GalleryType.getEnum(data.getGallary_type())) {
                        CUR_GALLERY_TYPE = GalleryType.getEnum(data.getGallary_type());
                        tv_title.setText("Updates");
                        iv_icon.setBackgroundResource(R.drawable.gallery_construction_tab_state);
                        tab_layout.addTab(tab_layout.newTab().setCustomView(mView).setTag(GalleryType.CONSTRUCTION_UPDATE));
                    } else if (GalleryType.VIRTUAL_SITE_VISIT == GalleryType.getEnum(data.getGallary_type())) {
                        CUR_GALLERY_TYPE = GalleryType.getEnum(data.getGallary_type());
                        tv_title.setText("Virtual Site");
                        iv_icon.setBackgroundResource(R.drawable.gallery_visualsite_tab_state);
                        tab_layout.addTab(tab_layout.newTab().setCustomView(mView).setTag(GalleryType.VIRTUAL_SITE_VISIT));
                    } else if (GalleryType.THREE_SIXTY_DEGREE_TOUR == GalleryType.getEnum(data.getGallary_type())) {
                        CUR_GALLERY_TYPE = GalleryType.getEnum(data.getGallary_type());
                        tv_title.setText("360 Degree Tour");
                        iv_icon.setBackgroundResource(R.drawable.gallery_threesixty_tab_state);
                        tab_layout.addTab(tab_layout.newTab().setCustomView(mView).setTag(GalleryType.THREE_SIXTY_DEGREE_TOUR));
                    }
                }
            }
        }
    }

    private void createGalleryTabsForLandscape() {
        if (mGalleryData != null) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(this);
            tabs = new ArrayList<>();
            for (GalleryRespData.Data data : mGalleryData) {
                if (!tabs.contains(data.getGallary_type())) {
                    tabs.add(data.getGallary_type());
                    View mView = mLayoutInflater.inflate(R.layout.gallery_tab_view, null);
                    TextView tv_title = (TextView) mView.findViewById(R.id.tv_title);
                    ImageView iv_icon = (ImageView) mView.findViewById(R.id.iv_icon);

                    if (GalleryType.IMAGE == GalleryType.getEnum(data.getGallary_type())) {
                        CUR_GALLERY_TYPE = GalleryType.getEnum(data.getGallary_type());
                        tv_title.setText("Image");
                        iv_icon.setBackgroundResource(R.drawable.gallery_photo_tab_state);
                        tab_layout.addTab(tab_layout.newTab().setCustomView(mView).setTag(GalleryType.IMAGE), true);
                    } else if (GalleryType.VIDEO == GalleryType.getEnum(data.getGallary_type())) {
                        CUR_GALLERY_TYPE = GalleryType.getEnum(data.getGallary_type());
                        tv_title.setText("Video");
                        iv_icon.setBackgroundResource(R.drawable.gallery_video_tab_state);
                        tab_layout.addTab(tab_layout.newTab().setCustomView(mView).setTag(GalleryType.VIDEO));
                    } else if (GalleryType.CONSTRUCTION_UPDATE == GalleryType.getEnum(data.getGallary_type())) {
                        CUR_GALLERY_TYPE = GalleryType.getEnum(data.getGallary_type());
                        tv_title.setText("Updates");
                        iv_icon.setBackgroundResource(R.drawable.gallery_construction_tab_state);
                        tab_layout.addTab(tab_layout.newTab().setCustomView(mView).setTag(GalleryType.CONSTRUCTION_UPDATE));
                    } else if (GalleryType.VIRTUAL_SITE_VISIT == GalleryType.getEnum(data.getGallary_type())) {
                        CUR_GALLERY_TYPE = GalleryType.getEnum(data.getGallary_type());
                        tv_title.setText("Virtual Site");
                        iv_icon.setBackgroundResource(R.drawable.gallery_visualsite_tab_state);
                        tab_layout.addTab(tab_layout.newTab().setCustomView(mView).setTag(GalleryType.VIRTUAL_SITE_VISIT));
                    } else if (GalleryType.THREE_SIXTY_DEGREE_TOUR == GalleryType.getEnum(data.getGallary_type())) {
                        CUR_GALLERY_TYPE = GalleryType.getEnum(data.getGallary_type());
                        tv_title.setText("360 Degree Tour");
                        iv_icon.setBackgroundResource(R.drawable.gallery_threesixty_tab_state);
                        tab_layout.addTab(tab_layout.newTab().setCustomView(mView).setTag(GalleryType.THREE_SIXTY_DEGREE_TOUR));
                    }
                }
            }
        }
    }

    private ArrayList<GalleryRespData.Data> filterTabBasedData(GalleryType mediaType) {
        if (mGalleryData == null) return null;
        ArrayList<GalleryRespData.Data> dataList = new ArrayList<>();
        for (GalleryRespData.Data data : mGalleryData) {
            if (mediaType == GalleryType.getEnum(data.getGallary_type())) {
                dataList.add(data);
            }
        }
        return dataList;
    }

    @Override
    public void myOnClickListener(Fragment fragment, Object obj) {
        if (fragment instanceof GalleryFragment && obj instanceof GalleryRespData.Data) {
            GalleryRespData.Data data = (GalleryRespData.Data) obj;
            if (MediaType.VIDEO == MediaType.getEnum(data.getType())) {
                if (data.getUrl() == null || data.getUrl().isEmpty()) {
                    showToast("Not a valid Youtube URL");
                } else {
                    Intent intent = YouTubeIntents.createPlayVideoIntentWithOptions(this, data.getUrl(), true, false);
                    startActivity(intent);
                }
            } else if (MediaType.PHOTO == MediaType.getEnum(data.getType())) {
                if (data.getUrl() == null || data.getUrl().isEmpty()) {
                    showToast("Image not available");
                }
                //showToast(data.getUrl());
            }
        }
    }


    ViewPager.OnPageChangeListener imgPageChangeForPortrait = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            curSelectedItemPos = position;
            setGalleryCount(CUR_GALLERY_TYPE, CUR_MEDIA_TYPE, curSelectedItemPos);
            final int count = ((LinearLayout) hs_thumbnail.getChildAt(0)).getChildCount();
            for (int i = 0; i < count; i++) {
                final View child = ((LinearLayout) hs_thumbnail.getChildAt(0)).getChildAt(i);
                ImageView iv = (ImageView) child.findViewById(R.id.iv_thumbnail);
                iv.setPadding(0, 0, 0, 0);
            }
            scrollToForPortrait();
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };
    ViewPager.OnPageChangeListener imgPageChangeForLandscape = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            curSelectedItemPos = position;
            setGalleryCount(CUR_GALLERY_TYPE, CUR_MEDIA_TYPE, curSelectedItemPos);
            final int count = ((LinearLayout) scrv_thumbnail.getChildAt(0)).getChildCount();
            for (int i = 0; i < count; i++) {
                final View child = ((LinearLayout) scrv_thumbnail.getChildAt(0)).getChildAt(i);
                ImageView iv = (ImageView) child.findViewById(R.id.iv_thumbnail);
                iv.setPadding(0, 0, 0, 0);
            }
            scrollToForLandscape();
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    private void scrollToForLandscape() {
        if (scrv_thumbnail != null) {
            final View child = ((LinearLayout) scrv_thumbnail.getChildAt(0)).getChildAt(curSelectedItemPos);
            if (child != null) {
                ImageView iv = (ImageView) child.findViewById(R.id.iv_thumbnail);
                int margin = (int) Utils.dp2px(3, GalleryView.this);
                iv.setPadding(margin, margin, margin, margin);
                int scrollY = (child.getTop() - (app.getHeight(GalleryView.this) / 2) + (int) Utils.dp2px(63, GalleryView.this)) + (child.getHeight() / 2);
                scrv_thumbnail.smoothScrollTo(0, scrollY);
            }
        }
    }

    private void scrollToForPortrait() {
        if (hs_thumbnail != null) {
            final View child = ((LinearLayout) hs_thumbnail.getChildAt(0)).getChildAt(curSelectedItemPos);
            if (child != null) {
                ImageView iv = (ImageView) child.findViewById(R.id.iv_thumbnail);
                int margin = (int) Utils.dp2px(3, GalleryView.this);
                iv.setPadding(margin, margin, margin, margin);
                int scrollX = (child.getLeft() - (app.getWidth(GalleryView.this) / 2)) + (child.getWidth() / 2);
                hs_thumbnail.smoothScrollTo(scrollX, 0);
            }
        }
    }

    private void setGalleryCount(GalleryType CUR_GALLERY_TYPE, MediaType mediaType, int curPos) {
        String mediaTypeStr = "Image ";
        if (CUR_GALLERY_TYPE == GalleryType.CONSTRUCTION_UPDATE) {
            mediaTypeStr = "Item ";
        } else {
            if (mediaType == MediaType.PHOTO) {
                mediaTypeStr = "Image ";
            } else if (mediaType == MediaType.VIDEO) {
                mediaTypeStr = "Video ";
            } else if (mediaType == MediaType.PDF) {
                mediaTypeStr = "Pdf ";
            }
        }
        if (currentTabDataList != null) {
            tv_count.setText(mediaTypeStr + (curPos + 1) + " of " + currentTabDataList.size());
        }
    }

    TabLayout.OnTabSelectedListener mOnTabSelectedListenerForPortrait = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            if (tab.getTag() instanceof GalleryType) {
                GalleryType type = (GalleryType) tab.getTag();
                curTabPos = tab.getPosition();
                updateUiForPortrait(type);
                CUR_GALLERY_TYPE = type;
                setGalleryCount(CUR_GALLERY_TYPE, CUR_MEDIA_TYPE, curSelectedItemPos);
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
    };
    TabLayout.OnTabSelectedListener mOnTabSelectedListenerForLandscape = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            if (tab.getTag() instanceof GalleryType) {
                GalleryType type = (GalleryType) tab.getTag();
                curTabPos = tab.getPosition();
                updateUiForLandscape(type);
                CUR_GALLERY_TYPE = type;
                setGalleryCount(CUR_GALLERY_TYPE, CUR_MEDIA_TYPE, curSelectedItemPos);
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
    };

    private void updateUiForPortrait(GalleryType type) {
        ll_thumbnail_container.removeAllViews();
        currentTabDataList = filterTabBasedData(type);
        final int margin = (int) Utils.dp2px(3, GalleryView.this);
        for (GalleryRespData.Data data : currentTabDataList) {
            final View view = LayoutInflater.from(this).inflate(R.layout.gallery_thumbnail, null);
            ImageView iv_thumbnail = (ImageView) view.findViewById(R.id.iv_thumbnail);
            ImageView iv_youtube = (ImageView) view.findViewById(R.id.iv_youtube);

            if (data.getType().equalsIgnoreCase("photo")) {
                CUR_MEDIA_TYPE = MediaType.PHOTO;
                iv_youtube.setVisibility(View.GONE);
                String separator = UrlFactory.IMG_BASEURL.endsWith("/") ? "" : "/";
                String imgurl = data.getUrl().startsWith(UrlFactory.IMG_BASEURL) ? data.getUrl() : UrlFactory.IMG_BASEURL + separator + data.getUrl();
                if (data.getUrl() != null && !data.getUrl().isEmpty()) {
                    Picasso.with(GalleryView.this).load(UrlFactory.getShortImageByWidthUrl(app.getWidth(GalleryView.this), imgurl)).placeholder(BMHConstants.PLACE_HOLDER).error(BMHConstants.PLACE_HOLDER).into(iv_thumbnail);
                }
            } else if (data.getType().equalsIgnoreCase("Video")) {
                CUR_MEDIA_TYPE = MediaType.VIDEO;
                iv_youtube.setVisibility(View.VISIBLE);
                iv_thumbnail.setImageResource(BMHConstants.PLACE_HOLDER);
                if (data.getUrl() != null && !data.getUrl().isEmpty()) {
                    Picasso.with(GalleryView.this).load(Utils.getYoutubeThumbnailUrl(data.getUrl())).placeholder(BMHConstants.PLACE_HOLDER).error(BMHConstants.PLACE_HOLDER).resize((int) Utils.dp2px(150, GalleryView.this), (int) Utils.dp2px(100, GalleryView.this)).into(iv_thumbnail);
                }
            }
            if (currentTabDataList.indexOf(data) == curSelectedItemPos) {
                iv_thumbnail.setPadding(margin, margin, margin, margin);
            } else {
                iv_thumbnail.setPadding(0, 0, 0, 0);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int scrollX = (view.getLeft() - (app.getWidth(GalleryView.this) / 2)) + (view.getWidth() / 2);
                    hs_thumbnail.smoothScrollTo(scrollX, 0);
                    final int count = ((LinearLayout) hs_thumbnail.getChildAt(0)).getChildCount();
                    for (int i = 0; i < count; i++) {
                        final View child = ((LinearLayout) hs_thumbnail.getChildAt(0)).getChildAt(i);
                        ImageView iv = (ImageView) child.findViewById(R.id.iv_thumbnail);
                        iv.setPadding(0, 0, 0, 0);
                    }
                    ImageView iv = (ImageView) v.findViewById(R.id.iv_thumbnail);
                    iv.setPadding(margin, margin, margin, margin);
                    curSelectedItemPos = ((LinearLayout) hs_thumbnail.getChildAt(0)).indexOfChild(v);
                    pager_full_img.setCurrentItem(curSelectedItemPos);
                    setGalleryCount(CUR_GALLERY_TYPE, CUR_MEDIA_TYPE, curSelectedItemPos);
                }
            });
            ll_thumbnail_container.addView(view);
        }// end of loop
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollToForPortrait();
            }
        }, 100);
        if (mGalleryAdapter == null) {
            mGalleryAdapter = new GalleryAdapter(getSupportFragmentManager(), GalleryView.this, currentTabDataList);
            pager_full_img.setAdapter(mGalleryAdapter);
            pager_full_img.setCurrentItem(0);
        } else {
            mGalleryAdapter.mGalleryRespData = currentTabDataList;
            mGalleryAdapter.notifyDataSetChanged();

        }

    }

    private void updateUiForLandscape(GalleryType type) {
        ll_thumbnail_container.removeAllViews();
        currentTabDataList = filterTabBasedData(type);
        final int margin = (int) Utils.dp2px(3, GalleryView.this);
        for (GalleryRespData.Data data : currentTabDataList) {
            final View view = LayoutInflater.from(this).inflate(R.layout.gallery_thumbnail, null);
            ImageView iv_thumbnail = (ImageView) view.findViewById(R.id.iv_thumbnail);
            ImageView iv_youtube = (ImageView) view.findViewById(R.id.iv_youtube);

            if (data.getType().equalsIgnoreCase("photo")) {
                CUR_MEDIA_TYPE = MediaType.PHOTO;
                iv_youtube.setVisibility(View.GONE);
                String separator = UrlFactory.IMG_BASEURL.endsWith("/") ? "" : "/";
                String imgurl = data.getUrl().startsWith(UrlFactory.IMG_BASEURL) ? data.getUrl() : UrlFactory.IMG_BASEURL + separator + data.getUrl();
                if (data.getUrl() != null && !data.getUrl().isEmpty()) {
                    Picasso.with(GalleryView.this).load(UrlFactory.getShortImageByWidthUrl(app.getWidth(GalleryView.this), imgurl)).placeholder(BMHConstants.PLACE_HOLDER).error(BMHConstants.PLACE_HOLDER).into(iv_thumbnail);
                }
            } else if (data.getType().equalsIgnoreCase("Video")) {
                CUR_MEDIA_TYPE = MediaType.VIDEO;
                iv_youtube.setVisibility(View.VISIBLE);
                iv_thumbnail.setImageResource(BMHConstants.PLACE_HOLDER);
                if (data.getUrl() != null && !data.getUrl().isEmpty()) {
                    Picasso.with(GalleryView.this).load(Utils.getYoutubeThumbnailUrl(data.getUrl())).placeholder(BMHConstants.PLACE_HOLDER).error(BMHConstants.PLACE_HOLDER).resize((int) Utils.dp2px(150, GalleryView.this), (int) Utils.dp2px(100, GalleryView.this)).into(iv_thumbnail);
                }
            }
            if (currentTabDataList.indexOf(data) == curSelectedItemPos) {
                iv_thumbnail.setPadding(margin, margin, margin, margin);
            } else {
                iv_thumbnail.setPadding(0, 0, 0, 0);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int scrollY = (view.getTop() - (app.getHeight(GalleryView.this) / 2) + (int) Utils.dp2px(63, GalleryView.this)) + (view.getHeight() / 2);
                    scrv_thumbnail.smoothScrollTo(0, scrollY);
                    final int count = ((LinearLayout) scrv_thumbnail.getChildAt(0)).getChildCount();
                    for (int i = 0; i < count; i++) {
                        final View child = ((LinearLayout) scrv_thumbnail.getChildAt(0)).getChildAt(i);
                        ImageView iv = (ImageView) child.findViewById(R.id.iv_thumbnail);
                        iv.setPadding(0, 0, 0, 0);
                    }
                    ImageView iv = (ImageView) v.findViewById(R.id.iv_thumbnail);
                    iv.setPadding(margin, margin, margin, margin);
                    curSelectedItemPos = ((LinearLayout) scrv_thumbnail.getChildAt(0)).indexOfChild(v);
                    pager_full_img.setCurrentItem(curSelectedItemPos);
                    setGalleryCount(CUR_GALLERY_TYPE, CUR_MEDIA_TYPE, curSelectedItemPos);
                }
            });
            ll_thumbnail_container.addView(view);
        }// end of loop
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollToForLandscape();
            }
        }, 100);
        if (mGalleryAdapter == null) {
            mGalleryAdapter = new GalleryAdapter(getSupportFragmentManager(), GalleryView.this, currentTabDataList);
            pager_full_img.setAdapter(mGalleryAdapter);
            pager_full_img.setCurrentItem(0);
        } else {
            mGalleryAdapter.mGalleryRespData = currentTabDataList;
            mGalleryAdapter.notifyDataSetChanged();

        }

    }
}
