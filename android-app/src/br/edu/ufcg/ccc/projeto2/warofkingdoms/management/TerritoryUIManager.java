package br.edu.ufcg.ccc.projeto2.warofkingdoms.management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Territory;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.util.ColorMatcher;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.util.TerritoryCenter;

/**
 * 
 * @author Arnett
 * 
 */
public class TerritoryUIManager {

	@SuppressLint("UseSparseArrays")
	private Map<Integer, String> regionColorsToTerritories = new HashMap<Integer, String>();
	private Map<String, TerritoryCenter> territoriesCenters = new HashMap<String, TerritoryCenter>();

	private ColorMatcher colorMatcher;

	private static TerritoryUIManager instance;

	private TerritoryUIManager() {
		generateTerritoriesMap();
		colorMatcher = new ColorMatcher(regionColorsToTerritories.keySet());
	}

	public synchronized static TerritoryUIManager getInstance() {
		if (instance == null) {
			instance = new TerritoryUIManager();
		}
		return instance;
	}

	/*
	 * All colors that represent a clickable area but doesnt represent a
	 * territory should be assigned to null in this map
	 */
	private void generateTerritoriesMap() {
		addNewTerritory("A", 677, 196, 0xFF000000);
		addNewTerritory("B", 816, 305, 0xFF54F433);
		addNewTerritory("C", 558, 390, 0xFFF4FB2A);
		addNewTerritory("D", 368, 501, 0xFF0958F9);
		addNewTerritory("E", 719, 514, 0xFFF909EB);
		addNewTerritory("F", 543, 679, 0xFFF90E24);
		addNewTerritory("G", 372, 674, 0xFFB008E7);
		addNewTerritory("H", 753, 763, 0xFF333330);
		addNewTerritory("I", 570, 838, 0xFFFD9D05);
		addNewTerritory("J", 333, 872, 0xFF96928D);
		addNewTerritory("K", 769, 909, 0xFF3FFFFC);
		addNewTerritory("L", 564, 976, 0xFF6A5281);
		addNewTerritory("M", 423, 1008, 0xFF8DA712);
		addNewTerritory("N", 717, 1045, 0xFF53332E);
		addNewTerritory("O", 731, 1164, 0xFFD1FE33);
		addNewTerritory("P", 519, 1158, 0xFFFEAB33);
		addNewTerritory("Q", 352, 1160, 0xFFFAABAB);
		addNewTerritory("R", 790, 1322, 0xFF730780);
		addNewTerritory("S", 629, 1272, 0xFFCD0655);
		addNewTerritory("T", 432, 1314, 0xFF2F11C8);
		addNewTerritory("U", 647, 1423, 0xFF8CCCC4);
		addNewTerritory("V", 512, 1412, 0xFF86665E);
		addNewTerritory("X", 385, 1483, 0xFF4E767E);
		addNewTerritory("Y", 814, 1560, 0xFFE66658);
		addNewTerritory("Z", 601, 1560, 0xFF93332A);

		// addNewTerritory("B", 625, 433, 0xFFBA2E88);
		// addNewTerritory("D", 844, 877, 0xFF745D15);
		// addNewTerritory("E", 590, 1028, 0xFF8BA2E6);
		// addNewTerritory("F", 283, 940, 0xFFA2E8B7);
		// addNewTerritory("G", 393, 1163, 0xFF5D1744);
		// addNewTerritory("H", 543, 1364, 0xFF2E8BA2);
		// addNewTerritory("I", 782, 1209, 0xFF45D173);
		// addNewTerritory("J", 549, 1647, 0xFF1745D1);
		// addNewTerritory("K", 829, 1379, 0xFF000000);

		// The sea, represented by the color white, should not be touchable
		regionColorsToTerritories.put(0xFFFFFFFF, null);
	}

	private void addNewTerritory(String territoryName, int x, int y, int color) {
		regionColorsToTerritories.put(color, territoryName);
		territoriesCenters.put(territoryName, new TerritoryCenter(x, y));
	}

	/**
	 * Returns the territory represented by the closest color to the specified
	 * <tt> color </tt> or null if the touched area is not a territory
	 * 
	 * @param color
	 *            any ARGB color
	 * @return
	 */
	public String getTerritoryByClosestColor(int color) {
		int colorMatch = colorMatcher.closestMatch(color);
		return regionColorsToTerritories.get(colorMatch);
	}

	public TerritoryCenter getTerritoryUICenter(Territory territory) {
		return territoriesCenters.get(territory.getName());
	}

	@SuppressWarnings("unused")
	private List<Territory> toTerritoryList(
			Map<Integer, Territory> territoriesMap) {
		List<Territory> territories = new ArrayList<Territory>();
		for (Integer territoryName : territoriesMap.keySet()) {
			territories.add(territoriesMap.get(territoryName));
		}
		return territories;
	}
}
