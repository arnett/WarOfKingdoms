package br.edu.ufcg.ccc.projeto2.warofkingdoms.entities;

import java.util.List;

public class GameState {
	
	private boolean isGameEnd;
	private List<Player> winnerList;
	private int currentTurn, totalTurns;
	
	public GameState() {}
	
	public GameState(boolean isGameEnd, List<Player> winnerList, int currentTurn, int totalTurns) {
		this.isGameEnd = isGameEnd;
		this.winnerList = winnerList;
		this.currentTurn = currentTurn;
		this.totalTurns = totalTurns;
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

	public int getCurrentTurn() {
		return currentTurn;
	}

	public void setCurrentTurn(int currentTurn) {
		this.currentTurn = currentTurn;
	}

	public int getTotalTurns() {
		return totalTurns;
	}

	public void setTotalTurns(int totalTurns) {
		this.totalTurns = totalTurns;
	}	
}
