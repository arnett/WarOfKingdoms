package br.edu.ufcg.ccc.projeto2.warofkingdoms.entities;

public class Move {

	private Action action;
	private Territory targetTerritory;

	public Move(Action action, Territory target) {
		this.action = action;
		this.targetTerritory = target;
	}

	public Territory getTargetTerritory() {
		return targetTerritory;
	}

	public void setTargetTerritory(Territory target) {
		this.targetTerritory = target;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

}
