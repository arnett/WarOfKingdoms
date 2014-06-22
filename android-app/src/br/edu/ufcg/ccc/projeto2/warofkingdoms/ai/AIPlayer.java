package br.edu.ufcg.ccc.projeto2.warofkingdoms.ai;

import java.util.ArrayList;
import java.util.List;

import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Move;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Player;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Territory;

public class AIPlayer extends Player {

	@SuppressWarnings("unused")
	private List<Territory> knownMap;

	public AIPlayer(List<Territory> updatedMap) {
		this.knownMap = updatedMap;
	}

	public void updateMap(List<Territory> updatedMap) {
		this.knownMap = updatedMap;
	}

	public List<Move> getGeneratedRoundMoves() {
		
		// TODO - algorithm to create pseudo-smart moves
		return new ArrayList<Move>();
	}
	
}
