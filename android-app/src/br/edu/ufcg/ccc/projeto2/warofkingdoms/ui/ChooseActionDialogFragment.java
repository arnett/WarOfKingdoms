package br.edu.ufcg.ccc.projeto2.warofkingdoms.ui;

import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Action;
import br.ufcg.edu.ccc.projeto2.R;

public class ChooseActionDialogFragment extends DialogFragment implements OnClickListener {

	private Action[] actions;
	private OnActionSelectedListener choiceListener;
	
	private ImageView attackActionBtn;
	private ImageView defenseActionBtn;
	
	@SuppressWarnings("unused")
	private LinearLayout actionDefenseLayout;
	private LinearLayout actionAttackLayout;
	private LinearLayout actionDialogDesignLayout;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			this.choiceListener = (OnActionSelectedListener) activity;
		} catch (final ClassCastException e) {
			ErrorAlertDialog errorDialog = new ErrorAlertDialog(activity, "Process error", 
					"Error while processing the move. Try Again");
			errorDialog.showAlertDialog();
		}
	}
	
	@Override
	public View onCreateView(
			LayoutInflater inflater, 
			ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.action_dialog, container);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
		
		final WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
		
		actionDefenseLayout = (LinearLayout) view.findViewById(R.id.action_defense_layout);
		actionAttackLayout = (LinearLayout) view.findViewById(R.id.action_attack_layout);
		actionDialogDesignLayout = (LinearLayout) view.findViewById(R.id.action_dialog_layout);
		
		attackActionBtn = (ImageView) view.findViewById(R.id.attack_action_button);
		attackActionBtn.setOnClickListener(this);
		
		defenseActionBtn = (ImageView) view.findViewById(R.id.defense_action_button);
		defenseActionBtn.setOnClickListener(this);
		
		if (actions.length == 1) {	// only attack - hiding the defense action
			
			actionAttackLayout.setVisibility(View.GONE);
			actionDialogDesignLayout.setVisibility(View.GONE);
		}
		
		return view;
	}

	public void setActions(Action[] actions) {
		this.actions = actions;
	}

	@Override
	public void onClick(View v) {
		
		if (v == attackActionBtn) {
			choiceListener.onActionSelected(Action.ATTACK);
			dismiss();

		} else if (v == defenseActionBtn) {
			choiceListener.onActionSelected(Action.DEFEND);
			dismiss();
		}
	}

	public static interface OnActionSelectedListener {
		public void onActionSelected(Action chosenAction);
	}
}
