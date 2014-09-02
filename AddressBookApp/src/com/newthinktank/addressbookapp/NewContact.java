package com.newthinktank.addressbookapp;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class NewContact extends Activity {
	
	// The EditText objects
	EditText firstName;
	EditText lastName;
	EditText phoneNumber;
	EditText emailAddress;
	EditText homeAddress;

	DBTools dbTools = new DBTools(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_new_contact);
		
		// Initialize the EditText objects
		firstName = (EditText) findViewById(R.id.firstName);
		lastName = (EditText) findViewById(R.id.lastName);
		phoneNumber = (EditText) findViewById(R.id.phoneNumber);
		emailAddress = (EditText) findViewById(R.id.emailAddress);
		homeAddress = (EditText) findViewById(R.id.homeAddress);
	}

	// android:onClick="addNewContact"
	public void addNewContact(View view) {
		HashMap<String, String> queryValuesMap = new HashMap<String, String>();

		// Get the values from the EditText boxes
		queryValuesMap.put("firstName", firstName.getText().toString());
		queryValuesMap.put("lastName", lastName.getText().toString());
		queryValuesMap.put("phoneNumber", phoneNumber.getText().toString());
		queryValuesMap.put("emailAddress", emailAddress.getText().toString());
		queryValuesMap.put("homeAddress", homeAddress.getText().toString());

		// Call for the HashMap to be added to the database
		dbTools.insertContact(queryValuesMap);
		
		// Call for MainActivity to execute
		this.callMainActivity(view);
	}

	private void callMainActivity(View view) {
		Intent theIntent = new Intent(getApplication(), MainActivity.class);
		startActivity(theIntent);
	}
}
