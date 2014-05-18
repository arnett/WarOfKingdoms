package br.edu.ufcg.ccc.projeto2.warofkingdoms.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.view.View.OnTouchListener;
import android.widget.Toast;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Territory;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.util.TerritoryManager;
import br.ufcg.edu.ccc.projeto2.R;

/**
 * 
 * @author Arnett
 * 
 */
public class GameActivity extends Activity implements OnTouchListener {

	private View mapImage;
	private View mapImageMask;
	
	private Bitmap maskImageBitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_game);
		

		mapImage = findViewById(R.id.map);
		mapImageMask = findViewById(R.id.map_mask);
		
		mapImage.setOnTouchListener(this);
		
		mapImageMask.addOnLayoutChangeListener(new OnLayoutChangeListener() {
			@Override
			public void onLayoutChange(View v, int left, int top, int right,
					int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
				reloadMaskImageBitmap();
			}
		});
	}
	
	private void reloadMaskImageBitmap() {
		mapImageMask.setDrawingCacheEnabled(true);
		maskImageBitmap = Bitmap.createBitmap(mapImageMask
				.getDrawingCache());
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

	@Override
	public boolean onTouch(View v, MotionEvent ev) {
		double starTime = System.nanoTime();
		
		final int action = ev.getAction();
		final int motionEventX = (int) ev.getX();
		final int motionEventY = (int) ev.getY();

		int touchedPixelColor = getHotspotColor(R.id.map_mask, motionEventX,
				motionEventY);

		Log.d("Touched Color", String.valueOf(touchedPixelColor));
		
		// When the user touches outside the image itself, onTouch is called
		// because the ImageView matches its parent, i.e. the whole screen, but
		// the image is not stretched to match the whole screen
		if (touchedPixelColor == 0 || touchedPixelColor == -1)
			return false;

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			// TODO Change the texture of the territory where the user touched
			// to pressed_territory, as we would do with buttons.
			break;
		case MotionEvent.ACTION_UP:
			Territory touchedTerritory = TerritoryManager
					.getTerritoryByClosestColor(touchedPixelColor);

			if (touchedTerritory != null) {
				Toast.makeText(getBaseContext(),
						String.valueOf(touchedTerritory), Toast.LENGTH_SHORT)
						.show();
			}
			
			break;
		}
		
		Log.d("onTouch Time", String.valueOf((System.nanoTime() - starTime) / 1000000));
		
		return true;
	}
}