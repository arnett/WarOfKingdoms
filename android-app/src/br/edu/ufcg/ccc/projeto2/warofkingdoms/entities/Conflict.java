package br.edu.ufcg.ccc.projeto2.warofkingdoms.entities;

import java.util.List;

public class Conflict {

	private List<House> houses;
	private Territory territory;

	public List<House> getHouses() {
		return houses;
	}

	public void setHouses(List<House> houses) {
		this.houses = houses;
	}

	public Territory getTerritory() {
		return territory;
	}

	public void setTerritory(Territory territory) {
		this.territory = territory;
	}


}
