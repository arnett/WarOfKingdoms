package br.edu.ufcg.ccc.projeto2.warofkingdoms.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import br.ufcg.edu.ccc.projeto2.R;

public class TutorialActivity extends Activity {
	private ImagePagerAdapter adapter;
	private boolean callHappened;
	private boolean mPageEnd;
	private int selectedIndex;
	private TutorialActivity context = this;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tutorial_activity);
		ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
		adapter = new ImagePagerAdapter();
		viewPager.setAdapter(adapter);
		OnPageChangeListener mListener = new OnPageChangeListener() {


			@Override
			public void onPageSelected(int arg0) {
				selectedIndex = arg0;

			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				if( mPageEnd && arg0 == selectedIndex && !callHappened)
				{
					Log.d(getClass().getName(), "Okay");
					mPageEnd = false;//To avoid multiple calls. 
					callHappened = true;
					startActivity(new Intent(context, ConnectActivity.class));
					context.finish();
				}else
				{
					mPageEnd = false;
				}
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				int count = adapter.getCount();
				if(selectedIndex == count - 1)
				{
					mPageEnd = true;
				}
			}
		};
		viewPager.setOnPageChangeListener(mListener);
	}

	private class ImagePagerAdapter extends PagerAdapter {
	
		private int[] mImages = new int[] {
				R.drawable.tutorial1,
				R.drawable.tutorial2,
				R.drawable.tutorial3,
				R.drawable.tutorial4,
				R.drawable.tutorial5,
				R.drawable.tutorial6,
				R.drawable.tutorial7,
				R.drawable.tutorial8,
				R.drawable.tutorial9,
		};

		@Override
		public int getCount() {
			return mImages.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((ImageView) object);
		}


		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Context context = TutorialActivity.this;
			ImageView imageView = new ImageView(context);
			int padding = context.getResources().getDimensionPixelSize(
					R.dimen.padding_medium);
			imageView.setPadding(padding, padding, padding, padding);
			imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			imageView.setImageResource(mImages[position]);
			((ViewPager) container).addView(imageView, 0);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((ImageView) object);
		}

	}


}