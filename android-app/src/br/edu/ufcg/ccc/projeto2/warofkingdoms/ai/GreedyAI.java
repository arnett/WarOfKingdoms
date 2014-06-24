package br.edu.ufcg.ccc.projeto2.warofkingdoms.ai;

import java.util.ArrayList;
import java.util.List;

import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Action;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.House;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Move;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Territory;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.util.RulesChecker;

/**
 * A greedy algorithm that, for every owned territory, selects the first found
 * adjacent territory that does not belong to the bot and attacks it.
 * 
 * @author Arnett
 * 
 */
public class GreedyAI implements BaseAIAlgorithm {

	@Override
	public List<Move> getNextMoves(House house, List<Territory> currentMap) {
		RulesChecker checker = RulesChecker.getInstance();
		List<Territory> territoriesWithMoves = new ArrayList<Territory>();
		List<Move> nextMoves = new ArrayList<Move>();
		for (Territory t : currentMap) {
			if (isMyTerritory(house, t) && !territoriesWithMoves.contains(t)) {
				for (Territory neighbor : checker.getNeighbors(t)) {
					if (!isMyTerritory(house, neighbor)
							&& !territoriesWithMoves.contains(neighbor)) {
						nextMoves.add(new Move(t, neighbor, Action.ATTACK));
						territoriesWithMoves.add(neighbor);
						territoriesWithMoves.add(t);
						break;
					}
				}
			}
		}
		return nextMoves;
	}

	private boolean isMyTerritory(House house, Territory t) {
		return t.getOwner() != null && t.getOwner().equals(house);
	}

}
