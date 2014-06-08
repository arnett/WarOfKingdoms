package br.edu.ufcg.ccc.projeto2.warofkingdoms.networking;

import static br.edu.ufcg.ccc.projeto2.warofkingdoms.management.RequestManager.requestPOST;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.networking.JSONParser.*;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.CONNECT_URI;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.SEND_MOVES_URI;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.activities.OnTaskCompleted;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Player;

public class ConnectAsyncTask extends AsyncTask<Player, Void, ConnectResult> {

	private final String LOG_TAG = "ConnectAsyncTask";

	private OnTaskCompleted taskCompletedListener;

	public ConnectAsyncTask(OnTaskCompleted taskCompletedListener) {
		this.taskCompletedListener = taskCompletedListener;
	}

	@Override
	protected ConnectResult doInBackground(Player... params) {
		String request = parsePlayerToJson(params[0]).toString();
		String response = requestPOST(CONNECT_URI, request);
		ConnectResult connectResult = null;

		try {
			connectResult = parseJsonToConnectResult(new JSONObject(response));
		} catch (JSONException e) {
			Log.e(LOG_TAG, e.toString());
		}

		Log.v(LOG_TAG, String.format("Request to %s = %s", SEND_MOVES_URI, request));
		Log.v(LOG_TAG, String.format("Result of %s = %s", SEND_MOVES_URI, response));
		return connectResult;
	}

	@Override
	protected void onPostExecute(ConnectResult connectResult) {
		super.onPostExecute(connectResult);
		taskCompletedListener.onConnectTaskCompleted(connectResult);
	}
}
