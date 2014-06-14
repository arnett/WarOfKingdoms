package br.edu.ufcg.ccc.projeto2.warofkingdoms.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Player;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.GameManager;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.HouseTokenManager;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.NetworkManager;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.networking.ConnectResult;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.networking.SendMovesResult;

public class ConnectActivity extends Activity implements OnClickListener,
		OnTaskCompleted {

	private String LOG_TAG = "ConnectActivity";

	private NetworkManager networkManager = NetworkManager.getInstance();
	private GameManager gameManager = GameManager.getInstance();
	private HouseTokenManager houseTokenManager = HouseTokenManager
			.getInstance();

	private Player currentPlayer = gameManager.getCurrentPlayer();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LinearLayout linearLayout = new LinearLayout(getBaseContext());

		Button button = new Button(getBaseContext());
		button.setText("Connect");
		button.setOnClickListener(this);

		linearLayout.addView(button);

		setContentView(linearLayout);
	}

	@Override
	public void onClick(View arg0) {
		networkManager.connect(this, currentPlayer);
	}

	@Override
	public void onSendMovesTaskCompleted(SendMovesResult result) {

	}

	@Override
	public void onConnectTaskCompleted(ConnectResult result) {
		gameManager.updateAllTerritories(result.getTerritories());
		gameManager.updateAllPlayers(result.getPlayers());

		houseTokenManager.setStartingHouseTerritories(result.getTerritories());

		Log.v(LOG_TAG, "Starting GameActivity");
		Intent intent = new Intent(this, GameActivity.class);
		startActivity(intent);
	}
}