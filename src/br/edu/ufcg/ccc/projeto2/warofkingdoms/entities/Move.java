package br.edu.ufcg.ccc.projeto2.warofkingdoms.entities;

public class Move {

	private Action action;
	private Territory target;

	public Move(Action action, Territory target) {
		this.action = action;
		this.target = target;
	}

	public Territory getTarget() {
		return target;
	}

	public void setTarget(Territory target) {
		this.target = target;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

}
