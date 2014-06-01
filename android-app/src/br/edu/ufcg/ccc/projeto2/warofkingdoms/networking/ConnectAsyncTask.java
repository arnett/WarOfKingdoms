package br.edu.ufcg.ccc.projeto2.warofkingdoms.networking;

import static br.edu.ufcg.ccc.projeto2.warofkingdoms.management.RequestManager.requestPOST;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.activities.OnTaskCompleted;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Territory;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.*;

public class ConnectAsyncTask extends AsyncTask<String, Void, Territory>{

	private final String LOG_TAG = "ConnectAsyncTask";

	private OnTaskCompleted taskCompletedListener;

	public ConnectAsyncTask(OnTaskCompleted taskCompletedListener) {
		this.taskCompletedListener = taskCompletedListener;
	}

	@Override
	protected Territory doInBackground(String... params) {
		JSONObject playerJson = new JSONObject();
		Territory startingTerritory = null;
		try {
			String id = params[0];
			String name = params[1];
			playerJson.put("id", id);
			playerJson.put("name", name);

			String territoryJson = requestPOST(CONNECT_URI, playerJson.toString());
			startingTerritory = JSONParser.parseJsonToTerritory(new JSONObject(territoryJson));
		} catch (JSONException e) {
			Log.e(LOG_TAG, e.toString());
		}
		return startingTerritory;
	}

	@Override
	protected void onPostExecute(Territory startingTerritory) {
		super.onPostExecute(startingTerritory);
		taskCompletedListener.onConnectTaskCompleted(startingTerritory);
	}
}
