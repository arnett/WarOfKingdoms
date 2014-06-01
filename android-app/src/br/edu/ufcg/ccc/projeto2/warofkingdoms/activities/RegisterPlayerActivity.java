package br.edu.ufcg.ccc.projeto2.warofkingdoms.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.database.UserSQLiteHelper;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Player;
import br.ufcg.edu.ccc.projeto2.R;

public class RegisterPlayerActivity extends Activity {

	private EditText emailEditText;
	private EditText nameEditText;
	private EditText passwordEditText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_player);
		
		emailEditText = (EditText) findViewById(R.id.editText_register_email);
		nameEditText = (EditText) findViewById(R.id.editText_register_name);
		passwordEditText = (EditText) findViewById(R.id.editText_register_password);
		
		Button registerButton = (Button) findViewById(R.id.button_register);
		Button backButton = (Button) findViewById(R.id.button_register_back);
		
		registerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String email = emailEditText.getText().toString();
				String name = emailEditText.getText().toString();
				String password = emailEditText.getText().toString();
				
				if (!isValuesValid()) {
					Toast.makeText(getApplicationContext(), "You Shall Not Pass!", Toast.LENGTH_SHORT).show();
				} else {
					if (addPlayer(email, name, password)) {
						Intent myIntent = new Intent(RegisterPlayerActivity.this, LoginActivity.class);
						startActivity(myIntent);
						finish();
					} else {
						Toast.makeText(getApplicationContext(), "You Shall Not Pass! Email already Registered.", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent myIntent = new Intent(RegisterPlayerActivity.this, LoginActivity.class);
				startActivity(myIntent);
				finish();
			}
		});
	}
	
	private boolean addPlayer(String email, String name, String password) {
		Player p = new Player();
		p.setEmail(emailEditText.getText().toString());
		p.setName(nameEditText.getText().toString());
		p.setPassword(passwordEditText.getText().toString());
		
		UserSQLiteHelper userDB = new UserSQLiteHelper(getApplicationContext());
		try {
			userDB.addPlayer(p);
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}
	
	private boolean isValuesValid() {
		if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
			Log.v("Invalid Field Value", "Invalid Email");
			return false;
		} if (nameEditText.getText().toString().isEmpty() || nameEditText.getText().toString().length() < 3) {
			Log.v("Invalid Field Value", "Name with length less than 3");
			return false;
		} if (passwordEditText.getText().toString().isEmpty() || passwordEditText.getText().toString().length() < 6) {
			Log.v("Invalid Field Value", "Password with length less than 6");
			return false;
		}
		return true;
	}
}
