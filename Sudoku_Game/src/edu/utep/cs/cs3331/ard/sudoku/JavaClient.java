package edu.utep.cs.cs3331.ard.sudoku;

import java.net.URL;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

/**
 * @author      Anthony DesArmier ardesarmier@miners.utep.edu
 * @version     1.0
 * @since       1.0
 */
public class JavaClient {

	/**
	 * Retrieves a JSON object supplied by a Sudoku Web Service API that provides various information.
	 * <p>
	 * See <a href="http://www.cs.utep.edu/cheon/ws/sudoku/info/">http://www.cs.utep.edu/cheon/ws/sudoku/info/</a>
	 * @return A JSON object containing various Sudoku Web Service API information.
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ParseException
	 */
	public static JSONObject getInfo() throws MalformedURLException, IOException, ParseException {
		String url = "http://www.cs.utep.edu/cheon/ws/sudoku/info/";
		JSONObject response = new JavaClient().sendGet(url);
		return response;
	}
	
	/**
	 * Retrieves a JSON object supplied by a Sudoku Web Service API that provides a Sudoku game board based on given parameters.
	 * @param size The dimension of the desired Sudoku board.
	 * @param level The difficulty level of the desired Sudoku board.
	 * @return A JSON Object containing information representing a Sudoku game board.
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ParseException
	 */
	public static JSONObject requestBoard(int size, int level) throws MalformedURLException, IOException, ParseException {
		String url = String.format("http://www.cs.utep.edu/cheon/ws/sudoku/new/?size=%d&level=%d", size, level);
		JSONObject response = new JavaClient().sendGet(url);
		return response;
	}

	/**
	 * Connects to a HttpURL and retrieves a JSON object from it.
	 * @param urlString The URL to connect to.
	 * @return The JSON object supplied by the HttpURL.
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ParseException
	 */
	private JSONObject sendGet (String urlString) throws MalformedURLException, IOException, ParseException {
		HttpURLConnection myURLConnection = null;
		URL url = new URL(urlString);
		myURLConnection = (HttpURLConnection) url.openConnection();
		myURLConnection.connect();
		JSONParser parser = new JSONParser();
		JSONObject response = (JSONObject) parser.parse(new BufferedReader(new InputStreamReader(myURLConnection.getInputStream())));
		if (myURLConnection != null)
			myURLConnection.disconnect();
		return response;
	}
	
}
