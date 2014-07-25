package br.edu.ufcg.ccc.projeto2.warofkingdoms.ui;

import java.security.PublicKey;
import java.util.Arrays;

import android.app.Activity;
import android.content.Context;
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
import android.widget.Button;
import android.widget.ImageView;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Connect;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Player;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.CommunicationManager;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.GameManager;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.HouseTokenManager;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.NetworkManager;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.ProfileManager;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.networking.ConnectResult;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.networking.SendMovesResult;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.ui.dialogs.CustomProgressDialog;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.ui.dialogs.MessageDialogFragment;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.util.ConnectionDetector;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants;
import br.ufcg.edu.ccc.projeto2.R;

import com.facebook.Request;
import com.facebook.RequestBatch;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.widget.LoginButton;

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
	private ImageView tutorialBtn;
	private Button logoutButton;
	private LoginButton authButton;
	private AlphaAnimation alphaAnimation = new AlphaAnimation(1F, 0.5F);
	private CustomProgressDialog waitDialog;
	private Session session;

	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			Log.v(LOG_TAG, "onSessionStateChange");
			onSessionStateChange(session, state, exception);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_screen);
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
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

		//		facebookBtn = (ImageView) findViewById(R.id.facebook_button);
		//		facebookBtn.setOnClickListener(this);

		tutorialBtn = (ImageView) findViewById(R.id.tutorial_button);
		tutorialBtn.setOnClickListener(this);
		
		setLogoutButton((Button) findViewById(R.id.logoutButton));
		getLogoutButton().setOnClickListener(this);

		waitDialog = new CustomProgressDialog(this, R.drawable.progress,
				getString(R.string.waiting_players_join));
		authButton = (LoginButton) findViewById(R.id.authButton);
		authButton.setReadPermissions(Arrays.asList("user_likes", "user_status"));
		
		session = Session.restoreSession(getApplicationContext(), null, callback, savedInstanceState);
		if (session == null || session.isClosed()) {
			authButton.setVisibility(View.VISIBLE);
			getLogoutButton().setVisibility(View.GONE);
		}
		else {
			authButton.setVisibility(View.GONE);
			getLogoutButton().setVisibility(View.VISIBLE);
			Session.setActiveSession(session);
		}


		openTutorial();
	}
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		if (state.isOpened()) {
			Session.setActiveSession(session);
			doBatchRequest();
			authButton.setVisibility(View.GONE);
			getLogoutButton().setVisibility(View.VISIBLE);
			//Execute the request
		} else if (state.isClosed()) {
			authButton.setVisibility(View.VISIBLE);
			getLogoutButton().setVisibility(View.GONE);
			callFacebookLogout(getApplicationContext());
		}
	}
	
	private void doBatchRequest() {
	    String[] requestIds = {"me", "4"};
	    RequestBatch requestBatch = new RequestBatch();
	    for (final String requestId : requestIds) {
	        requestBatch.add(new Request(Session.getActiveSession(), 
	                requestId, null, null, new Request.Callback() {
	            public void onCompleted(Response response) {
	                GraphObject graphObject = response.getGraphObject();
	                if (graphObject != null) {
	                    if (graphObject.getProperty("id") != null) {
	                        String userName = String.format("%s", 
	                                graphObject.getProperty("name"));
	                        String userId = String.format("%s", 
	                                graphObject.getProperty("id"));
	                        ProfileManager.getInstance().saveString(Constants.USER_NAME, userName, getApplicationContext());
	                        ProfileManager.getInstance().saveString(Constants.USER_ID, userId, getApplicationContext());
	                		Log.v(LOG_TAG,userName + ":" + userId);
	                    }
	                }
	            }
	        }));
	    }
	    requestBatch.executeAsync();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Session session = Session.getActiveSession();
		if (session != null &&
				(session.isOpened() || session.isClosed()) ) {
			onSessionStateChange(session, session.getState(), null);
		}

		uiHelper.onResume();
	}


	private void updateMac() {
		currentPlayer.setId(String.valueOf(getMacAddress().hashCode()));
	}

	private void loadAlphaWarning() {
		openMessageDialog(
				getString(R.string.alpha_label),
				getString(R.string.alpha_message), 
				Constants.DIALOG_INFO);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onPause() {
		super.onPause();
		uiHelper.onPause();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}
	
	private void openTutorial() {

		if (isFirstTimeOpenningApp()) {
			savePreferences("firstOpen", false);
			startTutorial();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}
	
	private boolean isFirstTimeOpenningApp() {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		boolean firstOpen = sharedPreferences.getBoolean("firstOpen", true);
		return firstOpen;
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
		} else if (v == tutorialBtn) {
			startTutorial();
		} else if (v == getLogoutButton()) {
			callFacebookLogout(getApplicationContext());
		}
		
	}

	private void startNetworkGame() {
		ConnectionDetector connectionDetector = new ConnectionDetector(
				getApplicationContext());
		if (connectionDetector.isConnectingToInternet()) {
			waitDialog.show();
			communicationManager.connect(this, connectEntity);
		} else {
			openMessageDialog(
					getResources().getString(R.string.sorry_label),
					getResources().getString(R.string.no_internet_msg), 
					Constants.DIALOG_ERROR);
		}
	}


	/**
	 * Opens a message dialog
	 * 
	 * @param header dialog title
	 * @param message
	 * @param type - error or info
	 */
	private void openMessageDialog(String header, String message, int type) {

		MessageDialogFragment msgDialog = new MessageDialogFragment();
		Bundle args = new Bundle();

		args.putString(Constants.DIALOG_MESSAGE_HEADER, header);
		args.putString(Constants.DIALOG_MESSAGE, message);
		args.putInt(Constants.DIALOG_TYPE, type);
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
			try {
				waitDialog.dismiss();
				openMessageDialog(
						getResources().getString(R.string.sorry_label),
						getResources().getString(R.string.server_down_msg), 
						Constants.DIALOG_ERROR);

			} catch (Exception e) {
				// TODO: handle exception
			}
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

	public void callFacebookLogout(Context context) {
		Session session = Session.getActiveSession();
		if (session != null) {

			if (!session.isClosed()) {
				session.closeAndClearTokenInformation();
        		ProfileManager profileManager = ProfileManager.getInstance();
				profileManager.saveString(Constants.USER_NAME, Constants.ANONYMOUS_NAME, getApplicationContext());
                profileManager.saveString(Constants.USER_ID, Constants.ANONYMOUS_ID, getApplicationContext());
                profileManager.saveInt(Constants.NUM_VICTORIES_KEY, 0, getApplicationContext());
                profileManager.saveInt(Constants.NUM_TIMES_PLAYED_KEY, 0, getApplicationContext());
                profileManager.saveString(Constants.TEN_LAST_GAMES, Constants.CLEAN_VICTORY_TRACK, getApplicationContext());
			}
		} else {

			session = new Session(context);
			Session.setActiveSession(session);

			session.closeAndClearTokenInformation();
			//clear your preferences if saved

		}
		logoutButton.setVisibility(View.INVISIBLE);
		authButton.setVisibility(View.VISIBLE);
	}

	public Button getLogoutButton() {
		return logoutButton;
	}

	public void setLogoutButton(Button logoutButton) {
		this.logoutButton = logoutButton;
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