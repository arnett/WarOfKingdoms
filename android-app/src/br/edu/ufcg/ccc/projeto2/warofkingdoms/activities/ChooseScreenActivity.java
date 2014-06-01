package br.edu.ufcg.ccc.projeto2.warofkingdoms.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import br.ufcg.edu.ccc.projeto2.R;

public class ChooseScreenActivity extends Activity {
	
	private Integer[] imagesId = {R.drawable.andorinha,
								  R.drawable.dragao,
								  R.drawable.gato,
								  R.drawable.lobo,
								  R.drawable.pato,
								  R.drawable.tubarao};
	
	private String[] objects = {"Andorinha",
								"Dragao",
								"Gato",
								"Lobo",
								"Pato",
								"Tubarao"};
	
	private ImageView imgAvatar;
	private TextView nameAvatar;
	
	private Button nextPhaseButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_house);
		
		CustomList adapter = new CustomList(this, imagesId, objects);
		
		nextPhaseButton = (Button) findViewById(R.id.nextPhaseButton);
		
		imgAvatar = (ImageView) findViewById(R.id.avatar);
		nameAvatar = (TextView) findViewById(R.id.name);
		
		imgAvatar.setImageResource(imagesId[0]);
		
		ListView list = (ListView) findViewById(R.id.list);
		list.setAdapter(adapter);
		
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				imgAvatar.setImageResource(imagesId[position]);
			}
		});
		
		nextPhaseButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(ChooseScreenActivity.this, GameActivity.class);
				startActivity(myIntent);
				finish();
			}
		});
	}
	
	public static void hideSoftKeyboard(Activity activity) {

        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
