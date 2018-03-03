package edu.utep.cs.cs3331.ard.sudoku.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import edu.utep.cs.cs3331.ard.sudoku.model.Board;
import edu.utep.cs.cs3331.ard.sudoku.net.JSONInfo;
import edu.utep.cs.cs3331.ard.sudoku.net.JacksonClient;

/**
 * Dialog template for playing simple Sudoku games.
 *
 * @author		Yoonsik Cheon
 * @author		Anthony DesArmier
 * @author 		Trevor McCarthy
 * @version     1.1.1
 * @since       1.1
 */
@SuppressWarnings("serial")
public class SudokuDialog extends JFrame {

    /** Default dimension of the dialog. */
    private final static Dimension DEFAULT_DIM = new Dimension(310, 430);
    
    /** Default size of the Sudoku game board. */
    private final static int DEFAULT_SIZE = 9;
    
    /** Default difficulty of the Sudoku game board. */
    private final static int DEFAULT_DIFFICULTY = 1;

    /** Relative path to the resource directory. */
    private final static String RES_DIR = "/";
    
    /** Click clip to be used on the panel. */
    private Clip clip;
    
    /** Sudoku board. */
    private Board board;

    /** Special panel to display a Sudoku board. */
    private BoardPanel boardPanel;

    /** Message bar to display various messages. */
    private JLabel msgBar = new JLabel("");

    /** Last number value selected. */
	private int lastValue = 0;

    /** Create a new dialog with default values. */
    public SudokuDialog() {
    	this(DEFAULT_DIM, DEFAULT_SIZE, DEFAULT_DIFFICULTY);
    }
    
    /** Create a new dialog with the default screen dimensions.
     * @param size Sudoku game board size.
     * @param difficulty Sudoku game difficulty.
     */
    public SudokuDialog(int size, int difficulty) {
    	this(DEFAULT_DIM, size, difficulty);
    }
    
    /** Create a new dialog.
     * @param dim dialog dimension.
     * @param size Sudoku game board size.
     * @param difficulty Sudoku game difficulty.
     */
    public SudokuDialog(Dimension dim, int size, int difficulty) {
        super("Sudoku");
        setSize(dim);
        board = new Board(JacksonClient.requestBoard(size, difficulty));
        boardPanel = new BoardPanel(board, this::boardClicked);
        configureUI();
        configureSound();
        //setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        //setResizable(false);
        showMessage("Selected Number: 0");
    }

	/**
     * Callback to be invoked when a square of the board is clicked.
     * @param x 0-based row index of the clicked square.
     * @param y 0-based column index of the clicked square.
     */
    private void boardClicked(int x, int y) {
    	//showMessage(String.format("Board clicked: x = %d, y = %d",  x, y));
		playClick();
    	board.update(new int[] {x,y,lastValue});
    	boardPanel.repaint();
    	if(board.isSolved()) {
    		JOptionPane.showMessageDialog(null,
    			    "The board has been completed!\nClick on a new game to play again.",
    			    "Finished!", JOptionPane.INFORMATION_MESSAGE);
    		playClick();
    	}
    }
    
    /**
     * Callback to be invoked when a number button is clicked.
     * @param number clicked number (1-9), or 0 for "X".
     */
    private void numberClicked(int number) {
		playClick();
        showMessage("Selected Number: " + number);
        lastValue = number;
    }
    
    /**
     * Callback to be invoked when a new button is clicked.
     * If the current game is over, start a new game of the given size;
     * otherwise, prompt the user for a confirmation and then proceed
     * accordingly.
     * @param size requested puzzle size, either 4 or 9.
     */
    private void newClicked(int size) {
        //showMessage("New clicked: " + size);
		playClick();
    	int x = JOptionPane.showConfirmDialog(null, String.format("Start a new %dx%d game?", size, size),
    			"New Game", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    	playClick();
    	if(x==0) {
    		List<Integer> levels = JacksonClient.getInfo().getLevels();
    		int difficulty = JOptionPane.showOptionDialog(null, "Choose Difficulty", "New Game",
    				JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, levels.toArray(), levels.get(0));
    		playClick();
    		if(difficulty!=-1) {
    			difficulty = levels.get(difficulty);
    			this.dispose();
    			new SudokuDialog(DEFAULT_DIM, size, difficulty);
    		}
    	}
    }

    /**
     * Display the given string in the message bar.
     * @param msg message to be displayed.
     */
    private void showMessage(String msg) {
        msgBar.setText(msg);
    }

    /** Configure the UI. */
    private void configureUI() {
        setIconImage(createImageIcon("sudoku.png").getImage());
        setLayout(new BorderLayout());
        
        JPanel buttons = makeControlPanel();
        // border: top, left, bottom, right
        buttons.setBorder(BorderFactory.createEmptyBorder(10,16,0,16));
        add(buttons, BorderLayout.NORTH);
        
        JPanel board = new JPanel();
        board.setBorder(BorderFactory.createEmptyBorder(10,16,0,16));
        board.setLayout(new GridLayout(1,1));
        board.add(boardPanel);
        add(board, BorderLayout.CENTER);
        
        msgBar.setBorder(BorderFactory.createEmptyBorder(10,16,10,0));
        add(msgBar, BorderLayout.SOUTH);
    }
    
    /** Configures sound clips. */
    private void configureSound() {
    	URL soundURL = getClass().getResource(RES_DIR + "click.wav");
    	try {
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL);
			clip = AudioSystem.getClip();
			clip.open(audioIn);
		} catch (UnsupportedAudioFileException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (LineUnavailableException e1) {
			e1.printStackTrace();
		}
	}
    
    /** Plays a specific sound file. */
    private void playClick() {
    	if (clip.isRunning())
    		clip.stop();
    	clip.setFramePosition(0);
    	clip.start();
    }
      
    /** 
     * Create a control panel consisting of new and number buttons.
     * @return configured JPanel.
     */
    private JPanel makeControlPanel() {
    	JPanel newButtons = new JPanel(new FlowLayout());
        JButton new4Button = new JButton("New (4x4)");
        for (JButton button: new JButton[] { new4Button, new JButton("New (9x9)") }) {
        	button.setFocusPainted(false);
            button.addActionListener(e -> {
                newClicked(e.getSource() == new4Button ? 4 : 9);
            });
            newButtons.add(button);
    	}
    	newButtons.setAlignmentX(LEFT_ALIGNMENT);
        
    	// buttons labeled 1, 2, ..., 9, and X.
    	JPanel numberButtons = new JPanel(new FlowLayout());
    	int maxNumber = board.getSize() + 1;
    	for (int i = 1; i <= maxNumber; i++) {
            int number = i % maxNumber;
            JButton button = new JButton(number == 0 ? "X" : String.valueOf(number));
            button.setFocusPainted(false);
            button.setMargin(new Insets(0,2,0,2));
            button.addActionListener(e -> numberClicked(number));
    		numberButtons.add(button);
    	}
    	numberButtons.setAlignmentX(LEFT_ALIGNMENT);

    	JPanel content = new JPanel();
    	content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
        content.add(newButtons);
        content.add(numberButtons);
        return content;
    }

    /**
     * Create an image icon from the given image file.
     * @return configured ImageIcon.
     */
    private ImageIcon createImageIcon(String filename) {
        URL imageUrl = getClass().getResource(RES_DIR + filename);
        if (imageUrl != null) {
            return new ImageIcon(imageUrl);
        }
        return null;
    }

    /**
     * Queries the user for a Sudoku game board size and difficulty, 
     * then constructs and handles the Sudoku board game.
     * @param args not used.
     */
    public static void main(String[] args) {
    	JSONInfo info = JacksonClient.getInfo();
    	List<Integer> sizes = info.getSizes();
    	int size = JOptionPane.showOptionDialog(null, "Choose Board Size: ", "New Game",
				JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, sizes.toArray(), sizes.get(0));
    	if(size==-1)
    		System.exit(0);
    	size = sizes.get(size);
    	List<Integer> levels = info.getLevels();
    	int difficulty = JOptionPane.showOptionDialog(null, "Choose Difficulty", "New Game",
				JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, levels.toArray(), levels.get(0));
    	if(difficulty==-1)
    		System.exit(0);
    	difficulty = levels.get(difficulty);
        new SudokuDialog(size, difficulty);
    }
}
