package br.edu.ufcg.ccc.projeto2.warofkingdoms.management;

import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.CONNECT_URI;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.SEND_MOVES_URI;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.SERVER_URL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import org.json.JSONObject;

import android.util.Log;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Player;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Territory;

public class RequestManager {

	private final static String LOG_TAG = "RequestManager";

	public static String requestPOST(String uri, String requestParams) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = null;

		try {
			HttpPost request = new HttpPost(SERVER_URL + uri);
			StringEntity params = new StringEntity(requestParams);
			request.addHeader("content-type", "application/json");
			request.setEntity(params);
			response = httpClient.execute(request);
		} catch (Exception e) {
			Log.e(LOG_TAG, e.toString());
		}

		return responseToString(response);
	}

	public static String requestPOSTfake(String uri, String requestParams) {
		if (uri.equals(CONNECT_URI)) {
			return createFakeConnectResponse();
		} else if (uri.equals(SEND_MOVES_URI)) {
			return createFakeSendMovesResponse();
		}
		Log.e(LOG_TAG, "Resource " + uri + " is unknown");
		return null;
	}

	private static String createFakeSendMovesResponse() {
		JSONArray result = new JSONArray();
		return result.toString();
	}

	private static String createFakeConnectResponse() {
		JSONObject result = new JSONObject();
		List<Territory> territories = createFakeTerritoryList();
		List<Player> players = createFakePlayerResponse();
		try {
			result.put("territories", territories);
			result.put("players", players);
		} catch (JSONException e) {
			Log.e(LOG_TAG, e.toString());
		}
		return result.toString();
	}

	private static ArrayList<Player> createFakePlayerResponse() {
		return new ArrayList<Player>();
	}

	private static ArrayList<Territory> createFakeTerritoryList() {
		return new ArrayList<Territory>();
	}

	private static String responseToString(HttpResponse response) {
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

}
