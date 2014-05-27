import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WarOfKingdomsServer {

	public static void main(String[] args){
		ServerSocket serverSocket = null;
		Socket socket = null;
		DataInputStream dataInputStream = null;
		DataOutputStream dataOutputStream = null;

		try {
			
			serverSocket = new ServerSocket(8888);
			System.out.println(serverSocket.getLocalSocketAddress());
			System.out.println("Listening :8888");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while(true){
			try {
				socket = serverSocket.accept();
				dataInputStream = new DataInputStream(socket.getInputStream());
				dataOutputStream = new DataOutputStream(socket.getOutputStream());
				String message = dataInputStream.readUTF();
				String[] fragmentedMessage = message.split(";");
				System.out.println("message: " + fragmentedMessage[1]);
				if(fragmentedMessage[0].equals("FirstClick")) {
					dataOutputStream.writeUTF("Server received message type: First Click");
				}
				else if(fragmentedMessage[0].equals("Attack")) {
					dataOutputStream.writeUTF("Server received message type: Attack");
				}
				else if(fragmentedMessage[0].equals("Defend")) {
					dataOutputStream.writeUTF("Server received message type: Defend");
				}
				else if(fragmentedMessage[0].equals("AttackSecondClick")) {
					dataOutputStream.writeUTF("Server received message type: AttackSecondClick");
				}
				else {
					dataOutputStream.writeUTF("Server received an invalid message");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally{
				if( socket!= null){
					try {
						socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if( dataInputStream!= null){
					try {
						dataInputStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if( dataOutputStream!= null){
					try {
						dataOutputStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
}