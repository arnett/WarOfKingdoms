package br.edu.ufcg.ccc.projeto2.warofkingdoms.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import br.ufcg.edu.ccc.projeto2.R;

public class PanelActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player_panel);
		
		Button changePasswordButton = (Button) findViewById(R.id.button_panel_changePassword);
		Button deleteAccountButton = (Button) findViewById(R.id.button_panel_deleteAccount);
		Button playButton = (Button) findViewById(R.id.button_panel_play);
		
		changePasswordButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Toast.makeText(getApplicationContext(), "TODO", Toast.LENGTH_SHORT).show();
			}
		});
		
		deleteAccountButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Toast.makeText(getApplicationContext(), "TODO", Toast.LENGTH_SHORT).show();
			}
		});
		
		playButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent myIntent = new Intent(PanelActivity.this, ChooseScreenActivity.class);
				startActivity(myIntent);
				finish();
			}
		});
	}
}
