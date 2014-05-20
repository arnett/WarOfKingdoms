package br.edu.ufcg.ccc.projeto2.warofkingdoms.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Action;
import br.ufcg.edu.ccc.projeto2.R;

public class ChooseActionDialogFragment extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		String[] s = Action.getStringValues();

		builder.setTitle(R.string.choose_an_action).setItems(s,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

					}
				});

		return builder.create();
	}

}
