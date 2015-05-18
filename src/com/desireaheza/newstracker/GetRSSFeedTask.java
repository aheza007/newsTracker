package com.desireaheza.newstracker;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.FeedException;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.SyndFeedInput;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.XmlReader;

public class GetRSSFeedTask extends AsyncTask<String, Integer, SyndFeed> {

	MainActivity mActivty;
	ProgressDialog dialog;
	String feedProviderName;

	public GetRSSFeedTask(MainActivity activity) {
		mActivty = activity;
	}
	
	@Override
	protected void onPreExecute() {
		
		super.onPreExecute();
		dialog = new ProgressDialog(mActivty);
		dialog.setMessage("Loading...");
		dialog.show();
	}

	protected void onPostExecute(SyndFeed result) {
		super.onPostExecute(result);
		dialog.dismiss();
		MainActivity.feedResults = result;
		Intent displyFeedDetails = new Intent(mActivty,
				FeedDetailActivity.class);
		displyFeedDetails.putExtra("providerName", feedProviderName);
		mActivty.startActivity(displyFeedDetails);
	}

	@Override
	protected SyndFeed doInBackground(String... params) {
		feedProviderName=params[1];
		return getRSS(params[0]);
	}

	private SyndFeed getRSS(String rss) {

		URL feedUrl;
		SyndFeed feed = null;
		try {
			Log.d("DEBUG", "Entered:" + rss);
			feedUrl = new URL(rss);

			SyndFeedInput input = new SyndFeedInput();
			feed = input.build(new XmlReader(feedUrl));

			// return feed;

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (FeedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return feed;
	}
}
