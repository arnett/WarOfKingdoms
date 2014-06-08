package br.edu.ufcg.ccc.projeto2.warofkingdoms.networking;

import java.util.List;

import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Player;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Territory;

public class ConnectResult {
	
	public ConnectResult(List<Territory> territories, List<Player> players) {
		this.territories = territories;
		this.players = players;
	}

	public ConnectResult() {
		
	}

	private List<Territory> territories;
	private List<Player> players;

	public List<Territory> getTerritories() {
		return territories;
	}

	public void setTerritories(List<Territory> territories) {
		this.territories = territories;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

}
