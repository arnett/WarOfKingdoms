package br.edu.ufcg.ccc.projeto2.warofkingdoms.networking;

import static br.edu.ufcg.ccc.projeto2.warofkingdoms.management.RequestManager.requestPOST;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.SEND_MOVES_URI;

import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.activities.OnTaskCompleted;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Move;

public class SendMovesAsyncTask extends AsyncTask<Move, Void, SendMovesResult> {

	private String LOG_TAG = "SendMovesAsyncTask";

	private OnTaskCompleted taskCompletedListener;

	public SendMovesAsyncTask(OnTaskCompleted taskCompletedListener) {
		this.taskCompletedListener = taskCompletedListener;
	}

	@Override
	protected SendMovesResult doInBackground(Move... moves) {
		String request = JSONParser.parseMovesToJson(Arrays.asList(moves))
				.toString();
		Log.v(LOG_TAG,
				String.format("Request to %s = %s", SEND_MOVES_URI, request));

		String response = requestPOST(SEND_MOVES_URI, request);
		Log.v(LOG_TAG,
				String.format("Result of %s = %s", SEND_MOVES_URI, response));

		SendMovesResult result = null;

		try {
			result = JSONParser.parseJsonToSendMovesResult(new JSONObject(
					response));
		} catch (JSONException e) {
			Log.e(LOG_TAG, e.toString());
		}

		return result;
	}

	@Override
	protected void onPostExecute(SendMovesResult result) {
		super.onPostExecute(result);

		taskCompletedListener.onSendMovesTaskCompleted(result);
	}
}