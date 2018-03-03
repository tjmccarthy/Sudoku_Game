package edu.utep.cs.cs3331.ard.sudoku.net;

/**
 * Sudoku game board square from a Sudoku Web Service API.
 * <p>
 * See <a href="http://www.cs.utep.edu/cheon/ws/sudoku/">http://www.cs.utep.edu/cheon/ws/sudoku/</a>
 * 
 * @author      Anthony DesArmier
 * @version     1.1
 * @since       1.1
 */
public class JSONSquare {
	/** x coordinate of the square. */
	private int x;
	/** y coordinate of the square. */
	private int y;
	/** Value of the square. */
	private int value;
	
	/** 
	 * Constructor for the class JSONSquare.
	 * @param x {@link x}
	 * @param y {@link y}
	 * @param value {@link value}
	 */
	public JSONSquare(int x, int y, int value) {
		this.x = x;
		this.y = y;
		this.value = value;
	}

	/**
	 * Getter for {@link #x}.
	 * @return {@link #x}
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Setter for {@link #x}.
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
	 */
	public void setValue(int value) {
		this.value = value;
	}
}