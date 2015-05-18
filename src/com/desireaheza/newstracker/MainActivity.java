package com.desireaheza.newstracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;

public class MainActivity extends Activity {

	private DrawerLayout mDrawerLayout;
	public ListView mDrawerList;
	public static SyndFeed feedResults;
	// private String[] mPlanetTitles;
	private String[] mRightPlanetTitles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_main);
			// mRightPlanetTitles = getResources().getStringArray(
			// R.array.category_name);
			// ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
			// android.R.layout.simple_list_item_1, mRightPlanetTitles);
			mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
			mDrawerList = (ListView) findViewById(R.id.right_drawer);
			// mDrawerList.setAdapter(adapter);

			if (savedInstanceState == null) {
				// on first time display view for first nav item
				displayView(0);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new FragmentMain();
			break;
		// case 1:
		// fragment = new FindPeopleFragment();
		// break;
		// case 2:
		// fragment = new PhotosFragment();
		// break;
		// case 3:
		// fragment = new CommunityFragment();
		// break;
		// case 4:
		// fragment = new PagesFragment();
		// break;
		// case 5:
		// fragment = new WhatsHotFragment();
		// break;

		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, fragment).commit();

			// update selected item and title, then close the drawer
			// mDrawerList.setItemChecked(position, true);
			// mDrawerList.setSelection(position);
			// setTitle(navMenuTitles[position]);
			// mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void open() {
		mDrawerLayout.openDrawer(Gravity.RIGHT);
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

	public class FragmentMain extends Fragment {

		View mRootView;
		Map<String, Integer> categoryNamIcon = new HashMap<String, Integer>();
		Map<String, Integer> providerNameIcon = new HashMap<String, Integer>();
		List<Category> mCategories = new ArrayList<Category>();
		TypedArray mCategoryIcons;
		String[] mCategoryTitles;
		String[] mProviderIconsName;
		String[] mProviderNames;
		GridView mGridView;
		Writer writer;
		List<FeedProvider> feedProviders = new ArrayList<FeedProvider>();
		MainCategoryAdapter adapter;
		InputStream jsonStream;
		Map<String, List<FeedProvider>> providerPerCategory = new HashMap<String, List<FeedProvider>>();
		NavigationDrawerAdapter adapterNavigationDrawer;
		ProgressDialog dialog;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			try {
				mRootView = inflater.inflate(R.layout.fragment_main_layout,
						container, false);

				// load slide menu items
				mCategoryTitles = this.getActivity().getResources()
						.getStringArray(R.array.category_name);

				mProviderNames = this.getActivity().getResources()
						.getStringArray(R.array.provider_name);
				providerNameIcon.clear();
				// nav drawer icons from resources
				mCategoryIcons = this.getActivity().getResources()
						.obtainTypedArray(R.array.category_icon);
				// int
				// id=this.getActivity().getResources().getIdentifier("abc_news_logo","drawable",
				// getActivity().getApplication().getPackageName());
				mProviderIconsName = this.getActivity().getResources()
						.getStringArray(R.array.provider_icons);

				for (int j = 0; j < mProviderIconsName.length; j++) {
					int id;
					try {
						id = this
								.getActivity()
								.getResources()
								.getIdentifier(
										mProviderIconsName[j],
										"drawable",
										getActivity().getApplication()
												.getPackageName());
						providerNameIcon.put(mProviderNames[j].trim(), id);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				mCategories.clear();
				for (int i = 0; i < mCategoryIcons.length(); i++) {

					mCategories.add(new Category(mCategoryTitles[i],
							mCategoryIcons.getResourceId(i, -1)));
				}

				JSONObject jsonObject = new JSONObject(getCategories());
				JSONArray jsonCategories = jsonObject
						.getJSONArray("categories");
				int count = jsonCategories.length();
				for (int i = 0; i < count; i++) {
					JSONObject category = jsonCategories.getJSONObject(i);
					String categoryName = category.keys().next();

					JSONArray providerDetail = category
							.getJSONArray(categoryName);
					for (int b = 0; b < providerDetail.length(); b++) {
						JSONObject item = providerDetail.getJSONObject(b);
						String providerName = item.getString("providername");
						FeedProvider provder = new FeedProvider(categoryName,
								item.getString("providername"),
								item.getString("link"),
								providerNameIcon.get(providerName.trim()));

						feedProviders.add(provder);
						if (providerPerCategory
								.containsKey(categoryName.trim()))
							providerPerCategory.get(categoryName.trim()).add(
									provder);
						else {
							List<FeedProvider> newList = new ArrayList<FeedProvider>();
							newList.add(provder);
							providerPerCategory.put(categoryName.trim(),
									newList);
						}
					}

					// FeedProvider providerItem=new FeedProvider();
					int c = jsonCategories.length();
					// System.out.print(c);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				adapterNavigationDrawer = new NavigationDrawerAdapter(
						this.getActivity(), R.layout.drawer_list_item,
						feedProviders);
				mDrawerList.setAdapter(adapterNavigationDrawer);

				mDrawerList.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
//						dialog = new ProgressDialog(MainActivity.this);
//						dialog.setMessage("Loading...");
//						dialog.show();
						GetRSSFeedTask task = new GetRSSFeedTask(
								((MainActivity) getActivity()));
						FeedProvider provider = (FeedProvider) view
								.getTag(R.id.textView_name);
						String url = provider.getProviderUrl();
						task.execute(new String[] { url,provider.getProviderName() });
						//dialog.dismiss();
					}
				});

				adapter = new MainCategoryAdapter(this.getActivity(),
						R.layout.grid_item, mCategories);
				mGridView = (GridView) mRootView.findViewById(R.id.gridView1);
				mGridView.setAdapter(adapter);
				mGridView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Category item = (Category) view
								.getTag(R.id.textView_name);
						String catNameAsKey = item.getCategoryName();
						if (item != null
								&& providerPerCategory
										.containsKey(catNameAsKey)) {
							adapterNavigationDrawer.clear();
							adapterNavigationDrawer.addAll(providerPerCategory
									.get(catNameAsKey));
							((MainActivity) getActivity()).open();
						}
					}
				});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return mRootView;
		}

		private String getCategories() {

			try {
				jsonStream = this.getResources()
						.openRawResource(R.raw.category);
				writer = new StringWriter();
				char[] buffer = new char[1024];

				Reader reader = new BufferedReader(new InputStreamReader(
						jsonStream, "UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					jsonStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return writer.toString();
		}
	}
}
