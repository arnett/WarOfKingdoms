package br.edu.ufcg.ccc.projeto2.warofkingdoms.entities;

public class Territory {

	private String name;

	public Territory(String name) {
		super();
		this.name = name;
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
}
