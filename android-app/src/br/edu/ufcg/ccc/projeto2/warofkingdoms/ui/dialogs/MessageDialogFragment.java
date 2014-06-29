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
import android.widget.TextView;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants;
import br.ufcg.edu.ccc.projeto2.R;

public class MessageDialogFragment extends DialogFragment implements OnClickListener {

	private String message; 
	
	private TextView messageTxt;
	private Button okButton;
	
	@Override
	public void setArguments(Bundle args) {
		super.setArguments(args);
		
		message = args.getString(Constants.DIALOG_MESSAGE);
	}
	
	@Override
	public View onCreateView(
			LayoutInflater inflater, 
			ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.message_dialog, container);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
		
		final WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
		
        messageTxt = (TextView) view.findViewById(R.id.messageTxt);
        messageTxt.setText(message);
        
        okButton = (Button) view.findViewById(R.id.okBtn);
        okButton.setOnClickListener(this);
        
		return view;
	}

	@Override
	public void onClick(View v) {

		if (v == okButton) {
			dismiss();
		}
	}
}
