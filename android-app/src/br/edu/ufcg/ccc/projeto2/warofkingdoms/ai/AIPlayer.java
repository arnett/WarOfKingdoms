package br.edu.ufcg.ccc.projeto2.warofkingdoms.ai;

import java.util.List;

import android.util.Log;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Move;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Player;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Territory;

public class AIPlayer extends Player {

	private final String LOG_TAG = "AIPlayer";

	private List<Territory> knownMap;
	private BaseAIAlgorithm myAlgorithm;

	public AIPlayer() {
		this.myAlgorithm = new GreedyAI();
	}

	public AIPlayer(BaseAIAlgorithm algorithm) {
		this.myAlgorithm = algorithm;
	}

	public AIPlayer(List<Territory> updatedMap) {
		this();
		this.knownMap = updatedMap;
	}

	public void updateMap(List<Territory> updatedMap) {
		this.knownMap = updatedMap;
	}

	public List<Move> getGeneratedRoundMoves() {
		List<Move> nextMoves = myAlgorithm.getNextMoves(getHouse(), knownMap);
		Log.v(LOG_TAG, String.format("Auto-generated moves for %s are %s",
				getName(), nextMoves));
		return nextMoves;
	}

}
