package com.newthinktank.addressbookapp;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditContact extends Activity {
	
	EditText firstName;
	EditText lastName;
	EditText phoneNumber;
	EditText emailAddress;
	EditText homeAddress;

	DBTools dbTools = new DBTools(this);
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_contact);

		// Get the EditText objects
		firstName = (EditText) findViewById(R.id.firstName);
		lastName = (EditText) findViewById(R.id.lastName);
		phoneNumber = (EditText) findViewById(R.id.phoneNumber);
		emailAddress = (EditText) findViewById(R.id.emailAddress);
		homeAddress = (EditText) findViewById(R.id.homeAddress);

		// Intent defines that an operation will be performed
		Intent theIntent = getIntent();

		// Get the extended data provided to this activity
		// putExtra("contactId", contactIdValue); in MainActivity
		// will pass contactId here
		String contactId = theIntent.getStringExtra("contactId");

		// Get the HashMap of data associated with the contactId
		HashMap<String, String> contactList = dbTools.getContactInfo(contactId);

		// Make sure there is something in the contactList
		if(contactList.size()!=0) {
			// Put the values in the EditText boxes
			firstName.setText(contactList.get("firstName"));
			lastName.setText(contactList.get("lastName"));
			phoneNumber.setText(contactList.get("phoneNumber"));
			emailAddress.setText(contactList.get("emailAddress"));
			homeAddress.setText(contactList.get("homeAddress"));
		}
	}

	// onClick editContact button
	public void editContact(View view) {
		HashMap<String, String> queryValuesMap =  new  HashMap<String, String>();

		// Get the EditText objects
		firstName = (EditText) findViewById(R.id.firstName);
		lastName = (EditText) findViewById(R.id.lastName);
		phoneNumber = (EditText) findViewById(R.id.phoneNumber);
		emailAddress = (EditText) findViewById(R.id.emailAddress);
		homeAddress = (EditText) findViewById(R.id.homeAddress);

		// Intent defines that an operation will be performed
		Intent theIntent = getIntent();

		// Get the extended data provided to this activity
		// putExtra("contactId", contactIdValue); in MainActivity
		// will pass contactId here
		String contactId = theIntent.getStringExtra("contactId");

		// Put the values in the EditTexts in the HashMap
		queryValuesMap.put("contactId", contactId);
		queryValuesMap.put("firstName", firstName.getText().toString());
		queryValuesMap.put("lastName", lastName.getText().toString());
		queryValuesMap.put("phoneNumber", phoneNumber.getText().toString());
		queryValuesMap.put("emailAddress", emailAddress.getText().toString());
		queryValuesMap.put("homeAddress", homeAddress.getText().toString());

		// Send the HashMap to update the data in the database
		dbTools.updateContact(queryValuesMap);
		
		// Call for MainActivity
		this.callMainActivity(view);
	}
	
	// onClick removeContact button
	public void removeContact(View view) {
		Intent theIntent = getIntent();
		String contactId = theIntent.getStringExtra("contactId");
		
		// Call for the contact with the contactId provided to be deleted
		dbTools.deleteContact(contactId);
		
		// Call for MainActivity
		this.callMainActivity(view);
	}
	
	// Calls for a switch to the MainActivity
	public void callMainActivity(View view) {
		// getApplication returns an Application object which allows 
		// you to manage your application and respond to different actions.
		// It returns an Application object which extends Context.
		
		// A Context provides information on the environment your application 
		// is currently running in. It provides services like how to obtain 
		// access to a database and preferences.
		
		// Google says a Context is an entity that represents various 
		// environment data. It provides access to local files, databases, 
		// class loaders associated to the environment, services including 
		// system-level services, and more.
		
		// The following Intent states that you want to switch to a new 
		// Activity being the MainActivity
		Intent objIntent = new Intent(getApplication(), MainActivity.class);
		startActivity(objIntent);
	}
}