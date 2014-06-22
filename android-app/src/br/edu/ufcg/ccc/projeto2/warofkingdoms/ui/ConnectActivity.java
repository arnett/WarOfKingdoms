package br.edu.ufcg.ccc.projeto2.warofkingdoms.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
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
import br.edu.ufcg.ccc.projeto2.warofkingdoms.ui.ChooseGameDialogFragment.OnGameSelectedListener;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.util.ConnectionDetector;
import br.ufcg.edu.ccc.projeto2.R;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.*;

public class ConnectActivity extends Activity implements OnClickListener,
		OnTaskCompleted, OnGameSelectedListener {

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

			startChooseGameDialog();
		} else if (v == aboutBtn) {
			
			startActivity(new Intent(this, AboutActivity.class));
		}
		else if (v == profileBtn) {
			
			Intent intent = new Intent(this, ProfileActivity.class); 
			startActivity(intent);
		}
	}

	private void startChooseGameDialog() {
		DialogFragment chooseGameDialogFragment = new ChooseGameDialogFragment();
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
			showAlertDialog(ConnectActivity.this, "No Internet Connection",
					"You don't have internet connection.", false);
		}
	}

	private void startAIGame() {
		communicationManager.connect(this, currentPlayer);
	}

	/**
	 * Function to display simple Alert Dialog
	 * 
	 * @param context
	 *            - application context
	 * @param title
	 *            - alert dialog title
	 * @param message
	 *            - alert message
	 * @param status
	 *            - success/failure (used to set icon)
	 * */
	@SuppressWarnings("deprecation")
	public void showAlertDialog(Context context, String title, String message,
			Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		// Setting Dialog Title
		alertDialog.setTitle(title);

		// Setting Dialog Message
		alertDialog.setMessage(message);

		// Setting alert dialog icon
		// alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		// Showing Alert Message
		alertDialog.show();
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

	@Override
	protected void onDestroy() {
		if (waitDialog.isShowing()) {
			waitDialog.dismiss();
		}
		super.onDestroy();
	}

	@Override
	public void onGameSelected(String selectedGame) {
		gameMode = selectedGame;

		if (selectedGame.equals(HUMAN_GAME_MODE)) {
			communicationManager = NetworkManager.getInstance();
			startNetworkGame();
		} else if (selectedGame.equals(CPU_GAME_MODE)) {
			communicationManager = AIManager.getInstance();
			startAIGame();
		}
	}
}

class ChooseGameDialogFragment extends DialogFragment {

	private OnGameSelectedListener gameListener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			this.gameListener = (OnGameSelectedListener) activity;
		} catch (final ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnCompleteListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final String[] GAME_TYPES = new String[] { CPU_GAME_MODE,
				HUMAN_GAME_MODE };
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Choose your enemy").setItems(GAME_TYPES,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						gameListener.onGameSelected(GAME_TYPES[which]);
					}
				});
		return builder.create();
	}

	public static interface OnGameSelectedListener {
		public void onGameSelected(String gameSelected);
	}
}