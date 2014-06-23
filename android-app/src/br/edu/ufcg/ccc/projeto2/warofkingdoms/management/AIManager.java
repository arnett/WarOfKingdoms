package br.edu.ufcg.ccc.projeto2.warofkingdoms.management;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.edu.ufcg.ccc.projeto2.warofkingdoms.ai.AIPlayer;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Conflict;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Connect;
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
	
	/*
	 * Fake sendCurrentMoves (POST) server method
	 */
	@Override
	public void sendCurrentMoves(OnTaskCompleted listener, List<Move> moves) {
		
		moves.addAll(getAIRoundMoves());
		
		List<Conflict> conflicts = processMoves(moves);
		
		SendMovesResult sendMovesResult = new SendMovesResult();
		sendMovesResult.setConflicts(conflicts);
		sendMovesResult.setGameState(new GameState(false,new ArrayList<Player>()));
		sendMovesResult.setUpdatedMap(updatedMap);
		listener.onSendMovesTaskCompleted(sendMovesResult);
	}

	/*
	 * Moves being processed as a fake server 
	 */
	private List<Conflict> processMoves(List<Move> moves) {
		
		List<Conflict> conflicts = generateConflicts(moves);
		List<Move> nonConflictingMoves = getNonConflictingMoves(moves, conflicts);
		
		updatedMap = updateTerritories(updatedMap, conflicts, nonConflictingMoves);
		return conflicts;
	}

	/*
	 * Fake connect (POST) server method
	 */
	@Override
	public void connect(OnTaskCompleted listener, Connect connectEntity) {
		human = connectEntity.getPlayer();

		updatedMap = generateFirstMap();
		this.bots = generateBots(Constants.NUM_PLAYERS - 1);
		setHumanHouse();

		ConnectResult connectResult = new ConnectResult();
		connectResult.setTerritories(updatedMap);
		connectResult.setPlayers(getAllPlayers());
		connectResult.setRoomId(DEFAULT_ROOM_ID);

		listener.onConnectTaskCompleted(connectResult);
	}
	
	private List<Move> getAIRoundMoves() {
		
		List<Move> AIMoves = new ArrayList<Move>();
		
		for (AIPlayer bot : bots) {
			AIMoves.addAll(bot.getGeneratedRoundMoves());
		}
		return AIMoves;
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

	private List<Conflict> generateConflicts(List<Move> moves) {
		
		List<Conflict> conflicts = new ArrayList<Conflict>();
		
		for (Move aMove : moves) {
			
			Conflict aConflict = null;
			
			// avoiding create multiples conflicts for a territory
			if (conflictAlreadyExists(aMove, conflicts)) {
				continue;
			}
			
			for (Move anotherMove : moves) {
				
				String aMoveTarget = aMove.getTarget().getName();
				String anotherMoveTarget = anotherMove.getTarget().getName();
				
				// verifying if has a conflict ("they want the same territory")
				if (aMoveTarget.equals(anotherMoveTarget)) {
					
					String aMoveOriginOwner = aMove.getOrigin().getOwner().getName();
					String anotherMoveOriginOwner = anotherMove.getOrigin().getOwner().getName();
					
					// avoiding create a conflict with itself
					if (! aMoveOriginOwner.equals(anotherMoveOriginOwner)) {
						
						if (aConflict == null) {
							aConflict = new Conflict(aMove.getTarget(), new ArrayList<House>(), new ArrayList<Integer>());
							aConflict.getHouses().add(aMove.getOrigin().getOwner());
							aConflict.getDiceValues().add(getRandomDiceValue());
						}
						aConflict.getHouses().add(anotherMove.getOrigin().getOwner());
						aConflict.getDiceValues().add(getRandomDiceValue());
					}
				}
			}
			if (aConflict != null) {
				conflicts.add(aConflict);
			}
		}
		
		return conflicts;
	}
	
	private Integer getRandomDiceValue() {
		Random rand = new Random();
		return rand.nextInt(6)+1; // from 1 to 6
	}

	private boolean conflictAlreadyExists(Move move, List<Conflict> conflicts) {
		
		for (Conflict conflict : conflicts) {
			
			String conflictTerritoryName = conflict.getTerritory().getName();
			
			if (conflictTerritoryName.equals(move.getTarget().getName())) {
				return true;
			}
		}
		return false;
	}

	private List<Move> getNonConflictingMoves(
											List<Move> moves,
											List<Conflict> conflicts) {
		
		List<Move> nonConflictingMoves = new ArrayList<Move>();
		
		for (Move move : moves) {
			
			boolean isConflicting = false;
			
			for (Conflict conflict : conflicts) {
				
				String moveTarget = move.getTarget().getName();
				String conflictTerritory = conflict.getTerritory().getName(); 
				
				if (moveTarget.equals(conflictTerritory)) {
					isConflicting = true;
					break;
				}
			}
			
			if (!isConflicting) {
				nonConflictingMoves.add(move);
			}
		}
		return nonConflictingMoves;
	}
	
	private List<Territory> updateTerritories(
			List<Territory> currentMap,
			List<Conflict> conflicts, 
			List<Move> nonConflictingMoves) {
		
		for (Move move : nonConflictingMoves) {
			
			int territoryIndex = currentMap.indexOf(move.getTarget());
			currentMap.get(territoryIndex).setOwner(move.getOrigin().getOwner());
		}
		
		for (Conflict conflict : conflicts) {
			
			int biggestDiceValueIndex = getBiggestDiceValueIndex(conflict.getDiceValues());
			
			if (biggestDiceValueIndex != -1) {
				
				int territoryIndex = currentMap.indexOf(conflict.getTerritory());
				currentMap.get(territoryIndex).setOwner(conflict.getHouses().get(biggestDiceValueIndex));
			}
		}
		
		return currentMap;
	}
	
	private int getBiggestDiceValueIndex(List<Integer> diceValues) {

		int biggestValue =  -1;
		int biggestValueIndex = -1;
		boolean hasMoreThanOneBiggestValue = false;
		
		for (int i = 0; i < diceValues.size(); i++) {
			
			if (diceValues.get(i) > biggestValue) {
				
				biggestValue = diceValues.get(i);
				biggestValueIndex = i;
				hasMoreThanOneBiggestValue = false;
			
			} else if (diceValues.get(i) == biggestValue) {
				
				hasMoreThanOneBiggestValue = true;
			}
		}
		
		if (hasMoreThanOneBiggestValue) {
			return -1;
		}

		return biggestValueIndex;
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
}
