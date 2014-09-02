package com.newthinktank.addressbookapp;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBTools extends SQLiteOpenHelper {

	// Context : provides access to application-specific resources and classes
	// As well as up-calls for application-level operations such as launching activities, broadcasting and receiving intents, etc.
	public DBTools(Context applicationContext) {
		super(applicationContext, "contactbook.db", null, 1);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// How to create a table in SQLite
		// Make sure you don't put a ; at the end of the query
		String query = "CREATE TABLE contacts (contactId INTEGER PRIMARY KEY, firstName TEXT, " +
				"lastName TEXT, phoneNumber TEXT, emailAddress TEXT, homeAddress TEXT)";
		
		// Executes the query provided as long as the query isn't a select
		// or if the query doesn't return any data
		db.execSQL(query);
	}

	// onUpgrade is used to drop tables, add tables, or do anything 
	// else it needs to upgrade
	// This is droping the table to delete the data and then calling
	// onCreate to make an empty table
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String query = "DROP TABLE IF EXISTS contacts";

		// Executes the query provided as long as the query isn't a select
		// or if the query doesn't return any data
		db.execSQL(query);
    	onCreate(db);
	}
	
	public void insertContact(HashMap<String, String> queryValues) {
		// Open a database for reading and writing	
		SQLiteDatabase database = this.getWritableDatabase();
		
		// Stores key value pairs being the column name and the data
		// ContentValues data type is needed because the database
		// requires its data type to be passed
		ContentValues values = new ContentValues();
		
		values.put("firstName", queryValues.get("firstName"));
		values.put("lastName", queryValues.get("lastName"));
		values.put("phoneNumber", queryValues.get("phoneNumber"));
		values.put("emailAddress", queryValues.get("emailAddress"));
		values.put("homeAddress", queryValues.get("homeAddress"));
		
		// Inserts the data in the form of ContentValues into the
		// table name provided
		database.insert("contacts", null, values);
		
		// Release the reference to the SQLiteDatabase object
		database.close();
	}
	
	public int updateContact(HashMap<String, String> queryValues) {
		// Open a database for reading and writing	
		SQLiteDatabase database = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		
		values.put("firstName", queryValues.get("firstName"));
		values.put("lastName", queryValues.get("lastName"));
		values.put("phoneNumber", queryValues.get("phoneNumber"));
		values.put("emailAddress", queryValues.get("emailAddress"));
		values.put("homeAddress", queryValues.get("homeAddress"));
		
		// update(TableName, ContentValueForTable, WhereClause, ArgumentForWhereClause)
		return database.update("contacts", values, 
				"contactId" + " = ?", new String[] {queryValues.get("contactId")});
	}
	
	public void deleteContact(String id) {
		SQLiteDatabase database = this.getWritableDatabase();
		String deleteQuery = "DELETE FROM contacts WHERE contactId='" + id + "'";
		database.execSQL(deleteQuery);
	}
	
	public ArrayList<HashMap<String, String>> getAllContacts() {
		// ArrayList that contains every row in the database
		// and each row key / value stored in a HashMap
		ArrayList<HashMap<String, String>> contactArrayList;
		contactArrayList = new ArrayList<HashMap<String, String>>();
		
		String selectQuery = "SELECT * FROM contacts ORDER BY lastName";
		
		// Open a database for reading and writing
	    SQLiteDatabase database = this.getWritableDatabase();
	    
	    // Cursor provides read and write access for the 
	    // data returned from a database query
	    
	    // rawQuery executes the query and returns the result as a Cursor
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    
	    // Cycle through the data
	    if(cursor.moveToFirst()) {
			do {
				HashMap<String, String> contactMap = new HashMap<String, String>();
				
				// Store the key / value pairs in a HashMap
				// Access the Cursor data by index that is in the same order
				// as used when creating the table
				contactMap.put("contactId", cursor.getString(0));
				contactMap.put("firstName", cursor.getString(1));
				contactMap.put("lastName", cursor.getString(2));
				contactMap.put("phoneNumber", cursor.getString(3));
				contactMap.put("emailAddress", cursor.getString(4));
				contactMap.put("homeAddress", cursor.getString(5));
				
				contactArrayList.add(contactMap);
			} while (cursor.moveToNext()); // Move Cursor to the next row
	    }
	    
	    return contactArrayList;
	}
	
	public HashMap<String, String> getContactInfo(String id) {
		HashMap<String, String> contactMap = new HashMap<String, String>();
		
		// Open a database for reading and writing
	    SQLiteDatabase database = this.getReadableDatabase();
	    
	    String selectQuery = "SELECT * FROM contacts where contactId='" + id + "'";

	    Cursor cursor = database.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	        do {
	        	contactMap.put("firstName", cursor.getString(1));
	        	contactMap.put("lastName", cursor.getString(2));
	        	contactMap.put("phoneNumber", cursor.getString(3));
	        	contactMap.put("emailAddress", cursor.getString(4));
	        	contactMap.put("homeAddress", cursor.getString(5));
	        } while (cursor.moveToNext());
	    }				    
	    return contactMap;
	}
}