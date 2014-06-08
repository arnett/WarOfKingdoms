package br.edu.ufcg.ccc.projeto2.warofkingdoms.networking;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Conflict;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.House;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Move;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Player;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Territory;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.*;

/**
 * 
 * @author Arnett
 * 
 */
public class JSONParser {

	private static String LOG_TAG = "JSONParser";

	/**
	 * Parses the given conflict to a <tt>JSONObject</tt>.
	 * 
	 * @param conflict
	 * @return
	 */
	public static JSONObject parseConflictToJson(Conflict conflict) {
		JSONObject conflictJson = new JSONObject();
		try {
			conflictJson.put(CONFLICT_TERRITORY_TAG,
					parseTerritoryToJson(conflict.getTerritory()));
			conflictJson.put(CONFLICT_HOUSES_TAG,
					parseHousesToJson(conflict.getHouses()));
			conflictJson.put(CONFLICT_DICE_VALUES_TAG, parseIntegersToJson(conflict.getDiceValues()));
		} catch (JSONException e) {
			Log.e(LOG_TAG, e.toString());
		}
		return conflictJson;
	}

	/**
	 * Parses the given list of moves to a <tt>JSONArray</tt>.
	 * 
	 * @param conflicts
	 * @return
	 */
	public static JSONArray parseConflictsToJson(List<Conflict> conflicts) {
		JSONArray conflictsJsonArray = new JSONArray();
		for (Conflict conflict : conflicts) {
			conflictsJsonArray.put(parseConflictToJson(conflict));
		}
		return conflictsJsonArray;
	}

	public static JSONArray parseIntegersToJson(List<Integer> diceValues) {
		JSONArray integersArray = new JSONArray();
		for (Integer diceValue : diceValues) {
			integersArray.put(diceValue);
		}
		return integersArray;
	}

	/**
	 * Parses the given player to a <tt>JSONObject</tt>.
	 * 
	 * @param player
	 * @return
	 */
	public static JSONObject parsePlayerToJson(Player player) {
		JSONObject playerJson = new JSONObject();
		try {
			playerJson.put(PLAYER_ID_TAG, player.getId());
			playerJson.put(PLAYER_NAME_TAG, player.getName());
			if (player.getHouse() == null) {
				playerJson.put(PLAYER_HOUSE_TAG, JSONObject.NULL);
			} else {
				playerJson.put(PLAYER_HOUSE_TAG,
						parseHouseToJson(player.getHouse()));
			}
		} catch (JSONException e) {
			Log.e(LOG_TAG, e.toString());
		}
		return playerJson;
	}

	/**
	 * Parses the given list of moves to a <tt>JSONArray</tt>.
	 * 
	 * @param players
	 * @return
	 */
	public static JSONArray parsePlayersToJson(List<Player> players) {
		JSONArray playersJsonArray = new JSONArray();
		for (Player player : players) {
			playersJsonArray.put(parsePlayerToJson(player));
		}
		return playersJsonArray;
	}

	/**
	 * Parses the given house to a <tt>JSONObject</tt>.
	 * 
	 * @param house
	 * @return
	 */
	public static JSONObject parseHouseToJson(House house) {
		JSONObject houseJson = new JSONObject();
		try {
			houseJson.put(HOUSE_NAME_TAG, house.getName());
		} catch (JSONException e) {
			Log.e(LOG_TAG, e.toString());
		}
		return houseJson;
	}

	/**
	 * Parses the given list of moves to a <tt>JSONArray</tt>.
	 * 
	 * @param moves
	 * @return
	 */
	public static JSONArray parseHousesToJson(List<House> houses) {
		JSONArray housesJsonArray = new JSONArray();
		for (House house : houses) {
			housesJsonArray.put(parseHouseToJson(house));
		}
		return housesJsonArray;
	}

	/**
	 * Parses the given territory to a <tt>JSONObject</tt>.
	 * 
	 * @param territory
	 * @return
	 */
	public static JSONObject parseTerritoryToJson(Territory territory) {
		JSONObject territoryJson = new JSONObject();
		try {
			territoryJson.put(TERRITORY_NAME_TAG, territory.getName());

			if (territory.getOwner() == null) {
				territoryJson.put(TERRITORY_OWNER_TAG, JSONObject.NULL);
			} else {
				territoryJson.put(TERRITORY_OWNER_TAG,
						parseHouseToJson(territory.getOwner()));
			}
		} catch (JSONException e) {
			Log.e(LOG_TAG, e.toString());
		}
		return territoryJson;
	}

	/**
	 * Parses the given list of territories to a <tt>JSONArray</tt>.
	 * 
	 * @param territories
	 * @return
	 */
	public static JSONArray parseTerritoriesToJson(List<Territory> territories) {
		JSONArray territoriesJsonArray = new JSONArray();
		for (Territory territory : territories) {
			territoriesJsonArray.put(parseTerritoryToJson(territory));
		}
		return territoriesJsonArray;
	}

	/**
	 * Parses the given move to a <tt>JSONObject</tt>.
	 * 
	 * @param move
	 * @return
	 */
	public static JSONObject parseMoveToJson(Move move) {
		JSONObject moveJson = new JSONObject();
		try {
			moveJson.put(MOVE_ORIGIN_TAG,
					parseTerritoryToJson(move.getOrigin()));
			moveJson.put(MOVE_TARGET_TAG,
					parseTerritoryToJson(move.getTarget()));
			moveJson.put(MOVE_ACTION_TAG, move.getAction().toString());
		} catch (JSONException e) {
			Log.e(LOG_TAG, e.toString());
		}
		return moveJson;
	}

	/**
	 * Parses the given list of moves to a <tt>JSONArray</tt>.
	 * 
	 * @param moves
	 * @return
	 */
	public static JSONArray parseMovesToJson(List<Move> moves) {
		JSONArray movesJsonArray = new JSONArray();
		for (Move move : moves) {
			movesJsonArray.put(parseMoveToJson(move));
		}
		return movesJsonArray;
	}

	public static ConnectResult parseJsonToConnectResult(
			JSONObject connectResult) {
		ConnectResult result = new ConnectResult();
		try {
			List<Territory> territories = parseJsonToTerritories(connectResult
					.getJSONArray(CONNECT_RESULT_TERRITORIES_TAG));
			List<Player> players = parseJsonToPlayers(connectResult
					.getJSONArray(CONNECT_RESULT_PLAYERS_TAG));
			result.setTerritories(territories);
			result.setPlayers(players);
		} catch (JSONException e) {
			Log.e(LOG_TAG, e.toString());
		}
		return result;
	}

	public static SendMovesResult parseJsonToSendMovesResult(
			JSONObject jsonSendMovesResult) {
		SendMovesResult result = new SendMovesResult();
		try {
			List<Conflict> conflicts = parseJsonToConflicts(jsonSendMovesResult
					.getJSONArray(SEND_MOVES_RESULT_CONFLICTS_TAG));
			List<Territory> updatedMap = parseJsonToTerritories(jsonSendMovesResult
					.getJSONArray(SEND_MOVES_RESULT_UPDATED_MAP_TAG));
			result.setConflicts(conflicts);
			result.setUpdatedMap(updatedMap);
		} catch (JSONException e) {
			Log.e(LOG_TAG, e.toString());
		}

		return result;
	}

	/**
	 * Parses the given <tt>JSONObject</tt> to a territory.
	 * 
	 * @param territoryJson
	 * @return
	 */
	public static Territory parseJsonToTerritory(JSONObject territoryJson) {
		Territory territory = new Territory();
		try {
			territory.setName(territoryJson.getString(TERRITORY_NAME_TAG));

			if (!territoryJson.isNull(TERRITORY_OWNER_TAG)) {
				House house = parseJsonToHouse(territoryJson
						.getJSONObject(TERRITORY_OWNER_TAG));
				territory.setOwner(house);
			}
		} catch (JSONException e) {
			Log.e(LOG_TAG, e.toString());
		}
		return territory;
	}

	/**
	 * Parses the given <tt>JSONArray</tt> to a list of territories.
	 * 
	 * @param territoriesJson
	 * @return
	 */
	public static List<Territory> parseJsonToTerritories(
			JSONArray territoriesJson) {
		List<Territory> territories = new ArrayList<Territory>();
		try {
			for (int i = 0; i < territoriesJson.length(); i++) {
				territories.add(parseJsonToTerritory(territoriesJson
						.getJSONObject(i)));
			}
		} catch (JSONException e) {
			Log.e(LOG_TAG, e.toString());
		}
		return territories;
	}

	/**
	 * Parses the given <tt>JSONObject</tt> to a player.
	 * 
	 * @param playerJson
	 * @return
	 */
	public static Player parseJsonToPlayer(JSONObject playerJson) {
		Player player = new Player();
		try {
			player.setId(playerJson.getString(PLAYER_ID_TAG));
			player.setName(playerJson.getString(PLAYER_NAME_TAG));

			House house = parseJsonToHouse(playerJson
					.getJSONObject(PLAYER_HOUSE_TAG));
			player.setHouse(house);
		} catch (JSONException e) {
			Log.e(LOG_TAG, e.toString());
		}
		return player;
	}

	/**
	 * Parses the given <tt>JSONArray</tt> to a list of players.
	 * 
	 * @param playersJson
	 * @return
	 */
	public static List<Player> parseJsonToPlayers(JSONArray playersJson) {
		List<Player> players = new ArrayList<Player>();
		try {
			for (int i = 0; i < playersJson.length(); i++) {
				players.add(parseJsonToPlayer(playersJson.getJSONObject(i)));
			}
		} catch (JSONException e) {
			Log.e(LOG_TAG, e.toString());
		}
		return players;
	}

	/**
	 * Parses the given <tt>JSONObject</tt> to a conflict.
	 * 
	 * @param conflictJson
	 * @return
	 */
	public static Conflict parseJsonToConflict(JSONObject conflictJson) {
		Conflict conflict = new Conflict();
		try {
			Territory territory = parseJsonToTerritory(conflictJson
					.getJSONObject(CONFLICT_TERRITORY_TAG));
			conflict.setTerritory(territory);

			List<House> houses = parseJsonToHouses(conflictJson
					.getJSONArray(CONFLICT_HOUSES_TAG));
			conflict.setHouses(houses);

			List<Integer> diceValues = parseJsonToIntegerList(conflictJson
					.getJSONArray(CONFLICT_DICE_VALUES_TAG));
			conflict.setDiceValues(diceValues);
		} catch (JSONException e) {
			Log.e(LOG_TAG, e.toString());
		}
		return conflict;
	}

	/**
	 * Parses the given <tt>JSONArray</tt> to a list of conflicts.
	 * 
	 * @param conflictsJson
	 * @return
	 */
	public static List<Conflict> parseJsonToConflicts(JSONArray conflictsJson) {
		List<Conflict> conflicts = new ArrayList<Conflict>();
		try {
			for (int i = 0; i < conflictsJson.length(); i++) {
				conflicts.add(parseJsonToConflict(conflictsJson
						.getJSONObject(i)));
			}
		} catch (JSONException e) {
			Log.e(LOG_TAG, e.toString());
		}
		return conflicts;
	}

	/**
	 * Parses the given <tt>JSONObject</tt> to a house.
	 * 
	 * @param houseJson
	 * @return
	 */
	public static House parseJsonToHouse(JSONObject houseJson) {
		House house = new House();
		try {
			house.setName(houseJson.getString(HOUSE_NAME_TAG));
		} catch (JSONException e) {
			Log.e(LOG_TAG, e.toString());
		}
		return house;
	}

	/**
	 * Parses the given <tt>JSONArray</tt> to a List of houses.
	 * 
	 * @param housesJson
	 * @return
	 */
	public static List<House> parseJsonToHouses(JSONArray housesJson) {
		List<House> houses = new ArrayList<House>();
		try {
			for (int i = 0; i < housesJson.length(); i++) {
				houses.add(parseJsonToHouse(housesJson.getJSONObject(i)));
			}
		} catch (JSONException e) {
			Log.e(LOG_TAG, e.toString());
		}
		return houses;
	}

	public static List<Integer> parseJsonToIntegerList(JSONArray jsonArray) {
		List<Integer> integers = new ArrayList<Integer>();
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				integers.add(jsonArray.getInt(i));
			}
		} catch (JSONException e) {
			Log.e(LOG_TAG, e.toString());
		}
		return integers;
	}

}
