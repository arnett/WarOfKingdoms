package br.edu.ufcg.ccc.projeto2.warofkingdoms.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import br.ufcg.edu.ccc.projeto2.R;

public class PrivateRoomActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_private_room);
		
		final List<String> roomsNames = new ArrayList<String>();
		roomsNames.add("Sala01");
		roomsNames.add("Sala02");
		roomsNames.add("Sala03");

		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_checked, roomsNames);
		
		final ListView listview = (ListView) findViewById(R.id.listView1);
		listview.setAdapter(adapter);
		
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

	      @Override
	      public void onItemClick(AdapterView<?> parent, final View view,
	          int position, long id) {
	        final String item = (String) parent.getItemAtPosition(position);
	        roomsNames.remove(item);
            adapter.notifyDataSetChanged();
            view.setAlpha(1);
	      }

	    });
		
		final EditText roomNameEditText = (EditText) findViewById(R.id.editText1);

		Button createRoomButton = (Button) findViewById(R.id.button1);
		createRoomButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				adapter.add(roomNameEditText.getText().toString());
			}
		});

	}
}
