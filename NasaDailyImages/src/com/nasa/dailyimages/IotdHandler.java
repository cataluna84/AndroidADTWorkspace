package com.nasa.dailyimages;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Proxy;
import android.os.StrictMode;

import com.nasa.dailyimages.beans.Block;

public class IotdHandler extends DefaultHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(IotdHandler.class);
	
	private String url = "http://www.nasa.gov/rss/image_of_the_day.rss";
	private boolean inUrl = false;
	private boolean inTitle = false;
	private boolean inDescription = false;
	private boolean inItem = false;
	private boolean inDate = false;
	/*private Bitmap image = null;
	private String imageUrl = null;
	private String title = null;
	private StringBuffer description = new StringBuffer();
	private String date = null;*/
	private List<Block> itemList = new ArrayList<Block>();
	private Block item;
	
	public List<Block> processFeed() {
		try {
			// This part is added to allow the network connection on a main GUI
			// thread...
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			XMLReader reader = parser.getXMLReader();
			reader.setContentHandler(this);
			
/*			DefaultHttpClient client = new DefaultHttpClient();
			HttpHost proxy = new HttpHost("proxy.tcs.com", 8080);
			
			client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
			client.getCredentialsProvider().setCredentials(
	                new AuthScope("proxy.tcs.com", 8080),
	                new UsernamePasswordCredentials("549259", "Internet_003"));
			
			//HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(url);
			// add request header   request.addHeader("User-Agent", USER_AGENT);
			HttpResponse response = client.execute(request);
			reader.parse(new InputSource(response.getEntity().getContent()));*/
			
			URL urlObj = new URL(url);
			InputStream inputStream = urlObj.openConnection().getInputStream();
			reader.parse(new InputSource(inputStream));
			return itemList;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.debug(new String("Got Exception General"));
		}
		return null;
	}

	private Bitmap getBitmap(String url) {
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) new URL(url).openConnection();
			LOGGER.debug(url);
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap bitmap = BitmapFactory.decodeStream(input);
			input.close();
			return bitmap;
		} catch (IOException ioe) {
			LOGGER.debug(new String("IOException in reading Image"));
			return null;
		} catch (Exception ioe) {
			LOGGER.debug(new String("IOException GENERAL"));
			return null;
		} finally {
			connection.disconnect();
		}
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		if (localName.startsWith("item")) {
			inItem = true;
			item = new Block();				// create a new block when <item> is encountered
		} else if (inItem) {
			if (localName.equals("title")) {
				inTitle = true;
			} else {
				inTitle = false;
			}
			if (localName.equals("description")) {
				inDescription = true;
			} else {
				inDescription = false;
			}
			if (localName.equals("enclosure")) {
				LOGGER.debug(new String("characters Image"));
				item.setImageUrl(attributes.getValue("", "url"));
				inUrl = true;
			} else {
				inUrl = false;
			}
			if (localName.equals("pubDate")) {
				inDate = true;
			} else {
				inDate = false;
			}
		}
	}

	public void characters(char ch[], int start, int length) {
		LOGGER.debug(new String("characters"));
		String chars = new String(ch).substring(start, start + length);
		LOGGER.debug(chars);
		
		if (inTitle /*&& title == null*/) {
			LOGGER.debug(new String("TITLE"));
			item.setTitle(chars);
			inTitle = false;				// to prevent an entry again
		}
		if (inDescription) {
			item.setDescription(chars);
			inDescription = false;
		}
		if (inUrl /*&& image == null*/) {
			LOGGER.debug(new String("IMAGE"));
			LOGGER.debug(item.getImageUrl());
			item.setImage(getBitmap(item.getImageUrl()));
			inUrl = false;
		}
		if (inDate/* && date == null*/) {
			item.setDate(chars);
			itemList.add(item);		// since it is the last tag
			inDate = false;
		}
	}

	/*public Bitmap getImage() {
		return image;
	}

	public String getTitle() {
		return title;
	}

	public StringBuffer getDescription() {
		return description;
	}

	public String getDate() {
		return date;
	}*/

}