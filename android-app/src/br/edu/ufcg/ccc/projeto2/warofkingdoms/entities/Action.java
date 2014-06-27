package br.edu.ufcg.ccc.projeto2.warofkingdoms.entities;

public enum Action {

	ATTACK("attack"), DEFEND("defend"), CANCEL("cancel"), OK("ok");

	private String actionName;

	private Action(String actionName) {
		this.actionName = actionName;
	}

	@Override
	public String toString() {
		return actionName;
	}
}