package br.edu.ufcg.ccc.projeto2.warofkingdoms.management;

import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.BARATHEON;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.GREYJOY;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.LANNISTER;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.MARTELL;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.STARK;
import static br.edu.ufcg.ccc.projeto2.warofkingdoms.util.Constants.TYRELL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.House;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Territory;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.ui.entities.HouseToken;
import br.ufcg.edu.ccc.projeto2.R;

public class HouseTokenManager {

	private static HouseTokenManager instance;

	private Map<House, HouseToken> houseTokens;
	private Map<Territory, House> startingHouseTerritories;

	private HouseTokenManager() {
		houseTokens = generateAllTokens();
	}

	public synchronized static HouseTokenManager getInstance() {
		if (instance == null) {
			instance = new HouseTokenManager();
		}
		return instance;
	}

	private Map<House, HouseToken> generateAllTokens() {
		Map<House, HouseToken> tokens = new HashMap<House, HouseToken>();

		HouseToken starkToken = new HouseToken(R.drawable.token_stark,
				R.drawable.token_stark_castle);
		tokens.put(new House(STARK), starkToken);

		HouseToken lannisterToken = new HouseToken(R.drawable.token_lannister,
				R.drawable.token_lannister_castle);
		tokens.put(new House(LANNISTER), lannisterToken);

		HouseToken baratheonToken = new HouseToken(R.drawable.token_baratheon,
				R.drawable.token_baratheon_castle);
		tokens.put(new House(BARATHEON), baratheonToken);

		HouseToken tyrellToken = new HouseToken(R.drawable.token_tyrell,
				R.drawable.token_tyrell_castle);
		tokens.put(new House(TYRELL), tyrellToken);

		HouseToken martellToken = new HouseToken(R.drawable.token_martell,
				R.drawable.token_martell_castle);
		tokens.put(new House(MARTELL), martellToken);

		HouseToken greyjoyToken = new HouseToken(R.drawable.token_greyjoy,
				R.drawable.token_greyjoy_castle);
		tokens.put(new House(GREYJOY), greyjoyToken);

		return tokens;
	}

	public void setStartingHouseTerritories(List<Territory> territories) {
		startingHouseTerritories = new HashMap<Territory, House>();
		for (Territory t : territories) {
			if (t.getOwner() != null) {
				startingHouseTerritories.put(t, t.getOwner());
			}
		}
	}

	/**
	 * Returns the token image representing the state of the provided territory.
	 * This should only be called after the starting territories are set up,
	 * i.e., call setStartingHouseTerritories before calling this method. The
	 * provided territory should also be owned by a house.
	 * 
	 * @param house
	 * @param territory
	 * @return
	 */
	public int getTokenImage(Territory territory) {
		if (territoryIsOwnedByFirstOwner(territory)) {
			return houseTokens.get(territory.getOwner()).getCastleToken();
		} else {
			return houseTokens.get(territory.getOwner()).getFlagToken();
		}
	}

	private boolean territoryIsOwnedByFirstOwner(Territory territory) {
		return territory.getOwner().equals(
				startingHouseTerritories.get(territory));
	}

	public HouseToken getHouseToken(House house) {
		return houseTokens.get(house);
	}
}
