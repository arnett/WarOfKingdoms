package br.edu.ufcg.ccc.projeto2.warofkingdoms.management;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Action;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Game;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.House;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Move;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Player;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Territory;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.util.RulesChecker;

/**
 * A singleton that manages the game state.
 * 
 */
public class GameManager {

	private static final int RANDOM_ID = new Random().nextInt(Integer.MAX_VALUE);
	private String NAME = "Anonymous Player";
	private String ID = "MAC " + RANDOM_ID;

	private static GameManager instance;

	private Game game;
	private List<Move> currentMoves = new ArrayList<Move>();
	private List<Player> currentPlayers;
	private String roomId;

	private Player currentPlayer;
	private Territory currentPlayerHomebase;

	private GameManager() {
		
		currentPlayer = new Player(ID, NAME);
		game = new Game();

		setCurrentPlayers(new ArrayList<Player>());
		getCurrentPlayers().add(currentPlayer);
	}

	public synchronized static GameManager getInstance() {
		if (instance == null) {
			instance = new GameManager();
		}
		return instance;
	}
	
	public void reset() {
		instance = null;
	}

	public void makeAttackMove(Territory origin, Territory target) {
		getCurrentMoves().add(new Move(origin, target, Action.ATTACK));
	}

	public void makeDefendMove(Territory target) {
		getCurrentMoves().add(new Move(target, target, Action.DEFEND));
	}

	public void startNextPhase() {
		getCurrentMoves().clear();
	}

	@Deprecated
	public void updateTerritories(List<Territory> territories) {
		for (Territory territory : territories) {
			game.updateTerritory(territory);
		}
	}

	public void updateAllTerritories(List<Territory> territories) {
		game.updateAllTerritories(territories);
	}

	public List<Move> getCurrentMoves() {
		return currentMoves;
	}

	public Action[] getApplicableActions(Territory territory) {
		RulesChecker runChecker = RulesChecker.getInstance();
		if (territory.getOwner() == null) {
			return null;
		}
		if (runChecker.areAllNeighborsOwnedByTheCurrentPlayer(territory, getAllTerritories()))	 {
			return new Action[] {Action.DEFEND};
		}
		if (territory.getOwner().equals(currentPlayer.getHouse())) {
			return new Action[] {Action.ATTACK, Action.DEFEND};
		} else {
			return null;
		}
	}
	
	public Action[] getCancelActions(Territory territory) {
		return new Action[] {Action.CANCEL};
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public List<Territory> getAllTerritories() {
		return game.getTerritories();
	}
	
	/*
	 * Gets all territories owned by the house
	 */
	public List<Territory> getTerritories(House house) {
		
		List<Territory> territories = new ArrayList<Territory>();
		for (Territory t : game.getTerritories()) {
			if (t.getOwner() == null) {
				continue;
			}
			
			if (t.getOwner().equals(house)){
				territories.add(t);
			}
		}
		return territories;
	}

	public void updateAllPlayers(List<Player> players) {
		for (Player player: players) {
			if (player.getId().equals(currentPlayer.getId())) {
				currentPlayer = player;
			}
		}
		setCurrentPlayers(players);
	}

	public Territory getTerritoryByName(String territoryName) {
		return game.getTerritoryByName(territoryName);
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public boolean currentPlayerWon(List<Player> winners) {

		for (Player player : winners) {
			
			if (currentPlayer.getId().equals(player.getId())) {
				return true;
			}
		}
		return false;
	}

	public Territory getCurrentPlayerHomebase() {
		return getTerritoryByName(currentPlayerHomebase.getName());
	}

	public void setCurrentPlayerHomebase(Territory currentPlayerHomebase) {
		this.currentPlayerHomebase = currentPlayerHomebase;
	}

	public List<Player> getCurrentPlayers() {
		return currentPlayers;
	}

	public void setCurrentPlayers(List<Player> currentPlayers) {
		this.currentPlayers = currentPlayers;
	}
	
}
