package br.edu.ufcg.ccc.projeto2.warofkingdoms.ui;

import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.GAME_MODE;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.MULTIPLAYER_GAME_MODE;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.SINGLEPLAYER_GAME_MODE;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Player;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.AIManager;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.CommunicationManager;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.GameManager;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.HouseTokenManager;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.NetworkManager;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.networking.ConnectResult;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.networking.SendMovesResult;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.ui.ChooseGameModeDialogFragment.OnGameModeSelectedListener;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.util.ConnectionDetector;
import br.ufcg.edu.ccc.projeto2.R;

public class ConnectActivity extends Activity implements OnClickListener,
OnTaskCompleted, OnGameModeSelectedListener {

	private final String LOG_TAG = "ConnectActivity";

	private boolean isOpenningGameActivity; // to just close the waitDialog when
	// the activity is started

	private CommunicationManager communicationManager;
	private GameManager gameManager;
	private HouseTokenManager houseTokenManager;

	private Player currentPlayer;

	private ImageView playBtn;
	private ImageView aboutBtn;
	private ImageView profileBtn;

	private CustomProgressDialog waitDialog;

	private String CHOOSE_GAME_DIALOG_FRAGMENT_TAG = "CHOOSE_GAME_DIALOG_FRAGMENT_TAG";

	private String gameMode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_screen);

		resetPreviousGameState();

		gameManager = GameManager.getInstance();
		houseTokenManager = HouseTokenManager.getInstance();

		currentPlayer = gameManager.getCurrentPlayer();

		playBtn = (ImageView) findViewById(R.id.playButton);
		playBtn.setOnClickListener(this);

		aboutBtn = (ImageView) findViewById(R.id.aboutButton);
		aboutBtn.setOnClickListener(this);

		profileBtn = (ImageView) findViewById(R.id.profileButton);
		profileBtn.setOnClickListener(this);

		waitDialog = new CustomProgressDialog(this, R.drawable.progress, null);

		isOpenningGameActivity = false;
	}

	private void resetPreviousGameState() {

		if (gameManager != null) {
			gameManager.reset();
		}
		if (houseTokenManager != null) {
			houseTokenManager.reset();
		}
	}

	@Override
	public void onClick(View v) {
		if (v == playBtn) {

			startChooseGameModeDialog();
		} else if (v == aboutBtn) {

			startActivity(new Intent(this, AboutActivity.class));
		}
		else if (v == profileBtn) {

			Intent intent = new Intent(this, ProfileActivity.class); 
			startActivity(intent);
		}
	}

	private void startChooseGameModeDialog() {
		DialogFragment chooseGameDialogFragment = new ChooseGameModeDialogFragment();
		chooseGameDialogFragment.show(getFragmentManager(),
				CHOOSE_GAME_DIALOG_FRAGMENT_TAG);
	}

	private void startNetworkGame() {
		ConnectionDetector connectionDetector = new ConnectionDetector(
				getApplicationContext());
		if (connectionDetector.isConnectingToInternet()) {
			waitDialog.show();
			communicationManager.connect(this, currentPlayer);
		} else {
			ErrorAlertDialog errorDialog = new ErrorAlertDialog(this, "No Internet Connection", 
					"You don't have internet connection.");
			errorDialog.showAlertDialog();
		}
	}

	private void startAIGame() {
		communicationManager.connect(this, currentPlayer);
	}

	@Override
	protected void onStop() {
		super.onStop();

		if (isOpenningGameActivity) {
			waitDialog.dismiss();
		}
	}

	@Override
	public void onSendMovesTaskCompleted(SendMovesResult result) {
	}

	@Override
	public void onConnectTaskCompleted(ConnectResult result) {
		if (result == null) {
			waitDialog.dismiss();
			ErrorAlertDialog errorDialog = new ErrorAlertDialog(this, "Server is down", 
					"The server is not responding.");
			errorDialog.showAlertDialog();
		}
		else {
			gameManager.updateAllTerritories(result.getTerritories());
			gameManager.updateAllPlayers(result.getPlayers());

			Log.v(LOG_TAG, "RoomId is " + result.getRoomId());
			gameManager.setRoomId(result.getRoomId());

			houseTokenManager.setStartingHouseTerritories(result.getTerritories());

			Log.v(LOG_TAG, "Starting GameActivity");
			Intent intent = new Intent(this, GameActivity.class);

			Bundle extras = new Bundle();
			extras.putString(GAME_MODE, gameMode);
			intent.putExtras(extras);

			startActivity(intent);

			isOpenningGameActivity = true;
		}
	}

	@Override
	protected void onDestroy() {
		if (waitDialog.isShowing()) {
			waitDialog.dismiss();
		}
		super.onDestroy();
	}

	@Override
	public void onGameModeSelected(String selectedMode) {
		gameMode = selectedMode;

		if (selectedMode.equals(MULTIPLAYER_GAME_MODE)) {
			communicationManager = NetworkManager.getInstance();
			startNetworkGame();
		} else if (selectedMode.equals(SINGLEPLAYER_GAME_MODE)) {
			communicationManager = AIManager.getInstance();
			startAIGame();
		}
	}
}