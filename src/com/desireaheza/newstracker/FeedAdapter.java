package com.desireaheza.newstracker;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndContent;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.squareup.picasso.Picasso;

public class FeedAdapter extends ArrayAdapter<SyndEntry> {

	Context mContext;
	LayoutInflater mInflator;

	public FeedAdapter(Context context, int resource,
			ArrayList<SyndEntry> feedItems) {
		super(context, resource, feedItems);
		mContext = context;
		mInflator = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;

		if (convertView == null) {
			convertView = mInflator.inflate(R.layout.feed_item_test, null);
			holder = new ViewHolder();
			holder.mTextViewTitle = (TextView) convertView
					.findViewById(R.id.feed_title);
			holder.mTextViewDescription = (TextView) convertView
					.findViewById(R.id.feed_description);
			holder.mFeedImage = (ImageView) convertView
					.findViewById(R.id.imageView_feed);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		SyndEntry feedItem = getItem(position);
		String Description = "";

		holder.mTextViewTitle.setText(Html.fromHtml(feedItem.getTitle()));
		if (feedItem.getDescription() == null) {
			// String feedDes=
			for (Iterator<?> it = feedItem.getContents().iterator(); it
					.hasNext();) {
				SyndContent syndContent = (SyndContent) it.next();

				if (syndContent != null) {
					Description = syndContent.getValue();
				}
			}
		} else if (feedItem.getDescription() != null) {
			Description = feedItem.getDescription().getValue();
		}
		
		int startImageLink = 0, imageEndTage = 0;
		int endImageLink = 0;
		String ImageUrl = "";
		boolean hasImage = false;
		if (Description.contains("<img")) {
			hasImage = true;
			startImageLink = Description.indexOf("src=",
					Description.indexOf("<img"));
			endImageLink = Description.indexOf('"',
					startImageLink + "src=".length() + 1);
			ImageUrl = Description.substring(startImageLink + 5, endImageLink);
			imageEndTage = Description.indexOf(">", endImageLink);
		}
		String descCont = Description.substring(hasImage ? imageEndTage+1 : 0)
				.replaceAll("\\n", " ").replaceAll("&nbsp;", " ")
				.replaceAll("&#160;", " ").trim();
	//	org.jsoup.nodes.Document rssEntry = Jsoup.parse(descCont);
		if(descCont.contains("<p>")){
			descCont=descCont.substring(0,descCont.indexOf("</p>")+4);
		}
		else if (descCont.contains("<"))
			descCont=descCont.substring(0,descCont.indexOf("<"));
		
		holder.mTextViewDescription.setText(trimTrailingWhitespace(Html.fromHtml(descCont)));
		if (!(descCont.isEmpty() || descCont.equals(" ") ||descCont.equals(""))) {
			try {
				URL myURL = new URL(ImageUrl);
				holder.mFeedImage.setVisibility(View.VISIBLE);
				Picasso.with(mContext).load(ImageUrl)
						.placeholder(R.drawable.ic_launcher) // optional
						.error(R.drawable.ic_launcher) // optional // optional
						.into(holder.mFeedImage);
			} catch (MalformedURLException e) {
				holder.mFeedImage.setVisibility(View.GONE);
			}
		}
		else{
			holder.mFeedImage.setVisibility(View.GONE);
		}
		String feedLink="";
		feedLink=(feedItem.getUri()==""||feedItem.getUri()==" "||feedItem.getUri().isEmpty())?feedItem.getLink():feedItem.getUri();
		convertView.setTag(R.id.feed_description, feedItem.getUri());
		return convertView;

	}

	public static CharSequence trimTrailingWhitespace(CharSequence source) {

	    if(source == null)
	        return "";

	    int i = source.length();

	    // loop back to the first non-whitespace character
	    while(--i >= 0 && Character.isWhitespace(source.charAt(i))) {
	    }

	    return source.subSequence(0, i+1);
	}
//	private void GetFeedContent(SyndEntry syndEntry) {
//		List<SyndEntry> items = syndEntry.getEnclosures();
//		System.out.print(items.size());
//	}

	private class ViewHolder {
		TextView mTextViewTitle;
		TextView mTextViewDescription;
		ImageView mFeedImage;
	}
}
