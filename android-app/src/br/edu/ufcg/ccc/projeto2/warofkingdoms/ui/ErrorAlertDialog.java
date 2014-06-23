package br.edu.ufcg.ccc.projeto2.warofkingdoms.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ErrorAlertDialog {
	
	private Context context;
	private String message;
	private String title;

	public ErrorAlertDialog(Context context, String title, String message) {
		this.context = context;
		this.title = title;
		this.message = message;
	}
	
	public void showAlertDialog() {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		alertDialog.show();
	}
	
}