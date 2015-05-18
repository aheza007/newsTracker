package com.desireaheza.newstracker;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainCategoryAdapter extends ArrayAdapter<Category> {

	LayoutInflater mInflater;
	Context mContext;

	public MainCategoryAdapter(Context context, int resource,
			List<Category> objects) {
		super(context, 0, objects);
		mContext = context;
		mInflater = (LayoutInflater) mContext
				.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mViewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.grid_item, parent, false);
			mViewHolder = new ViewHolder();
			mViewHolder.mImageView = (ImageView) convertView
					.findViewById(R.id.imageView_icon);
			mViewHolder.mTextView = (TextView) convertView
					.findViewById(R.id.textView_category_name);
			convertView.setTag(mViewHolder);
		} else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}
		Category category = getItem(position);
		mViewHolder.mImageView.setImageResource(category.categoryIconId);
		mViewHolder.mTextView.setText(category.getCategoryName());
		convertView.setTag(R.id.textView_name, category);
		return convertView;
	}

	class ViewHolder {
		ImageView mImageView;
		TextView mTextView;
	}
}
