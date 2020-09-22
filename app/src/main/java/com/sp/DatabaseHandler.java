package com.sp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.utils.LocalityData;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 2;

	// Database Name
	private static final String DATABASE_NAME = "locationManager";

	// Contacts table name
	private static final String TABLE_CLOCATIONS = "locations";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_LOCATOIN_ID = "location_id";
	private static final String KEY_LOCATION_NAME = "location_name";
	private static final String KEY_TITLE = "title";
	private static final String KEY_SUB_TITLE = "sub_title";
	private static final String KEY_CITY_ID = "city_id";
	private static final String FALTUID = "faltuid";
	
	private static final String KEY_BUILDER_NAME  = "builder_name";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CLOCATIONS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY,"
				+ KEY_LOCATOIN_ID + " TEXT,"
				+ KEY_TITLE + " TEXT,"
				+ KEY_SUB_TITLE + " TEXT,"
				+ KEY_CITY_ID + " TEXT, "
				+ KEY_BUILDER_NAME + " TEXT, "
				+ KEY_LOCATION_NAME + " TEXT"+ " "
				+" )";
		
//		+ KEY_BUILDER_NAME + " TEXT"+ "
		
		db.execSQL(CREATE_CONTACTS_TABLE);
//		
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLOCATIONS);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new contact
	public void addContact(LocalityData model, int citySelectedId) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_LOCATOIN_ID, model.getId()); // SuggestionModel Name
		values.put(KEY_TITLE, model.getTitle()); // SuggestionModel Phone
		values.put(KEY_SUB_TITLE, model.getSubtitle()); // SuggestionModel Phone
		values.put(KEY_CITY_ID, String.valueOf(citySelectedId)); // SuggestionModel Phone
		values.put(KEY_BUILDER_NAME, model.getBuildername());
		values.put(KEY_LOCATION_NAME, model.getLocation_Name());

		// Inserting Row
		if (IsRecordExist(model.getId())) {
			db.delete(TABLE_CLOCATIONS, KEY_LOCATOIN_ID+"='"+ model.getId()+"'", null);
//			db.update(TABLE_CLOCATIONS, values, KEY_LOCATOIN_ID+"='"+ model.getId()+"'", null);
		}
		db.insert(TABLE_CLOCATIONS, null, values);
		
		db.close(); // Closing database connection
	}
	
	public boolean IsRecordExist(String id){
		String selectQuery = "SELECT  * FROM " + TABLE_CLOCATIONS +" WHERE "+KEY_LOCATOIN_ID+"='"+ id+"'";
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
//	SuggestionModel getContact(int id) {
//		SQLiteDatabase db = this.getReadableDatabase();
//
//		Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
//				KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",
//				new String[] { String.valueOf(id) }, null, null, null, null);
//		if (cursor != null)
//			cursor.moveToFirst();
//
//		SuggestionModel contact = new SuggestionModel(Integer.parseInt(cursor.getString(0)),
//				cursor.getString(1), cursor.getString(2));
//		// return contact
//		return contact;
//	}
	
	// Getting All Contacts
	public LocalityData[] getAllContacts(int cityId) {
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_CLOCATIONS +" WHERE "+KEY_CITY_ID+"='"+ String.valueOf(cityId)+"' ORDER BY "+ KEY_ID +" DESC LIMIT 5";
		LocalityData[] localityDatas = null;
		SQLiteDatabase db = this.getWritableDatabase();
		if(db == null)return null;
		Cursor cursor = db.rawQuery(selectQuery, null);
		if(cursor == null && cursor.getCount() == 0)return  null;
		if (cursor.moveToFirst()) {
		   localityDatas = new LocalityData[cursor.getCount()];
			int i = 0;
			do {
				LocalityData localityData = new LocalityData();
				localityData.setId(cursor.getString(cursor.getColumnIndex(KEY_LOCATOIN_ID)));
				localityData.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
				localityData.setSubtitle(cursor.getString(cursor.getColumnIndex(KEY_SUB_TITLE)));
				localityData.setCity(cursor.getString(cursor.getColumnIndex(KEY_CITY_ID)));
				localityData.setBuildername(cursor.getString(cursor.getColumnIndex(KEY_BUILDER_NAME)));
				localityData.setLocation_Name(cursor.getString(cursor.getColumnIndex(KEY_LOCATION_NAME)));
				// Adding contact to list
				localityDatas[i] = localityData;
				i++;
			} while (cursor.moveToNext());
			cursor.close();
		}

		// return contact list
		return localityDatas;
	}

	// Updating single contact
//	public int updateContact(SuggestionModel contact) {
//		SQLiteDatabase db = this.getWritableDatabase();
//
//		ContentValues values = new ContentValues();
//		values.put(KEY_NAME, contact.getName());
//		values.put(KEY_PH_NO, contact.getPhoneNumber());
//
//		// updating row
//		return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
//				new String[] { String.valueOf(contact.getID()) });
//	}

	// Deleting single contact
//	public void deleteContact(SuggestionModel contact) {
//		SQLiteDatabase db = this.getWritableDatabase();
//		db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
//				new String[] { String.valueOf(contact.getID()) });
//		db.close();
//	}


	// Getting contacts Count
//	public int getContactsCount() {
//		String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
//		SQLiteDatabase db = this.getReadableDatabase();
//		Cursor cursor = db.rawQuery(countQuery, null);
//		cursor.close();
//
//		// return count
//		return cursor.getCount();
//	}

}