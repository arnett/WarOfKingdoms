package br.edu.ufcg.ccc.projeto2.warofkingdoms.entities;

public class Territory {

	// Name will be the primary key for this object
	private String name;
	private Player owner;

	public Territory() {

	}

	public Territory(String name) {
		this.name = name;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	public boolean isFree() {
		return owner == null;
	}
}