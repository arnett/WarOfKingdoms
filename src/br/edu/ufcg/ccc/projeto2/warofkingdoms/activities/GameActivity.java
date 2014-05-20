package br.edu.ufcg.ccc.projeto2.warofkingdoms.activities;

import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.GAME_ACTIVITY_LOG_TAG;
import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Territory;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.util.TerritoryManager;
import br.ufcg.edu.ccc.projeto2.R;

/**
 * 
 * @author Arnett
 * 
 */
public class GameActivity extends Activity implements OnTouchListener {

	private RelativeLayout tokenLayout;

	private View mapImage;
	private View mapImageMask;

	private Bitmap maskImageBitmap;

	private Bitmap imageToken;

	private String CHOOSE_ACTION_DIALOG_FRAGMENT_TAG = "CHOOSE_ACTION_DIALOG_FRAGMENT_TAG";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_game);

		mapImage = findViewById(R.id.map);
		mapImageMask = findViewById(R.id.map_mask);
		tokenLayout = (RelativeLayout) findViewById(R.id.token);
		tokenLayout.setBackgroundColor(100);

		imageToken = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.ic_launcher);

		mapImage.setOnTouchListener(this);

		mapImageMask.addOnLayoutChangeListener(new OnLayoutChangeListener() {
			@Override
			public void onLayoutChange(View v, int left, int top, int right,
					int bottom, int oldLeft, int oldTop, int oldRight,
					int oldBottom) {
				reloadMaskImageBitmap();
			}
		});
	}

	/**
	 * Method to recreate the maskImageBitmap, it is used when the layout
	 * changes (mainly for screen rotation).
	 */
	private void reloadMaskImageBitmap() {
		mapImageMask.setDrawingCacheEnabled(true);
		maskImageBitmap = Bitmap.createBitmap(mapImageMask.getDrawingCache());
		mapImageMask.setDrawingCacheEnabled(false);
	}

	/**
	 * Returns the color of the pixel <tt>(x, y)</tt> on the image represented
	 * by <tt>hotspotId</tt>
	 * 
	 * @param activity
	 *            the activity that contains the mask image
	 * @param hotspotId
	 *            the hotspot id of the mask image
	 * @param x
	 *            the x position of the pixel to be look at
	 * @param y
	 *            the y position of the pixel to be look at
	 * @return
	 */
	private int getHotspotColor(int hotspotId, int x, int y) {
		return maskImageBitmap.getPixel(x, y);
	}

	private void addTokenToLayout(int x, int y, RelativeLayout layout) {
		// Centralize the image
		x -= imageToken.getWidth() / 2;
		y -= imageToken.getHeight() / 2;

		ImageView imageView = new ImageView(this);
		imageView.setImageBitmap(imageToken);

		// Parameter to place the image in the right place, centered in x, y
		// Create a param object with same dimensions as imageToken
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				imageToken.getWidth(), imageToken.getHeight());
		// The image will be placed 'x' units far from the left border
		// and y far from the top border
		params.leftMargin = x;
		params.topMargin = y;

		layout.addView(imageView, params);
	}

	@Override
	public boolean onTouch(View touchedView, MotionEvent event) {
		double DEBUG_StartTime = System.nanoTime();

		final int action = event.getAction();
		final int motionEventX = (int) event.getX();
		final int motionEventY = (int) event.getY();

		int touchedPixelColor = getHotspotColor(R.id.map_mask, motionEventX,
				motionEventY);

		Log.v(GAME_ACTIVITY_LOG_TAG,
				"touched pixel color is "
						+ Integer.toHexString(touchedPixelColor));

		// When the user touches outside the image itself, onTouch is called
		// because the ImageView matches its parent, i.e. the whole screen, but
		// the image is not stretched to match the whole screen
		if (touchedPixelColor == 0 || touchedPixelColor == -1) {
			return false;
		}

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			// TODO Change the texture of the territory where the user touched
			// to pressed_territory, as we would do with buttons.
			break;
		case MotionEvent.ACTION_UP:
			Territory touchedTerritory = TerritoryManager
					.getTerritoryByClosestColor(touchedPixelColor);

			Log.d(GAME_ACTIVITY_LOG_TAG, "touched territory is " + touchedTerritory);

			startDialog();

			addTokenToLayout(motionEventX, motionEventY, tokenLayout);

			Log.v(GAME_ACTIVITY_LOG_TAG, "onTouch total time was "
					+ (System.nanoTime() - DEBUG_StartTime) / 1000000);
			break;
		}

		return true;
	}

	private void startDialog() {
		DialogFragment chooseActionDialogFragment = new ChooseActionDialogFragment();
		chooseActionDialogFragment.show(getFragmentManager(), CHOOSE_ACTION_DIALOG_FRAGMENT_TAG);
	}
}