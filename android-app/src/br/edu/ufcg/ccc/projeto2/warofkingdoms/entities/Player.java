package br.edu.ufcg.ccc.projeto2.warofkingdoms.entities;

public class Player {

	private String id;
	private String name;
	private House house;

	private String email;
	private String password;

	public Player() {

	}

	public Player(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public House getHouse() {
		return house;
	}

	public void setHouse(House house) {
		this.house = house;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return getEmail() + ": " + getName() + " - " + getPassword();
	}
}
