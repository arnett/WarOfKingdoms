package br.edu.ufcg.ccc.projeto2.warofkingdoms.management;

import java.util.ArrayList;
import java.util.List;

import br.edu.ufcg.ccc.projeto2.warofkingdoms.ai.AIPlayer;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Conflict;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.GameState;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.House;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Move;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Player;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Territory;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.networking.ConnectResult;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.networking.SendMovesResult;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.ui.OnTaskCompleted;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.util.RandomEntities;

public class AIManager implements CommunicationManager {

	private static final String DEFAULT_ROOM_ID = "0";

	private List<AIPlayer> bots;
	private List<Territory> updatedMap;

	private Player human;

	private RandomEntities generator = new RandomEntities();

	private static AIManager instance;

	private AIManager() {

	}

	public synchronized static AIManager getInstance() {
		if (instance == null) {
			instance = new AIManager();
		}
		return instance;
	}

	private List<Territory> generateFirstMap() {
		List<Territory> territories = new ArrayList<Territory>();
		territories.add(new Territory("A"));
		territories.add(new Territory("B"));
		territories.add(new Territory("C"));
		territories.add(new Territory("D"));
		territories.add(new Territory("E"));
		territories.add(new Territory("F"));
		territories.add(new Territory("G"));
		territories.add(new Territory("H"));
		territories.add(new Territory("I"));
		territories.add(new Territory("J"));
		territories.add(new Territory("K"));
		territories.add(new Territory("L"));
		territories.add(new Territory("M"));
		territories.add(new Territory("N"));
		territories.add(new Territory("O"));
		territories.add(new Territory("P"));
		territories.add(new Territory("Q"));
		territories.add(new Territory("R"));
		territories.add(new Territory("S"));
		territories.add(new Territory("T"));
		territories.add(new Territory("U"));
		territories.add(new Territory("V"));
		territories.add(new Territory("X"));
		territories.add(new Territory("Y"));
		territories.add(new Territory("Z"));

		territories.get(2).setOwner(new House("Stark"));
		territories.get(9).setOwner(new House("Greyjoy"));
		territories.get(12).setOwner(new House("Lannister"));
		territories.get(17).setOwner(new House("Baratheon"));
		territories.get(19).setOwner(new House("Tyrell"));
		territories.get(23).setOwner(new House("Martell"));
		return territories;
	}

	private List<AIPlayer> generateBots(int numBots) {
		List<AIPlayer> bots = new ArrayList<AIPlayer>();
		List<House> chosenHouses = new ArrayList<House>();
		for (int i = 0; i < numBots; i++) {
			AIPlayer randomAiPlayer = nextRandomAiPlayer(chosenHouses);
			chosenHouses.add(randomAiPlayer.getHouse());
			bots.add(randomAiPlayer);
		}
		return bots;
	}

	private AIPlayer nextRandomAiPlayer(List<House> chosenHouses) {
		Player nextPlayer = generator.nextPlayer(chosenHouses);
		AIPlayer randomAiPlayer = new AIPlayer(updatedMap);
		randomAiPlayer.setName(nextPlayer.getName());
		randomAiPlayer.setId(nextPlayer.getId());
		return randomAiPlayer;
	}

	@Override
	public void sendCurrentMoves(OnTaskCompleted listener, List<Move> moves) {
		SendMovesResult sendMovesResult = new SendMovesResult();
		sendMovesResult.setConflicts(new ArrayList<Conflict>());
		sendMovesResult.setGameState(new GameState(false,
				new ArrayList<Player>()));
		sendMovesResult.setUpdatedMap(updatedMap);
		listener.onSendMovesTaskCompleted(sendMovesResult);
	}

	@Override
	public void connect(OnTaskCompleted listener, Player player) {
		human = player;

		updatedMap = generateFirstMap();
		this.bots = generateBots(Constants.NUM_PLAYERS - 1);
		setHumanHouse();

		ConnectResult connectResult = new ConnectResult();
		connectResult.setTerritories(updatedMap);
		connectResult.setPlayers(getAllPlayers());
		connectResult.setRoomId(DEFAULT_ROOM_ID);

		listener.onConnectTaskCompleted(connectResult);
	}

	private void setHumanHouse() {
		List<House> chosenHouses = new ArrayList<House>();
		for (AIPlayer bot : bots) {
			chosenHouses.add(bot.getHouse());
		}
		human.setHouse(generator.nextHouse(chosenHouses));
	}

	private List<Player> getAllPlayers() {
		List<Player> players = new ArrayList<Player>();
		for (Player bot : bots) {
			players.add(bot);
		}
		players.add(human);
		return players;
	}

}
