package br.edu.ufcg.ccc.projeto2.warofkingdoms.management;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.GameState;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants;

public class ProfileManager {

	// NR stands for NOT_RECORDED
	private final String CLEAN_VICTORY_TRACK = "NR NR NR NR NR NR NR NR NR NR";
	public static final String LOST = "L";
	public static final String VICTORY = "V";

	private static ProfileManager instance;

	private ProfileManager() {

	}

	public synchronized static ProfileManager getInstance() {
		if (instance == null) {
			instance = new ProfileManager();
		}
		return instance;
	}

	public void saveGameStatistics(GameState gameState, Context preferenceContext) {

		int numTimesPlayed = getIntSharedPreference(Constants.NUM_TIMES_PLAYED_KEY, preferenceContext);
		int numVictories = getIntSharedPreference(Constants.NUM_VICTORIES_KEY, preferenceContext);

		if (GameManager.getInstance().currentPlayerWon(gameState.getWinnerList())) {

			numVictories++;
			saveInt(Constants.NUM_VICTORIES_KEY, numVictories, preferenceContext);

			String tenLastGames = addHistoryEntry(true, preferenceContext);
			saveString(Constants.TEN_LAST_GAMES, tenLastGames, preferenceContext);
		} else {

			String tenLastGames = addHistoryEntry(false, preferenceContext);
			saveString(Constants.TEN_LAST_GAMES, tenLastGames, preferenceContext);
		}

		numTimesPlayed++;
		saveInt(Constants.NUM_TIMES_PLAYED_KEY, numTimesPlayed, preferenceContext);
	}

	private String addHistoryEntry(boolean victory, Context preferenceContext) {

		String tenLastGames = getStringSharedPreference(Constants.TEN_LAST_GAMES, preferenceContext);

		tenLastGames = removeOldestTrack(tenLastGames);

		if (victory) {
			tenLastGames = VICTORY +" "+ tenLastGames;
		} else {
			tenLastGames = LOST +" "+ tenLastGames;
		}

		return tenLastGames;
	}

	private String removeOldestTrack(String tenLastGames) {
		String[] individualGames = tenLastGames.split(" ");
		String editedVictoryTrack = "";

		for (int i = 0; i < individualGames.length-1; i++) {
			editedVictoryTrack += individualGames[i] +" ";
		}
		return editedVictoryTrack.trim();
	}

	public String getStringSharedPreference(String key, Context preferenceContext) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(preferenceContext);
		return sharedPreferences.getString(key, CLEAN_VICTORY_TRACK);
	}

	public void saveString(String key, String value, Context preferenceContext){
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(preferenceContext);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public void saveInt(String key, int value, Context preferenceContext){
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(preferenceContext);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public int getIntSharedPreference(String key, Context preferenceContext){
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(preferenceContext);
		return sharedPreferences.getInt(key, 0);
	}

}
