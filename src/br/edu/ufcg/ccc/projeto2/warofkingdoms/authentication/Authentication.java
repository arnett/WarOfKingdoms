package br.edu.ufcg.ccc.projeto2.warofkingdoms.authentication;

import android.content.Context;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.database.UserSQLiteHelper;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Player;

public class Authentication {
	
	public static boolean isCredentialsValid(Context context, String email, String password) {
		UserSQLiteHelper userDB = new UserSQLiteHelper(context);
		Player player = userDB.getPlayer(email);
		
		if (player == null)
			return false;
		
		return player.getPassword().equals(password);
	}
}
