package br.edu.ufcg.ccc.projeto2.warofkingdoms.entities;

public class AttackMove extends Move {

	private Territory origin;
	private Territory target;

	public Territory getOrigin() {
		return origin;
	}

	public void setOrigin(Territory origin) {
		this.origin = origin;
	}

	public Territory getTarget() {
		return target;
	}

	public void setTarget(Territory target) {
		this.target = target;
	}
}
