package br.edu.ufcg.ccc.projeto2.warofkingdoms.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.GameManager;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.HouseTokenManager;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.util.GoogleFormHttpRequest;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.util.RulesChecker;
import br.ufcg.edu.ccc.projeto2.R;

public class FeedbackActivity extends Activity implements OnClickListener {

	private RatingBar ratingBar;
	private RadioGroup payOrNotOption;
	private EditText generalComments;

	private Button sendDataBtn;
	private Button laterBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback_screen);

		ratingBar = (RatingBar) findViewById(R.id.ratingBar);

		payOrNotOption = (RadioGroup) findViewById(R.id.payOrNotRadioGroup);
		generalComments = (EditText) findViewById(R.id.generalComments);

		sendDataBtn = (Button) findViewById(R.id.sendBtn);
		sendDataBtn.setOnClickListener(this);

		laterBtn = (Button) findViewById(R.id.laterBtn);
		laterBtn.setOnClickListener(this);
	}

	public void onBackPressed() {
		leaveFeedbackScreen();
	}

	@Override
	public void onClick(View v) {
		if (v == sendDataBtn) {
			String commenterId = String.valueOf(getMacAddress().hashCode());
			String rating = String.valueOf(ratingBar.getRating());
			String wouldBuy = getWouldBuySelectedOption() == 0 ? "Y" : "N";
			String comments = generalComments.getText().toString();

			AsyncTask<String, Void, Void> rateRequest = new PostEvaluationRequest(
					commenterId, rating, wouldBuy, comments);
			rateRequest.execute();
		} else if (v == laterBtn) {

		}
		leaveFeedbackScreen();
	}

	private int getWouldBuySelectedOption() {
		int radioButtonID = payOrNotOption.getCheckedRadioButtonId();
		View radioButton = payOrNotOption.findViewById(radioButtonID);
		return payOrNotOption.indexOfChild(radioButton);
	}

	private String getMacAddress() {
		WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = manager.getConnectionInfo();
		return info.getMacAddress();
	}

	private void leaveFeedbackScreen() {
		resetPreviousGameState();
		startActivity(new Intent(this, ConnectActivity.class));
		finish();
	}

	private void resetPreviousGameState() {
		GameManager.getInstance().reset();
		HouseTokenManager.getInstance().reset();
		RulesChecker.getInstance().reset();
	}

	private class PostEvaluationRequest extends AsyncTask<String, Void, Void> {

		private final String formUrl = "https://docs.google.com/forms/d/1hhMEbBq3WtGdPWuEMl1FoTPvHZpQz9-U56j48btUegg/formResponse";

		private final String commenterIdTag = "entry_1290337783";
		private final String ratingTag = "entry_1425278761";
		private final String wouldBuyTag = "entry_1200686582";
		private final String commentsTag = "entry_772083215";

		private String commenterId;
		private String rating;
		private String wouldBuy;
		private String comments;

		public PostEvaluationRequest(String commenterId, String rating,
				String wouldBuy, String comments) {
			super();
			this.commenterId = commenterId;
			this.rating = rating;
			this.wouldBuy = wouldBuy;
			this.comments = comments;
		}

		@Override
		protected Void doInBackground(String... params) {
			postEvaluation();
			return null;
		}

		private void postEvaluation() {
			String requestData = String.format("%s=%s&%s=%s&%s=%s&%s=%s",
					commenterIdTag, commenterId, ratingTag, rating,
					wouldBuyTag, wouldBuy, commentsTag, comments);

			GoogleFormHttpRequest httpRequest = new GoogleFormHttpRequest();
			httpRequest.sendPost(formUrl, requestData);
		}
	}
}