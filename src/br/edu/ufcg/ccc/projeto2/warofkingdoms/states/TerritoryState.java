package br.edu.ufcg.ccc.projeto2.warofkingdoms.states;

import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Action;

public interface TerritoryState {

	public Action[] getApplicableActions();

}
