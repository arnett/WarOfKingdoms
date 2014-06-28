package br.edu.ufcg.ccc.projeto2.warofkingdoms.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

/**
 * @author Rafael
 *
 *	Class to help the creation of Alert dialogs.
 */
public class ErrorAlertDialog {
	
	private Context context;
	private String message;
	private String title;

	/**
	 * @param context
	 * @param title
	 * @param message
	 */
	public ErrorAlertDialog(Context context, String title, String message) {
		this.context = context;
		this.title = title;
		this.message = message;
	}
	
	/**
	 * Method to show the alert dialogs.
	 */
	public void showAlertDialog() {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(context, ConnectActivity.class);
				context.startActivity(intent);
			}
		});
		alertDialog.show();
	}
	
}