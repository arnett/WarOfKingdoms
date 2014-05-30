package br.edu.ufcg.ccc.projeto2.warofkingdoms.activities;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import br.ufcg.edu.ccc.projeto2.R;

public class CustomList extends ArrayAdapter<String> {

	private Activity context;
	private Integer[] imageId;
	private String[] objects;
	
	public CustomList(Activity context, Integer[] imageId, String[] objects) {
		super(context, R.layout.activity_choose_house_list_row, objects);
		this.context = context;
		this.imageId = imageId;
		this.objects = objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.activity_choose_house_list_row, null, true);
		TextView txtTitle = (TextView) rowView.findViewById(R.id.text);
		ImageView imgView = (ImageView) rowView.findViewById(R.id.image);
		
		txtTitle.setText(objects[position]);
		imgView.setImageResource(imageId[position]);
		
		return rowView;
	}
}
