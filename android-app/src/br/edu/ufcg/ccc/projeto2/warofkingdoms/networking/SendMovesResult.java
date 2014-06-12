package br.edu.ufcg.ccc.projeto2.warofkingdoms.networking;

import java.util.List;

import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Conflict;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.GameState;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Territory;

public class SendMovesResult {

	private List<Conflict> conflicts;
	private List<Territory> updatedMap;
	private GameState gameState;

	public SendMovesResult(List<Conflict> conflicts, List<Territory> updatedMap, GameState gameState) {
		super();
		this.conflicts = conflicts;
		this.updatedMap = updatedMap;
		this.gameState = gameState;
	}

	public SendMovesResult() {

	}

	public List<Conflict> getConflicts() {
		return conflicts;
	}

	public void setConflicts(List<Conflict> conflicts) {
		this.conflicts = conflicts;
	}

	public List<Territory> getUpdatedMap() {
		return updatedMap;
	}

	public void setUpdatedMap(List<Territory> updatedMap) {
		this.updatedMap = updatedMap;
	}
	
	public GameState getGameState() {
		return gameState;
	}
	
	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}
}
