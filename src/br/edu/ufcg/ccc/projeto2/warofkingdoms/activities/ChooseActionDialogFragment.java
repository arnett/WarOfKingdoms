package br.edu.ufcg.ccc.projeto2.warofkingdoms.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Action;
import br.ufcg.edu.ccc.projeto2.R;

public class ChooseActionDialogFragment extends DialogFragment {

	private Action[] actions;
	private OnActionSelectedListener choiceListener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			this.choiceListener = (OnActionSelectedListener) activity;
		} catch (final ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnCompleteListener");
		}
	}

	private String[] getStringValues(Action[] actions) {
		String[] actionsAsString = new String[actions.length];
		for (int i = 0; i < actions.length; i++) {
			actionsAsString[i] = actions[i].toString();
		}
		return actionsAsString;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setTitle(R.string.choose_an_action).setItems(
				getStringValues(actions),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						choiceListener.onActionSelected(actions[which]);
					}
				});

		return builder.create();
	}

	public void setActions(Action[] actions) {
		this.actions = actions;
	}

	public static interface OnActionSelectedListener {
		public void onActionSelected(Action chosenAction);
	}
}
