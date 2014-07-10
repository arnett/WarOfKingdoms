package br.edu.ufcg.ccc.projeto2.warofkingdoms.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import br.ufcg.edu.ccc.projeto2.R;

public class TutorialActivity extends Activity {
	private ImagePagerAdapter adapter;
	private boolean callHappened;
	private boolean mPageEnd;
	private int selectedIndex;
	private TutorialActivity context = this;
	private ViewPager viewPager;
	
	private final int[] tutorialTexts = new int[] {
			R.string.tutorial_txt1,
			R.string.tutorial_txt2,
			R.string.tutorial_txt3,
			R.string.tutorial_txt4,
			R.string.tutorial_txt5,
			R.string.tutorial_txt6b,
			R.string.tutorial_txt6,
			R.string.tutorial_txt6a,
			R.string.tutorial_txt7,
			R.string.tutorial_txt8,
			R.string.tutorial_txt8a,
			R.string.tutorial_txt9,
			R.string.tutorial_txt9a
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tutorial_activity);
		viewPager = (ViewPager) findViewById(R.id.view_pager);
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
					mPageEnd = false;//To avoid multiple calls. 
					callHappened = true;
					startActivity(new Intent(context, ConnectActivity.class));
					context.finish();
				}else
				{
					TextView tutorialTxt = (TextView) findViewById(R.id.tutorial_text);
					tutorialTxt.setText(getString(tutorialTexts[viewPager.getCurrentItem()]));

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
		viewPager.setPageTransformer(false, new SlidePageTransformer());
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		startActivity(new Intent(context, ConnectActivity.class));
		context.finish();	
	}

	private class ImagePagerAdapter extends PagerAdapter {
	
		private int[] tutorialImages = new int[] {
				R.drawable.tutorial1,
				R.drawable.tutorial2,
				R.drawable.tutorial3,
				R.drawable.tutorial4,
				R.drawable.tutorial5,
				R.drawable.tutorial6b,
				R.drawable.tutorial6,
				R.drawable.tutorial6a,
				R.drawable.tutorial7,
				R.drawable.tutorial8,
				R.drawable.tutorial8a,
				R.drawable.tutorial9,
				R.drawable.tutorial9a
		};
		
		@Override
		public int getCount() {
			return tutorialImages.length;
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
			imageView.setImageResource(tutorialImages[position]);
			((ViewPager) container).addView(imageView, 0);
			
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((ImageView) object);
		}
		
	}

	public class SlidePageTransformer implements ViewPager.PageTransformer {
        private float MIN_SCALE = 0.85f;
        private float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                        (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }

    }

}