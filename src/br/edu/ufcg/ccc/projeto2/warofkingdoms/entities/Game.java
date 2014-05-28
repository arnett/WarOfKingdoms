package br.edu.ufcg.ccc.projeto2.warofkingdoms.entities;

import java.util.HashMap;
import java.util.Map;

public class Game {

	private Map<String, Territory> territories = new HashMap<String, Territory>();

	public Game(Map<String, Territory> territories) {
		this.territories = territories;
	}

	public void updateTerritory(Territory territory) {
		if (territories.get(territory.getId()) != null) {
			territories.put(territory.getId(), territory);
		}
	}

	public void addTerritory(Territory territory) {
		territories.put(territory.getId(), territory);
	}

	public void removeTerritory(Territory territory) {
		territories.remove(territory.getId());
	}

	public Territory getTerritory(Territory territory) {
		return territories.get(territory.getId());
	}
}
