package com.sp;

import com.utils.LocalityData;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandlerCity extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	// Contacts table name
	private static final String TABLE_CITYS = "city";
	// Database Name
	private static final String DATABASE_NAME = "cityManager";
	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_CITY_ID = "city_id";

	public DatabaseHandlerCity(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CITYS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
		+ KEY_CITY_ID + " TEXT )";
		db.execSQL(CREATE_CONTACTS_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITYS);
		// Create tables again
		onCreate(db);

	}

	// Adding new contact
	public void addContact(LocalityData model, int citySelectedId) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_CITY_ID, String.valueOf(citySelectedId)); 
		// SuggestionModel Phone Inserting Row
		if (IsRecordExist(model.getId())) {
			db.delete(TABLE_CITYS, KEY_CITY_ID + "='" + model.getId() + "'", null);
			// db.update(TABLE_CLOCATIONS, values, KEY_LOCATOIN_ID+"='"+
			// model.getId()+"'", null);
		}
		db.insert(TABLE_CITYS, null, values);

		db.close(); // Closing database connection
	}

	public boolean IsRecordExist(String id) {
		String selectQuery = "SELECT  * FROM " + TABLE_CITYS + " WHERE " + KEY_CITY_ID + "='" + id + "'";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		boolean isfound = false;
		if (cursor.moveToFirst()) {
			if (cursor.getCount() > 0) {
				isfound = true;
			}
		}

		return isfound;
	}

	// Getting single contact
	// SuggestionModel getContact(int id) {
	// SQLiteDatabase db = this.getReadableDatabase();
	//
	// Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
	// KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",
	// new String[] { String.valueOf(id) }, null, null, null, null);
	// if (cursor != null)
	// cursor.moveToFirst();
	//
	// SuggestionModel contact = new
	// SuggestionModel(Integer.parseInt(cursor.getString(0)),
	// cursor.getString(1), cursor.getString(2));
	// // return contact
	// return contact;
	// }

	// Getting All Contacts
	public LocalityData[] getAllContacts(int cityId) {
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_CITYS + " WHERE " + KEY_CITY_ID + "='" + String.valueOf(cityId)
				+ "' ORDER BY " + KEY_ID;
		LocalityData[] localityDatas = null;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list

		if (cursor.moveToFirst()) {
			localityDatas = new LocalityData[cursor.getCount()];
			int i = 0;
			do {
				LocalityData localityData = new LocalityData();
				localityData.setId(cursor.getString(1));
				// localityData.setTitle(cursor.getString(2));
				// localityData.setSubtitle("Recent Search");
				// localityData.setSubtitle(cursor.getString(3));
				// localityData.setBuildername(cursor.getString(cursor.getColumnIndex(KEY_BUILDER_NAME)));
				// Adding contact to list
				localityDatas[i] = localityData;
				i++;
			} while (cursor.moveToNext());
		}

		// return contact list
		return localityDatas;
	}

}
