package br.edu.ufcg.ccc.projeto2.warofkingdoms.networking;

import static br.edu.ufcg.ccc.projeto2.warofkingdoms.management.RequestManager.requestPOST;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.networking.JSONParser.parseJsonToConnectResult;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.networking.JSONParser.parsePlayerToJson;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.CONNECT_URI;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Player;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.ui.OnTaskCompleted;

public class ConnectAsyncTask extends AsyncTask<Player, Void, ConnectResult> {

	private final String LOG_TAG = "ConnectAsyncTask";

	private OnTaskCompleted taskCompletedListener;

	public ConnectAsyncTask(OnTaskCompleted taskCompletedListener) {
		this.taskCompletedListener = taskCompletedListener;
	}

	@Override
	protected ConnectResult doInBackground(Player... params) {
		String request = null;
		try {
			request = parsePlayerToJson(params[0]).toString();
		} catch (JSONException e) {
			Log.e(LOG_TAG, e.toString());
		}
		Log.v(LOG_TAG,
				String.format("Request to %s = %s", CONNECT_URI, request));

		String response = null;
		try {
			response = requestPOST(CONNECT_URI, request);
		} catch (ClientProtocolException e1) {
			Log.e(LOG_TAG, e1.toString());
		} catch (IOException e1) {
			Log.e(LOG_TAG, e1.toString());
		}
		Log.v(LOG_TAG,
				String.format("Result of %s = %s", CONNECT_URI, response));

		ConnectResult connectResult = null;
		if (response != null) {
			try {
				connectResult = parseJsonToConnectResult(new JSONObject(response));
			} catch (JSONException e) {
				Log.e(LOG_TAG, e.toString());
			}
		}

		return connectResult;
	}

	@Override
	protected void onPostExecute(ConnectResult connectResult) {
		super.onPostExecute(connectResult);
		taskCompletedListener.onConnectTaskCompleted(connectResult);
	}
}
