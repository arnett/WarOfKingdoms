package br.edu.ufcg.ccc.projeto2.warofkingdoms.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Connect;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Player;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.CommunicationManager;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.GameManager;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.HouseTokenManager;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.NetworkManager;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.networking.ConnectResult;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.networking.SendMovesResult;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.ui.dialogs.CustomProgressDialog;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.ui.dialogs.MessageDialogFragment;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.util.ConnectionDetector;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants;
import br.ufcg.edu.ccc.projeto2.R;

public class ConnectActivity extends Activity implements OnClickListener,
		OnTaskCompleted {

	private final String LOG_TAG = "ConnectActivity";

	// to just close the waitDialog when
	// the activity is started
	private boolean isOpenningGameActivity;

	private CommunicationManager communicationManager;
	private GameManager gameManager;
	private HouseTokenManager houseTokenManager;

	private Player currentPlayer;
	private Connect connectEntity;

	private ImageView playBtn;
	private ImageView aboutBtn;
	private ImageView profileBtn;
	private ImageView facebookBtn;
	private ImageView tutorialBtn;

	private AlphaAnimation alphaAnimation = new AlphaAnimation(1F, 0.5F);

	private CustomProgressDialog waitDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_screen);

		gameManager = GameManager.getInstance();
		houseTokenManager = HouseTokenManager.getInstance();

		currentPlayer = gameManager.getCurrentPlayer();

		updateMac();

		connectEntity = new Connect(Constants.NUM_PLAYERS, currentPlayer);

		playBtn = (ImageView) findViewById(R.id.playBtn);
		playBtn.setOnClickListener(this);

		aboutBtn = (ImageView) findViewById(R.id.aboutBtn);
		aboutBtn.setOnClickListener(this);

		profileBtn = (ImageView) findViewById(R.id.profileBtn);
		profileBtn.setOnClickListener(this);

		facebookBtn = (ImageView) findViewById(R.id.facebook_button);
		facebookBtn.setOnClickListener(this);

		tutorialBtn = (ImageView) findViewById(R.id.tutorial_button);
		tutorialBtn.setOnClickListener(this);

		waitDialog = new CustomProgressDialog(this, R.drawable.progress,
				getString(R.string.waiting_players_join));
		loadSavedPreferences();
	}

	private void updateMac() {
		currentPlayer.setId(String.valueOf(getMacAddress().hashCode()));
	}

	private void loadAlphaWarning() {
		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setMessage(getString(R.string.alpha_message));
		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				alertDialog.dismiss();
			}
		});
		alertDialog.show();
	}

	private void loadSavedPreferences() {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		boolean firstOpen = sharedPreferences.getBoolean("firstOpen", true);
		if (firstOpen) {
			savePreferences("firstOpen", false);
			loadAlphaWarning();
			startTutorial();
		}
	}

	private void startTutorial() {
		startActivity(new Intent(this, TutorialActivity.class));
	}

	private void savePreferences(String key, boolean value) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	@Override
	public void onClick(View v) {
		v.startAnimation(alphaAnimation);

		if (v == playBtn) {

			communicationManager = NetworkManager.getInstance();
			startNetworkGame();
		} else if (v == aboutBtn) {

			startActivity(new Intent(this, AboutActivity.class));
		} else if (v == profileBtn) {

			Intent intent = new Intent(this, ProfileActivity.class);
			startActivity(intent);
		} else if (v == facebookBtn) {
			// TODO - facebook connection
		} else if (v == tutorialBtn) {
			startTutorial();
		}
	}

	private void startNetworkGame() {
		ConnectionDetector connectionDetector = new ConnectionDetector(
				getApplicationContext());
		if (connectionDetector.isConnectingToInternet()) {
			waitDialog.show();
			communicationManager.connect(this, connectEntity);
		} else {
			openMessageDialog(getResources()
					.getString(R.string.no_internet_msg));
		}
	}

	private void openMessageDialog(String message) {

		MessageDialogFragment msgDialog = new MessageDialogFragment();
		Bundle args = new Bundle();
		args.putString(Constants.DIALOG_MESSAGE, message);
		msgDialog.setArguments(args);

		try {
			msgDialog.show(getFragmentManager(), "msgDialog");
		} catch (Exception e) {
			// do nothing
		}
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
			openMessageDialog(getResources()
					.getString(R.string.server_down_msg));
		} else {
			gameManager.updateAllTerritories(result.getTerritories());
			gameManager.updateAllPlayers(result.getPlayers());

			Log.v(LOG_TAG, "RoomId is " + result.getRoomId());
			gameManager.setRoomId(result.getRoomId());

			houseTokenManager.setStartingHouseTerritories(result
					.getTerritories());

			Log.v(LOG_TAG, "Starting GameActivity");
			Intent intent = new Intent(this, GameActivity.class);
			startActivity(intent);

			isOpenningGameActivity = true;
		}
	}

	private String getMacAddress() {
		WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = manager.getConnectionInfo();
		return info.getMacAddress();
	}

	// @Override
	// protected void onDestroy() {
	//
	// if (waitDialog.isShowing()) {
	// waitDialog.dismiss();
	// }
	// super.onDestroy();
	// }
}