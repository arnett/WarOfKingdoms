package br.edu.ufcg.ccc.projeto2.warofkingdoms.ui;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants;
import br.ufcg.edu.ccc.projeto2.R;

public class ProfileActivity extends Activity implements OnClickListener {
	
	
	private static int[] COLORS = new int[] { Color.rgb(0, 153, 64), Color.rgb(187, 67, 58) };
	
	private static String[] NAME_LIST = new String[] { "Won", "Lost"};
	
	private CategorySeries mSeries = new CategorySeries("");
	
	private DefaultRenderer mRenderer = new DefaultRenderer();
	
	private GraphicalView mChartView;

	private int numVictories;
	private int numTimesPlayed;
	
	private TextView numVictoriesLabel;
	private TextView numLostsLabel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_screen);
		
		setGraphParameters();
		
		double[] statistics = getStatistics();
		
		insertChartValues(statistics);
		
		numVictoriesLabel = (TextView) findViewById(R.id.num_victories_textview);
		String victoriesLabel = statistics[0] +"% ("+numVictories+")";
		numVictoriesLabel.setText(victoriesLabel);
		
		numLostsLabel = (TextView) findViewById(R.id.num_losts_textview);
		String lostsLabel = statistics[1] +"% ("+(numTimesPlayed-numVictories)+")";
		numLostsLabel.setText(lostsLabel);
	}

	private double[] getStatistics() {
		
		numTimesPlayed = loadInt(Constants.NUM_TIMES_PLAYED_KEY);
		numVictories = loadInt(Constants.NUM_VICTORIES_KEY);
		
		if (numTimesPlayed == 0) {
			return new double[] { 50, 50 }; 
		}

		double vitoriesPercentage = (numVictories * 100)/numTimesPlayed;
		double lostPercentage = 100 - vitoriesPercentage;
				
		return new double[] { vitoriesPercentage, lostPercentage };
	}
	
	public int loadInt(String key){
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		return sharedPreferences.getInt(key, 0);
	}

	private void insertChartValues(double[] values) {
		
		for (int i = 0; i < values.length; i++) {
			mSeries.add(NAME_LIST[i] + " " + values[i], values[i]);
			SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
			renderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);
			mRenderer.addSeriesRenderer(renderer);
		}
		
		if (mChartView != null) {
			mChartView.repaint();
		}
	}

	@Override
	public void onClick(View v) {
		
	}

	@Override
	protected void onPause() {
		startActivity(new Intent(this, ConnectActivity.class));
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (mChartView == null) {
			
			LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
			mChartView = ChartFactory.getPieChartView(this, mSeries, mRenderer);
			mRenderer.setSelectableBuffer(10);

			layout.addView(mChartView);
		}
		else {
			mChartView.repaint();
		}
	}
	
	private void setGraphParameters() {
		
		mRenderer.setShowLabels(false);
		
		mRenderer.setShowLegend(false);
		
		mRenderer.setApplyBackgroundColor(true);
		mRenderer.setBackgroundColor(Color.rgb(107, 160, 176));
		mRenderer.setChartTitleTextSize(50);
		mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
		mRenderer.setZoomButtonsVisible(false);
		mRenderer.setClickEnabled(false);
		mRenderer.setAntialiasing(true);
		mRenderer.setPanEnabled(false);
		mRenderer.setStartAngle(90);
	}
}
