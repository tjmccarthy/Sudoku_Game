package edu.utep.cs.cs3331.ard.sudoku.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.utep.cs.cs3331.ard.sudoku.model.Square.State;
import edu.utep.cs.cs3331.ard.sudoku.net.JsonBoard;
import edu.utep.cs.cs3331.ard.sudoku.net.JsonSquare;

/**
 * Sudoku game board and various game logic.
 * 
 * @author      Anthony DesArmier
 * @version     1.2
 */
public class Board {
	/** Dimension of a Sudoku game board. */
	private int size;
	/** Dimension of a sub-grid of a Sudoku game board. */
	private int cellDim;
	/** Sudoku game board has been solved or not. */
	private boolean solved;
	/** Sudoku game board represented as a 2D integer array containing cell values. */
	private List<Square> grid;
	/** x, y coordinate of the last selected square. */
	private int[] lastSelected;
	/** Indices of the last set of error squares. */
	private Set<Integer> lastError;
	
	/**
	 * Constructor for the class Board.
	 * <p>
	 * Generates a Sudoku game board.
	 * @param boardJSON	JsonObject containing information used to create a Sudoku game board.
	 * @see #board
	 */
	public Board(JsonBoard jsonBoard) {
		this.size = jsonBoard.getSize();
		this.cellDim = (int)Math.sqrt(size); // Should be a perfect square
		grid = new ArrayList<>(size*size);
		for(int i=0; i<size*size; i++)
			grid.add(new Square());
        for (JsonSquare square : jsonBoard.getSquares()) {
        	int index = square.getX()*size + square.getY();
        	grid.get(index).setValue(square.getValue());
        	grid.get(index).setState(State.FIXED);
        }
        lastSelected = new int[] {-1,-1};
        lastError = new HashSet<>();
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
	 * Getter for {@link #grid}.
	 * @return {@link #grid}
	 */
	public List<Square> getGrid() {
		return grid;
	}
	
	/**
	 * Getter for {@link #lastSelected}.
	 * @return {@link #lastSelected}
	 */
	public int[] getLastSelected() {
		return lastSelected;
	}
	
	/**
	 * Returns the value in a given cell of a Sudoku game board.
	 * @param x x position of the cell space.
	 * @param y y position of the cell space.
	 * @return value of the provided cell space.
	 */
	public int getValue(int x, int y) {
		return grid.get(x*size+y).getValue();
	}

	/**
	 * Inserts a value into a given cell space on the Sudoku game board if valid.
	 * @param values x,y and z values corresponding to the Sudoku game board position and value.
	 */
	public void update(int[] values) {
		if(solved)
			return;
		lastSelected[0] = lastSelected[1] = -1;
		if(grid.get(values[0]*size+values[1]).getState().contains(State.FIXED))
			return;
		if(!isValidEntry(values))
			return;
		int index = values[0]*size+values[1];
		Square square = grid.get(index);
		square.setValue(values[2]);
		square.setState(State.SELECTED);
		lastSelected[0] = values[0];
		lastSelected[1] = values[1];
		if(values[2]!=0 && isValidSudoku()) // no need to check board if a 0 was just inserted
			solved = true;
	}

	/**
	 * Determines if a certain input is valid for the Sudoku game board.
	 * <p>
	 * A valid integer can be any integer (0,board size).
	 * @param value	value to determine if it is valid for the Sudoku game board.
	 * @return true if the input is valid, false otherwise.
	 * @deprecated
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
	 * @return true of the Sudoku game board is complete and valid, false otherwise.
	 */
	private boolean isValidSudoku() {
		boolean[] bitmap;
		for(int i=0; i<size; i++) {
			bitmap = new boolean[size+1];
			bitmap[0] = true;
			for(int j=0; j<size; j++)
				if (!(bitmap[grid.get(i*size+j).getValue()] ^= true))
					return false;
		}
		
		for(int i=0; i<size; i++) {
			bitmap = new boolean[size+1];
			bitmap[0] = true;
			for(int j=0; j<size; j++)
				if (!(bitmap[grid.get(j*size+i).getValue()] ^= true))
					return false;
		}
		
		for(int i=0; i<cellDim; i++)
			for(int j=0; j<cellDim; j++) {
				bitmap = new boolean[size+1];
				bitmap[0] = true;
				for(int x=i*cellDim; x<i*cellDim+cellDim; x++)
					for(int y=j*cellDim; y<j*cellDim+cellDim; y++)
	                   	if (!(bitmap[grid.get(x*size+y).getValue()] ^= true))
	                   		return false;
			}

		return true;
	}
	
	/**
	 * Checks if a given input is a valid move for a Sudoku game. Populates {@link #errorSquares} as any are found.
	 * <p>
	 * A valid move is considered to be inserting a number that does not already exist
	 * within the same row, column, or sub-grid.
	 * @param values x,y and z values corresponding to the Sudoku game board position and value.
	 * @return true if valid, false otherwise.
	 */
	private boolean isValidEntry(int[] values) {
		lastError.forEach(i -> grid.get(i).removeState(State.ERROR)); // clear out old errors
		lastError.clear();
		if(values[2]!=0 ) { // no validity check if value is 0
			int[] subGrid = {(values[0]/cellDim)*cellDim, (values[1]/cellDim)*cellDim}; // floor indices to nearest multiple of cell dimension
			int j=-1, x=0, y=0, index=0;
			boolean error = false;
			Square square;
			for(int i=0; i<size; i++) {
				index = values[0]*size+i;
				square = grid.get(index); // column
				if(square.equals(values[2]) ) {
					square.setState(State.ERROR);
					lastError.add(index);
					error = true;
				}
				index = i*size+values[1];
				square = grid.get(index); // row
				if(square.equals(values [2])) { 
					square.setState(State.ERROR);
					lastError.add(index);
					error = true;
				}
				if(i%cellDim==0) j++; 
				x = i%cellDim+subGrid[0];
				y = j%cellDim+subGrid[1];
				index = x*size+y;
				square = grid.get(index); // sub-grid
				if(square.equals(values[2])) {
					square.setState(State.ERROR);
					lastError.add(index);
					error = true;
				}
			}
			if(error) return false;
		}
		return true;
	}

}
