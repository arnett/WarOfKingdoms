package br.edu.ufcg.ccc.projeto2.warofkingdoms.networking;

import java.util.List;
import java.util.Set;

import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Move;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Territory;

/**
 * Communicates with the server.
 * 
 * @author luan
 * 
 */
public class ClientToServerFacade {

	/**
	 * Returns the starting territory for the provided client.
	 * 
	 * @param clientId
	 * @return the starting territory for the provided client
	 */
	public String connect(String clientId) {
		return null;
	}

	/**
	 * 
	 * @param moves
	 */
	public List<Territory> readyForActionPhase(Set<Move> moves) {
		return null;
	}
}
