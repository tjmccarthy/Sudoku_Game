package edu.utep.cs.cs3331.ard.sudoku.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Sudoku game board square consisting of a value and state.
 * 
 * @author      Anthony DesArmier
 * @version     1.2
 */
public class Square {
	
	/** Various states a square may be in.
	 * A fixed square is one that cannot be modified. */
	public enum State {
		NORMAL, SELECTED, ERROR, FIXED
	}
	
	/** x coordinate of the square. */
	private int x;
	/** y coordinate of the square. */
	private int y;
	/** Value of the square. */
	private int value;

	/** States of the square. */
	private Set<State> states;
	
	/** Creates a default empty square */
	public Square() {
		this(0, State.NORMAL);
	}
	
	/** Creates a square with a given value and state. */
	public Square(int value, State state) {
		this.value = value;
		states = new HashSet<>();
		states.add(state);
	}
	
	/** 
	 * Constructor for the class Square.
	 * @param x {@link x}
	 * @param y {@link y}
	 * @param value {@link value}
	 */
	public Square(int x, int y, int value) {
		this.x = x;
		this.y = y;
		this.value = value;
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
	 * Getter for {@link #states}.
	 * @return {@link #states}
	 */
	public Set<State> getState() {
		return states;
	}

	/**
	 * Assigns one or more {@link #states}.
	 * @param states one or more states to add.
	 */
	public void setState(State... states) {
		for(State state : states)
			this.states.add(state);
	}
	
	/**
	 * Removes one or more {@link #states}.
	 * @param states one or more states to remove.
	 */
	public void removeState(State... states) {
		for(State state : states) {
			if(this.states.contains(state))
				this.states.remove(state);
			else
				System.out.printf("Tried to remove state that did not exist: %s%n", state.toString());
		}
	}
	
	/**
	 * Compares this square's value with another value.
	 * @param value value to compare with.
	 * @return true if equal, false otherwise.
	 */
	public boolean equals(int value ) {
		return this.getValue()==value;
	}
}
