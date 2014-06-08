package br.edu.ufcg.ccc.projeto2.warofkingdoms.networking;

import static br.edu.ufcg.ccc.projeto2.warofkingdoms.management.FakeRequestManager.requestPOST;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.networking.JSONParser.*;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.CONNECT_URI;

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
		ConnectResult connectResult = null;
		try {
			String connectResultJson = requestPOST(CONNECT_URI,
					parsePlayerToJson(params[0]).toString());
			connectResult = parseJsonToConnectResult(new JSONObject(connectResultJson));
		} catch (JSONException e) {
			Log.e(LOG_TAG, e.toString());
		}
		return connectResult;
	}

	@Override
	protected void onPostExecute(ConnectResult connectResult) {
		super.onPostExecute(connectResult);
		taskCompletedListener.onConnectTaskCompleted(connectResult);
	}
}
