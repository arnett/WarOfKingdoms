package br.edu.ufcg.ccc.projeto2.warofkingdoms.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import br.ufcg.edu.ccc.projeto2.R;

public class FeedbackActivity extends Activity implements OnClickListener{

	private RatingBar ratingBar;
	private RadioGroup payOrNotOption;
	private RadioButton payOpt;
	private RadioButton notPayOpt;
	private EditText generalComments;
	
	private Button sendDataBtn;
	private Button laterBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback_screen);

		ratingBar = (RatingBar) findViewById(R.id.ratingBar);
		
		payOrNotOption = (RadioGroup) findViewById(R.id.payOrNotRadioGroup);
		payOpt = (RadioButton) findViewById(R.id.yesRadioOpt);
		notPayOpt = (RadioButton) findViewById(R.id.noRadioOpt);
		generalComments = (EditText) findViewById(R.id.generalComments);
		
		sendDataBtn = (Button) findViewById(R.id.sendBtn);
		sendDataBtn.setOnClickListener(this);
		
		laterBtn = (Button) findViewById(R.id.laterBtn);
		laterBtn.setOnClickListener(this);
	}
	
	public void onBackPressed() {
		startActivity(new Intent(this, ConnectActivity.class));
		super.onBackPressed();
	}
	
	@Override
	public void onClick(View v) {

		if (v == sendDataBtn) {
			// TODO
		}
		else if (v == laterBtn) {
			// TODO
		}
	}

}