package br.edu.ufcg.ccc.projeto2.warofkingdoms.management;

import java.util.ArrayList;
import java.util.List;

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

	private static GameManager instance;

	private Game game;
	private List<Move> currentMoves = new ArrayList<Move>();

	private Player currentPlayer;

	private GameManager() {

	}

	public synchronized static GameManager getInstance() {
		if (instance == null) {
			instance = new GameManager();
		}
		return instance;
	}

	public void makeAttackMove(Territory target) {
		getCurrentMoves().add(new Move(Action.ATTACK, target));
	}

	public void makeDefendMove(Territory target) {
		getCurrentMoves().add(new Move(Action.DEFEND, target));
	}

	public void startNextPhase() {
		getCurrentMoves().clear();
	}

	public void updateTerritories(List<Territory> territories) {
		for (Territory territory : territories) {
			game.updateTerritory(territory);
		}
	}

	public List<Move> getCurrentMoves() {
		return currentMoves;
	}

	public Action[] getApplicableActions(Territory territory) {
		if (territory.isFree()) {
			return new Action[] {Action.ATTACK};
		} else {
			if (territory.getOwner().equals(currentPlayer)) {
				return new Action[] {Action.ATTACK, Action.DEFEND};
			} else {
				return null;
			}
		}
	}
}
