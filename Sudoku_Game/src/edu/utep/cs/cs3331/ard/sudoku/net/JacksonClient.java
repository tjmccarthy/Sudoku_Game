package edu.utep.cs.cs3331.ard.sudoku.net;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Static helper methods to retrieve and parse information provided by a Sudoku Web Service API.
 * <p>
 * See <a href="http://www.cs.utep.edu/cheon/ws/sudoku/">http://www.cs.utep.edu/cheon/ws/sudoku/</a>
 * Occasionally the API will hang on size 9 difficulty 3.
 * 
 * @author      Anthony DesArmier
 * @version     1.1
 * @since       1.1
 */
public class JacksonClient {
	
	/** An ObjectMapper for processing JSON fields in Jackson. */
	private static ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * Retrieves and parses a JSON object supplied by a Sudoku Web Service API that provides various information.
	 * @return An JSONInfo containing various Sudoku Web Service API information.
	 */
	public static JSONInfo getInfo() {
		URL url;
		JSONInfo info = null;
		try {
			url = new URL("http://www.cs.utep.edu/cheon/ws/sudoku/info/");
			info = mapper.readValue(url, JSONInfo.class);
		} catch (MalformedURLException e) {
			printError("Malformed URL.\n", e);
		} catch (JsonParseException e) {
			printError("Invalid JSON data recieved from server.\n", e);
		} catch (JsonMappingException e) {
			printError("Invalid JSON data recieved from server.\n", e);
		} catch (IOException e) {
			printError("Failed to connect to server.\n", e);
		}
		return info;
	}
	
	/**
	 * Retrieves and parses a JSON object supplied by a Sudoku Web Service API that provides a Sudoku game board based on given parameters.
	 * @param size The dimension of the desired Sudoku board.
	 * @param level The difficulty level of the desired Sudoku board.
	 * @return A JSONBoard containing information representing a Sudoku game board.
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * @throws IOException
	 */
	public static JSONBoard requestBoard(int size, int level) {
		URL url;
		JSONBoard board = null;
		try {
			url = new URL(String.format("http://www.cs.utep.edu/cheon/ws/sudoku/new/?size=%d&level=%d", size, level));
			board = mapper.readValue(url, JSONBoard.class);
		} catch (MalformedURLException e) {
			printError("Malformed URL.\n", e);
		} catch (JsonParseException e) {
			printError("Invalid JSON data recieved from server.\n", e);
		} catch (JsonMappingException e) {
			printError("Invalid JSON data recieved from server.\n", e);
		} catch (IOException e) {
			printError("Failed to connect to server.\n", e);
		}
		return board;
	}
	
    /**
     * Prints a message and stack trace to an error log.
     * 
     * @param message relevant message to be printed.
     * @param e exception whose stacktrace is to be printed.
     */
    private static void printError(String message, Exception e) {
    	PrintStream out = null;
		try {
			out = new PrintStream(new FileOutputStream("errorlog.txt"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
    	System.setOut(out);
    	System.setErr(out);
    	System.out.println(new java.util.Date() +":"+ message);
    	e.printStackTrace(out);
    	out.close();
    	System.exit(0);
    }
	
}
