package br.edu.ufcg.ccc.projeto2.warofkingdoms.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.House;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Move;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Player;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Territory;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.management.GameManager;

public class RulesChecker {

	
	Map<String, List<String>> territoriesBorders = new HashMap<String, List<String>>();
	private Player currentPlayer;
	private House playerHouse;
	private static RulesChecker instance;
	
	public static RulesChecker getInstance() {
		if (instance == null) {
			instance = new RulesChecker();
		}
		return instance;
	}

	public RulesChecker() {
		generateMapEntry("A", "B C");
		generateMapEntry("B", "A C E");
		generateMapEntry("C", "A B D E F");
		generateMapEntry("D", "G C");
		generateMapEntry("E", "C F");
		generateMapEntry("F", "C E G I");
		generateMapEntry("G", "D F I J");
		generateMapEntry("H", "I K");
		generateMapEntry("I", "F G H J K L");
		generateMapEntry("J", "G I L M");
		generateMapEntry("K", "H I L N");
		generateMapEntry("L", "I J K M N O P");
		generateMapEntry("M", "J L P Q");
		generateMapEntry("N", "K L O");
		generateMapEntry("O", "L N P S R");
		generateMapEntry("P", "L M O Q S T");
		generateMapEntry("Q", "M P T");
		generateMapEntry("R", "O S U");
		generateMapEntry("S", "O P R T U V");
		generateMapEntry("T", "P Q S V X");
		generateMapEntry("U", "R S V Z");
		generateMapEntry("V", "S T U X Z");
		generateMapEntry("X", "T V Z");
		generateMapEntry("Y", "Z");
		generateMapEntry("Z", "X V U Y");
		
		currentPlayer = GameManager.getInstance().getCurrentPlayer();
		playerHouse = currentPlayer.getHouse();
	}
	
	private void generateMapEntry(String key, String values) {
		String[] valuesArray = values.split(" ");
		List<String> valuesList = new ArrayList<String>(Arrays.asList(valuesArray));
		territoriesBorders.put(key, valuesList);
	}
	
	/**
	 * Check if the origin territory of a move is owned by
	 * the player who is doing the move.
	 * @param originTerritory
	 * @return True if the origin territory is owned by the player
	 */
	public boolean checkOriginIsOwnedByPlayer(Territory originTerritory) {
		if (originTerritory.getOwner() == null) {
			return false;
		}
		if (originTerritory.getOwner().equals(playerHouse)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Check if the target territory is owned by the player
	 * who is doing the move. 
	 * @param targetTerritory
	 * @return True if the target territory is owned by
	 * the player. 
	 */
	public boolean checkTargetIsOwnedByPlayer(Territory targetTerritory) {
		if (targetTerritory.getOwner() == null) {
			return false;
		}
		if (targetTerritory.getOwner().equals(playerHouse)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Check if the target territory is already a target in another
	 * move.
	 * @param List of moves
	 * @return True if the target territory is already a target
	 * in another move.
	 */
	public boolean checkTerritorryAlreadyATarget(Territory targetTerritory) {
		List<Move> moves = GameManager.getInstance().getCurrentMoves();
		for (Move move : moves) {
			if(move.getTarget().equals(targetTerritory)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean checkTerritorryAlreadyAnOrigin(Territory targetTerritory) {
		List<Move> moves = GameManager.getInstance().getCurrentMoves();
		for (Move move : moves) {
			if(move.getOrigin().equals(targetTerritory)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Check if the target territory is adjacent to the origin territory.
	 * @param originTerritory
	 * @param targetTerritory
	 * @return True if the target is adjacent to the origin.
	 */
	public boolean checkTargetIsAdjacentToOrigin(Territory originTerritory, Territory targetTerritory) {
		if (territoriesBorders.get(originTerritory.getName()).contains(targetTerritory.getName())) {
			return true;
		}
		return false;
	}
	
	/** 
	 * Check if the territory to defend is owned by the player. 
	 * @return True if the territory is owned by the player.
	 */
	public boolean checkDefendHisOwnTerritory(Territory defendedTerritory) {
		if (defendedTerritory.getOwner().equals(playerHouse)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Check if the number of moves is equals or smaller than the number
	 * of territories owned by the player.
	 * @return
	 */
	public boolean checkNumberOfMoves(List<Move> moves) {
		List<Territory> territories = GameManager.getInstance().getAllTerritories();
		int numberOfOccupiedTerritories = 0;
		for (Territory territory : territories) {
			if(territory.getOwner().equals(playerHouse)) {
				numberOfOccupiedTerritories++;
			}
		}
		if (moves.size() <= numberOfOccupiedTerritories) {
			return true;
		}
		return false;
	}
}
