package br.edu.ufcg.ccc.projeto2.warofkingdoms.management;

import java.util.List;

import android.os.AsyncTask;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Move;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Territory;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.networking.SendMovesAsyncTask;


/**
 * A singleton that manages the game state
 * 
 */
public class NetworkManager {

	private static NetworkManager instance;

	private NetworkManager() {

	}

	public synchronized static NetworkManager getInstance() {
		if (instance == null) {
			instance = new NetworkManager();
		}
		return instance;
	}

	public void sendCurrentMoves(List<Move> moves) {
		AsyncTask<Move, Void, Territory> sendMovesTask = new SendMovesAsyncTask();
		sendMovesTask.execute((Move[]) moves.toArray());
	}

	
}
