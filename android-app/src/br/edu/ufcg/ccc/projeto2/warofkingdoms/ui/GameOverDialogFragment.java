package br.edu.ufcg.ccc.projeto2.warofkingdoms.ui;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import br.ufcg.edu.ccc.projeto2.R;

public class GameOverDialogFragment extends DialogFragment implements OnClickListener {

	private Button gameOverOkBtn;
	private TextView winnersTextView;
	private String winners;
	
	@Override
	public View onCreateView(
							LayoutInflater inflater, 
							ViewGroup container,
							Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.game_over_custom_dialog, container);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		
		setStyle(STYLE_NO_FRAME, android.R.style.Theme_Translucent_NoTitleBar);
		
		gameOverOkBtn = (Button) view.findViewById(R.id.gameOverOk);
		gameOverOkBtn.setOnClickListener(this);
		
		winnersTextView = (TextView) view.findViewById(R.id.winners_text_view);
		winnersTextView.setText(winners);
		
		return view;
	}

	public void onClick(View v) {
		
		if (v == gameOverOkBtn) {
			
			startActivity(new Intent(getActivity(), ConnectActivity.class));
			getActivity().finish();
		}
		
	}

	public String getWinners() {
		return winners;
	}

	public void setWinners(String winners) {
		this.winners = winners;
	}
}
