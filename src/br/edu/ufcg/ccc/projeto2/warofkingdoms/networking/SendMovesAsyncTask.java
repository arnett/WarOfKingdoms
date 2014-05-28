package br.edu.ufcg.ccc.projeto2.warofkingdoms.networking;

import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Move;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Territory;
import android.os.AsyncTask;


public class SendMovesAsyncTask extends AsyncTask<Move, Void, Territory> {

	@Override
	protected Territory doInBackground(Move... params) {
		return null;
	}

	@Override
	protected void onPostExecute(Territory result) {
		super.onPostExecute(result);
	}
}
