package br.edu.ufcg.ccc.projeto2.warofkingdoms.entities;

public class House {

	private String name;

	public House(String name) {
		this.name = name;
	}

	public House() {
		
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

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof House)) {
			return false;
		}

		House other = (House) o;
		return other.getName().equals(this.getName());
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
