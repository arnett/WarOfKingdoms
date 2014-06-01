package br.edu.ufcg.ccc.projeto2.warofkingdoms.networking;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Conflict;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Move;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Player;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Territory;

public class JSONParser {

	private static String LOG_TAG = "JSONParser";

	public static JSONObject parseMoveToJson(Move move) {
		JSONObject moveJson = new JSONObject();
		try {
			moveJson.put("action", move.getAction());

			JSONObject territoryJson = parseTerritoryToJson(move
					.getTargetTerritory());
			moveJson.put("targetTerritory", territoryJson);
		} catch (JSONException e) {
			Log.e(LOG_TAG, e.toString());
		}
		return moveJson;
	}

	public static JSONArray parseMovesToJson(Move[] moves) {
		JSONArray movesJsonArray = new JSONArray();
		for (Move move : moves) {
			movesJsonArray.put(parseMoveToJson(move));
		}
		return movesJsonArray;
	}

	public static JSONObject parseTerritoryToJson(Territory territory) {
		JSONObject territoryJson = new JSONObject();
		try {
			territoryJson.put("name", territory.getName());
		} catch (JSONException e) {
			Log.e(LOG_TAG, e.toString());
		}
		return territoryJson;
	}

	public static Territory parseJsonToTerritory(JSONObject territoryJson) {
		Territory territory = new Territory();
		try {
			territory.setName(territoryJson.getString("name"));
		} catch (JSONException e) {
			Log.e(LOG_TAG, e.toString());
		}
		return territory;
	}

	public static Player parseJsonToPlayer(JSONObject playerJson) {
		Player player = new Player();
		try {
			player.setId(playerJson.getString("id"));
		} catch (JSONException e) {
			Log.e(LOG_TAG, e.toString());
		}
		return player;
	}

	public static Player[] parseJsonToPlayers(JSONArray playersJson) {
		Player[] players = new Player[playersJson.length()];
		try {
			for (int i = 0; i < players.length; i++) {
				players[i] = parseJsonToPlayer(playersJson.getJSONObject(i));
			}
		} catch (JSONException e) {
			Log.e(LOG_TAG, e.toString());
		}
		return players;
	}

	public static Conflict parseJsonToConflict(JSONObject conflictJson) {
		Conflict conflict = new Conflict();
		try {
			conflict.setTerritory(parseJsonToTerritory(conflictJson.getJSONObject("territory")));
			conflict.setPlayers(parseJsonToPlayers(conflictJson.getJSONArray("players")));
		} catch (JSONException e) {
			Log.e(LOG_TAG, e.toString());
		}
		return conflict;
	}

	public static Conflict[] parseJsonToConflicts(JSONArray conflictsJson) {
		Conflict[] conflicts = new Conflict[conflictsJson.length()];
		try {
			for (int i = 0; i < conflicts.length; i++) {
				conflicts[i] = parseJsonToConflict(conflictsJson.getJSONObject(i));
			}
		} catch (JSONException e) {
			Log.e(LOG_TAG, e.toString());
		}
		return conflicts;
	}
}
