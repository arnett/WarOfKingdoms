package br.edu.ufcg.ccc.projeto2.warofkingdoms.management;

import java.util.List;

import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Connect;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Move;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.ui.OnTaskCompleted;

public interface CommunicationManager {

	public void sendCurrentMoves(OnTaskCompleted listener, List<Move> moves);

	public void connect(OnTaskCompleted listener, Connect connectEntity);

}
