package br.edu.ufcg.ccc.projeto2.warofkingdoms.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Conflict;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.House;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.GameManager;
import br.ufcg.edu.ccc.projeto2.R;

public class ConflictActivity extends Activity implements OnClickListener{

	private final int WON = 1;
	private final int DRAW = 0;
	private final int LOST = -1;
	
	private ArrayList<Conflict> conflicts;
	private int currentConflictIndex = 0;
	private Conflict currentConflict;
	
	private GameManager gameManager; 
	
	private TextView territoryInConflictTextView;
	private ImageView unknownDiceImg; 
	
	private LinearLayout headerLayout;
	private Button nextConflictBtn;
	
	private TextView tapDiceLabel;
	
	
	@Override
	@SuppressWarnings("unchecked")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conflict_screen);
		
		gameManager = GameManager.getInstance();
		
		conflicts = (ArrayList<Conflict>) getIntent().getExtras().getSerializable("conflicts");
		currentConflict = conflicts.get(currentConflictIndex);
		
		initializeComponents();
	}

	private void initializeComponents() {
		
		territoryInConflictTextView = (TextView) findViewById(R.id.territory_in_conflict_label);
		territoryInConflictTextView.setText("Territory "+currentConflict.getTerritory().getName());
		
		unknownDiceImg = (ImageView) findViewById(R.id.unknownDiceImg);
		unknownDiceImg.setOnClickListener(this);
		
		tapDiceLabel = (TextView) findViewById(R.id.tapDiceLabel);
		
		headerLayout = (LinearLayout) findViewById(R.id.conflict_header_layout);
		
		nextConflictBtn = (Button) findViewById(R.id.nextConflictBtn);
		nextConflictBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		
		if (v == unknownDiceImg) {
			
			int diceValueIndex = getDiceValueIndexCurrentPlayer(currentConflict);
			int conflictResult = wonConflict(
											currentConflict.getDiceValues().get(diceValueIndex), 
											diceValueIndex, 
											currentConflict.getDiceValues());
			
			updateScreenWithConflictResult(conflictResult, currentConflict.getDiceValues().get(diceValueIndex));
			
			nextConflictBtn.setVisibility(View.VISIBLE);
			
		} else if (v == nextConflictBtn) {
			
			if (conflictsAreOver()) {
				finish();
				return;
			}
			
			moveToNexConflict();
		}
	}
	
	private void updateScreenWithConflictResult(int conflictResult, int diceValue) {
		
		int backgroundColor = 0;
		String resultStr = "";
		
		switch (conflictResult) {
		case WON:
			
			backgroundColor = getResources().getColor(R.color.green);
			resultStr = "You won the conflict";
			break;
		case LOST:
			
			backgroundColor = getResources().getColor(R.color.red);
			resultStr = "You lost the conflict";
			break;
		case DRAW:
			
			backgroundColor = getResources().getColor(R.color.gray);
			resultStr = "It's a draw! No one won";
			headerLayout.setBackgroundColor(getResources().getColor(R.color.gray));
			break;
		}
		
		headerLayout.setBackgroundColor(backgroundColor);
		unknownDiceImg.setImageResource(getDiceImg(diceValue));
		tapDiceLabel.setText(resultStr);
		tapDiceLabel.setTextColor(backgroundColor);
	}

	private int getDiceImg(int diceValue) {
		
		switch (diceValue) {
		case 1:
			return R.drawable.dice_one;
		case 2:
			return R.drawable.dice_two;
		case 3:
			return R.drawable.dice_three;
		case 4:
			return R.drawable.dice_four;
		case 5:
			return R.drawable.dice_five;
		case 6:
			return R.drawable.dice_six;
		}

		return 0;
	}

	private void moveToNexConflict() {
		
		currentConflictIndex++;
		currentConflict = conflicts.get(currentConflictIndex);
		tapDiceLabel.setText(getResources().getString(R.string.tap_to_throw_dice));
		tapDiceLabel.setTextColor(getResources().getColor(R.color.blue_play));
		headerLayout.setBackgroundColor(getResources().getColor(R.color.blue_play));
		nextConflictBtn.setVisibility(View.INVISIBLE);
		territoryInConflictTextView.setText("Territory "+currentConflict.getTerritory().getName());
		unknownDiceImg.setImageResource(R.drawable.unknown_dice);
	}

	private boolean conflictsAreOver() {
		
		return (currentConflictIndex == conflicts.size()-1);
	}

	public int getDiceValueIndexCurrentPlayer(Conflict currentConflict) {
		
		List<House> conflictHouses = currentConflict.getHouses();
		
		for (int i = 0; i < conflictHouses.size(); i++) {
			
			House currentPlayersHouse = gameManager.getCurrentPlayer().getHouse();
			
			if (conflictHouses.get(i).equals(currentPlayersHouse)) {
				return i;
			}
		}
		
		return -1;
	}
	
	/*
	 * Verify if currentPlayer won the conflict
	 * 
	 *   - Return 0 if has a draw
	 *   - Return -1 if lost conflict
	 *   - Return 1 if won conflict
	 * 
	 */
	public int wonConflict(int myDiceValue, int myDiceValueIndex, List<Integer> diceValues) {
		
		int maxDiceValue = getBigestDiceValue(diceValues);
		
		if (hasRepetition(maxDiceValue, diceValues)) {
			return DRAW;
		}
		
		for (int i = 0; i < diceValues.size(); i++) {
			
			if (i == myDiceValueIndex) {	// avoiding comparing its value with itself
				continue;
			}
			
			if (myDiceValue <= diceValues.get(i)) {
				return LOST;
			}
		}

		return WON;
	}

	private boolean hasRepetition(int maxDiceValue, List<Integer> diceValues) {
		
		int counter = 0;
		for (Integer dice : diceValues) {
			
			if (dice == maxDiceValue) {
				counter++;
			}
		}
		
		return counter != 1;
	}

	private int getBigestDiceValue(List<Integer> diceValues) {
		
		int bigestDiceValue = -1;
		
		for (Integer dice : diceValues) {
			
			if (dice > bigestDiceValue) {
				bigestDiceValue = dice;
			}
		}
		
		return bigestDiceValue;
	}

}