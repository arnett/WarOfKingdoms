package br.edu.ufcg.ccc.projeto2.warofkingdoms.management;

import static br.edu.ufcg.ccc.projeto2.warofkingdoms.networking.JSONParser.parsePlayersToJson;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.networking.JSONParser.parseTerritoriesToJson;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.CONNECT_RESULT_PLAYERS_TAG;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.CONNECT_RESULT_TERRITORIES_TAG;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.CONNECT_URI;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.SEND_MOVES_URI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Player;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Territory;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.util.RandomEntities;

public class FakeRequestManager {

	private final static String LOG_TAG = "RequestManager";

	private static RandomEntities generator = new RandomEntities();
	private static Random random = new Random();

	public static String requestPOST(String uri, String requestParams) {
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

		for (int i = 0; i < 3; i++) {
			Player randomPlayer = players.get(random.nextInt(players.size() - 1));
			Territory randomTerritory = territories.get(random.nextInt(territories
					.size() - 1));
			randomTerritory.setOwner(randomPlayer.getHouse());
		}

		try {
			result.put(CONNECT_RESULT_TERRITORIES_TAG,
					parseTerritoriesToJson(territories));
			result.put(CONNECT_RESULT_PLAYERS_TAG, parsePlayersToJson(players));
		} catch (JSONException e) {
			Log.e(LOG_TAG, e.toString());
		}
		return result.toString();
	}

	private static ArrayList<Player> createFakePlayerResponse() {
		ArrayList<Player> players = new ArrayList<Player>();
		for (int i = 0; i < 2; i++) {
			players.add(generator.nextPlayer());
		}
		return players;
	}

	private static ArrayList<Territory> createFakeTerritoryList() {
		ArrayList<Territory> territories = new ArrayList<Territory>();
		for (char territoryLetter = 65; territoryLetter < 91; territoryLetter++) {
			if (territoryLetter != 'W') {
				territories.add(new Territory(String.valueOf(territoryLetter)));
			}
		}
		return territories;
	}

}
