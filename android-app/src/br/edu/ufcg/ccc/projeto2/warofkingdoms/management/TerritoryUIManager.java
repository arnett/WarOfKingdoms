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
		addNewTerritory("A", 632, 229, 0xFF000000);
		addNewTerritory("B", 783, 330, 0xFF54F433);
		addNewTerritory("C", 524, 413, 0xFFF4FB2A);
		addNewTerritory("D", 338, 531, 0xFF0958F9);
		addNewTerritory("E", 690, 531, 0xFFF909EB);
		addNewTerritory("F", 495, 697, 0xFFF90E24);
		addNewTerritory("G", 338, 695, 0xFFB008E7);
		addNewTerritory("H", 731, 760, 0xFF333330);
		addNewTerritory("I", 527, 825, 0xFFFD9D05);
		addNewTerritory("J", 295, 880, 0xFF96928D);
		addNewTerritory("K", 724, 900, 0xFF3FFFFC);
		addNewTerritory("L", 524, 975, 0xFF6A5281);
		addNewTerritory("M", 389, 1000, 0xFF8DA712);
		addNewTerritory("N", 675, 1010, 0xFF53332E);
		addNewTerritory("O", 660, 1150, 0xFFD1FE33);
		addNewTerritory("P", 470, 1145, 0xFFFEAB33);
		addNewTerritory("Q", 336, 1170, 0xFFFAABAB);
		addNewTerritory("R", 740, 1280, 0xFF730780);
		addNewTerritory("S", 590, 1240, 0xFFCD0655);
		addNewTerritory("T", 380, 1290, 0xFF2F11C8);
		addNewTerritory("U", 610, 1390, 0xFF8CCCC4);
		addNewTerritory("V", 490, 1400, 0xFF86665E);
		addNewTerritory("X", 370, 1500, 0xFF4E767E);
		addNewTerritory("Y", 800, 1545, 0xFFE66658);
		addNewTerritory("Z", 580, 1550, 0xFF93332A);
		
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
