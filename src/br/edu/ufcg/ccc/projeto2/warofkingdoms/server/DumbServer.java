package br.edu.ufcg.ccc.projeto2.warofkingdoms.server;

public class DumbServer {

	private String receivedMessage;
	
	public void sendMessage(String outputMessage) {
		receivedMessage = outputMessage;
	}

	public String getMessage() {
		String message = "";
		String[] fragmentedMessage = receivedMessage.split(";");
		
		System.out.println("message: " + fragmentedMessage[1]);
		if(fragmentedMessage[0].equals("FirstClick"))
			message = "Server received message type: First Click";
		else if(fragmentedMessage[0].equals("Attack"))
			message = "Server received message type: Attack";
		else if(fragmentedMessage[0].equals("Defend"))
			message = "Server received message type: Defend";
		else if(fragmentedMessage[0].equals("AttackSecondClick"))
			message = "Server received message type: AttackSecondClick";
		else
			message = "Server received an invalid message";
		
		return message;
	}

}
