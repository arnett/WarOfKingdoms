package br.edu.ufcg.ccc.projeto2.warofkingdoms.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 
 * @author Arnett
 * 
 */
public class ColorMatcher {

	private List<Integer> possibleColors;

	/**
	 * 
	 * @param possibleColors a list with all colors to be matched against
	 */
	public ColorMatcher(Collection<Integer> possibleColors) {
		this.possibleColors = new ArrayList<Integer>(possibleColors);
	}

	/**
	 * Returns the difference of the two specified colors
	 * 
	 * @param oneColor
	 * @param anotherColor
	 * @return
	 */
	private int colorDifference(int oneColor, int anotherColor) {
		return Math.abs(oneColor - anotherColor);
	}

	/**
	 * Returns the closest color in <tt> possibleColors </tt> to the
	 * specified <tt> color </tt>
	 * 
	 * @param color
	 * @return
	 */
	public int closestMatch(int color) {
		int closestColor = possibleColors.get(0);
		for (int possibleClosestColor : possibleColors) {
			if (colorDifference(color, possibleClosestColor) < colorDifference(
					color, closestColor)) {
				closestColor = possibleClosestColor;
			}
		}
		return closestColor;
	}

	/**
	 * Returns the list of possible colors to be matched against
	 * @return
	 */
	public List<Integer> getPossibleColors() {
		return possibleColors;
	}
}
