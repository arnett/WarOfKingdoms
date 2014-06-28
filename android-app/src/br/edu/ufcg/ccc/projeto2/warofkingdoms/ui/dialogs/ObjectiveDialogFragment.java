package br.edu.ufcg.ccc.projeto2.warofkingdoms.ui.dialogs;

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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants;
import br.ufcg.edu.ccc.projeto2.R;

public class ObjectiveDialogFragment extends DialogFragment implements OnClickListener {
	
	private Button okBtn;
	
	private int numConqueredNorth;
	private int numConqueredCenter;
	private int numConqueredSouth;
	private boolean hasHomebase;
	private boolean withData = false;
	
	private TextView southObjective;
	private TextView northObjective;
	private TextView centerObjective;
	private TextView homeBaseTxt;
	private LinearLayout objectiveMsg;

	@Override
	public void setArguments(Bundle args) {
		super.setArguments(args);
		
		numConqueredNorth = args.getInt(Constants.NUM_CONQUERED_NORTH);
		numConqueredCenter = args.getInt(Constants.NUM_CONQUERED_CENTER);
		numConqueredSouth = args.getInt(Constants.NUM_CONQUERED_SOUTH);
		hasHomebase = args.getBoolean(Constants.HAS_HOMEBASE);
		
		withData = true;
	}
	
	@Override
	public View onCreateView(
			LayoutInflater inflater, 
			ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.objective_dialog, container);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
		
		final WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        
        okBtn = (Button) view.findViewById(R.id.okBtn);
        okBtn.setOnClickListener(this);
		
        if (withData) {
        	initTextViews(view);
        }
        
		return view;
	}

	private void initTextViews(View view) {
		
		northObjective = (TextView) view.findViewById(R.id.north_objective);
		northObjective.setText("["+numConqueredNorth+"/3]");
		
		southObjective = (TextView) view.findViewById(R.id.south_objective);
		southObjective.setText("["+numConqueredSouth+"/3]");
		
		centerObjective = (TextView) view.findViewById(R.id.center_objective);
		centerObjective.setText("["+numConqueredCenter+"/3]");
		
		objectiveMsg = (LinearLayout) view.findViewById(R.id.objective_msg_layout);
		objectiveMsg.setVisibility(View.GONE);
		
		homeBaseTxt = (TextView) view.findViewById(R.id.my_house_txt);
		if (hasHomebase) {
			homeBaseTxt.setText("Keep Homebase");
		} else {
			homeBaseTxt.setText("Retrieve Homebase!");
		}
	}

	@Override
	public void onClick(View v) {
		
		if (v == okBtn) {
			dismiss();
		}
	}
}
