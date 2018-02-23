package edu.utep.cs.cs3331.ard.sudoku;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

/**
 * @author      Anthony DesArmier ardesarmier@miners.utep.edu
 * @version     1.0
 * @since       1.0
 */
public class ConsoleUI {
	/**
	 * The PrintStream to output all visuals to.
	 */
	private PrintStream out;
	/**
	 * The scanner used to handle all user inputs.
	 */
	private Scanner scanner;
	/**
	 * A string used to create the top and bottom borders of the sudoku game grid.
	 */
	private String rowBorder;
	/**
	 * An integer to denote a default dimension of the Sudoku game board.
	 */
	private int defaultSize;
	/**
	 * An integer to denote a default difficulty level of the Sudoku game board.
	 */
	private int defaultLevel;
	/**
	 * An ArrayList of integers used to store a valid list of Sudoku game board dimensions.
	 */
	private ArrayList<Integer> sizes;
	/**
	 * An ArrayList of integers used to store a valid list of Sudoku game difficulty levels.
	 */
	private ArrayList<Integer> levels;
	
	/**
	 * A default constructor for class ConsoleUI.
	 * <p>
	 * Defaults to setting the InputStream and PrintStream to System.in and System.out respectively.
	 * @see #ConsoleUI(InputStream, PrintStream)
	 */
	public ConsoleUI() {
		this(System.in, System.out);
	}

	/**
	 * A constructor for the class ConsoleUI.
	 * @param in An InputStream to handle all user inputs.
	 * @param out An OutputStream to output all visuals to.
	 */
	public ConsoleUI(InputStream in, PrintStream out) {
		this.out = out;
		scanner = new Scanner(in);
	}
	
	/**
	 * Prints a message to the PrintStream.
	 * @param string The message to print to the PrintStream.
	 */
	public void printMessage(String string) {
		out.print(string);
	}
	
	/**
	 * Formats and prints information supplied by a Sudoku Web Service API.
	 * @see #JavaClient.getInfo()
	 */
	public void printInfo() {
		JSONObject info = null;
		try {
			info = JavaClient.getInfo();
		} catch (MalformedURLException e) {
			//e.printStackTrace();
			printMessage("Malformed URL.\n");
		} catch (IOException e) {
			//e.printStackTrace();
			printMessage("Failed to connect to server.\n");
		} catch (ParseException e) {
			//e.printStackTrace();
			printMessage("Invalid data recieved from server.\n");
		} finally {
			if(info == null)
				return;
		}
		
		sizes = handleJSONArray((JSONArray) info.get("sizes"));
		levels = handleJSONArray((JSONArray) info.get("levels"));
		defaultSize =   Math.toIntExact((long) info.get("defaultSize"));
		defaultLevel = Math.toIntExact((long) info.get("defaultLevel"));
		
		printMessage(String.format("Board sizes: %s%n"
									+"Difficulty Levels: 0, %s%n"
									+"Default Size: %d%n"
									+"Default Level: %d%n", sizes.toString(), levels.toString(), defaultSize, defaultLevel));
	}
	
	/**
	 * Queries the user for a valid Sudoku board size.
	 * @return A JSON object containing information used to create a Sudoku game board.
	 * @see #JavaClient.requestBoard()
	 */
	public JSONObject askNewBoard() {
		JSONObject board = null;
		int size = defaultSize;
		int level = defaultLevel;
		size = queryUser("Size of board: ");
		level = queryUser("Difficulty level: ");
		if(level == 0) {
			board = new JSONObject();
			for(int i : sizes) {
				if(i == size) {
			        board.put("response", true);
			        board.put("size", size);
			        board.put("squares", new JSONArray());
			        return board;
				}
			}
			board.put("response", false);
			board.put("reason", "Unknown size, "+size);
			return board;
		}
		else {
			try {
				board = JavaClient.requestBoard(size, level);
			} catch (MalformedURLException e) {
				//e.printStackTrace();
				printMessage("Malformed URL.\n");
			} catch (IOException e) {
				//e.printStackTrace();
				printMessage("Failed to connect to server.\n");
			} catch (ParseException e) {
				//e.printStackTrace();
				printMessage("Invalid data recieved from server.\n");
			} 
			if(board == null)
				board.put("response", false);
			return board;
		}
	}

	/**
	 * Generates and prints the current state of a supplied Sudoku board.
	 * @param board	The Sudoku game board to generate and print.
	 */
	public void printBoard(Board board) {
		String drawBoard = "\n";
		for(int i=0; i<board.getSize(); i++) {
			if(i%board.getCellDim()==0)
				drawBoard = drawBoard.concat(rowBorder+"\n");
			for(int j=0; j<board.getSize(); j++) {
				if(j%board.getCellDim()==0)
					drawBoard = drawBoard.concat("| ");
				int number = (board.getValue(i, j));
				if(number == 0)
					drawBoard = drawBoard.concat("  ");
				else
					drawBoard = drawBoard.concat(Integer.toString(number)+" ");
			}
			drawBoard = drawBoard.concat("|\n");		
		}
		drawBoard = drawBoard.concat(rowBorder+"\n");
		printMessage(drawBoard + "\n");
	}

	/**
	 * Queries the user for a valid Sudoku game input.
	 * <p>
	 * Queries the user for a position (x,y) and a value (z).
	 * @param board	The Sudoku game board for which the input is for.
	 * @return An integer array containing the x,y and z values corresponding to the Sudoku game board position and value.
	 */
	public int[] askInput(Board board) {
		int[] value = new int[3];
		toploop:
		while(true) {
			printMessage("Enter cell (x,y) and value (z) as \"x y z\", value \"0\" to clear: ");
			String input = scanner.nextLine();
			checkQuit(input);
			String[] inputs = input.split("\\s+");
			if(inputs.length == 3) {
				for(int i = 0; i<3; i++) {
					try {
						value[i] = Integer.parseInt(inputs[i]);
						if(!board.validInput(value[i])) {
							printMessage("Invalid input: " + input + "\n");
							continue toploop;
						}
					}
					catch (NumberFormatException e) {
						printMessage("Invalid input: " + input + "\n");
						continue toploop;
					}
				}
			}
			else {
				printMessage("Invalid input: "+ input + "\n");
				continue toploop;	
			}
			return value;
		}
	}

	/**
	 * Creates a string of dashes ('-') that are used to represent the top and bottom of the Sudoku game board.
	 * @param board	The Sudoku game board for which to generate the top and bottom borders for.
	 */
	public void createBorders(Board board) {
		int i = 0;
		switch(board.getSize()) {
			case 4: i = 13;
					break;
			case 9: i = 25;
					break;
		}
		char[] arr = new char[i];
		Arrays.fill(arr, '-');
		rowBorder = new String(arr);
	}
	
	/**
	 * Queries the user to restart the game and awaits a valid input: y/n/q.
	 * @return True of the user wishes to restart the game, false otherwise.
	 */
	public boolean restart() {
		while(true) {
			printMessage("Again? (y/n/q) ");
			String input = scanner.nextLine();
			checkQuit(input);
			if(input.equalsIgnoreCase("y"))
				return true;
			if(input.equalsIgnoreCase("n"))
				return false;
			printMessage("Invalid input: " + input + "\n");
		}
	}
	
	/**
	 * Converts a JSON Array of integers into an ArrayList of integers.
	 * @param jsonArr The JSON Array to convert.
	 * @return An ArrayList of integers.
	 */
	private static ArrayList<Integer> handleJSONArray (JSONArray jsonArr) {
		ArrayList<Integer> list = new ArrayList<Integer>();
        Iterator<Long> iterator = jsonArr.iterator();
        while (iterator.hasNext()) {
            list.add(Math.toIntExact(iterator.next()));
        }
		return list;
	}
	
	/**
	 * Queries the user with a specified string message.
	 * @param query The string message to query the user with.
	 * @return An integer provided by the user.
	 */
	private int queryUser(String query) {
		String input;
		int item;
		while(true) {
			printMessage(query);
			input = scanner.nextLine();
			checkQuit(input);
			try {
				item = Integer.parseInt(input);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				printMessage("Invalid input: " + input + "\n");
				continue;
			}
			return item;
		}
	}
	
	/**
	 * Checks to see if a given input is the quit command ('q'), and exits the program if so.
	 * @param input The string to check whether it is the quit command ('q').
	 */
	private void checkQuit(String input) {
		if(input.equalsIgnoreCase("q"))
			System.exit(0);
	}
}