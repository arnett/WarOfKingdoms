package br.edu.ufcg.ccc.projeto2.warofkingdoms.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Action;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Conflict;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.GameState;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.House;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Move;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Player;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Territory;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.CommunicationManager;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.GameManager;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.HouseTokenManager;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.NetworkManager;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.ProfileManager;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.TerritoryUIManager;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.networking.ConnectResult;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.networking.SendMovesResult;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.ui.ChooseActionDialogFragment.OnActionSelectedListener;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.ui.entities.HouseToken;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.ui.enums.SelectionState;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.util.RulesChecker;
import br.ufcg.edu.ccc.projeto2.R;

/**
 * 
 * @author Arnett
 * 
 */
public class GameActivity extends Activity implements OnTouchListener,
OnActionSelectedListener, OnClickListener, OnTaskCompleted {

	private static final int SOLVE_CONFLICT_RETURN = 1;

	private boolean isOpenningConflictActivity = false; // to just close the
	// waitDialog when the
	// activity is started

	private String LOG_TAG = "GameActivity";

	private String CHOOSE_ACTION_DIALOG_FRAGMENT_TAG = "ChooseActionDialogFragmentTag";
	private String GAME_OVER_DIALOG_FRAGMENT_TAG = "GameOverDialogFragmentTag";

	private RelativeLayout tokenLayout;
	private Bitmap imageToken;

	private View mapImage;
	private View mapImageMask;
	private Bitmap maskImageBitmap;
	private ImageView nextPhaseButton;
	private ImageView objectiveButton;
	private TextView timeCounter;

	private SelectionState currentActionSelectionState = SelectionState.SELECTING_ORIGIN;

	private GameManager gameManager;
	private CommunicationManager communicationManager;
	private TerritoryUIManager territoryManager;
	private HouseTokenManager houseTokenManager;
	private ProfileManager profileManager;

	private Territory firstSelectedTerritoryForTheCurrentMove;

	private ImageView currentPlayerToken;

	private MyCountDownTimer countDown;

	private CustomProgressDialog waitDialog;

	private ChooseActionDialogFragment chooseActionDialogFragment;

	private long startTime = 65 * 1000;
	private long interval = 1 * 1000;
	private long seconds = startTime / 1000;
	private long minutes = seconds / 60;

	//	TODO MOVE TO ANOTHER CLASS
	private String[] NorthTerritories = new String[] {"A", "B", "C", "D", "E", "F", "G", "H"};
	private String[] CenterTerritories = new String[] {"I", "J", "K", "L", "M", "N", "O", "P", "Q"};
	private String[] SouthTerritories = new String[] {"R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		gameManager = GameManager.getInstance();
		territoryManager = TerritoryUIManager.getInstance();
		houseTokenManager = HouseTokenManager.getInstance();
		profileManager = ProfileManager.getInstance();
		communicationManager = NetworkManager.getInstance();

		setContentView(R.layout.map_screen);

		waitDialog = new CustomProgressDialog(this, R.drawable.progress, null);

		mapImage = findViewById(R.id.map);
		mapImageMask = findViewById(R.id.map_mask);

		nextPhaseButton = (ImageView) findViewById(R.id.nextPhaseButton);
		objectiveButton = (ImageView) findViewById(R.id.objectiveButton);
		timeCounter = (TextView) findViewById(R.id.time_counter);
		tokenLayout = (RelativeLayout) findViewById(R.id.token);
		tokenLayout.setBackgroundColor(100);

		mapImage.setOnTouchListener(this);
		nextPhaseButton.setOnClickListener(this);
		objectiveButton.setOnClickListener(this);

		currentPlayerToken = (ImageView) findViewById(R.id.currentPlayerToken);
		drawCurrentPlayerToken();

		mapImageMask.addOnLayoutChangeListener(new OnLayoutChangeListener() {
			@Override
			public void onLayoutChange(View v, int left, int top, int right,
					int bottom, int oldLeft, int oldTop, int oldRight,
					int oldBottom) {
				reloadMaskImageBitmap();
			}
		});
		long formatedSeconds = seconds % 60;
		timeCounter.setText(String.format("%02d", minutes) + ":"
				+ String.format("%02d", formatedSeconds));

		countDown = new MyCountDownTimer(startTime, interval);

		countDown.start();

		showDialog(
				"Objective",
				"You have to conquer at least 3 territories in each region (North, Center and South), and keep you initial territory");
	}

	private void showDialog(String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.TempDialogTheme);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("OK", null);
		builder.create().show();
	}

	private void drawCurrentPlayerToken() {
		House currentHouse = GameManager.getInstance().getCurrentPlayer()
				.getHouse();
		HouseToken playerHouseToken = houseTokenManager
				.getHouseToken(currentHouse);
		currentPlayerToken.setImageResource(playerHouseToken.getCastleToken());
	}

	private boolean areFirstTokensDrawn = true;

	private SendMovesResult sendMovesResult;

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

		/**
		 * The map image is only plotted when the activity is loaded, i.e. when
		 * the state of the activity is about to become "Running". Since the
		 * centers of the territories are calculated based on the current size
		 * of the map, the territory centers don't exist before this.
		 */
		if (areFirstTokensDrawn) {
			drawTerritoryOwnershipTokens();
			areFirstTokensDrawn = false;
		}
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
				int tokenImage = getTokenImage(territory);
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

	private void drawActionTokens() {
		for (Move move : gameManager.getCurrentMoves()) {
			if (move.getAction().equals(Action.ATTACK)) {
				int xTerritoryCenter = territoryManager.getTerritoryUICenter(
						move.getTarget()).getCenterX(getMapWidth());
				int yTerritoryCenter = territoryManager.getTerritoryUICenter(
						move.getTarget()).getCenterY(getMapHeight());
				addTokenToLayout(R.drawable.token_attack, xTerritoryCenter,
						yTerritoryCenter, tokenLayout);
			}
			else if (move.getAction().equals(Action.DEFEND)) {
				int xTerritoryCenter = territoryManager.getTerritoryUICenter(
						move.getTarget()).getCenterX(getMapWidth());
				int yTerritoryCenter = territoryManager.getTerritoryUICenter(
						move.getTarget()).getCenterY(getMapHeight());
				addTokenToLayout(R.drawable.token_defense, xTerritoryCenter,
						yTerritoryCenter, tokenLayout);
			}
		}
	}

	private int getTokenImage(Territory territory) {
		return houseTokenManager.getTokenImage(territory);
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
		
		Log.d(LOG_TAG, "touched position X "
				+ motionEventX +" Y "+motionEventY);
		
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

		if(rulesChecker.isTerritoryAlreadyATarget(firstSelectedTerritoryForTheCurrentMove)) {
			Action[] applicableActionsToThisTerritory = gameManager
					.getCancelActions(firstSelectedTerritoryForTheCurrentMove);

			startCancelMovePopup(applicableActionsToThisTerritory);
		}
		else if (rulesChecker
				.isTerritoryAlreadyAnOrigin(firstSelectedTerritoryForTheCurrentMove)) {
			Toast.makeText(
					getBaseContext(),
					"Invalid move: "
							+ "This territory was already used during this turn",
							Toast.LENGTH_SHORT).show();
		}

		else if (!rulesChecker
				.isTerritoryOwnedByTheCurrentPlayer(firstSelectedTerritoryForTheCurrentMove)) {
			Toast.makeText(
					getBaseContext(),
					"Invalid move: "
							+ "The first territory must be owned by you",
							Toast.LENGTH_SHORT).show();
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

		if (rulesChecker.isTerritoryOwnedByTheCurrentPlayer(touchedTerritory)) {
			Toast.makeText(getBaseContext(),
					"Invalid move: " + "This territory is owned by you.",
					Toast.LENGTH_SHORT).show();
		} else if (!rulesChecker.isTargetAdjacentToOrigin(
				firstSelectedTerritoryForTheCurrentMove, touchedTerritory)) {
			Toast.makeText(getBaseContext(),
					"Invalid move: " + "Target is not adjacent to the origin.",
					Toast.LENGTH_SHORT).show();
		} else if (rulesChecker.isTerritoryAlreadyATarget(touchedTerritory)) {
			Toast.makeText(getBaseContext(),
					"Invalid move: " + "You already attacked this territory.",
					Toast.LENGTH_SHORT).show();
		} else {
			gameManager.makeAttackMove(firstSelectedTerritoryForTheCurrentMove,
					touchedTerritory);

			int xTerritoryCenter = territoryManager.getTerritoryUICenter(
					touchedTerritory).getCenterX(getMapWidth());
			int yTerritoryCenter = territoryManager.getTerritoryUICenter(
					touchedTerritory).getCenterY(getMapHeight());
			addTokenToLayout(R.drawable.token_attack, xTerritoryCenter,
					yTerritoryCenter, tokenLayout);
		}
		currentActionSelectionState = SelectionState.SELECTING_ORIGIN;
	}

	private void startActionPopup(Action[] actions) {
		chooseActionDialogFragment = new ChooseActionDialogFragment();
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
		case OK:
			Move moveToRemove = null;
			List<Move> currentMoves = gameManager.getCurrentMoves();
			for (Move move : currentMoves) {
				if(move.getTarget().equals(firstSelectedTerritoryForTheCurrentMove)) {
					moveToRemove = move;
				}
			}
			gameManager.getCurrentMoves().remove(moveToRemove);
			drawTerritoryOwnershipTokens();
			drawActionTokens();	
			currentActionSelectionState = SelectionState.SELECTING_ORIGIN;
			break;
		case CANCEL:
			currentActionSelectionState = SelectionState.SELECTING_ORIGIN;
			break;
		}
	}

	private void startCancelMovePopup(Action[] actions) {
		AlertDialog cancelDialog = new AlertDialog.Builder(this, R.style.TempDialogTheme).create();
		cancelDialog.setTitle("Cancel move");
		cancelDialog.setButton(DialogInterface.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				onActionSelected(Action.OK);
			}
		});
		cancelDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		cancelDialog.show();
	}

	private boolean contains(String[] array, String element) {
		for (String string : array)
			if (string.equalsIgnoreCase(element))
				return true;
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.nextPhaseButton:
			countDown.cancel();
			waitDialog.show();

			communicationManager
			.sendCurrentMoves(this, gameManager.getCurrentMoves());
			gameManager.startNextPhase();
			break;
		case R.id.objectiveButton:
			int north = 0, center = 0, south = 0;
			for (Territory territory : gameManager.getAllTerritories()) {
				if (!territory.isFree() && territory.getOwner().getName().equals(gameManager.getCurrentPlayer().getHouse().getName())) {
					if (contains(NorthTerritories, territory.getName()))
						north++;
					else if (contains(CenterTerritories, territory.getName()))
						center++;
					else if (contains(SouthTerritories, territory.getName()))
						south++;
				}
			}
			showDialog(
					"Objective",
					"North:" + north + "/3\nCenter:" + center + "/3\nSouth:" + south + "/3");
			break;
		default:
			return;
		}
	}

	@Override
	public void onSendMovesTaskCompleted(SendMovesResult result) {
		sendMovesResult = result;
		if(sendMovesResult == null) {
			ErrorAlertDialog errorDialog = new ErrorAlertDialog(this, "Server is down", 
					"The server is not responding.");
			errorDialog.showAlertDialog();

		}
		else {
			if (chooseActionDialogFragment != null && chooseActionDialogFragment.isVisible()) {
				chooseActionDialogFragment.dismiss();
			}
			firstSelectedTerritoryForTheCurrentMove = null;

			if (result.getConflicts() != null && 
					result.getConflicts().size() != 0 && 
					currentPlayerIsInvolved(result.getConflicts())) {

				isOpenningConflictActivity = true;

				Intent intent = new Intent(this, ConflictActivity.class);
				Bundle bundle = new Bundle();
				bundle.putParcelableArrayList("conflicts",
						(ArrayList<? extends Parcelable>) result.getConflicts());
				intent.putExtras(bundle);
				startActivityForResult(intent, SOLVE_CONFLICT_RETURN);

			} else {
				isOpenningConflictActivity = false;
				doActionsAfterSendMovesReturned();
			}

			if (!isOpenningConflictActivity) {
				waitDialog.dismiss();
			}

			resetCountDown();
		}
	}

	/*
	 * Verify if the current player is involved in the current conflicts
	 */
	private boolean currentPlayerIsInvolved(List<Conflict> conflicts) {

		for (Conflict conflict : conflicts) {
			for (House house : conflict.getHouses()) {

				if (house.equals(gameManager.getCurrentPlayer().getHouse())) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	protected void onStop() {
		super.onStop();
		countDown.cancel();
		if (isOpenningConflictActivity) {
			waitDialog.dismiss();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == SOLVE_CONFLICT_RETURN) {
			doActionsAfterSendMovesReturned();
			isOpenningConflictActivity = false;
			waitDialog.dismiss();
			resetCountDown();
		}

	}

	private void resetCountDown() {
		countDown = new MyCountDownTimer(startTime, interval);
		long formatedSeconds = seconds % 60;
		timeCounter.setText(String.format("%02d", minutes) + ":"
				+ String.format("%02d", formatedSeconds));

		countDown.start();
	}

	private void doActionsAfterSendMovesReturned() {
		gameManager.updateAllTerritories(sendMovesResult.getUpdatedMap());
		drawTerritoryOwnershipTokens();

		if (sendMovesResult.getGameState().isGameFinished()) {
			profileManager.saveGameStatistics(sendMovesResult.getGameState(), getApplicationContext());
			saveGameStatistics(sendMovesResult.getGameState());
			openGameFinishedDialog();
		}
	}

	private void saveGameStatistics(GameState gameState) {

		int numTimesPlayed = loadInt(Constants.NUM_TIMES_PLAYED_KEY);
		int numVictories = loadInt(Constants.NUM_VICTORIES_KEY);

		if (currentPlayerWon(gameState.getWinnerList())) {

			numVictories++;
			saveInt(Constants.NUM_VICTORIES_KEY, numVictories);
		}

		numTimesPlayed++;
		saveInt(Constants.NUM_TIMES_PLAYED_KEY, numTimesPlayed);
	}

	public void saveInt(String key, int value) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public int loadInt(String key) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		return sharedPreferences.getInt(key, 0);
	}

	private boolean currentPlayerWon(List<Player> winners) {

		for (Player player : winners) {

			if (gameManager.getCurrentPlayer().getId().equals(player.getId())) {
				return true;
			}
		}

		return false;
	}

	private void openGameFinishedDialog() {
		countDown.cancel();
		GameOverDialogFragment gameOverDialog = new GameOverDialogFragment();
		gameOverDialog.setWinners(getWinners());
		gameOverDialog
		.show(getFragmentManager(), GAME_OVER_DIALOG_FRAGMENT_TAG);
	}

	private String getWinners() {
		String winners = "";
		for (Player p : sendMovesResult.getGameState().getWinnerList()) {
			winners += p.getName() + " (" + p.getHouse().getName() + ") won!\n";
		}
		return winners;
	}

	@Override
	protected void onDestroy() {
		countDown.cancel();
		if (waitDialog.isShowing()) {
			waitDialog.dismiss();
		}

		Log.v(LOG_TAG, "Destroying activity");

		super.onDestroy();
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
		return mapImageView.getMeasuredWidth();
	}

	private int getMapHeight() {
		ImageView mapImageView = (ImageView) mapImage;
		return mapImageView.getMeasuredHeight();
	}

	/**
	 * @author Rafael
	 *	A class to manipulate the game's countdown timer.
	 */
	private class MyCountDownTimer extends CountDownTimer {

		public MyCountDownTimer(long startTime, long interval) {
			super(startTime, interval);
		}

		/* (non-Javadoc)
		 * @see android.os.CountDownTimer#onFinish()
		 */
		@Override
		public void onFinish() {
			timeCounter.setText("00:00");
			waitDialog.show();
			communicationManager
			.sendCurrentMoves(GameActivity.this, gameManager.getCurrentMoves());
			gameManager.startNextPhase();
		}

		/* (non-Javadoc)
		 * @see android.os.CountDownTimer#onTick(long)
		 */
		@Override
		public void onTick(long millisUntilFinished) {
			long seconds = millisUntilFinished / 1000;
			long formatedSeconds = seconds % 60;
			long minutes = seconds / 60;
			if (minutes == 0 && seconds <= 10) {
				timeCounter.setTextColor(Color.parseColor("#FF0000"));
			} else {
				timeCounter.setTextColor(Color.parseColor("#000000"));
			}
			timeCounter.setText(String.format("%02d", minutes) + ":"
					+ String.format("%02d", formatedSeconds));
		}
	}
}