package br.edu.ufcg.ccc.projeto2.warofkingdoms.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Player;

public class UserSQLiteHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "PlayerDB";
	
	public static final String TABLE_PLAYER	 	   = "player";
	
	public static final String KEY_PLAYER_ID       = "_id",
							   KEY_PLAYER_EMAIL    = "number",
							   KEY_PLAYER_NAME	   = "name",
							   KEY_PLAYER_PASSWORD = "password";
	
	public static final String[] COLUMNS_PLAYER = {
									KEY_PLAYER_ID,
									KEY_PLAYER_EMAIL,
									KEY_PLAYER_NAME,
									KEY_PLAYER_PASSWORD};
	
	public static final String CREATE_PLAYER_TABLE = "CREATE TABLE " + TABLE_PLAYER + " ( " + 
			   KEY_PLAYER_ID 		 + " INTEGER PRIMARY KEY, " +
			   KEY_PLAYER_EMAIL 	 + " TEXT UNIQUE         NOT NULL, " +
			   KEY_PLAYER_NAME 		 + " TEXT                NOT NULL, " +
			   KEY_PLAYER_PASSWORD 	 + " TEXT                NOT NULL);";
	
	public static final String DROP_PLAYER_TABLE   = "DROP TABLE IF EXISTS " + TABLE_PLAYER + ";";
	
	public UserSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_PLAYER_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DROP_PLAYER_TABLE);
		this.onCreate(db);
	}
	
	public void addPlayer(Player player) {
		Log.d("Player", player.toString());
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_PLAYER_EMAIL, player.getEmail());
		values.put(KEY_PLAYER_NAME, player.getName());
		values.put(KEY_PLAYER_PASSWORD, player.getPassword());
		
		db.insert(TABLE_PLAYER, null, values);
		
		db.close();
	}
	
	public Player getPlayer(String email) {
		Player player;
		
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_PLAYER,
								 COLUMNS_PLAYER,
								 KEY_PLAYER_EMAIL + "=?",
								 new String[] { email },
								 null,
								 null,
								 null);
		
		if (cursor != null)
			cursor.moveToFirst();
		else {
			return null;
		}
		
		if (cursor.getCount() < 1) {
			cursor.close();
			return null;
		}
		
		player = new Player();
		player.setEmail(cursor.getString(1));
		player.setName(cursor.getString(2));
		player.setPassword(cursor.getString(3));
		
		Log.d("getPlayer(" + email + ")", player.toString());
		
		cursor.close();
		db.close();
		
		return player;
	}
	
	private Cursor fetchAllPlayer(String sortBy) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_PLAYER, COLUMNS_PLAYER, null, null, null, null, sortBy);
		
		if (cursor != null)
			cursor.moveToFirst();
		return cursor;
	}
	
	public List<Player> getAllPlayer(String sortBy) {
		List<Player> playerList = new ArrayList<Player>();
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = fetchAllPlayer(sortBy);
		
		Player player = null;
		if (cursor.moveToFirst()) {
			do {
				player = new Player();
				player.setEmail(cursor.getString(1));
				player.setName(cursor.getString(2));
				player.setPassword(cursor.getString(3));
				
				playerList.add(player);
			} while (cursor.moveToNext());
		}
		
		Log.d("getAllPlayer()", playerList.toString());
		
		db.close();
		
		return playerList;
	}
	
	public List<Player> getAllPlayer() {
		return getAllPlayer(KEY_PLAYER_ID);
	}
}
