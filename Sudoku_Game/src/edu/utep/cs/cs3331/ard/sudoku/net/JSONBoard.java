package edu.utep.cs.cs3331.ard.sudoku.net;

/**
 * A representation of a Sudoku game board from a Sudoku Web Service API.
 * <p>
 * See <a href="http://www.cs.utep.edu/cheon/ws/sudoku/">http://www.cs.utep.edu/cheon/ws/sudoku/</a>
 * 
 * @author		Anthony DesArmier
 * @version     1.1
 * @since       1.1
 */
public class JSONBoard {
	/** True if API produced a valid response, false otherwise. */
	private boolean response;
	/** Size of the Sudoku game board. */
 	private int size;
 	/** An array of Sudoku game board squares.
 	 * @see {@link JSONSquare}
 	 */
	private JSONSquare[] squares;
	/** Message for was response was false. */
	private String reason;

	/**
	 * Getter for {@link #response}.
	 * @return {@link #response}
	 */
	public boolean isResponse() {
		return response;
	}

	/**
	 * Setter for {@link #response}.
	 * @return {@link #response}
	 */
	public void setResponse(boolean response) {
		this.response = response;
	}

	/**
	 * Getter for {@link #size}.
	 * @return {@link #size}
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Setter for {@link #size}.
	 * @return {@link #size}
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * Getter for {@link #squares}.
	 * @return {@link #squares}
	 */
	public JSONSquare[] getSquares() {
		return squares;
	}

	/**
	 * Setter for {@link #squares}.
	 * @return {@link #squares}
	 */
	public void setSquares(JSONSquare[] squares) {
		this.squares = squares;
	}
	
	/**
	 * Getter for {@link #reason}.
	 * @return {@link #reason}
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * Setter for {@link #reason}.
	 * @return {@link #reason}
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}
}
