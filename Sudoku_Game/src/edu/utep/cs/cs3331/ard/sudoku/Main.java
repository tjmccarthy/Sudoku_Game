package edu.utep.cs.cs3331.ard.sudoku;

import org.json.simple.JSONObject;

/**
 * @author      Anthony DesArmier ardesarmier@miners.utep.edu
 * @version     1.0
 * @since       1.0
 */
public class Main {
	private static Board board;
	private static ConsoleUI ui;
	
	/**
	 * Executes the {@link #play()} method.
	 * @param args	Not used.
	 */
	public static void main(String[] args) {
		play();
	}

	/**
	 * The main game loop that handles all game functions.
	 * <p>
	 * Creates a new board and UI and handles all related commands until the user prompts to quit.
	 */
	private static void play() {
		int[] values;
		boolean run = true;
		while(run) {
			ui = new ConsoleUI();
			ui.printMessage("Welcome to Sudoku! Enter 'q' at any time to quit.\n");
			ui.printInfo();
			JSONObject boardJSON = ui.askNewBoard();
			if((boolean) boardJSON.get("response")) {
				board = new Board(boardJSON);
			}
			else {
				ui.printMessage("Failed to create board.\nReason: "+(String) boardJSON.get("reason")+"\n\n");
				continue;
			}
			ui.createBorders(board);
			while(!board.isSolved()) {
				ui.printBoard(board);
				values = ui.askInput(board);
				board.update(values);
			}
			ui.printBoard(board);
			ui.printMessage("Finished!\n");
			run = ui.restart();
			ui.printMessage("---\n");
		}
	}

}
