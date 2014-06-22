package br.edu.ufcg.ccc.projeto2.warofkingdoms.entities;

import java.util.List;

public class GameState {
	
	private boolean isGameEnd;
	private List<Player> winnerList;
	
	public GameState() {

	}
	
	public GameState(boolean isGameEnd, List<Player> winnerList) {
		this.isGameEnd = isGameEnd;
		this.winnerList = winnerList;
	}

	public boolean isGameEnd() {
		return isGameEnd;
	}

	public void setGameEnd(boolean isGameEnd) {
		this.isGameEnd = isGameEnd;
	}

	public List<Player> getWinnerList() {
		return winnerList;
	}

	public void setWinnerList(List<Player> winnerList) {
		this.winnerList = winnerList;
	}
}
