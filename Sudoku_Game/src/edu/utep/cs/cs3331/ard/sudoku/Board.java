package edu.utep.cs.cs3331.ard.sudoku;

import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * @author      Anthony DesArmier ardesarmier@miners.utep.edu
 * @version     1.0
 * @since       1.0
 */
public class Board {
	/**
	 * An integer representing the dimension of a Sudoku game board.
	 */
	private int size;
	/**
	 * A integer to denote the dimension of a sub-grid of a Sudoku game board.
	 */
	private int cellDim;
	/**
	 * A boolean value used to denote whether the Sudoku game board has been solved or not.
	 */
	private boolean solved;
	/**
	 * A Sudoku game board represented as a 2D integer array containing cell values.
	 */
	private int[][] board;
	
	/**
	 * A constructor for the class Board.
	 * <p>
	 * Generates a Sudoku game board.
	 * @param boardJSON	A JSON object containing information used to create a Sudoku game board.
	 * @see #board
	 */
	public Board(JSONObject boardJSON) {
		Object obj = boardJSON.get("size");
		if(obj instanceof String) // Handle if API specifies size as a String
			this.size = Integer.valueOf((String) obj); 
		else
			this.size = Math.toIntExact((long) obj);
		board = new int[size][size];
		this.cellDim = (int)Math.sqrt(size); // Should be a perfect square
		JSONObject temp;
		JSONArray arr = (JSONArray) boardJSON.get("squares");
		Iterator<JSONObject> iterator = arr.iterator();
        while (iterator.hasNext()) {
            temp = iterator.next();
            board[Math.toIntExact((long) temp.get("x"))][Math.toIntExact((long) temp.get("y"))] = Math.toIntExact((long) temp.get("value"));
        }
	}

	/**
	 * Getter for {@link #solved}.
	 * @return {@link #solved}
	 */
	public boolean isSolved() {
		return solved;
	}
	
	/**
	 * Getter for {@link #size}.
	 * @return {@link #size}
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * Returns the value in a given cell of a Sudoku game board.
	 * @param x The x position of the cell space.
	 * @param y The y position of the cell space.
	 * @return The integer value in the provided cell space.
	 */
	public int getValue(int x, int y) {
		return board[x][y];
	}
	
	/**
	 * Getter for {@link #cellDim}.
	 * @return {@link #cellDim}
	 */
	public int getCellDim() {
		return cellDim;
	}

	/**
	 * Inserts a value into a given cell space on the Sudoku game board.
	 * @param values An integer array containing the x,y and z values corresponding to the Sudoku game board position and value.
	 */
	public void update(int[] values) {
		board[values[0]-1][values[1]-1] = values [2];
		if(isValidSudoku())
			solved = true;
	}

	/**
	 * Determines if a certain input is valid for the Sudoku game board.
	 * <p>
	 * A valid integer can be any integer (0,board size).
	 * @param value	The integer value to determine if it is valid for the Sudoku game board.
	 * @return True if the input is valid, false otherwise.
	 */
	public boolean validInput(int value) {
		if(value > size || value < 0)
			return false;
		return true;
	}
	
	/**
	 * Determines whether the Sudoku game game is complete and valid.
	 * <p>
	 * Uses bitmapping to determine the requirements for a completed Sudoku game board:
	 * Only one of each number exists in every row, column, and sub-grid.
	 * <p>
	 * Runs in O(n) time.
	 * @return True of the Sudoku game board is complete and valid, false otherwise.
	 */
	private boolean isValidSudoku() {
		boolean[] bitmap;
		for(int i=0; i<size; i++) {
			bitmap = new boolean[size+1];
			bitmap[0] = true;
			for(int j=0; j<size; j++)
				if (!(bitmap[board[i][j]] ^= true))
					return false;
		}
		
		for(int i=0; i<size; i++) {
			bitmap = new boolean[size+1];
			bitmap[0] = true;
			for(int j=0; j<size; j++)
				if (!(bitmap[board[j][i]] ^= true))
					return false;
		}
		
		for(int i=0; i<cellDim; i++)
			for(int j=0; j<cellDim; j++) {
				bitmap = new boolean[size+1];
				bitmap[0] = true;
				for(int x=i*cellDim; x<i*cellDim+cellDim; x++)
					for(int y=j*cellDim; y<j*cellDim+cellDim; y++)
	                   	if (!(bitmap[board[x][y]] ^= true))
	                   		return false;
			}

		return true;
	}

}
