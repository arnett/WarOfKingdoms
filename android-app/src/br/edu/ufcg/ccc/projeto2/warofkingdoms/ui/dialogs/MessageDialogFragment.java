package br.edu.ufcg.ccc.projeto2.warofkingdoms.ui.dialogs;

import android.app.DialogFragment;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.GameManager;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.HouseTokenManager;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.ui.ConnectActivity;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.util.RulesChecker;
import br.ufcg.edu.ccc.projeto2.R;

public class MessageDialogFragment extends DialogFragment implements OnClickListener {

	private String message; 
	private int messageType;
	private String msgHeader;
	
	private TextView messageHeader;
	private TextView messageTxt;
	private Button okButton;
	private ImageView messageImage;
	
	@Override
	public void setArguments(Bundle args) {
		super.setArguments(args);
		
		msgHeader = args.getString(Constants.DIALOG_MESSAGE_HEADER);
		message = args.getString(Constants.DIALOG_MESSAGE);
		messageType = args.getInt(Constants.DIALOG_TYPE);
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
		
        messageHeader = (TextView) view.findViewById(R.id.msgHeader);
        messageHeader.setText(msgHeader);
        
        messageTxt = (TextView) view.findViewById(R.id.messageTxt);
        messageTxt.setText(message);
        
        okButton = (Button) view.findViewById(R.id.okBtn);
        okButton.setOnClickListener(this);
        
        messageImage = (ImageView) view.findViewById(R.id.messageImage);
        switch (messageType) {
		case Constants.DIALOG_ERROR:
			messageImage.setImageResource(R.drawable.error_icon);
			break;
		case Constants.DIALOG_INFO:
			messageImage.setImageResource(R.drawable.info_icon);
			break;
		}
        
		return view;
	}

	@Override
	public void onClick(View v) {

		if (v == okButton) {
			
			dismiss();
			if (messageType == Constants.DIALOG_ERROR) {
				resetPreviousGameState();
				startActivity(new Intent(getActivity(), ConnectActivity.class));
				getActivity().finish();
			}
		}
	}
	
	private void resetPreviousGameState() {
		GameManager.getInstance().reset();
		HouseTokenManager.getInstance().reset();
		RulesChecker.getInstance().reset();
	}
}
