package br.edu.ufcg.ccc.projeto2.warofkingdoms.util;

public class Constants {

	public final static String ACTIONS_KEY = "actions";

	public final static String CHOOSE_ACTION_DIALOG_FRAGMENT_LOG_TAG = "ChooseActionDialogFragment";

	// Networking constants
//	public static String SERVER_URL = "http://ec2-54-207-113-191.sa-east-1.compute.amazonaws.com:4000";
	public final static String SERVER_URL = "http://192.168.2.1:3000";
	
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
	
	public final static String HOUSE_NAME_TAG = "name";
}
