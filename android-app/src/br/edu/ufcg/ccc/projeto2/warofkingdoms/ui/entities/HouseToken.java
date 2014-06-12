package br.edu.ufcg.ccc.projeto2.warofkingdoms.ui.entities;

public class HouseToken {

	private int flagToken;
	private int castleToken;

	public HouseToken(int flagToken, int castleToken) {
		super();
		this.flagToken = flagToken;
		this.castleToken = castleToken;
	}

	public int getFlagToken() {
		return flagToken;
	}

	public void setFlagToken(int flagToken) {
		this.flagToken = flagToken;
	}

	public int getCastleToken() {
		return castleToken;
	}

	public void setCastleToken(int castleToken) {
		this.castleToken = castleToken;
	}

}
