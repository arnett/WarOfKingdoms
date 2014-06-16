package br.edu.ufcg.ccc.projeto2.warofkingdoms.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
			waitDialog.show();
			networkManager.connect(this, currentPlayer);
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