package br.edu.ufcg.ccc.projeto2.warofkingdoms.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Conflict;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Player;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Territory;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.GameManager;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.NetworkManager;

public class ConnectActivity extends Activity implements OnClickListener,
		OnTaskCompleted {

	private String LOG_TAG = "ConnectActivity";

	private NetworkManager networkManager = NetworkManager.getInstance();
	private GameManager gameManager = GameManager.getInstance();
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
		String id = currentPlayer.getId();
		String name = currentPlayer.getName();
		networkManager.connect(this, id, name);
	}

	@Override
	public void onSendMovesTaskCompleted(Conflict[] conflicts) {

	}

	@Override
	public void onConnectTaskCompleted(Territory startingTerritory) {
		startingTerritory.setOwner(currentPlayer);

		Log.v(LOG_TAG, startingTerritory.getName());
		Log.v(LOG_TAG, startingTerritory.getOwner().getName());
		Log.v(LOG_TAG, startingTerritory.getOwner().getId());

		ArrayList<Territory> territoriestoBeUpdated = new ArrayList<Territory>();
		territoriestoBeUpdated.add(startingTerritory);

		gameManager.updateTerritories(territoriestoBeUpdated);

		Intent intent = new Intent(this, GameActivity.class);
		startActivity(intent);
	}
}