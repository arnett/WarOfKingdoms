package br.edu.ufcg.ccc.projeto2.warofkingdoms.entities;

public class Room {
	
	private String name, password;

	public Room(String name, String password) {
		setName(name);
		setPassword(password);
	}
	
	public Room() {
		// TODO Auto-generated constructor stub
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
}
