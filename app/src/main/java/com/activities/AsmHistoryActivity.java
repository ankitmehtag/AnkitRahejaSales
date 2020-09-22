package com.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.AppEnums.APIType;
import com.adapters.AsmHistoryAdapter;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.appnetwork.WEBAPI;
import com.fragments.HistoryNotesFragment;
import com.fragments.WorkHistoryFragment;
import com.helper.BMHConstants;
import com.helper.ParamsConstants;
import com.sp.BMHApplication;
import com.sp.R;
import com.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AsmHistoryActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener,
        AsmHistoryAdapter.OnBackPressListener, View.OnClickListener, MediaPlayer.OnCompletionListener {

    private static final String TAG = AsmHistoryActivity.class.getSimpleName();
    private AsmHistoryActivity.SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private ProgressDialog dialog;
    public Bundle mBundle = new Bundle();
    MediaPlayer mediaPlayer;
    SeekBar seekBar;
    boolean wasPlaying = false;
    ImageView imgPlay, imgClear;
    Dialog audioDialog;
    String audioPath;
    boolean currPlaying;
    boolean isSeekBarPositionChanged;
    Uri fileUri;
    int currentPosition, total;
    String strrtr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_transactions);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        Toolbar toolbar = initView();

        String enquiryId = getIntent().getStringExtra(BMHConstants.ENQUIRY_ID);
                strrtr =   getIntent().getStringExtra("TabName");
        // API CALL
        getAsmHistory(enquiryId);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @NonNull
    private Toolbar initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.txt_history));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        return toolbar;
    }


    private void getAsmHistory(final String enquiryId) {

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, WEBAPI.getWEBAPI(APIType.GET_ASM_HISTORY),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }

                        tabLayout = findViewById(R.id.tabs);
                        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

                        if (!response.equalsIgnoreCase("null")) {

                            tabLayout.getTabAt(0).setText(AsmHistoryActivity.this.getResources().getString(R.string.tab_work_history));
                            tabLayout.getTabAt(1).setText(AsmHistoryActivity.this.getResources().getString(R.string.tab_history_notes));
                            mBundle.putString("JSON_STRING", response);
                            if (isNotesAvailable(response) == false) {

                                tabLayout.removeTab(tabLayout.getTabAt(1));
                            }

                            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
                            mViewPager = findViewById(R.id.viewPager);
                            mViewPager.setAdapter(mSectionsPagerAdapter);


                            tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.sky_blue));
                            tabLayout.setSelectedTabIndicatorHeight((int) (3 * getResources().getDisplayMetrics().density));
                            tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#ffffff"));

                            tabLayout.setTabTextColors(
                                    ContextCompat.getColor(AsmHistoryActivity.this, R.color.black_alpha_40),
                                    ContextCompat.getColor(AsmHistoryActivity.this, R.color.black)
                            );
                            // tabLayout.setupWithViewPager(mViewPager);
                            tabLayout.addOnTabSelectedListener(AsmHistoryActivity.this);
                            mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                        } else {
                            Toast.makeText(AsmHistoryActivity.this, "Some thing went wrong!", Toast.LENGTH_SHORT).show();
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
        )   {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(ParamsConstants.ENQUIRY_ID, enquiryId);
                params.put(ParamsConstants.BUILDER_ID, BMHConstants.CURRENT_BUILDER_ID);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap();
                headers.put("Content-Type", WEBAPI.contentTypeFormData);
                return headers;
            }
        };
        BMHApplication.getInstance().addToRequestQueue(stringRequest, "headerRequest");
    }

    private boolean isNotesAvailable(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getBoolean("success")) {
                JSONObject mJsonObject = jsonObject.getJSONObject("data");
                JSONArray notesArray = mJsonObject.optJSONArray("notes");
                if (notesArray != null && notesArray.length() > 0) {
                    return true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        clearMediaPlayer();
        super.onBackPressed();
        Intent intenta = getIntent();
       Bundle b = new Bundle();
       b.putString("abc","followup");

        intenta.putExtras(b);
      //  intenta.putExtra("Tabname","follow");
      //  setResult(2,intenta);
        setResult(Activity.RESULT_OK,intenta);
        //finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings ? true : super.onOptionsItemSelected(item);
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
        mSectionsPagerAdapter.getItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
        mSectionsPagerAdapter.getItem(tab.getPosition());
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        try {
            currentPosition = 0;
            seekBar.setProgress(currentPosition);
            if (mp != null && !mp.isPlaying()) {
                mediaPlayer = mp;
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
                currPlaying = false;
            }
            imgPlay.setImageDrawable(ContextCompat.getDrawable(AsmHistoryActivity.this, R.drawable.ic_play_circle));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class SectionsPagerAdapter extends FragmentPagerAdapter {
        //integer to count number of tabs
        int tabCount;

        SectionsPagerAdapter(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount = tabCount;
        }

        @Override
        public Fragment getItem(int position) {
            //Returning the current tabs
            switch (position) {
                case 0:
                    return new WorkHistoryFragment();
                case 1:
                    return new HistoryNotesFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }


    @Override
    public void createAndPlayDialog(String audioFilePath) {
        createPlayerDialog(this, audioFilePath);
    }

    private void createPlayerDialog(Context context, String audioFilePath) {
        // custom audioDialog
        currPlaying = false;
        isSeekBarPositionChanged = false;
        audioDialog = new AppCompatDialog(context);
        audioDialog.setContentView(R.layout.dialog_audio_player);

        imgClear = audioDialog.findViewById(R.id.imageView_clear);
        imgPlay = audioDialog.findViewById(R.id.imageView_play);
        final TextView seekBarHint = audioDialog.findViewById(R.id.textView);
        seekBar = audioDialog.findViewById(R.id.seekBar);
        String extension = audioFilePath.substring(audioFilePath.lastIndexOf("."));
        if (!extension.equalsIgnoreCase(BMHConstants.CALL_REC_EXTENSION_MP3)) {
            Utils.showToast(AsmHistoryActivity.this, getString(R.string.file_extension_not_correct));
            return;
        }
        fileUri = Uri.parse(audioFilePath);
       /* File mp4File = Utils.renameFileExtension(Objects.requireNonNull(audioFilePath), BMHConstants.CALL_REC_EXTENSION_MP4);
        File file = Utils.getCallRecordingDir(AsmHistoryActivity.this, mp4File.getName());
        if (file.exists())
        {
            audioPath = file.getPath();
        } else {
            audioPath = audioFilePath;
        }*/

        audioPath = audioFilePath;
        imgPlay.setOnClickListener(this);
        imgClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (audioDialog != null && audioDialog.isShowing()) {
                    clearMediaPlayer();
                    audioDialog.dismiss();
                }
            }
        });
        audioDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    clearMediaPlayer();
                    finish();
                }
                return false;
            }
             });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int mProgress = 0;

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBarHint.setVisibility(View.VISIBLE);
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
                mProgress = progress;
                if (mediaPlayer == null && fromTouch)
                    mediaPlayer = new MediaPlayer();
                else if (mediaPlayer != null && fromTouch) {
                    currentPosition = progress;
                    mediaPlayer.seekTo(progress);
                }

                seekBarHint.setVisibility(View.VISIBLE);
                int x = (int) Math.ceil(progress / 1000f);
                if (x < 10)
                    seekBarHint.setText("0:0" + x);
                else
                    seekBarHint.setText("0:" + x);

                double percent = progress / (double) seekBar.getMax();
                int offset = seekBar.getThumbOffset();
                int seekWidth = seekBar.getWidth();
                int val = (int) Math.round(percent * (seekWidth - 2 * offset));
                int labelWidth = seekBarHint.getWidth();
                seekBarHint.setX(offset + seekBar.getX() + val
                        - Math.round(percent * offset)
                        - Math.round(percent * labelWidth / 2));

                if (progress > 0 && mediaPlayer != null && !mediaPlayer.isPlaying()) {
                    seekBar.setProgress(progress);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer != null) {
                    //isSeekBarTracked = true;
                    // currentPosition = seekBar.getProgress();
                    mediaPlayer.seekTo(seekBar.getProgress());
                    //  seekBar.setProgress(seekBar.getProgress());
                }

            }
        });
        audioDialog.setCanceledOnTouchOutside(false);
        audioDialog.show();
    }

    public void playSong(String audioPath) {
        try {
            currPlaying = true;
            if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                wasPlaying = true;
                mediaPlayer.start();
                updateSeekBarStatus(mediaPlayer);
            }
            if (!wasPlaying) {
                if (mediaPlayer == null) {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.reset();
                    mediaPlayer.setOnCompletionListener(this);
                }
                mediaPlayer.setDataSource(audioPath);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.prepare();
                //  Thread.sleep(500);
                mediaPlayer.setVolume(0.5f, 1.0f);
                mediaPlayer.setLooping(false);
                total = mediaPlayer.getDuration() + BMHConstants.GAP_TIME;
                seekBar.setMax(total);
                mediaPlayer.start();
                setSeekBarStatus(mediaPlayer);
            }
            wasPlaying = false;
        } catch (Exception e) {
            clearMediaPlayer();
            e.printStackTrace();
        }
    }

    private void setSeekBarStatus(final MediaPlayer mediaPlayer) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    while (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        currentPosition = mediaPlayer.getCurrentPosition();
                        if (currentPosition < total) {
                            seekBar.setProgress(currentPosition);
                        }
                    }
                } catch (IllegalStateException e) {
                    clearMediaPlayer();
                }
            }
        };
        new Thread(runnable).start();
    }

    private void updateSeekBarStatus(final MediaPlayer mediaPlayer) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    while (mediaPlayer != null) {
                        if (mediaPlayer.isPlaying()) {
                            currentPosition = mediaPlayer.getCurrentPosition();
                            if (currentPosition >= 0 && total > 0) {
                                if (currentPosition < total) {
                                    seekBar.setProgress(currentPosition);
                                }
                            }
                        }/*else{
                            currentPosition = mediaPlayer.getCurrentPosition();
                            if (currentPosition < total) {
                                seekBar.setProgress(currentPosition);
                            }
                        }*/
                        if (!mediaPlayer.isPlaying() && isSeekBarPositionChanged != true) {
                            mediaPlayer.setDataSource(audioPath);
                            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            mediaPlayer.prepare();
                            //  Thread.sleep(500);
                            mediaPlayer.setVolume(0.5f, 1.0f);
                            mediaPlayer.setLooping(false);
                            total = mediaPlayer.getDuration() + BMHConstants.GAP_TIME;
                            seekBar.setMax(total);
                            mediaPlayer.start();
                           /* currentPosition = mediaPlayer.getCurrentPosition();
                            if (currentPosition < total) {
                                seekBar.setProgress(currentPosition);
                            }*/
                            setSeekBarStatus(mediaPlayer);
                            isSeekBarPositionChanged = true;
                        }
                    }
                } catch (IllegalStateException e) {
                    clearMediaPlayer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    public void pauseAudio() {
        if (mediaPlayer != null) {
            currPlaying = false;
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.start();
            }
        }
    }

    private void clearMediaPlayer() {
        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        } catch (IllegalStateException e) {
            e.getMessage();
        }
    }

    @Override
    public void onClick(View v) {
        if (!currPlaying) {
            imgPlay.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_pause_circle));
            playSong(audioPath);

          /*  Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(fileUri, "audio/*");
            this.startActivity(intent);
          */
        } else {
            imgPlay.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play_circle));
            pauseAudio();
        }
    }
}
