package br.edu.ufcg.ccc.projeto2.warofkingdoms.ui.dialogs;

import java.util.List;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Player;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.GameManager;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.HouseTokenManager;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.ui.ConnectActivity;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.ui.FeedbackActivity;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.util.RulesChecker;
import br.ufcg.edu.ccc.projeto2.R;

public class GameOverDialogFragment extends DialogFragment implements
		OnClickListener {

	private Button gameOverOkBtn;
	private TextView winnersTextView;
	private TextView dialogTitle;
	private List<Player> winners;
	private boolean wo;
	
	private ImageView starkToken;
	private ImageView lannisterToken;
	private ImageView martellToken;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.game_over_custom_dialog,
				container);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));

		final WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;

		gameOverOkBtn = (Button) view.findViewById(R.id.gameOverOk);
		gameOverOkBtn.setOnClickListener(this);

		winnersTextView = (TextView) view.findViewById(R.id.winners_text_view);
		winnersTextView.setText(getWinnersText());
		
		dialogTitle = (TextView) view.findViewById(R.id.gameOverTitle);
		
		if (wo) {
			dialogTitle.setText(getActivity().getString(R.string.wo_label));
		}

		starkToken = (ImageView) view.findViewById(R.id.starkTokenImage);
		lannisterToken = (ImageView) view.findViewById(R.id.lannisterTokenImage);
		martellToken = (ImageView) view.findViewById(R.id.martellTokenImage);
		
		showWinnersHouses();

		setCancelable(false);

		return view;
	}
	
	private void showWinnersHouses() {
		
		for (Player winner : winners) {
			
			if (winner.getHouse().getName().equals("Stark")) {
				starkToken.setVisibility(View.VISIBLE);
			}
			else if (winner.getHouse().getName().equals("Lannister")) {
				lannisterToken.setVisibility(View.VISIBLE);
			}
			else if (winner.getHouse().getName().equals("Martell")) {
				martellToken.setVisibility(View.VISIBLE);
			}
		}
	}

	private String getWinnersText() {
		String winnersText = "";
		for (Player p : winners) {
			
			String playerName = p.getName();
			
			if (p.getName().equals("Anonymous Player")) {
				playerName = getString(R.string.anonymous_player);
			}
			
			if (p.equals(GameManager.getInstance().getCurrentPlayer())) {
			
				winnersText += playerName +" ("+ getString(R.string.you_label) +") "+ getString(R.string.won_label)+"\n";
			} 
			else {
				winnersText += playerName +" "+ getString(R.string.won_label)+"\n";
			}
			
		}
		return winnersText;
	}

	public void onClick(View v) {
		if (v == gameOverOkBtn) {
			if (!wasFeedbackSent()) {

				startActivity(new Intent(getActivity(), FeedbackActivity.class));
				getActivity().finish();
			} else {
				leaveGameScreen();
			}
		}
	}
	
	private void leaveGameScreen() {
		resetPreviousGameState();
		startActivity(new Intent(getActivity(), ConnectActivity.class));
		dismiss();
	}
	
	private void resetPreviousGameState() {
		GameManager.getInstance().reset();
		HouseTokenManager.getInstance().reset();
		RulesChecker.getInstance().reset();
	}

	private boolean wasFeedbackSent() {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		return sharedPreferences.getBoolean("feedbackSent", false);
	}

	public List<Player> getWinners() {
		return winners;
	}

	public void setWinners(List<Player> list) {
		this.winners = list;
	}

	public boolean isWO() {
		return wo;
	}

	public void setWO(boolean wo) {
		this.wo = wo;
	}
}
