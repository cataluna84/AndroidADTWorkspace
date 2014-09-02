package com.newthinktank.stockquote;

import java.util.Arrays;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends Activity {

	public final static String STOCK_SYMBOL = "com.newthinktank.stockquote.STOCK";
	private SharedPreferences stocksSymbolsEntered;
	private TableLayout stockTableScrollView;
	private EditText stockSymbolEditText;
	Button enterStockSymbolButton;
	Button deleteStocksButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		stocksSymbolsEntered = getSharedPreferences("stockList", MODE_PRIVATE);
		
		stockTableScrollView = (TableLayout) findViewById(R.id.stockTableScrollView);
		stockSymbolEditText = (EditText) findViewById(R.id.stockSymbolEditText);
		enterStockSymbolButton = (Button) findViewById(R.id.enterStockSymbolButton);
		deleteStocksButton = (Button) findViewById(R.id.deleteStockButton);
		
		enterStockSymbolButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(stockSymbolEditText.getText().length() > 0) {
					saveStockSymbol(stockSymbolEditText.getText().toString());
					stockSymbolEditText.setText("");
					
					// to close the keyboard
					InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					inputMethodManager.hideSoftInputFromWindow(stockSymbolEditText.getWindowToken(), 0);
				} else {
					// to show an alert dialogue box
					AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
					builder.setTitle(R.string.invalid_stock_symbol);
					builder.setPositiveButton(R.string.ok, null);
					builder.setMessage(R.string.missing_stock_symbol);
					
					AlertDialog alertDialog = builder.create();
					alertDialog.show();
				}
				
			}
		});
		
		deleteStocksButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				deleteAllStocks();
				
				SharedPreferences.Editor editor = stocksSymbolsEntered.edit();
				editor.clear();
				editor.apply();
			}
		});
		
		updateSavedStockList(null);
	}
	
	private void updateSavedStockList(String newStockSymbol) {
		String[] stocks = stocksSymbolsEntered.getAll().keySet().toArray(new String[0]);
		Arrays.sort(stocks, String.CASE_INSENSITIVE_ORDER);
		
		if(newStockSymbol != null) {
			insertStockInScrollView(newStockSymbol, Arrays.binarySearch(stocks, newStockSymbol));
		} else {
			for(int i=0; i<stocks.length; i++) {
				insertStockInScrollView(stocks[i], i);
			}
		}
	}
	
	private void saveStockSymbol(String newStock) {
		String isTheStockNew = stocksSymbolsEntered.getString(newStock, null);
		SharedPreferences.Editor preferencesEditor = stocksSymbolsEntered.edit();
		preferencesEditor.putString(newStock, newStock);
		preferencesEditor.apply();
		
		if(isTheStockNew == null) {
			updateSavedStockList(newStock);
		}
	}
	
	private void insertStockInScrollView(String stock, int arrayIndex) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View newStockRow = inflater.inflate(R.layout.stock_quote_row, null);
		
		TextView newStockTextView = (TextView) newStockRow.findViewById(R.id.stockSymbolTextView);
		
		newStockTextView.setText(stock);
		
		Button stockQuoteButton = (Button) newStockRow.findViewById(R.id.stockQuoteButton);
		stockQuoteButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TableRow tableRow = (TableRow) v.getParent();
				TextView stockTextView = (TextView) tableRow.findViewById(R.id.stockSymbolTextView);
				String stockSymbol = stockTextView.getText().toString();
				
				Intent intent = new Intent(MainActivity.this, StockInfoActivity.class);
				intent.putExtra(STOCK_SYMBOL, stockSymbol);
				startActivity(intent);
			}
		});
		
		Button quoteFromWebButton = (Button) newStockRow.findViewById(R.id.quoteFromWebButton);
		quoteFromWebButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TableRow tableRow = (TableRow) v.getParent();
				TextView stockTextView = (TextView) tableRow.findViewById(R.id.stockSymbolTextView);
				String stockSymbol = stockTextView.getText().toString();
				
				String stockURL = getString(R.string.yahoo_stock_url) + stockSymbol;
				
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(stockURL));
				startActivity(intent);
			}
		});
		
		stockTableScrollView.addView(newStockRow, arrayIndex);
		
	}
	
	/*public OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
	};*/
	
	private void deleteAllStocks() {
		stockTableScrollView.removeAllViews();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
