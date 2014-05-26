package br.edu.ufcg.ccc.projeto2.warofkingdoms.states;

import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Action;

public class ConqueredTerritory implements TerritoryState {

	@Override
	public Action[] getApplicableActions() {
		return new Action[] {Action.ATTACK, Action.DEFEND};
	}

}
