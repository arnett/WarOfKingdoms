package br.edu.ufcg.ccc.projeto2.warofkingdoms.entities;

public enum Action {

	ATTACK("Attack"), DEFEND("Defend");

	private String actionName;

	private Action(String actionName) {
		this.actionName = actionName;
	}

	@Override
	public String toString() {
		return actionName;
	}
}