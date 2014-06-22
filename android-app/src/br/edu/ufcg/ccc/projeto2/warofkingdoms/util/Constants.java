package br.edu.ufcg.ccc.projeto2.warofkingdoms.util;

public class Constants {

	// Game Constants
	public final static int NUM_PLAYERS = 2;

	public final static String ACTIONS_KEY = "actions";

	public final static String CHOOSE_ACTION_DIALOG_FRAGMENT_LOG_TAG = "ChooseActionDialogFragment";

	// Networking constants

	//public final static String SERVER_URL = "http://ec2-54-207-125-231.sa-east-1.compute.amazonaws.com";
	public final static String SERVER_URL = "http://10.0.0.4:3000";

	public final static String CONNECT_URI = "/connect";
	public final static String SEND_MOVES_URI = "/sendMoves";

	// JSON constants
	public final static String TERRITORY_NAME_TAG = "name";
	public final static String TERRITORY_OWNER_TAG = "owner";

	public final static String MOVE_ORIGIN_TAG = "origin";
	public final static String MOVE_TARGET_TAG = "target";
	public final static String MOVE_ACTION_TAG = "action";

	public final static String PLAYER_ID_TAG = "id";
	public final static String PLAYER_NAME_TAG = "name";
	public final static String PLAYER_HOUSE_TAG = "house";

	public final static String CONFLICT_TERRITORY_TAG = "territory";
	public final static String CONFLICT_HOUSES_TAG = "houses";
	public final static String CONFLICT_DICE_VALUES_TAG = "diceValues";

	public final static String GAME_STATE_IS_GAME_END_TAG = "isGameEnd";
	public final static String GAME_STATE_WINNER_LIST_TAG = "winnerList";
	public final static String GAME_STATE_CURRENT_TURN_TAG = "currentTurn";
	public final static String GAME_STATE_TOTAL_TURNS_TAG = "totalTurns";

	public final static String HOUSE_NAME_TAG = "name";

	public final static String CONNECT_RESULT_TERRITORIES_TAG = "territories";
	public final static String CONNECT_RESULT_PLAYERS_TAG = "players";
	public final static String CONNECT_RESULT_ROOM_ID_TAG = "roomId";

	public final static String SEND_MOVES_RESULT_CONFLICTS_TAG = "conflicts";
	public final static String SEND_MOVES_RESULT_UPDATED_MAP_TAG = "updatedMap";
	public final static String SEND_MOVES_RESULT_GAME_STATE_TAG = "gameState";

	public final static String SEND_MOVES_REQUEST_MOVES_TAG = "moves";
	public final static String SEND_MOVES_REQUEST_ROOM_ID_TAG = "roomId";

	// House names
	public final static String STARK = "Stark";
	public final static String LANNISTER = "Lannister";
	public final static String BARATHEON = "Baratheon";
	public final static String TYRELL = "Tyrell";
	public final static String MARTELL = "Martell";
	public final static String GREYJOY = "Greyjoy";

	public final static String NUM_TIMES_PLAYED_KEY = "timesPlayed";
	public final static String NUM_VICTORIES_KEY = "numVictories";
	public static final String VICTORY_TRACK_SHARED_PREFERENCE = "victoryTrackPreference";
	public static final String TEN_LAST_GAMES = "tenLastGames";

	// Activity parameters
	public static final String GAME_MODE = "gameMode";
	public static final String SINGLEPLAYER_GAME_MODE = "Singleplayer";
	public static final String MULTIPLAYER_GAME_MODE = "Multiplayer";
}
