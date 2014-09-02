package com.nasa.dailyimages;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.nasa.dailyimages.beans.Block;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Settings.System.putString(getContentResolver(), Settings.System.HTTP_PROXY, "http://549259:Internet_003@proxy.tcs.com:8080");
		setContentView(R.layout.activity_main);
		IotdHandler handler = new IotdHandler();
		List<Block> blocks = handler.processFeed();
		resetDisplay(blocks);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void resetDisplay(List<Block> blocks) {
		
		for(Block block:blocks)
		{
			TextView titleView = (TextView) findViewById(R.id.imageTitle);
			titleView.setText(block.getTitle());
			
			TextView dateView = (TextView) findViewById(R.id.imageDate);
			dateView.setText(block.getDate());
			
			ImageView imageView = (ImageView) findViewById(R.id.imageDisplay);
			imageView.setImageBitmap(block.getImage());
			
			TextView descriptionView = (TextView)findViewById(R.id.imageDescription);;
			descriptionView.setText(block.getDescription());
		}
	}
}