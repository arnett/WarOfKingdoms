package br.edu.ufcg.ccc.projeto2.warofkingdoms.entities;

public class Move {

	private Territory origin;
	private Territory target;
	private Action action;

	public Move(Territory origin, Territory target, Action action) {
		super();
		this.origin = origin;
		this.target = target;
		this.action = action;
	}

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

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	@Override
	public String toString() {
		return String.format("origin(%s) target(%s), action(%s)", origin, target, action);
	}
}
