package br.edu.ufcg.ccc.projeto2.warofkingdoms.management;

import java.util.List;

import android.os.AsyncTask;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Connect;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Move;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.networking.ConnectAsyncTask;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.networking.ConnectResult;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.networking.SendMovesAsyncTask;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.networking.SendMovesResult;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.ui.OnTaskCompleted;

public class NetworkManager implements CommunicationManager {

	private static NetworkManager instance;

	private NetworkManager() {
	}

	public synchronized static NetworkManager getInstance() {
		if (instance == null) {
			instance = new NetworkManager();
		}
		return instance;
	}
	
	@Override
	public void sendCurrentMoves(OnTaskCompleted listener, List<Move> moves) {
		AsyncTask<Move, Void, SendMovesResult> sendMovesTask = new SendMovesAsyncTask(
				listener);
		sendMovesTask.execute(moves.toArray(new Move[0]));
	}

	@Override
	public void connect(OnTaskCompleted listener, Connect connectEntity) {
		AsyncTask<Connect, Void, ConnectResult> connectTask = new ConnectAsyncTask(
				listener);
		connectTask.execute(connectEntity);
	}
}