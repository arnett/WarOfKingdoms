package br.edu.ufcg.ccc.projeto2.warofkingdoms.util;

public class TerritoryCenter {

	private final int NEXUS_7_MAP_WIDTH = 1123;
	private final int NEXUS_7_MAP_HEIGHT = 1662;

	private int x;
	private int y;

	public TerritoryCenter(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getCenterY(int minhaAltura) {
		return minhaAltura * y / NEXUS_7_MAP_HEIGHT;
	}

	public int getCenterX(int minhaLargura) {
		return minhaLargura * x / NEXUS_7_MAP_WIDTH;
	}

}