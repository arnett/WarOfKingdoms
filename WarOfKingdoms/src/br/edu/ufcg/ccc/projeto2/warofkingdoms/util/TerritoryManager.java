package br.edu.ufcg.ccc.projeto2.warofkingdoms.util;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import br.edu.ufcg.ccc.projeto2.warofkingdoms.entities.Territory;

/**
 * 
 * @author Arnett
 * 
 */
public class TerritoryManager {

	private final static Map<Integer, Territory> regionColorsToTerritories = populateRegionsMap();

	private final static ColorMatcher colorMatcher = new ColorMatcher(
			regionColorsToTerritories.keySet());

	/*
	 * All colors that represent a clickable area but doesnt represent a
	 * territory should be assigned to null in this map
	 */
	@SuppressLint("UseSparseArrays")
	private static Map<Integer, Territory> populateRegionsMap() {
		Map<Integer, Territory> regions = new HashMap<Integer, Territory>();
		regions.put(0xFF000000, new Territory("K"));
		regions.put(0xFF1745D1, new Territory("J"));
		regions.put(0xFF2E8BA2, new Territory("H"));
		regions.put(0xFF45D173, new Territory("I"));
		regions.put(0xFF5D1744, new Territory("G"));
		regions.put(0xFF745D15, new Territory("D"));
		regions.put(0xFF8BA2E6, new Territory("E"));
		regions.put(0xFFA2E8B7, new Territory("F"));
		regions.put(0xFFBA2E88, new Territory("B"));
		regions.put(0xFFD17459, new Territory("C"));
		regions.put(0xFFE8BA2A, new Territory("A"));
		regions.put(0xFFFFFFFF, null);
		return regions;
	}

	/**
	 * Returns the territory represented by the closest color to the specified
	 * <tt> color </tt> or null if the touched area is not a territory
	 * 
	 * @param color
	 *            any ARGB color
	 * @return
	 */
	public static Territory getTerritoryByClosestColor(int color) {
		int colorMatch = colorMatcher.closestMatch(color);
		return regionColorsToTerritories.get(colorMatch);
	}
}
