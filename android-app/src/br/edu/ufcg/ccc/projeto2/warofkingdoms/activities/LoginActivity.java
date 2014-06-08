package br.edu.ufcg.ccc.projeto2.warofkingdoms.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.authentication.Authentication;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.database.UserSQLiteHelper;
import br.ufcg.edu.ccc.projeto2.R;

public class LoginActivity extends Activity {
	
	@SuppressWarnings("unused")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		
		String adminEmail    = "admin@admin.com";
		String adminPassword = "123456";
		boolean DEVELOPMENT = true;
		
		final EditText emailText = (EditText) findViewById(R.id.editText_login_email);
		final EditText passwordText = (EditText) findViewById(R.id.editText_login_password);
		
		Button registerButton = (Button) findViewById(R.id.button_login_register);
		Button forgotPasswordButton = (Button) findViewById(R.id.button_login_forgot_password);
		
		emailText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if (!hasFocus) {
					if (!isEmailValid(emailText.getText().toString()))
						emailText.setError("Invalid Email");
				}
			}
		});
		
		passwordText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				if (isEmailValid(emailText.getText().toString()))
					if (Authentication.isCredentialsValid(getApplicationContext(), 
							emailText.getText().toString(), 
							passwordText.getText().toString()))
						changeActivity(PanelActivity.class);
					else
						passwordText.setError("Incorrect Password");
			}
		});
		
		registerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent myIntent = new Intent(LoginActivity.this, RegisterPlayerActivity.class);
				startActivity(myIntent);
				finish();
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
	
	private void changeActivity(Class<?> newActivity) {
		Intent myIntent = new Intent(LoginActivity.this, newActivity);
		startActivity(myIntent);
		finish();
	}
	
	private boolean isEmailValid(String email) {
		return Authentication.isLoginEmailValid(getApplicationContext(), email);
	}
	
	private void getUsers() {
		UserSQLiteHelper userDB = new UserSQLiteHelper(getApplicationContext());
		Log.v("userDB.getAllPlayer()", userDB.getAllPlayer().toString());
	}
}
