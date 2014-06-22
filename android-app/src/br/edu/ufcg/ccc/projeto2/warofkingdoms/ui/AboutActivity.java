package br.edu.ufcg.ccc.projeto2.warofkingdoms.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import br.ufcg.edu.ccc.projeto2.R;

public class AboutActivity extends Activity implements OnClickListener{

	private String LOG_TAG = "AboutActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_screen);

	}
	
	@Override
	public void onBackPressed() {
		startActivity(new Intent(this, ConnectActivity.class));
		super.onBackPressed();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}