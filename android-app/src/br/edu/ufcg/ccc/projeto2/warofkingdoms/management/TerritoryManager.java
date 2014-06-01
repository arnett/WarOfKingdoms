package br.edu.ufcg.ccc.projeto2.warofkingdoms.management;

import java.util.HashMap;
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
public class TerritoryManager {

	@SuppressLint("UseSparseArrays")
	private Map<Integer, Territory> regionColorsToTerritories = new HashMap<Integer, Territory>();
	private Map<String, TerritoryCenter> territoriesCenters = new HashMap<String, TerritoryCenter>();

	private ColorMatcher colorMatcher;

	private static TerritoryManager instance;
	
	private TerritoryManager() {
		generateTerritoriesMap();
		colorMatcher = new ColorMatcher(regionColorsToTerritories.keySet());
	}

	public synchronized static TerritoryManager getInstance() {
		if (instance == null) {
			instance = new TerritoryManager();
		}
		return instance;
	}
	
	/*
	 * All colors that represent a clickable area but doesnt represent a
	 * territory should be assigned to null in this map
	 */
	private void generateTerritoriesMap() {
		addNewTerritory("B", 625, 433, 0xFFBA2E88);
		addNewTerritory("D", 844, 877, 0xFF745D15);
		addNewTerritory("E", 590, 1028, 0xFF8BA2E6);
		addNewTerritory("F", 283, 940, 0xFFA2E8B7);
		addNewTerritory("G", 393, 1163, 0xFF5D1744);
		addNewTerritory("H", 543, 1364, 0xFF2E8BA2);
		addNewTerritory("I", 782, 1209, 0xFF45D173);
		addNewTerritory("J", 549, 1647, 0xFF1745D1);
		addNewTerritory("K", 829, 1379, 0xFF000000);

		// The sea, represented by the color white, should not be touchable
		regionColorsToTerritories.put(0xFFFFFFFF, null);
	}

	private void addNewTerritory(String territoryName, int x, int y,
			int color) {
		Territory newTerritory = new Territory(territoryName);
		regionColorsToTerritories.put(color, newTerritory);
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
	public Territory getTerritoryByClosestColor(int color) {
		int colorMatch = colorMatcher.closestMatch(color);
		return regionColorsToTerritories.get(colorMatch);
	}
}
