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

	public static String[] getStringValues() {
		String[] stringValues = new String[values().length];
		for (int i = 0; i < values().length; i++) {
			stringValues[i] = values()[i].toString();
		}
		return stringValues;
	}
}