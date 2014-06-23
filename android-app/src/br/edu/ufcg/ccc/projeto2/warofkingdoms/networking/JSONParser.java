package br.edu.ufcg.ccc.projeto2.warofkingdoms.networking;

import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.CONFLICT_DICE_VALUES_TAG;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.CONFLICT_HOUSES_TAG;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.CONFLICT_TERRITORY_TAG;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.CONNECT_RESULT_PLAYERS_TAG;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.CONNECT_RESULT_ROOM_ID_TAG;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.CONNECT_RESULT_TERRITORIES_TAG;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.GAME_STATE_IS_GAME_END_TAG;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.GAME_STATE_WINNER_LIST_TAG;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.HOUSE_NAME_TAG;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.MOVE_ACTION_TAG;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.MOVE_ORIGIN_TAG;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.MOVE_TARGET_TAG;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.PLAYER_HOUSE_TAG;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.PLAYER_ID_TAG;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.PLAYER_NAME_TAG;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.SEND_MOVES_RESULT_CONFLICTS_TAG;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.SEND_MOVES_RESULT_GAME_STATE_TAG;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.SEND_MOVES_RESULT_UPDATED_MAP_TAG;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.TERRITORY_NAME_TAG;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.TERRITORY_OWNER_TAG;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Conflict;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.GameState;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.House;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Move;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Player;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Territory;

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
	 * @throws JSONException 
	 */
	public static JSONObject parseConflictToJson(Conflict conflict) throws JSONException {
		JSONObject conflictJson = new JSONObject();
		conflictJson.put(CONFLICT_TERRITORY_TAG,
				parseTerritoryToJson(conflict.getTerritory()));
		conflictJson.put(CONFLICT_HOUSES_TAG,
				parseHousesToJson(conflict.getHouses()));
		conflictJson.put(CONFLICT_DICE_VALUES_TAG,
				parseIntegersToJson(conflict.getDiceValues()));
		return conflictJson;
	}

	/**
	 * Parses the given list of moves to a <tt>JSONArray</tt>.
	 * 
	 * @param conflicts
	 * @return
	 * @throws JSONException 
	 */
	public static JSONArray parseConflictsToJson(List<Conflict> conflicts) throws JSONException {
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
	 * @throws JSONException 
	 */
	public static JSONObject parsePlayerToJson(Player player) throws JSONException {
		JSONObject playerJson = new JSONObject();
		playerJson.put(PLAYER_ID_TAG, player.getId());
		playerJson.put(PLAYER_NAME_TAG, player.getName());
		if (player.getHouse() == null) {
			playerJson.put(PLAYER_HOUSE_TAG, JSONObject.NULL);
		} else {
			playerJson.put(PLAYER_HOUSE_TAG,
					parseHouseToJson(player.getHouse()));
		}
		return playerJson;
	}

	/**
	 * Parses the given list of moves to a <tt>JSONArray</tt>.
	 * 
	 * @param players
	 * @return
	 * @throws JSONException 
	 */
	public static JSONArray parsePlayersToJson(List<Player> players) throws JSONException {
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
	 * @throws JSONException 
	 */
	public static JSONObject parseHouseToJson(House house) throws JSONException {
		JSONObject houseJson = new JSONObject();
		houseJson.put(HOUSE_NAME_TAG, house.getName());
		return houseJson;
	}

	/**
	 * Parses the given list of moves to a <tt>JSONArray</tt>.
	 * 
	 * @param moves
	 * @return
	 * @throws JSONException 
	 */
	public static JSONArray parseHousesToJson(List<House> houses) throws JSONException {
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
	 * @throws JSONException 
	 */
	public static JSONObject parseTerritoryToJson(Territory territory) throws JSONException {
		JSONObject territoryJson = new JSONObject();
		territoryJson.put(TERRITORY_NAME_TAG, territory.getName());

		if (territory.getOwner() == null) {
			territoryJson.put(TERRITORY_OWNER_TAG, JSONObject.NULL);
		} else {
			territoryJson.put(TERRITORY_OWNER_TAG,
					parseHouseToJson(territory.getOwner()));
		}
		return territoryJson;
	}

	/**
	 * Parses the given list of territories to a <tt>JSONArray</tt>.
	 * 
	 * @param territories
	 * @return
	 * @throws JSONException 
	 */
	public static JSONArray parseTerritoriesToJson(List<Territory> territories) throws JSONException {
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
	 * @throws JSONException 
	 */
	public static JSONObject parseMoveToJson(Move move) throws JSONException {
		JSONObject moveJson = new JSONObject();
		moveJson.put(MOVE_ORIGIN_TAG,
				parseTerritoryToJson(move.getOrigin()));
		moveJson.put(MOVE_TARGET_TAG,
				parseTerritoryToJson(move.getTarget()));
		moveJson.put(MOVE_ACTION_TAG, move.getAction().toString());
		return moveJson;
	}

	/**
	 * Parses the given list of moves to a <tt>JSONArray</tt>.
	 * 
	 * @param moves
	 * @return
	 * @throws JSONException 
	 */
	public static JSONArray parseMovesToJson(List<Move> moves) throws JSONException {
		JSONArray movesJsonArray = new JSONArray();
		for (Move move : moves) {
			movesJsonArray.put(parseMoveToJson(move));
		}
		return movesJsonArray;
	}

	public static ConnectResult parseJsonToConnectResult(
			JSONObject connectResult) throws JSONException {
		ConnectResult result = new ConnectResult();
		List<Territory> territories = parseJsonToTerritories(connectResult
				.getJSONArray(CONNECT_RESULT_TERRITORIES_TAG));
		List<Player> players = parseJsonToPlayers(connectResult
				.getJSONArray(CONNECT_RESULT_PLAYERS_TAG));
		String roomId = connectResult.getString(CONNECT_RESULT_ROOM_ID_TAG);

		result.setTerritories(territories);
		result.setPlayers(players);
		result.setRoomId(roomId);
		return result;
	}

	public static SendMovesResult parseJsonToSendMovesResult(
			JSONObject jsonSendMovesResult) throws JSONException {
		SendMovesResult result = new SendMovesResult();
		List<Conflict> conflicts = parseJsonToConflicts(jsonSendMovesResult
				.getJSONArray(SEND_MOVES_RESULT_CONFLICTS_TAG));
		List<Territory> updatedMap = parseJsonToTerritories(jsonSendMovesResult
				.getJSONArray(SEND_MOVES_RESULT_UPDATED_MAP_TAG));
		GameState gameState = parseJsonToGameState(jsonSendMovesResult
				.getJSONObject(SEND_MOVES_RESULT_GAME_STATE_TAG));
		result.setConflicts(conflicts);
		result.setUpdatedMap(updatedMap);
		result.setGameState(gameState);

		return result;
	}

	/**
	 * Parses the given <tt>JSONObject</tt> to a territory.
	 * 
	 * @param territoryJson
	 * @return
	 * @throws JSONException 
	 */
	public static Territory parseJsonToTerritory(JSONObject territoryJson) throws JSONException {
		Territory territory = new Territory();
		territory.setName(territoryJson.getString(TERRITORY_NAME_TAG));

		if (!territoryJson.isNull(TERRITORY_OWNER_TAG)) {
			House house = parseJsonToHouse(territoryJson
					.getJSONObject(TERRITORY_OWNER_TAG));
			territory.setOwner(house);
		}
		return territory;
	}

	/**
	 * Parses the given <tt>JSONArray</tt> to a list of territories.
	 * 
	 * @param territoriesJson
	 * @return
	 * @throws JSONException 
	 */
	public static List<Territory> parseJsonToTerritories(
			JSONArray territoriesJson) throws JSONException {
		List<Territory> territories = new ArrayList<Territory>();
		for (int i = 0; i < territoriesJson.length(); i++) {
			territories.add(parseJsonToTerritory(territoriesJson
					.getJSONObject(i)));
		}
		return territories;
	}

	/**
	 * Parses the given <tt>JSONObject</tt> to a player.
	 * 
	 * @param playerJson
	 * @return
	 * @throws JSONException 
	 */
	public static Player parseJsonToPlayer(JSONObject playerJson) throws JSONException {
		Player player = new Player();
		player.setId(playerJson.getString(PLAYER_ID_TAG));
		player.setName(playerJson.getString(PLAYER_NAME_TAG));

		House house = parseJsonToHouse(playerJson
				.getJSONObject(PLAYER_HOUSE_TAG));
		player.setHouse(house);
		return player;
	}

	/**
	 * Parses the given <tt>JSONArray</tt> to a list of players.
	 * 
	 * @param playersJson
	 * @return
	 * @throws JSONException 
	 */
	public static List<Player> parseJsonToPlayers(JSONArray playersJson) throws JSONException {
		List<Player> players = new ArrayList<Player>();
		for (int i = 0; i < playersJson.length(); i++) {
			players.add(parseJsonToPlayer(playersJson.getJSONObject(i)));
		}
		return players;
	}

	/**
	 * Parses the given <tt>JSONObject</tt> to a GameState.
	 * 
	 * @param gameStateJson
	 * @return
	 * @throws JSONException 
	 */
	public static GameState parseJsonToGameState(JSONObject gameStateJson) throws JSONException {
		GameState gameState = new GameState();
		gameState.setGameEnd(gameStateJson
				.getBoolean(GAME_STATE_IS_GAME_END_TAG));
		gameState.setWinnerList(parseJsonToPlayers(gameStateJson
				.getJSONArray(GAME_STATE_WINNER_LIST_TAG)));

		return gameState;
	}

	/**
	 * Parses the given <tt>JSONObject</tt> to a conflict.
	 * 
	 * @param conflictJson
	 * @return
	 * @throws JSONException 
	 */
	public static Conflict parseJsonToConflict(JSONObject conflictJson) throws JSONException {
		Conflict conflict = new Conflict();
		Territory territory = parseJsonToTerritory(conflictJson
				.getJSONObject(CONFLICT_TERRITORY_TAG));
		conflict.setTerritory(territory);

		List<House> houses = parseJsonToHouses(conflictJson
				.getJSONArray(CONFLICT_HOUSES_TAG));
		conflict.setHouses(houses);

		List<Integer> diceValues = parseJsonToIntegerList(conflictJson
				.getJSONArray(CONFLICT_DICE_VALUES_TAG));
		conflict.setDiceValues(diceValues);
		return conflict;
	}

	/**
	 * Parses the given <tt>JSONArray</tt> to a list of conflicts.
	 * 
	 * @param conflictsJson
	 * @return
	 * @throws JSONException 
	 */
	public static List<Conflict> parseJsonToConflicts(JSONArray conflictsJson) throws JSONException {
		List<Conflict> conflicts = new ArrayList<Conflict>();
		for (int i = 0; i < conflictsJson.length(); i++) {
			conflicts.add(parseJsonToConflict(conflictsJson
					.getJSONObject(i)));
		}
		return conflicts;
	}

	/**
	 * Parses the given <tt>JSONObject</tt> to a house.
	 * 
	 * @param houseJson
	 * @return
	 * @throws JSONException 
	 */
	public static House parseJsonToHouse(JSONObject houseJson) throws JSONException {
		House house = new House();
		house.setName(houseJson.getString(HOUSE_NAME_TAG));
		return house;
	}

	/**
	 * Parses the given <tt>JSONArray</tt> to a List of houses.
	 * 
	 * @param housesJson
	 * @return
	 * @throws JSONException 
	 */
	public static List<House> parseJsonToHouses(JSONArray housesJson) throws JSONException {
		List<House> houses = new ArrayList<House>();
		for (int i = 0; i < housesJson.length(); i++) {
			houses.add(parseJsonToHouse(housesJson.getJSONObject(i)));
		}
		return houses;
	}

	public static List<Integer> parseJsonToIntegerList(JSONArray jsonArray) throws JSONException {
		List<Integer> integers = new ArrayList<Integer>();
		for (int i = 0; i < jsonArray.length(); i++) {
			integers.add(jsonArray.getInt(i));
		}
		return integers;
	}

}
