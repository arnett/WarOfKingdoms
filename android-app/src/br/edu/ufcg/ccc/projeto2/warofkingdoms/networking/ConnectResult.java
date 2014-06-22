package br.edu.ufcg.ccc.projeto2.warofkingdoms.networking;

import java.util.List;

import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Player;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Territory;

public class ConnectResult {

	private List<Territory> territories;
	private List<Player> players;
	private String roomId;

	public ConnectResult(List<Territory> territories, List<Player> players,
			String roomId) {
		this.territories = territories;
		this.players = players;
		this.roomId = roomId;
	}

	public ConnectResult() {

	}

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

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
}