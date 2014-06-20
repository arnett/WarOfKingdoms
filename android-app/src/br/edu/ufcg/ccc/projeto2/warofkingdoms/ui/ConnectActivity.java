package br.edu.ufcg.ccc.projeto2.warofkingdoms.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Player;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.GameManager;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.HouseTokenManager;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.NetworkManager;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.networking.ConnectResult;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.networking.SendMovesResult;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.util.ConnectionDetector;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants;
import br.ufcg.edu.ccc.projeto2.R;

public class ConnectActivity extends Activity implements OnClickListener,
OnTaskCompleted {

	private String LOG_TAG = "ConnectActivity";

	private boolean isOpenningGameActivity;	// to just close the waitDialog when the activity is started

	private NetworkManager networkManager;
	private GameManager gameManager;
	private HouseTokenManager houseTokenManager;

	private Player currentPlayer;

	private ImageView playBtn;
	private ImageView aboutBtn;
	private ImageView profileBtn;

	private CustomProgressDialog waitDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_screen);

		resetPreviousGameState();

		networkManager = NetworkManager.getInstance();
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
		ConnectionDetector connectionDetector = new ConnectionDetector(getApplicationContext());
		if (v == playBtn) {
			if (connectionDetector.isConnectingToInternet()) {
				waitDialog.show();
				networkManager.connect(this, currentPlayer);
			}
			else {
				showAlertDialog(ConnectActivity.this, "No Internet Connection",
                        "You don't have internet connection.", false);
			}
		}	
		
		else if (v == aboutBtn) {
			startActivity(new Intent(this, AboutActivity.class));
		}
		
		else if (v == profileBtn) {
			
			startActivity(new Intent(this, ProfileActivity.class));
		}
	}
	
    /**
     * Function to display simple Alert Dialog
     * @param context - application context
     * @param title - alert dialog title
     * @param message - alert message
     * @param status - success/failure (used to set icon)
     * */
    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
 
        // Setting Dialog Title
        alertDialog.setTitle(title);
 
        // Setting Dialog Message
        alertDialog.setMessage(message);
         
        // Setting alert dialog icon
//        alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
 
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
		startActivity(intent);

		isOpenningGameActivity = true;
	}

	@Override
	protected void onDestroy() {
		if (waitDialog.isShowing() ) {
			waitDialog.dismiss();
		}
		super.onDestroy();
	}

}