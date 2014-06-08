package br.edu.ufcg.ccc.projeto2.warofkingdoms.management;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Action;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Game;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Move;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Player;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Territory;

/**
 * A singleton that manages the game state.
 * 
 */
public class GameManager {

	private String NAME = "Player " + new Random().nextInt(100);
	private String ID = "MAC" + new Random().nextInt(100);

	private static GameManager instance;

	private Game game;
	private List<Move> currentMoves = new ArrayList<Move>();
	private List<Player> currentPlayers;

	private Player currentPlayer;

	private GameManager() {
		currentPlayer = new Player(NAME, ID);
		game = new Game();

		currentPlayers = new ArrayList<Player>();
		currentPlayers.add(currentPlayer);
	}

	public synchronized static GameManager getInstance() {
		if (instance == null) {
			instance = new GameManager();
		}
		return instance;
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
		if (territory.isFree()) {
			return new Action[] {Action.ATTACK};
		} else {
			if (territory.getOwner().equals(currentPlayer.getHouse())) {
				return new Action[] {Action.ATTACK, Action.DEFEND};
			} else {
				return null;
			}
		}
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public List<Territory> getAllTerritories() {
		return game.getTerritories();
	}

	public void updateAllPlayers(List<Player> players) {
		currentPlayers = players;
	}
}
