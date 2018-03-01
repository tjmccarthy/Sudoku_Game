package edu.utep.cs.cs3331.ard.sudoku.net;

/**
 * A representation of a single Sudoku game board square from a Sudoku Web Service API.
 * <p>
 * See <a href="http://www.cs.utep.edu/cheon/ws/sudoku/">http://www.cs.utep.edu/cheon/ws/sudoku/</a>
 * 
 * @author      Anthony DesArmier
 * @version     1.1
 * @since       1.1
 */
public class JSONSquare {
	/** X coordinate for a Sudoku game board square. */
	private int x;
	/** Y coordinate for a Sudoku game board square. */
	private int y;
	/** Square value for a Sudoku game board square. */
	private int value;
	
	/**
	 * Getter for {@link #x}.
	 * @return {@link #x}
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Setter for {@link #x}.
	 * @return {@link #x}
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * Getter for {@link #y}.
	 * @return {@link #y}
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Setter for {@link #y}.
	 * @return {@link #y}
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * Getter for {@link #value}.
	 * @return {@link #value}
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * Setter for {@link #value}.
	 * @return {@link #value}
	 */
	public void setValue(int value) {
		this.value = value;
	}
}