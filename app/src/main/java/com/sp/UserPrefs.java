//package com.queppelin.bookmyhouse;
//
//import android.os.Bundle;
//import android.preference.EditTextPreference;
//import android.preference.PreferenceActivity;
//import android.text.method.DigitsKeyListener;
//import android.util.Log;
//
//public class UserPrefs extends PreferenceActivity {
//    public int termLength = 1;
//    public int yearLength = 1;
//
//    @Override //OnCreate never runs, why?
//    protected void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        addPreferencesFromResource(R.id.citySpinner); 
//
//        EditTextPreference usersTermLength =(EditTextPreference)
//        findPreference(getString(R.string.city_id)); //finds 
//        //the edittext box by its key, term_length_set
//        usersTermLength.getEditText().setKeyListener(DigitsKeyListener.
//            getInstance()); //DigitsKeyListener only allows digits to be typed in
//        Log.v("UserPrefs", "UsersTermLength: " + usersTermLength);
//
//        EditTextPreference usersYearLength =(EditTextPreference)
//            findPreference(getString(R.string.year_length_set));
//        usersYearLength.getEditText().setKeyListener(DigitsKeyListener.
//            getInstance());
//    }
//}