package br.edu.ufcg.ccc.projeto2.warofkingdoms.entities;

public class Connect {
	
	private int maxPlayersInRoom = 2;
	private Player player;
	
	public Connect() {  }
	
	public Connect(int maxPlayersInRoom, Player player) {
		this.maxPlayersInRoom = maxPlayersInRoom;
		this.player = player;
	}

	public int getMaxPlayersInRoom() {
		return maxPlayersInRoom;
	}

	public void setMaxPlayersInRoom(int maxPlayersInRoom) {
		this.maxPlayersInRoom = maxPlayersInRoom;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
	@Override
	public String toString() {
		return maxPlayersInRoom + ", " + player.toString();
	}
}
