package br.edu.ufcg.ccc.projeto2.warofkingdoms.activities;

import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Conflict;

public interface OnTaskCompleted {

	void onSendMovesTaskCompleted(Conflict[] conflicts);

}
