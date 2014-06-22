package br.edu.ufcg.ccc.projeto2.warofkingdoms.util;

import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.BARATHEON;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.GREYJOY;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.LANNISTER;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.MARTELL;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.STARK;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.TYRELL;

import java.util.List;
import java.util.Random;

import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.House;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Player;

public class RandomEntities {

	private Random random;
	private String[] houseNames = { STARK, LANNISTER, BARATHEON, TYRELL,
			MARTELL, GREYJOY };

	public RandomEntities() {
		random = new Random();
	}

	public House nextHouse(List<House> chosenHouses) {
		House house = new House(nextRandomHouseName());
		while (chosenHouses.contains(house)) {
			house = new House(nextRandomHouseName());
		}
		return house;
	}

	private String nextRandomHouseName() {
		return houseNames[random.nextInt(houseNames.length - 1)];
	}

	public Player nextPlayer(List<House> chosenHouses) {
		Player player = new Player();
		player.setId("MAC " + random.nextInt());
		player.setName("Player " + random.nextInt(100));

		player.setHouse(nextHouse(chosenHouses));
		return player;
	}

//	public Conflict nextConflict(Territory territory) {
//		Conflict conflict = new Conflict();
//		conflict.setHouses(new ArrayList<House>());
//		for (int i = 0; i < random.nextInt(2) + 1; i++) {
//			conflict.getHouses().add(nextHouse());
//		}
//		conflict.setTerritory(territory);
//		return conflict;
//	}

}
