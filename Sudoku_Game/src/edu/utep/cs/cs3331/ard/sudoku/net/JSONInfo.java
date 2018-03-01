package edu.utep.cs.cs3331.ard.sudoku.net;

/**
 * Various information from a Sudoku Web Service API.
 * <p>
 * See <a href="http://www.cs.utep.edu/cheon/ws/sudoku/">http://www.cs.utep.edu/cheon/ws/sudoku/</a>
 * 
 * @author		Anthony DesArmier
 * @version     1.1
 * @since       1.1
 */
public class JSONInfo {
	/** A list of valid Sudoku game board sizes. */
	private int[] sizes;
	/** A list of valid Sudoku game board levels. */
	private int[] levels;
	/** Default Sudoku game board size. */
	private int defaultSize;
	/** Default Sudoku game board difficulty. */
	private int defaultLevel;
	
	/**
	 * Getter for {@link #sizes}.
	 * @return {@link #sizes}
	 */
	public int[] getSizes() {
		return sizes;
	}
	
	/**
	 * Setter for {@link #sizes}.
	 * @return {@link #sizes}
	 */
	public void setSizes(int[] sizes) {
		this.sizes = sizes;
	}
	
	/**
	 * Getter for {@link #levels}.
	 * @return {@link #levels}
	 */
	public int[] getLevels() {
		return levels;
	}
	
	/**
	 * Setter for {@link #levels}.
	 * @return {@link #levels}
	 */
	public void setLevels(int[] levels) {
		this.levels = levels;
	}
	
	/**
	 * Getter for {@link #defaultSize}.
	 * @return {@link #defaultSize}
	 */
	public int getDefaultSize() {
		return defaultSize;
	}
	
	/**
	 * Setter for {@link #defaultSize}.
	 * @return {@link #defaultSize}
	 */
	public void setDefaultSize(int defaultSize) {
		this.defaultSize = defaultSize;
	}
	
	/**
	 * Getter for {@link #defaultLevel}.
	 * @return {@link #defaultLevel}
	 */
	public int getDefaultLevel() {
		return defaultLevel;
	}
	
	/**
	 * Setter for {@link #defaultLevel}.
	 * @return {@link #defaultLevel}
	 */
	public void setDefaultLevel(int defaultLevel) {
		this.defaultLevel = defaultLevel;
	}
}
