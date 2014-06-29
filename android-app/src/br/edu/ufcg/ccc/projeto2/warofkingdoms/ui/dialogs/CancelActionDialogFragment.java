package br.edu.ufcg.ccc.projeto2.warofkingdoms.ui.dialogs;

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
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Action;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.ui.OnActionSelectedListener;
import br.ufcg.edu.ccc.projeto2.R;

public class CancelActionDialogFragment extends DialogFragment implements OnClickListener {

	private OnActionSelectedListener choiceListener;
	
	private ImageView cancelBtn;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			this.choiceListener = (OnActionSelectedListener) activity;
		} catch (final ClassCastException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public View onCreateView(
			LayoutInflater inflater, 
			ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.cancel_action_dialog, container);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
		
		final WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        
        cancelBtn = (ImageView) view.findViewById(R.id.cancel_action_button);
        cancelBtn.setOnClickListener(this);
        
		return view;
	}

	@Override
	public void onClick(View v) {
		
		if (v == cancelBtn) {
			choiceListener.onActionSelected(Action.CANCEL);
			dismiss();
		}
	}
}

