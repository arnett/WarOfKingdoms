package br.edu.ufcg.ccc.projeto2.warofkingdoms.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Action;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.House;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Player;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Territory;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.GameManager;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.NetworkManager;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.TerritoryUIManager;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.networking.ConnectResult;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.networking.SendMovesResult;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.ui.ChooseActionDialogFragment.OnActionSelectedListener;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.ui.enums.SelectionState;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.util.RulesChecker;
import br.ufcg.edu.ccc.projeto2.R;

/**
 * 
 * @author Arnett
 * 
 */
public class GameActivity extends Activity implements OnTouchListener,
OnActionSelectedListener, OnClickListener, OnTaskCompleted {

	private String LOG_TAG = "GameActivity";

	private String CHOOSE_ACTION_DIALOG_FRAGMENT_TAG = "ChooseActionDialogFragmentTag";

	private RelativeLayout tokenLayout;
	private Bitmap imageToken;

	private View mapImage;
	private View mapImageMask;
	private Bitmap maskImageBitmap;
	private Button nextPhaseButton;

	private SelectionState currentActionSelectionState = SelectionState.SELECTING_ORIGIN;

	private GameManager gameManager;
	private NetworkManager networkManager;
	private TerritoryUIManager territoryManager;

	private Territory firstSelectedTerritoryForTheCurrentMove;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		gameManager = GameManager.getInstance();
		networkManager = NetworkManager.getInstance();
		territoryManager = TerritoryUIManager.getInstance();

		setContentView(R.layout.activity_game);

		mapImage = findViewById(R.id.map);
		mapImageMask = findViewById(R.id.map_mask);

		nextPhaseButton = (Button) findViewById(R.id.nextPhaseButton);
		tokenLayout = (RelativeLayout) findViewById(R.id.token);
		tokenLayout.setBackgroundColor(100);

		mapImage.setOnTouchListener(this);
		nextPhaseButton.setOnClickListener(this);

		mapImageMask.addOnLayoutChangeListener(new OnLayoutChangeListener() {
			@Override
			public void onLayoutChange(View v, int left, int top, int right,
					int bottom, int oldLeft, int oldTop, int oldRight,
					int oldBottom) {
				reloadMaskImageBitmap();
			}
		});

		new WaitUntilMapIsDrawnAsyncTask().execute();
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

	private void addTokenToLayout(int imageResource, int x, int y,
			RelativeLayout layout) {
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

	private void drawTerritoryOwnershipTokens() {
		tokenLayout.removeAllViews();
		for (Territory territory : gameManager.getAllTerritories()) {
			if (!territory.isFree()) {
				int tokenImage = getHouseTokenImage(territory.getOwner());
				int centerX = TerritoryUIManager.getInstance()
						.getTerritoryUICenter(territory)
						.getCenterX(getMapWidth());
				int centerY = TerritoryUIManager.getInstance()
						.getTerritoryUICenter(territory)
						.getCenterY(getMapHeight());
				addTokenToLayout(tokenImage, centerX, centerY, tokenLayout);
			}
		}
	}

	private int getHouseTokenImage(House house) {
		if (gameManager.getCurrentPlayer().getHouse().equals(house)) {
			return R.drawable.token_current_player;
		} else {
			return R.drawable.token_enemy;
		}
	}

	@Override
	public boolean onTouch(View touchedView, MotionEvent event) {
		double DEBUG_StartTime = System.nanoTime();

		int action = event.getAction();
		int motionEventX = (int) event.getX();
		int motionEventY = (int) event.getY();

		int touchedPixelColor = getHotspotColor(R.id.map_mask, motionEventX,
				motionEventY);

		Log.v(LOG_TAG,
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

		Log.v(LOG_TAG, "onTouch total time was "
				+ (System.nanoTime() - DEBUG_StartTime) / 1000000);
		return true;
	}

	private void processTouch(int motionEventX, int motionEventY,
			int touchedPixelColor) {
		switch (currentActionSelectionState) {
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
		String touchedTerritoryName = territoryManager
				.getTerritoryByClosestColor(touchedPixelColor);

		firstSelectedTerritoryForTheCurrentMove = gameManager
				.getTerritoryByName(touchedTerritoryName);

		Log.d(LOG_TAG, "touched territory is "
				+ firstSelectedTerritoryForTheCurrentMove);

		RulesChecker rulesChecker = RulesChecker.getInstance();

		if(rulesChecker.checkTerritorryAlreadyAnOrigin(firstSelectedTerritoryForTheCurrentMove)) {
			Toast.makeText(getBaseContext(), "Invalid move: "
					+ "This territory was already used during this turn", Toast.LENGTH_SHORT).show();
		}

		else if(!rulesChecker.checkOriginIsOwnedByPlayer(firstSelectedTerritoryForTheCurrentMove)) {
			Toast.makeText(getBaseContext(), "Invalid move: "
					+ "The first territory must be owned by you", Toast.LENGTH_SHORT).show();
		}

		else {

			Action[] applicableActionsToThisTerritory = gameManager
					.getApplicableActions(firstSelectedTerritoryForTheCurrentMove);

			startActionPopup(applicableActionsToThisTerritory);
		}
	}

	private void processSecondTouch(int motionEventX, int motionEventY,
			int touchedPixelColor) {
		String touchedTerritoryName = territoryManager
				.getTerritoryByClosestColor(touchedPixelColor);

		Territory touchedTerritory = gameManager
				.getTerritoryByName(touchedTerritoryName);

		RulesChecker rulesChecker = RulesChecker.getInstance();

		if (rulesChecker.checkTargetIsOwnedByPlayer(touchedTerritory)) {
			Toast.makeText(getBaseContext(), "Invalid move: "
					+ "This territory is owned by you.", Toast.LENGTH_SHORT).show();
		}
		else if (!rulesChecker.checkTargetIsAdjacentToOrigin(firstSelectedTerritoryForTheCurrentMove, touchedTerritory)) {
			Toast.makeText(getBaseContext(), "Invalid move: "
					+ "Target is not adjacent to the origin.", Toast.LENGTH_SHORT).show();
		}

		else if (rulesChecker.checkTerritorryAlreadyATarget(touchedTerritory)) {
			Toast.makeText(getBaseContext(), "Invalid move: "
					+ "You already attacked this territory.", Toast.LENGTH_SHORT).show();
		}

		else {
			gameManager.makeAttackMove(firstSelectedTerritoryForTheCurrentMove,
					touchedTerritory);

			int xTerritoryCenter = territoryManager.getTerritoryUICenter(
					touchedTerritory).getCenterX(getMapWidth());
			int yTerritoryCenter = territoryManager.getTerritoryUICenter(
					touchedTerritory).getCenterY(getMapHeight());
			addTokenToLayout(R.drawable.token_attack, xTerritoryCenter,
					yTerritoryCenter, tokenLayout);

			currentActionSelectionState = SelectionState.SELECTING_ORIGIN;
		}
	}

	private void startActionPopup(Action[] actions) {
		ChooseActionDialogFragment chooseActionDialogFragment = new ChooseActionDialogFragment();
		chooseActionDialogFragment.setActions(actions);
		chooseActionDialogFragment.show(getFragmentManager(),
				CHOOSE_ACTION_DIALOG_FRAGMENT_TAG);
	}

	@Override
	public void onActionSelected(Action chosenAction) {
		switch (chosenAction) {
		case ATTACK:
			currentActionSelectionState = SelectionState.SELECTING_TARGET;
			break;
		case DEFEND:
			gameManager.makeDefendMove(firstSelectedTerritoryForTheCurrentMove);

			int xTerritoryCenter = territoryManager.getTerritoryUICenter(
					firstSelectedTerritoryForTheCurrentMove).getCenterX(
							getMapWidth());
			int yTerritoryCenter = territoryManager.getTerritoryUICenter(
					firstSelectedTerritoryForTheCurrentMove).getCenterY(
							getMapHeight());
			addTokenToLayout(R.drawable.token_defense, xTerritoryCenter,
					yTerritoryCenter, tokenLayout);

			currentActionSelectionState = SelectionState.SELECTING_ORIGIN;
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.nextPhaseButton:
			networkManager
			.sendCurrentMoves(this, gameManager.getCurrentMoves());
			gameManager.startNextPhase();
			break;
		default:
			return;
		}
		// TODO make a loading dialog
	}

	@Override
	public void onSendMovesTaskCompleted(SendMovesResult result) {
		// TODO close loading dialog window
		// TODO manage conflicts here

		if (result.getConflicts() == null || result.getConflicts().size() != 0) {
			Toast.makeText(getBaseContext(),
					"number of conflicts = " + result.getConflicts().size(),
					Toast.LENGTH_SHORT).show();

			// TODO show the dice value for each conflict on the result
		}

		gameManager.updateAllTerritories(result.getUpdatedMap());
		drawTerritoryOwnershipTokens();
		
		Toast.makeText(getBaseContext(),
				"Turn: " + result.getGameState().getCurrentTurn() + "/" + result.getGameState().getTotalTurns(),
				Toast.LENGTH_SHORT).show();
		
		if (result.getGameState().isGameEnd()) {
			String gameStatus = "Game Finished\n";
			if (result.getGameState().getWinnerList().size() > 0) {
				gameStatus += "Winner(s):";
				for (Player p : result.getGameState().getWinnerList()) {
					gameStatus += p.getName() + "\n";
				}
			} else {
				gameStatus += "Draw";
			}
			Toast.makeText(getBaseContext(),
					gameStatus,
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onConnectTaskCompleted(ConnectResult result) {
		// TODO Remove this method from OnTaskCompleted and create a global
		// interface with a request id (as we do in startActivityForResult(act,
		// i)) or create multiple listeners, each for a single asynchronous
		// task.
	}

	private int getMapWidth() {
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

	private int getMapHeight() {
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

	private class WaitUntilMapIsDrawnAsyncTask extends
	AsyncTask<Void, Void, Void> {

		/**
		 * The map image is only plotted when the activity is loaded, i.e. when
		 * the state of the activity is about to become "Running". Since the
		 * centers of the territories are calculated based on the current size
		 * of the map, the territory centers don't exist before this.
		 */
		@Override
		protected Void doInBackground(Void... arg0) {
			/**
			 * Only one thread should handle the UI.
			 */
			runOnUiThread(new Runnable() {

				/**
				 * The map image is only plotted when the activity is loaded, i.e.
				 * when the state of the activity is about to become "Running".
				 * Since the centers of the territories are calculated based on the
				 * current size of the map, the territory centers don't exist before
				 * this.
				 */
				@Override
				public void run() {
					while (getMapWidth() == 0) {

					}

					drawTerritoryOwnershipTokens();
				}

			});
			return null;
		}

	}
}
