package br.edu.ufcg.ccc.projeto2.warofkingdoms.entities;

import java.util.ArrayList;
import java.util.List;

public class Game {

	private List<Territory> territories;

	public Game(List<Territory> territories) {
		this.territories = territories;
	}
	
	public Game() {
		this.territories = new ArrayList<Territory>();
	}

	/**
	 * Updates the provided territory if it exists.
	 * 
	 * @param territory
	 */
	@Deprecated
	public boolean updateTerritory(Territory territory) {
		int territoryPosition = territories.indexOf(territory);
		if (territoryPosition != -1) {
			territories.set(territoryPosition, territory);
		}
		return false;
	}

	public void updateAllTerritories(List<Territory> territories) {
		this.territories = territories;
	}
	/**
	 * Adds the provided territory to the map.
	 * 
	 * @param territory
	 * @return
	 */
	public boolean addTerritory(Territory territory) {
		return territories.add(territory);
	}

	/**
	 * Returns all the territories of the map.
	 * 
	 * @return
	 */
	public List<Territory> getTerritories() {
		return territories;
	}

	public Territory getTerritoryByName(String territoryName) {
		for (Territory territory : territories) {
			if (territory.getName().equals(territoryName)) {
				return territory;
			}
		}
		return null;
	}
}
