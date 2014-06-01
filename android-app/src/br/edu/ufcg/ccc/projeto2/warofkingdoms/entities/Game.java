package br.edu.ufcg.ccc.projeto2.warofkingdoms.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {

	private Map<String, Territory> territories;

	public Game(Map<String, Territory> territories) {
		this.territories = territories;
	}

	public Game(List<Territory> territories) {
		this.territories = new HashMap<String, Territory>();
		for (Territory territory : territories) {
			this.territories.put(territory.getName(), territory);
		}
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

	private List<Territory> toTerritoryList(
			Map<String, Territory> territoriesMap) {
		List<Territory> territories = new ArrayList<Territory>();
		for (String territoryName : territoriesMap.keySet()) {
			territories.add(territoriesMap.get(territoryName));
		}
		return territories;
	}

	public List<Territory> getTerritories() {
		return toTerritoryList(territories);
	}
}
