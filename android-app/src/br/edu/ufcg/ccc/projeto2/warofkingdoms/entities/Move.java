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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Move other = (Move) obj;
		if (action != other.action)
			return false;
		if (origin == null) {
			if (other.origin != null)
				return false;
		} else if (!origin.equals(other.origin))
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("origin(%s) target(%s), action(%s)", origin, target, action);
	}
	
	public Move clone() {
		return new Move(origin.clone(), target.clone(), action);
	}
}
