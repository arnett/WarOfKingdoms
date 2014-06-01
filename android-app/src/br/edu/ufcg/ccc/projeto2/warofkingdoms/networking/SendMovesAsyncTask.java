package br.edu.ufcg.ccc.projeto2.warofkingdoms.networking;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import android.os.AsyncTask;
import android.util.Log;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.activities.OnTaskCompleted;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Conflict;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Move;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.*;

/**
 * Produces a collection of conflicts based on the provided Moves.
 * 
 * 
 */
public class SendMovesAsyncTask extends AsyncTask<Move, Void, Conflict[]> {

	private String LOG_TAG = "SendMovesAsyncTask";

	private OnTaskCompleted taskCompletedListener;

	public SendMovesAsyncTask(OnTaskCompleted taskCompletedListener) {
		this.taskCompletedListener = taskCompletedListener;
	}

	private String requestPOST(String uri, String requestParams) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = null;

		try {
			HttpPost request = new HttpPost(SERVER_URL + uri);
			StringEntity params = new StringEntity(requestParams);
			request.addHeader("content-type", "application/json");
			request.setEntity(params);
			response = httpClient.execute(request);
		} catch (Exception ex) {
			Log.e(LOG_TAG, ex.toString());
		}

		return responseToString(response);
	}

	private String responseToString(HttpResponse response) {
		HttpEntity entity = response.getEntity();

		String responseString = null;
		try {
			responseString = EntityUtils.toString(entity, "UTF-8");
		} catch (ParseException e) {
			Log.e(LOG_TAG, e.toString());
		} catch (IOException e) {
			Log.e(LOG_TAG, e.toString());
		}

		return responseString;
	}

	@Override
	protected Conflict[] doInBackground(Move... moves) {
		String response = requestPOST(SEND_MOVES_URI, JSONParser
				.parseMovesToJson(moves).toString());

		Conflict[] conflicts = null;
		try {
			conflicts = JSONParser
					.parseJsonToConflicts(new JSONArray(response));
		} catch (JSONException e) {
			Log.e(LOG_TAG, e.toString());
		}
		return conflicts;
	}

	@Override
	protected void onPostExecute(Conflict[] conflicts) {
		super.onPostExecute(conflicts);

		taskCompletedListener.onSendMovesTaskCompleted(conflicts);
	}
}