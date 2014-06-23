package br.edu.ufcg.ccc.projeto2.warofkingdoms.entities;

import java.util.List;

public class GameState {
	
	private boolean isGameFinished;
	private List<Player> winnerList;
	
	public GameState() {

	}
	
	public GameState(boolean isGameEnd, List<Player> winnerList) {
		this.isGameFinished = isGameEnd;
		this.winnerList = winnerList;
	}

	public boolean isGameFinished() {
		return isGameFinished;
	}

	public void setGameFinished(boolean isGameEnd) {
		this.isGameFinished = isGameEnd;
	}

	public List<Player> getWinnerList() {
		return winnerList;
	}

	public void setWinnerList(List<Player> winnerList) {
		this.winnerList = winnerList;
	}
}
