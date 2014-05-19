package br.edu.ufcg.ccc.projeto2.warofkingdoms.entities;

import java.util.List;

public class Move {

	private Action action;
	private List<Territory> territories;

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public List<Territory> getTerritories() {
		return territories;
	}

	public void addTerritory(Territory territory) {
		territories.add(territory);
	}

}
