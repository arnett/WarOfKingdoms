package br.edu.ufcg.ccc.projeto2.warofkingdoms.activities;

import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.GAME_ACTIVITY_LOG_TAG;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.activities.ChooseActionDialogFragment.OnActionSelectedListener;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.activities.enums.SelectionState;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Action;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Territory;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.GameManager;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.util.TerritoryManager;
import br.ufcg.edu.ccc.projeto2.R;

/**
 * 
 * @author Arnett
 * 
 */
public class GameActivity extends Activity implements OnTouchListener,
		OnActionSelectedListener {

	private String CHOOSE_ACTION_DIALOG_FRAGMENT_TAG = "ChooseActionDialogFragmentTag";

	private RelativeLayout tokenLayout;
	private Bitmap imageToken;

	private View mapImage;
	private View mapImageMask;
	private Bitmap maskImageBitmap;
	
	String outputMessage;
	String inputMessage;

	private SelectionState currentSelectionState = SelectionState.SELECTING_ORIGIN;

	private GameManager gameManager = GameManager.getInstance();

	private Territory firstSelectedTerritory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_game);

		mapImage = findViewById(R.id.map);
		mapImageMask = findViewById(R.id.map_mask);
		tokenLayout = (RelativeLayout) findViewById(R.id.token);
		tokenLayout.setBackgroundColor(100);

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

	private void addTokenToLayout(int imageResource, int x, int y, RelativeLayout layout) {
		imageToken = BitmapFactory.decodeResource(this.getResources(),
				imageResource);
		
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

		int action = event.getAction();
		int motionEventX = (int) event.getX();
		int motionEventY = (int) event.getY();

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
			processTouch(motionEventX, motionEventY, touchedPixelColor);
			break;
		}

		Log.v(GAME_ACTIVITY_LOG_TAG,
				"onTouch total time was "
						+ (System.nanoTime() - DEBUG_StartTime) / 1000000);
		return true;
	}

	private void processTouch(int motionEventX, int motionEventY,
			int touchedPixelColor) {
		switch (currentSelectionState) {
		case SELECTING_ORIGIN:
			processFirstTouch(motionEventX, motionEventY, touchedPixelColor);
			break;
		case SELECTING_TARGET:
			processSecondTouch(motionEventX, motionEventY, touchedPixelColor);
			break;
		}
	}

	private void processFirstTouch(int motionEventX, int motionEventY,
			int touchedPixelColor) {
		firstSelectedTerritory = TerritoryManager
				.getTerritoryByClosestColor(touchedPixelColor);

		Log.d(GAME_ACTIVITY_LOG_TAG, "touched territory is "
				+ firstSelectedTerritory);
		outputMessage = "FirstClick;First Click: touched territory is " + firstSelectedTerritory.toString();
		MyTask task = new MyTask();
		task.execute();
		while (inputMessage == null) {
		}
		
		Toast.makeText(getApplicationContext(), inputMessage, 
				Toast.LENGTH_SHORT).show();

		inputMessage = null;
		Action[] appliableActionsToThisTerritory = firstSelectedTerritory
				.getCurrentState().getApplicableActions();
		startActionPopup(appliableActionsToThisTerritory);
	}

	private void processSecondTouch(int motionEventX, int motionEventY,
			int touchedPixelColor) {
		Territory touchedTerritory = TerritoryManager
				.getTerritoryByClosestColor(touchedPixelColor);

		gameManager.makeAttackMove(firstSelectedTerritory, touchedTerritory);
		addTokenToLayout(R.drawable.token_attack, touchedTerritory.getCenterX(getScreenWidth()),
				touchedTerritory.getCenterY(getScreenHeight()), tokenLayout);
		outputMessage = "AttackSecondClick;First Click: Attack origin is: " + firstSelectedTerritory.toString() + 
				" - Attack target is: " + touchedTerritory;
		MyTask task = new MyTask();
		task.execute();
		while (inputMessage == null) {
		}
		Toast.makeText(getApplicationContext(), inputMessage, 
				Toast.LENGTH_SHORT).show();
		currentSelectionState = SelectionState.SELECTING_ORIGIN;
		inputMessage = null;
	}

	private void startActionPopup(Action[] actions) {
		ChooseActionDialogFragment chooseActionDialogFragment = new ChooseActionDialogFragment();
		chooseActionDialogFragment.setActions(actions);
		chooseActionDialogFragment.show(getFragmentManager(),
				CHOOSE_ACTION_DIALOG_FRAGMENT_TAG);
	}

	@Override
	public void onActionSelected(Action chosenAction) {
		MyTask task = null;
		switch (chosenAction) {
		case ATTACK:
			currentSelectionState = SelectionState.SELECTING_TARGET;
			outputMessage = "Attack;Attack: Attack move selected";
			task = new MyTask();
			task.execute();
			while (inputMessage == null) {
			}
			Toast.makeText(getApplicationContext(), inputMessage, 
					Toast.LENGTH_SHORT).show();
			break;
		case DEFEND:
			gameManager.makeDefendMove(firstSelectedTerritory);
			addTokenToLayout(R.drawable.token_defense,
					firstSelectedTerritory.getCenterX(getScreenWidth()),
					firstSelectedTerritory.getCenterY(getScreenHeight()),
					tokenLayout);
			currentSelectionState = SelectionState.SELECTING_ORIGIN;
			outputMessage = "Defend;Defend: Defend move selected";
			task = new MyTask();
			task.execute();
			while (inputMessage == null) {
			}

			Toast.makeText(getApplicationContext(), inputMessage, 
					Toast.LENGTH_SHORT).show();
			break;
		}
		inputMessage = null;
	}

	private int getScreenWidth() {
		ImageView mapImageView = (ImageView) mapImage;
		int ih = mapImageView.getMeasuredHeight();
		int iw = mapImageView.getMeasuredWidth();
		int iH = mapImageView.getDrawable().getIntrinsicHeight();
		int iW = mapImageView.getDrawable().getIntrinsicWidth();

		if (ih / iH <= iw / iW) {
			iw = iW * ih / iH;
		}

		return iw;
	}

	private int getScreenHeight() {
		ImageView mapImageView = (ImageView) mapImage;
		int ih = mapImageView.getMeasuredHeight();
		int iw = mapImageView.getMeasuredWidth();
		int iH = mapImageView.getDrawable().getIntrinsicHeight();
		int iW = mapImageView.getDrawable().getIntrinsicWidth();

		if (ih / iH > iw / iW) {
			iw = iW * ih / iH;
		}

		return ih;
	}
	
	private class MyTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... arg0) {
			Socket socket = null;
			DataOutputStream dataOutputStream = null;
			DataInputStream dataInputStream = null;

			try {
				socket = new Socket("192.168.43.168", 8888);
				dataOutputStream = new DataOutputStream(socket.getOutputStream());
				dataInputStream = new DataInputStream(socket.getInputStream());
				dataOutputStream.writeUTF(outputMessage);
				inputMessage = dataInputStream.readUTF();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally{
				if (socket != null){
					try {
						socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (dataOutputStream != null){
					try {
						dataOutputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				if (dataInputStream != null){
					try {
						dataInputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			return null;
		}
	}
}
