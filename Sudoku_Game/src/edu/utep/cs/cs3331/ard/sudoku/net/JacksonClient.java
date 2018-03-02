package edu.utep.cs.cs3331.ard.sudoku.net;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

/**
 * Retrieve and parse information provided by a Sudoku Web Service API.
 * <p>
 * See <a href="http://www.cs.utep.edu/cheon/ws/sudoku/">http://www.cs.utep.edu/cheon/ws/sudoku/</a>
 * Occasionally the API will hang on size 9 difficulty 3.
 * 
 * @author      Anthony DesArmier
 * @version     1.2
 * @since       1.1
 */
public class JacksonClient {
	
	/**
	 * Retrieves and parses a JSON object supplied by a Sudoku Web Service API that provides various information.
	 * @return JSONInfo containing various Sudoku Web Service API information.
	 * @see JSONInfo
	 */
	public static JSONInfo getInfo() {
		URL url;
		JsonObject info = null;
		try {
			url = new URL("http://www.cs.utep.edu/cheon/ws/sudoku/info/");
			InputStreamReader in = new InputStreamReader(url.openStream());
			info = Json.parse(in).asObject();
		} catch (MalformedURLException e) {
			printError("Malformed URL.\n", e);
		} catch (IOException e) {
			printError("Failed to connect to server.\n", e);
		}
		List<Integer> list = new ArrayList<>();
		JSONInfo jsonInfo = new JSONInfo();
		JsonArray jsonList;
		jsonList = info.get("sizes").asArray();
		for(JsonValue size:jsonList)
			list.add(size.asInt());
		jsonInfo.setSizes(list);
		list.clear();
		jsonList = info.get("levels").asArray();
		for(JsonValue level:jsonList)
			list.add(level.asInt());
		jsonInfo.setLevels(list);
		jsonInfo.setDefaultSize(info.getInt("defaultSize", -1));
		jsonInfo.setDefaultLevel(info.getInt("defaultLevel", -1));
		return jsonInfo;
	}
	
	/**
	 * Retrieves and parses a JSON object supplied by a Sudoku Web Service API that provides a Sudoku game board based on given parameters.
	 * @param size dimension of the desired Sudoku board.
	 * @param level difficulty level of the desired Sudoku board.
	 * @return JSONBoard containing information representing a Sudoku game board.
	 * @see JSONBoard
	 */
	public static JSONBoard requestBoard(int size, int level) {
		URL url;
		JsonObject board = null;
		try {
			url = new URL(String.format("http://www.cs.utep.edu/cheon/ws/sudoku/new/?size=%d&level=%d", size, level));
			InputStreamReader in = new InputStreamReader(url.openStream());
			board = Json.parse(in).asObject();
		} catch (MalformedURLException e) {
			printError("Malformed URL.\n", e);
		} catch (IOException e) {
			printError("Failed to connect to server.\n", e);
		}
		JSONBoard jsonBoard = new JSONBoard();
		JsonArray jsonList;
		boolean response = board.getBoolean("response", false);
		if(!response)
			printError("API did not generate request.\n", new Exception());
		jsonBoard.setResponse(response);
		jsonBoard.setSize(Integer.valueOf(board.getString("size", "-1"))); // API returns size as a String
		jsonList = board.get("squares").asArray();
		for(JsonValue square:jsonList) {
			int x = square.asObject().getInt("x", -1);
			int y = square.asObject().getInt("y", -1);
			int value = square.asObject().getInt("value", -1);
			jsonBoard.addSquare(x, y, value);
		}
		return jsonBoard;
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
