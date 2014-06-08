package br.edu.ufcg.ccc.projeto2.warofkingdoms.management;

import java.util.List;

import android.os.AsyncTask;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.activities.OnTaskCompleted;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Conflict;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Move;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Player;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.networking.ConnectAsyncTask;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.networking.ConnectResult;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.networking.SendMovesAsyncTask;


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

	public void sendCurrentMoves(OnTaskCompleted listener, List<Move> moves) {
		AsyncTask<Move, Void, List<Conflict>> sendMovesTask = new SendMovesAsyncTask(listener);
		sendMovesTask.execute(moves.toArray(new Move[0]));
	}

	public void connect(OnTaskCompleted listener, Player player) {
		AsyncTask<Player, Void, ConnectResult> connectTask = new ConnectAsyncTask(listener);
		connectTask.execute(player);
	}
}
