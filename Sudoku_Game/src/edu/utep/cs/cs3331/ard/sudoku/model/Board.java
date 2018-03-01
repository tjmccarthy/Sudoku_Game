package edu.utep.cs.cs3331.ard.sudoku.model;

import java.util.HashSet;
import java.util.Set;

import edu.utep.cs.cs3331.ard.sudoku.net.JSONBoard;
import edu.utep.cs.cs3331.ard.sudoku.net.JSONSquare;


/**
 * A representation of a Sudoku game board and various game logic.
 * 
 * @author      Anthony DesArmier
 * @version     1.1
 * @since       1.0
 */
public class Board {
	/** An integer representing the dimension of a Sudoku game board. */
	private int size;
	/** An integer to denote the dimension of a sub-grid of a Sudoku game board. */
	private int cellDim;
	/** A boolean value used to denote whether the Sudoku game board has been solved or not. */
	private boolean solved;
	/** A Sudoku game board represented as a 2D integer array containing cell values. */
	private int[][] board;
	/** X, Y coordinates for the last selected board square. */
	private int[] lastSelect;
	/** X, Y coordinates for squares that are conflicting with a chosen square. */
	private Set<Integer[]> errorSquares;
	/** X, Y coordinates for squares that are pre-filled and cannot be changed. */
	private Set<Integer[]> fixedSquares;
	
	/**
	 * A constructor for the class Board.
	 * <p>
	 * Generates a Sudoku game board.
	 * @param boardJSON	A JSON object containing information used to create a Sudoku game board.
	 * @see #board
	 */
	public Board(JSONBoard json_board) {
		this.size = json_board.getSize();
		board = new int[size][size];
		this.cellDim = (int)Math.sqrt(size); // Should be a perfect square
		errorSquares = new HashSet<>();
		fixedSquares = new HashSet<>();
        for (JSONSquare square : json_board.getSquares()) {
            board[square.getX()][square.getY()] = square.getValue();
            fixedSquares.add(new Integer[] {square.getX(), square.getY()});
        }
        lastSelect = new int[] {-1,-1};
        errorSquares = new HashSet<>();
	}

	/**
	 * Getter for {@link #size}.
	 * @return {@link #size}
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * Getter for {@link #cellDim}.
	 * @return {@link #cellDim}
	 */
	public int getCellDim() {
		return cellDim;
	}
	
	/**
	 * Getter for {@link #solved}.
	 * @return {@link #solved}
	 */
	public boolean isSolved() {
		return solved;
	}
	
	/**
	 * Getter for {@link #lastSelect}.
	 * @return {@link #lastSelect}
	 */
	public int[] getLastSelect() {
		return lastSelect;
	}
	
	/**
	 * Getter for {@link #errorSquares}.
	 * @return {@link #errorSquares}
	 */
	public Set<Integer[]> getErrorSquares() {
		return errorSquares;
	}
	
	/**
	 * Getter for {@link #fixedSquares}.
	 * @return {@link #fixedSquares}
	 */
	public Set<Integer[]> getFixedSquares() {
		return fixedSquares;
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
	 * Inserts a value into a given cell space on the Sudoku game board if valid.
	 * @param values An integer array containing the x,y and z values corresponding to the Sudoku game board position and value.
	 */
	public void update(int[] values) {
		if(solved) // no updates if completed
			return;
		errorSquares.clear();
		for(Integer[] fixedSquare : fixedSquares) { // no update if selected square is fixed
			if(fixedSquare[0]==values[0] && fixedSquare[1]==values[1]) {
				lastSelect = new int[] {-1,-1};
				return;
			}
		}
		if(!isValidEntry(values))
			return;
		board[values[0]][values[1]] = values [2];
		lastSelect = values.clone();
		if(values[2]!=0 && isValidSudoku()) // no need to check board if a 0 was just inserted
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
	
	/**
	 * Checks if a given input is a valid move for a Sudoku game. Populates {@link #errorSquares} as any are found.
	 * <p>
	 * A valid move is considered to be inserting a number that does not already exist
	 * within the same row, column, or sub-grid.
	 * @param values An integer array containing the x,y and z values corresponding to the Sudoku game board position and value.
	 * @return True if valid, false otherwise.
	 */
	private boolean isValidEntry(int[] values) {
		if(values[2]!=0 ) { // no validity check if value is 0
			int[] subGrid = {(values[0]/cellDim)*cellDim, (values[1]/cellDim)*cellDim}; // floor indices to nearest multiple of cell dimension
			int j=-1, x=0, y=0;
			for(int i=0; i<size; i++) {
				if(board[values[0]][i]==values [2] ) // column
					errorSquares.add(new Integer[] {values[0],i});
				if(board[i][values[1]]==values [2]) // row
					errorSquares.add(new Integer[] {i,values[1]});
				if(i%cellDim==0) j++; // sub-grid
				x = i%cellDim+subGrid[0];
				y = j%cellDim+subGrid[1];
				if(board[x][y]==values [2]) 
					errorSquares.add(new Integer[] {x,y});
			}
			if(!errorSquares.isEmpty())
				return false;
		}
		return true;
	}

}
