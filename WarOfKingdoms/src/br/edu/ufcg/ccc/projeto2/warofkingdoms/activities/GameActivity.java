package br.edu.ufcg.ccc.projeto2.warofkingdoms.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
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

	@SuppressWarnings("unused")
	private View mapImageMask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_game);

		mapImage = findViewById(R.id.map);
		mapImageMask = findViewById(R.id.map_mask);

		mapImage.setOnTouchListener(this);
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
		ImageView maskImage = (ImageView) findViewById(hotspotId);
		maskImage.setDrawingCacheEnabled(true);
		Bitmap maskImageBitmap = Bitmap.createBitmap(maskImage
				.getDrawingCache());
		maskImage.setDrawingCacheEnabled(false);
		return maskImageBitmap.getPixel(x, y);
	}

	@Override
	public boolean onTouch(View v, MotionEvent ev) {
		final int action = ev.getAction();
		final int motionEventX = (int) ev.getX();
		final int motionEventY = (int) ev.getY();

		int touchedPixelColor = getHotspotColor(R.id.map_mask, motionEventX,
				motionEventY);

		// When the user touches outside the image itself, onTouch is called
		// because the ImageView matches its parent, i.e. the whole screen, but
		// the image is not stretched to match the whole screen
		if (touchedPixelColor == 0) {
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

			if (touchedTerritory != null) {
				Toast.makeText(getBaseContext(),
						String.valueOf(touchedTerritory), Toast.LENGTH_SHORT)
						.show();
			}
			break;
		}
		return true;
	}
}