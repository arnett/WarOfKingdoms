package br.edu.ufcg.ccc.projeto2.warofkingdoms.management;

import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.SERVER_URL;

import java.io.IOException;
import java.net.UnknownHostException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;

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
		} catch (UnknownHostException e) {
			Log.e(LOG_TAG, e.toString());
		} catch (Exception e) {
			Log.e(LOG_TAG, e.toString());
		}

		return responseToString(response);
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
