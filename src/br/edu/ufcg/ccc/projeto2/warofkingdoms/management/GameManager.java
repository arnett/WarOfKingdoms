package br.edu.ufcg.ccc.projeto2.warofkingdoms.management;

import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Territory;

/**
 * A singleton that manages the game state
 *
 */
public class GameManager {

	private static GameManager instance;
	
	private GameManager() {
		
	}

	public synchronized static GameManager getInstance() {
		if (instance == null) {
			instance = new GameManager();
		}
		return instance;
	}

	public void makeAttackMove(Territory origin, Territory target) {

	}
	
	public void makeDefendMove(Territory territory) {

	}
}
