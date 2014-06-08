package br.edu.ufcg.ccc.projeto2.warofkingdoms.entities;

public class Territory {

	// Name will be the primary key for this object
	private String name;
	private House owner;

	public Territory() {

	}

	public Territory(String name) {
		this.name = name;
	}

	public House getOwner() {
		return owner;
	}

	public void setOwner(House owner) {
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
		return "name:" + name + ",owner:" + owner;
	}

	public boolean isFree() {
		return owner == null;
	}

	@Override
	public boolean equals(Object object) {
		if (object == null) {
			return false;
		}
		if (!(object instanceof Territory)) {
			return false;
		}

		Territory other = (Territory) object;

		return name == other.getName();
	}
}