
package com.rs.mobile.wportal.adapter.sm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rs.mobile.common.C;
import com.rs.mobile.common.L;
import com.rs.mobile.common.image.ImageUtil;
import com.rs.mobile.common.util.PageUtil;
import com.rs.mobile.common.view.WImageView;
import com.rs.mobile.wportal.activity.sm.SmGoodsDetailActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class SmViewPagerAdapter extends PagerAdapter {

	private Context context;

	private JSONArray arr;

	private int count;

	private float ratio;

	public SmViewPagerAdapter(Context context, JSONObject json) {
		// TODO Auto-generated constructor stub
		this.context = context;
		try {
			this.arr = json.getJSONArray(C.KEY_JSON_BANNERAD);
			this.count = arr.length();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			L.e(e);
		}
	}

	public SmViewPagerAdapter(Context context, JSONObject json, float ratio) {
		// TODO Auto-generated constructor stub
		this.context = context;
		try {
			this.arr = json.getJSONArray(C.KEY_JSON_BANNER);
			this.count = arr.length();
			this.ratio = ratio;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			L.e(e);
		}
	}

	@Override
	public int getCount() {

		// TODO Auto-generated method stub
		return count;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {

		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		View view = null;
		view = LayoutInflater.from(context).inflate(com.rs.mobile.wportal.R.layout.layout_cviewpager_item, null);
		// ImageView imageView = (ImageView)
		// view.findViewById(R.id.cviewpager_item_img);

		WImageView wImageView = (WImageView) view.findViewById(com.rs.mobile.wportal.R.id.cviewpager_item_freimg);
		WImageView wImageView2 = (WImageView) view.findViewById(com.rs.mobile.wportal.R.id.cviewpager_item_freimg1);
		wImageView.setVisibility(view.GONE);
		wImageView2.setVisibility(view.VISIBLE);
		if (ratio != 0) {
			wImageView2.setAspectRatio(ratio);

		}

		try {

			final JSONObject jsonObject = new JSONObject(arr.get(position).toString());

			ImageUtil.drawImageView1(context, wImageView2, jsonObject, C.KEY_JSON_ADIMAGE, C.KEY_JSON_VERSION);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					// TODO Auto-generated method stub
					String item_code = null;
					try {
						item_code = jsonObject.getString("item_code");
						Bundle bundle = new Bundle();
						bundle.putString("item_code", item_code);
						bundle.putString(C.KEY_DIV_CODE, C.DIV_CODE);
						PageUtil.jumpTo(context, SmGoodsDetailActivity.class, bundle);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		container.addView(view);
		return view;

	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {

		// TODO Auto-generated method stub
		container.removeView((View) object);
	}

}
