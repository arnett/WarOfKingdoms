package br.edu.ufcg.ccc.projeto2.warofkingdoms.ui;

import br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class ChooseGameModeDialogFragment extends DialogFragment {

	private OnGameSelectedListener gameListener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			this.gameListener = (OnGameSelectedListener) activity;
		} catch (final ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnCompleteListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final String[] GAME_TYPES = new String[] { 
				Constants.SINGLEPLAYER_GAME_MODE,
				Constants.MULTIPLAYER_GAME_MODE };
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Game Mode");
		builder.setItems(GAME_TYPES,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						gameListener.onGameSelected(GAME_TYPES[which]);
					}
				});
		return builder.create();
	}

	public static interface OnGameSelectedListener {
		public void onGameSelected(String gameSelected);
	}
}