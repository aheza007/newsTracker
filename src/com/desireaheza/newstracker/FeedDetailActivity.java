package com.desireaheza.newstracker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;

public class FeedDetailActivity extends Activity {

	ListView mFeedItems;
	FeedAdapter adapter;
	TextView title;
	FeedDetailActivity activity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_detail);
		title = (TextView) findViewById(R.id.textView1);
		mFeedItems = (ListView) findViewById(R.id.listView_feeds);
		adapter = new FeedAdapter(FeedDetailActivity.this,  R.layout.feed_item,new ArrayList<SyndEntry>());
		mFeedItems.setAdapter(adapter);
		activity=this;
		if (MainActivity.feedResults != null) {
			@SuppressWarnings("unchecked")
			List<SyndEntry> entries = MainActivity.feedResults.getEntries();
//			Toast.makeText(this, "#Feeds retrieved: " + entries.size(),
//					Toast.LENGTH_SHORT).show();

			Iterator<SyndEntry> iterator = entries.listIterator();
			Intent intent=getIntent();
			title.setText(intent.getStringExtra("providerName")+" News Feeds");
			while (iterator.hasNext()) {
				SyndEntry ent = iterator.next();
				adapter.add(ent);
			}
			
			mFeedItems.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent startWebView=new Intent(FeedDetailActivity.this,WebViewActivity.class);
					startWebView.putExtra("FEEDURL", view.getTag(R.id.feed_description).toString());
					activity.startActivity(startWebView);
				}
			});
			
			adapter.notifyDataSetChanged();

		} else {

			title.setText("No Feed Found");
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.feed_detail, menu);
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
