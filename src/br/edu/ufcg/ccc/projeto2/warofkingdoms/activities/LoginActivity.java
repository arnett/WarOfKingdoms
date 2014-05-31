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
import br.edu.ufcg.ccc.projeto2.warofkingdoms.authentication.Authentication;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.database.UserSQLiteHelper;
import br.ufcg.edu.ccc.projeto2.R;

public class LoginActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		String adminEmail    = "admin@admin.com";
		String adminPassword = "123456";
		boolean DEVELOPMENT = true;
		
		final EditText emailText = (EditText) findViewById(R.id.editText_login_email);
		final EditText passwordText = (EditText) findViewById(R.id.editText_login_password);
		
		if (DEVELOPMENT) {
			emailText.setText(adminEmail);
			passwordText.setText(adminPassword);
		}
		
		Button registerButton = (Button) findViewById(R.id.button_login_register);
		Button loginButton = (Button) findViewById(R.id.button_login);
		Button forgotPasswordButton = (Button) findViewById(R.id.button_login_forgot_password);
		
		registerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent myIntent = new Intent(LoginActivity.this, RegisterPlayerActivity.class);
				startActivity(myIntent);
				finish();
			}
		});
		
		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (Authentication.isCredentialsValid(getApplicationContext(), 
						emailText.getText().toString(), 
						passwordText.getText().toString())) {
					Log.v("TODO", "Login");
					Intent myIntent = new Intent(LoginActivity.this, ChooseScreenActivity.class);
					startActivity(myIntent);
					finish();
				} else {
					Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		forgotPasswordButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Toast.makeText(getApplicationContext(), "TODO", Toast.LENGTH_SHORT).show();
			}
		});
		
		getUsers();
	}
	
	private void getUsers() {
		UserSQLiteHelper userDB = new UserSQLiteHelper(getApplicationContext());
		Log.v("userDB.getAllPlayer()", userDB.getAllPlayer().toString());
	}
}
