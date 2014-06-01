package br.edu.ufcg.ccc.projeto2.warofkingdoms.entities;

import java.util.HashMap;
import java.util.Map;

public class Game {

	private Map<String, Territory> territories = new HashMap<String, Territory>();

	public Game(Map<String, Territory> territories) {
		this.territories = territories;
	}

	/**
	 * Updates the provided territory if it exists.
	 * 
	 * @param territory
	 */
	public void updateTerritory(Territory territory) {
		if (territories.get(territory.getName()) != null) {
			territories.put(territory.getName(), territory);
		}
	}

	public void addTerritory(Territory territory) {
		territories.put(territory.getName(), territory);
	}

	public void removeTerritory(Territory territory) {
		territories.remove(territory.getName());
	}

	public Territory getTerritory(Territory territory) {
		return territories.get(territory.getName());
	}
}
