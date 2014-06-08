package br.edu.ufcg.ccc.projeto2.warofkingdoms.util;

import java.util.ArrayList;
import java.util.Random;

import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Conflict;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.House;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Player;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Territory;

public class RandomEntities {

	private Random random;

	public RandomEntities() {
		random = new Random();
	}

	public House nextHouse() {
		House house = new House();
		house.setName("House " + random.nextInt());
		return house;
	}

	public Player nextPlayer() {
		Player player = new Player();
		player.setId("ID " + random.nextInt());
		player.setName("Player " + random.nextInt(100));
		player.setHouse(nextHouse());
		return player;
	}

	public Conflict nextConflict(Territory territory) {
		Conflict conflict = new Conflict();
		conflict.setHouses(new ArrayList<House>());
		for (int i = 0; i < random.nextInt(2) + 1; i++) {
			conflict.getHouses().add(nextHouse());
		}
		conflict.setTerritory(territory);
		return conflict;
	}

}
