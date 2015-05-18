package com.desireaheza.newstracker;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavigationDrawerAdapter extends ArrayAdapter<FeedProvider> {

	Context mContext;
	LayoutInflater mInflater;

	public NavigationDrawerAdapter(Context context, int textViewResourceId,
			List<FeedProvider> objects) {
		super(context, 0, objects);
		mContext = context;
		mInflater = (LayoutInflater) mContext
				.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder lViewHolder;
		try {
			if(convertView==null){
				convertView=mInflater.inflate(R.layout.navigation_drawer_list_item,parent, false);
				lViewHolder=new ViewHolder();
				lViewHolder.mImageView=(ImageView)convertView.findViewById(R.id.imageView_icon);
				lViewHolder.mTextView=(TextView)convertView.findViewById(R.id.textView_name);
				convertView.setTag(lViewHolder);
			}
			else{
				lViewHolder=(ViewHolder)convertView.getTag();
			}
			FeedProvider provider=getItem(position);
			lViewHolder.mTextView.setText(provider.getProviderName());
			Integer icon=Integer.valueOf(provider.getProviderIcon());
			lViewHolder.mImageView.setImageResource(provider.getProviderIcon());
			convertView.setTag(R.id.textView_name, provider);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}

	class ViewHolder {
		ImageView mImageView;
		TextView mTextView;
	}

}
