package br.edu.ufcg.ccc.projeto2.warofkingdoms.states;

import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Action;

public class FreeTerritory implements TerritoryState {

	/**
	 * A free territory has no applicable actions. If, in the future, a new
	 * action is created for free territories, it should be added here.
	 */
	@Override
	public Action[] getApplicableActions() {
		return new Action[] { };
	}

}