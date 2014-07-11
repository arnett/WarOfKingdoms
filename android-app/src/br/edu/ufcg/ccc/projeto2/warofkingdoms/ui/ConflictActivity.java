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
import android.widget.ListView;
import android.widget.TextView;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.adapter.CustomListViewAdapter;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Conflict;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.House;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Player;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.GameManager;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.util.RowItem;
import br.ufcg.edu.ccc.projeto2.R;

public class ConflictActivity extends Activity implements OnClickListener{

	private final int WON = 1;
	private final int DRAW = 0;
	private final int LOST = -1;
	
	private List<Conflict> conflicts;
	private int currentConflictIndex = 0;
	private Conflict currentConflict;
	
	private GameManager gameManager; 
	
	private TextView territoryInConflictTextView;
	private ListView resultList;
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
		
		conflicts = currentPlayerConflicts(conflicts);
		
		currentConflict = conflicts.get(currentConflictIndex);
		
		initializeComponents();
	}

	private List<Conflict> currentPlayerConflicts(List<Conflict> conflicts) {
		
		List<Conflict> myConflicts = new ArrayList<Conflict>();
		
		for (Conflict conflict : conflicts) {
			
			if (conflict.getHouses().contains(gameManager.getCurrentPlayer().getHouse())) {
				myConflicts.add(conflict);
			}
		}
		
		return myConflicts;
	}

	private void initializeComponents() {
		
		territoryInConflictTextView = (TextView) findViewById(R.id.territory_in_conflict_label);
		territoryInConflictTextView.setText("Territory "+currentConflict.getTerritory().getName());
		
		unknownDiceImg = (ImageView) findViewById(R.id.unknownDiceImg);
		unknownDiceImg.setOnClickListener(this);
		
		resultList = (ListView) findViewById(R.id.list);

		tapDiceLabel = (TextView) findViewById(R.id.tapDiceLabel);
		
		headerLayout = (LinearLayout) findViewById(R.id.conflict_header_layout);
		
		nextConflictBtn = (Button) findViewById(R.id.nextConflictBtn);
		nextConflictBtn.setOnClickListener(this);
	}

	private List<Integer> getDiceValues(List<House> houses, List<Integer> diceValues) {
		List<Integer> diceValue = new ArrayList<Integer>(); 
		Player currentPlayer = gameManager.getCurrentPlayer();
		for (int i = 0; i < houses.size(); i++) { 
			if(!currentPlayer.getHouse().equals(houses.get(i))) {
				diceValue.add(diceValues.get(i));
			}
		}
		return diceValue;
	}

	private List<String> getPlayersNames(List<House> houses) {
		List<Player> players = gameManager.getCurrentPlayers();
		List<String> playersName = new ArrayList<String>();
		Player currentPlayer = gameManager.getCurrentPlayer();
		for (House house : houses) {
			for (Player player : players) {
				if (player.getHouse().equals(house) && !player.equals(currentPlayer)) {
					
					String playerName = player.getName(); 
					if (player.getName().equals("Anonymous Player")) {
						playerName = getString(R.string.anonymous_player);
					}
					
					playersName.add(playerName);
					break;
				}
			}
		}
		return playersName;
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
			resultStr = getString(R.string.conflict_won_label);
			break;
		case LOST:
			
			backgroundColor = getResources().getColor(R.color.red);
			resultStr = getString(R.string.conflict_lost_label);
			break;
		case DRAW:
			
			backgroundColor = getResources().getColor(R.color.gray);
			resultStr = getString(R.string.conflict_tie_label);
			headerLayout.setBackgroundColor(getResources().getColor(R.color.gray));
			break;
		}
		List<RowItem> rowItems = new ArrayList<RowItem>();
        List<Integer> diceValues = currentConflict.getDiceValues();
        List<House> houses = currentConflict.getHouses();
        List<String> playerName = getPlayersNames(houses);
        List<Integer> listDiceValues = getDiceValues(houses,diceValues);
		for (int i = 0; i < listDiceValues.size(); i++) {
			RowItem item = new RowItem(getDiceImg(listDiceValues.get(i)), playerName.get(i));
            rowItems.add(item);
        }
		CustomListViewAdapter adapter = new CustomListViewAdapter(this,
                R.layout.list_players_item, rowItems);
        resultList.setAdapter(adapter);
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
		resultList.setAdapter(null);
	}

	private boolean conflictsAreOver() {
		
		return (currentConflictIndex == conflicts.size()-1);
	}

	public int getDiceValueIndexCurrentPlayer(Conflict currentConflict) {
		
		List<House> conflictHouses = currentConflict.getHouses();
		House currentPlayersHouse = gameManager.getCurrentPlayer().getHouse();
		
		for (int i = 0; i < conflictHouses.size(); i++) {
			
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