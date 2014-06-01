package br.edu.ufcg.ccc.projeto2.warofkingdoms.entities;

public class Territory {

	// Name will be the primary key for this object
	private String name;
	private Player owner;

	private int centerX;
	private int centerY;

	private final int NEXUS_7_MAP_WIDTH = 886;
	private final int NEXUS_7_MAP_HEIGHT = 1774;

	public Territory(String name) {
		this.name = name;
	}

	public Territory() {
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public int getCenterX(int minhaLargura) {
		return minhaLargura * centerX / NEXUS_7_MAP_WIDTH;
	}

	public void setCenterX(int centerX) {
		this.centerX = centerX;
	}

	public int getCenterY(int minhaAltura) {
		return minhaAltura * centerY / NEXUS_7_MAP_HEIGHT;
	}

	public void setCenterY(int centerY) {
		this.centerY = centerY;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	public boolean isFree() {
		return owner == null;
	}
}