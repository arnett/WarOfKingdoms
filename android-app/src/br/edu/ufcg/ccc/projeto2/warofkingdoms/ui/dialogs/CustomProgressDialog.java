package br.edu.ufcg.ccc.projeto2.warofkingdoms.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants;
import br.ufcg.edu.ccc.projeto2.R;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class CustomProgressDialog extends Dialog {

	private ImageView iv;

	private AdView adView;

	public CustomProgressDialog(
			Context context, 
			int resourceIdOfImage,
			String title) {
		super(context, R.style.TransparentProgressDialog);
		WindowManager.LayoutParams wlmp = getWindow().getAttributes();
		wlmp.gravity = Gravity.CENTER_HORIZONTAL;
		getWindow().setAttributes(wlmp);
		setTitle(title);
		setCancelable(false);
		setOnCancelListener(null);
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		iv = new ImageView(context);
		iv.setImageResource(resourceIdOfImage);
		layout.addView(iv, params);
		addContentView(layout, params);

		adView = new AdView(context);
		adView.setAdSize(AdSize.BANNER);
		adView.setAdUnitId(Constants.AD_UNIT_ID);

		LinearLayout adsLayout = new LinearLayout(context);
		adsLayout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams adsParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		adsParams.setMargins(0, 150, 0, 0);
		adsLayout.setLayoutParams(adsParams);
		
		adsLayout.addView(adView);
		
		// Just for test!
//		AdRequest adRequest = new AdRequest.Builder()
//        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//        .addTestDevice("INSERT_YOUR_HASHED_DEVICE_ID_HERE")
//        .build();
//		
//	    adView.loadAd(adRequest);

		layout.addView(adsLayout);
	}

	@Override
	public void show() {
		super.show();
		RotateAnimation anim = new RotateAnimation(0.0f, 360.0f,
				Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF,
				.5f);
		anim.setInterpolator(new LinearInterpolator());
		anim.setRepeatCount(Animation.INFINITE);
		anim.setDuration(3000);
		iv.setAnimation(anim);
		iv.startAnimation(anim);
		
		if (adView != null) {
			adView.resume();
		}
	}
}