package br.edu.ufcg.ccc.projeto2.warofkingdoms.activities;

import java.util.List;

import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Conflict;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.networking.ConnectResult;

public interface OnTaskCompleted {

	void onSendMovesTaskCompleted(List<Conflict> conflicts);

	void onConnectTaskCompleted(ConnectResult connectResult);
}
