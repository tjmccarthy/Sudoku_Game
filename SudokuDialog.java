package edu.utep.cs.cs3331.ard.sudoku.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import edu.utep.cs.cs3331.ard.sudoku.model.Board;

/**Dialog template for playing simple Sudoku games.
 * @author		Yoonsik Cheon
 * @author		Anthony DesArmier
 * @author 		Trevor McCarthy
 * @version     1.1.1
 */
@SuppressWarnings("serial")
public class SudokuDialog extends JFrame {

    /** Default dimension of the dialog. */
    private final static Dimension DEFAULT_DIM = new Dimension(374, 540);
    
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
    
    /** Coordinates of the last area of the board painted. */
	private int[] lastPainted = {-1, -1};   

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
//      board = new Board(JsonClient.requestBoard(size, difficulty));
        board = new Board(size, difficulty);
        boardPanel = new BoardPanel(board, this::boardClicked);
        this.setJMenuBar(makeMenuBar());       
        configureUI();
        configureSound();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
        showMessage("Select an empty square and then a number to place.");
    }

	/**
     * Callback to be invoked when a square of the board is clicked.
     * @param x 0-based row index of the clicked square.
     * @param y 0-based column index of the clicked square.
     */
    private void boardClicked(int x, int y) {
    	if (board.isSolved()) return;                                                                // Current game is over.
       	if (lastPainted[0] == x && lastPainted[1] == y) return;                                      // Same square clicked.
    	if (lastPainted[0] == -1) {                                                                  // First selected.
    		board.setSelected(new int[] {x,y}); 
    	}
    	else {
    		boardPanel.repaint(lastPainted[0]*boardPanel.getSquareSize(), lastPainted[1]*boardPanel.getSquareSize(), boardPanel.getSquareSize(), boardPanel.getSquareSize());
    		board.setSelected(new int[] {x,y});					
    	} 	
    	boardPanel.repaint(x*boardPanel.getSquareSize(), y*boardPanel.getSquareSize(), boardPanel.getSquareSize(), boardPanel.getSquareSize());
    	playClick();
    	showMessage("Square at (" + x + ", " + y + ") selected.");
    	lastPainted[0] = x;
    	lastPainted[1] = y;
    }
    
    /**
     * Callback to be invoked when a number button is clicked.
     * @param number clicked number (1-9), or 0 for "X".
     */
    protected void numberClicked(int number) {
    	if (board.isSolved() || lastPainted[0] == -1) return;                                        // Input disabled between games and while no squares are active.
        board.update(new int[] {lastPainted[0], lastPainted[1], number});                        // Update board with current selection.
        boardPanel.repaint();                                                         
        playClick();
        
	if (board.isSolved()) {                                                                      // Check if new entry completed the game.
		boardPanel.repaint();
		showMessage("Congratulations on your victory!!");
		JOptionPane.showMessageDialog(null,
			    "The board has been completed!\nClick on a new game to play again.",
			    "Finished!", JOptionPane.INFORMATION_MESSAGE);
		playClick();   		
	}   
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
/*		playClick();
    	int x = JOptionPane.showConfirmDialog(null, String.format("Start a new %dx%d game?", size, size),
    			"New Game", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    	playClick();
    	if(x==0) {
    		List<Integer> levels = JsonClient.getInfo().getLevels();
    		List<Integer> sizes = new ArrayList<>();
        	sizes.add(4);
        	sizes.add(9);
            List<Integer> levels = new ArrayList<>();
            levels.add(1);
        	levels.add(2);
        	levels.add(3);
    		
    		
    		
    		int difficulty = JOptionPane.showOptionDialog(null, "Choose Difficulty", "New Game",
    				JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, levels.toArray(), levels.get(0));
    		playClick();
    		if(difficulty!=-1) {
    			difficulty = levels.get(difficulty);
    			this.dispose();
    			new SudokuDialog(DEFAULT_DIM, size, difficulty);
    		}
    	}
    }*/
		playClick();
    	int x = JOptionPane.showConfirmDialog(null, String.format("Start a new %dx%d game?", size, size),
    			"New Game", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    	playClick();
    	if(x==0) {
    		List<Integer> sizes = new ArrayList<>();
        	sizes.add(4);
        	sizes.add(9);
            List<Integer> levels = new ArrayList<>();
            levels.add(1);
        	levels.add(2);
        	levels.add(3);
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
        buttons.setBorder(BorderFactory.createEmptyBorder(20,16,0,16));
        buttons.setBackground(Color.BLACK);
        add(buttons, BorderLayout.NORTH);
        
        JPanel board = new JPanel();
        board.setBorder(BorderFactory.createEmptyBorder(20,16,0,16));
        board.setLayout(new GridLayout(1,1));
        board.add(boardPanel);
        board.setBackground(Color.BLACK);
        add(board, BorderLayout.CENTER);
        
        JPanel blank1 = new JPanel();
        blank1.setBackground(Color.BLACK);
        add(blank1, BorderLayout.WEST);
        
        JPanel blank2 = new JPanel();
        blank2.setBackground(Color.BLACK);
        add(blank2, BorderLayout.EAST);
        
        msgBar.setBorder(BorderFactory.createEmptyBorder(16,26,26,16));
        //msgBar.setBackground(Color.BLACK);
        msgBar.setForeground(Color.WHITE);
        add(msgBar, BorderLayout.SOUTH);
        getContentPane().setBackground(Color.BLACK);
    }
    
    /** 
     * Create menu bars consisting of the menu items.
     * @return configured JMenuBar.
     */
 public JMenuBar makeMenuBar() {
        JMenuBar theBar = new JMenuBar();
        JMenu menu1 = new JMenu("File");
        menu1.setMnemonic(KeyEvent.VK_F);
        menu1.getAccessibleContext().setAccessibleDescription("Game Menu");
        theBar.add(menu1);                  
        JMenuItem item = new JMenuItem("New Game", KeyEvent.VK_N);
        //item.setIcon(...));
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));
        item.getAccessibleContext().setAccessibleDescription("Play a new game");
 //       item.addActionListener(ActionListener e);
        menu1.add(item);       
        menu1.add("Check");
        menu1.add("Solve");
        menu1.add("Exit");
        
        JMenu menu2 = new JMenu("Help");
        menu2.setMnemonic(KeyEvent.VK_H);
        menu2.getAccessibleContext().setAccessibleDescription("Help Menu");
        menu2.add("How to Play");
        theBar.add(menu2);
        theBar.add(Box.createHorizontalGlue());
        
        JMenu menu3 = new JMenu("Settings");
        menu3.add("Filler For Optional Settings #1");
        menu3.add("Filler For Optional Settings #2");
        theBar.add(menu3);  
        return theBar;
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
    	newButtons.setBackground(Color.BLACK);
        JButton new4Button = new JButton("New (4x4)");
        
        for (JButton button: new JButton[] { new4Button, new JButton("New (9x9)") }) {
        	button.setFocusPainted(false);
        	button.setPreferredSize(new Dimension(120, 25));
        	button.setBackground(new Color(167, 247, 161));
            button.setToolTipText("Start a new game?");
        	
            button.addActionListener(e -> {
                newClicked(e.getSource() == new4Button ? 4 : 9);
            });
            newButtons.add(button);
    	}
    	newButtons.setAlignmentX(LEFT_ALIGNMENT);
        
    	// buttons labeled 1, 2, ..., 9, and X.
    	JPanel numberButtons = new JPanel(new FlowLayout());
    	numberButtons.setBackground(Color.BLACK);
    	
    	int maxNumber = board.getSize() + 1;
    	for (int i = 1; i <= maxNumber; i++) {
            int number = i % maxNumber;
            JButton button = new JButton(number == 0 ? "X" : String.valueOf(number));
            button.setFocusPainted(false);
            button.setBackground(new Color(167, 247, 161));
            button.setMargin(new Insets(0,5,0,5));
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
    	/** Sudoku game board sizes. */
    	List<Integer> sizes = new ArrayList<>();
    	sizes.add(4);
    	sizes.add(9);
    		
    	/** Sudoku game board levels. */
        List<Integer> levels = new ArrayList<>();
        levels.add(1);
    	levels.add(2);
    	levels.add(3);
    	
 //   	JsonInfo info = JsonClient.getInfo();
 //   	List<Integer> sizes = info.getSizes();
    	int size = JOptionPane.showOptionDialog(null, "Choose Board Size: ", "New Game",
				JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, sizes.toArray(), sizes.get(0));
    	System.out.println("sizes.get(size)" + sizes.get(size) );
    	if(size==-1)
    		System.exit(0);
    	size = sizes.get(size);
//    	List<Integer> levels = info.getLevels();
    	int difficulty = JOptionPane.showOptionDialog(null, "Choose Difficulty", "New Game",
				JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, levels.toArray(), levels.get(0));
    	if(difficulty==-1)
    		System.exit(0);
    	difficulty = levels.get(difficulty);
        new SudokuDialog(size, difficulty);
    }
}
