package br.edu.ufcg.ccc.projeto2.warofkingdoms.networking;

import static br.edu.ufcg.ccc.projeto2.warofkingdoms.management.RequestManager.requestPOST;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.PLAYER_ID_TAG;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.SEND_MOVES_REQUEST_MOVES_TAG;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.SEND_MOVES_REQUEST_ROOM_ID_TAG;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.SEND_MOVES_URI;

import java.io.IOException;
import java.util.Arrays;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Move;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.GameManager;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.ui.OnTaskCompleted;

public class SendMovesAsyncTask extends AsyncTask<Move, Void, SendMovesResult> {

	private String LOG_TAG = "SendMovesAsyncTask";

	private OnTaskCompleted taskCompletedListener;

	public SendMovesAsyncTask(OnTaskCompleted taskCompletedListener) {
		this.taskCompletedListener = taskCompletedListener;
	}

	@Override
	protected SendMovesResult doInBackground(Move... moves) {
		JSONArray movesJson = null;
		try {
			movesJson = JSONParser.parseMovesToJson(Arrays.asList(moves));
		} catch (JSONException e2) {
			Log.e(LOG_TAG, e2.toString());
		}
		JSONObject requestJson = new JSONObject();

		try {
			requestJson.put(SEND_MOVES_REQUEST_MOVES_TAG, movesJson.toString());
			requestJson.put(SEND_MOVES_REQUEST_ROOM_ID_TAG, GameManager
					.getInstance().getRoomId());
			requestJson.put(PLAYER_ID_TAG, GameManager
					.getInstance().getCurrentPlayer().getId());
		} catch (JSONException e) {
			Log.e(LOG_TAG, e.toString());
		}

		String request = requestJson.toString();

		Log.v(LOG_TAG,
				String.format("Request to %s = %s", SEND_MOVES_URI, request));

		String response = null;
		try {
			response = requestPOST(SEND_MOVES_URI, request);
		} catch (ClientProtocolException e1) {
			Log.e(LOG_TAG, e1.toString());
		} catch (IOException e1) {
			Log.e(LOG_TAG, e1.toString());
		}
		Log.v(LOG_TAG,
				String.format("Result of %s = %s", SEND_MOVES_URI, response));

		SendMovesResult result = null;
		if (response != null) {
			try {
				result = JSONParser.parseJsonToSendMovesResult(new JSONObject(
						response));
			} catch (JSONException e) {
				Log.e(LOG_TAG, e.toString());
			}
		}

		return result;
	}

	@Override
	protected void onPostExecute(SendMovesResult result) {
		super.onPostExecute(result);

		taskCompletedListener.onSendMovesTaskCompleted(result);
	}
}