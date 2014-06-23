package br.edu.ufcg.ccc.projeto2.warofkingdoms.management;

import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.SERVER_URL;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class RequestManager {

	private final static String LOG_TAG = "RequestManager";

	public static String requestPOST(String uri, String requestParams) throws ClientProtocolException, IOException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = null;

		HttpPost request = new HttpPost(SERVER_URL + uri);
		StringEntity params = new StringEntity(requestParams);
		request.addHeader("content-type", "application/json");
		request.setEntity(params);
		response = httpClient.execute(request);
		return responseToString(response);
	}

	private static String responseToString(HttpResponse response) throws ParseException, IOException {
		HttpEntity entity = response.getEntity();

		String responseString = null;
		responseString = EntityUtils.toString(entity, "UTF-8");

		return responseString;
	}

}
