package br.edu.ufcg.ccc.projeto2.warofkingdoms.ui;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Action;
import br.ufcg.edu.ccc.projeto2.R;

public class ChooseActionDialogFragment extends DialogFragment implements OnClickListener {

	private Action[] actions;
	private OnActionSelectedListener choiceListener;
	
	private ImageView attackActionBtn;
	private ImageView defenseActionBtn;
	
	private LinearLayout actionDefenseLayout;
	private LinearLayout actionDialogDesignLayout;

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
	
	@Override
	public View onCreateView(
			LayoutInflater inflater, 
			ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.custom_dialog, container);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		
		actionDefenseLayout = (LinearLayout) view.findViewById(R.id.action_defense_layout);
		actionDialogDesignLayout = (LinearLayout) view.findViewById(R.id.action_dialog_layout);
		
		attackActionBtn = (ImageView) view.findViewById(R.id.attack_action_button);
		attackActionBtn.setOnClickListener(this);
		
		defenseActionBtn = (ImageView) view.findViewById(R.id.defense_action_button);
		defenseActionBtn.setOnClickListener(this);
		
		setStyle(STYLE_NO_FRAME, android.R.style.Theme_Translucent_NoTitleBar);
		
		
		if (actions.length == 1) {	// only attack - hiding the defense action
			
			actionDefenseLayout.setVisibility(View.GONE);
			actionDialogDesignLayout.setVisibility(View.GONE);
		}
		
		return view;
	}

	public void setActions(Action[] actions) {
		this.actions = actions;
	}

	public static interface OnActionSelectedListener {
		public void onActionSelected(Action chosenAction);
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
}
