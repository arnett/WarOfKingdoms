package br.edu.ufcg.ccc.projeto2.warofkingdoms.ai;

import java.util.List;

import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.House;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Move;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Territory;

/**
 * All algorithms should implement this interface.
 * 
 * @author Arnett
 * 
 */
public interface BaseAIAlgorithm {

	public List<Move> getNextMoves(House house, List<Territory> currentMap);

}
