package br.edu.ufcg.ccc.projeto2.warofkingdoms.ui;

import br.edu.ufcg.ccc.projeto2.warofkingdoms.networking.ConnectResult;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.networking.SendMovesResult;

public interface OnTaskCompleted {

	void onSendMovesTaskCompleted(SendMovesResult conflicts);

	void onConnectTaskCompleted(ConnectResult connectResult);
}
