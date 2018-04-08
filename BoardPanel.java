package edu.utep.cs.cs3331.ard.sudoku.dialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import edu.utep.cs.cs3331.ard.sudoku.model.Board;
import edu.utep.cs.cs3331.ard.sudoku.model.Square;
import edu.utep.cs.cs3331.ard.sudoku.model.Square.State;

/**
 * A special panel class to display a Sudoku board modeled by the
 * {@link edu.utep.cs.cs3331.ard.sudoku.model.Board} class.
 *
 * @author		Anthony DesArmier
 * @author 		Trevor McCarthy
 * @author		Yoonsik Cheon
 * @version     1.2
 */
@SuppressWarnings("serial")
public class BoardPanel extends JPanel {
    
	public interface ClickListener {
		
		/** Callback to notify clicking of a square. 
		 * 
		 * @param x 0-based column index of the clicked square.
		 * @param y 0-based row index of the clicked square.
		 */
		void clicked(int x, int y);
	}
	
	/** Board background color. */
	private static final Color BOARD_COLOR = new Color(167, 247, 161);
	/** Fixed square color. */
	private static final Color FIXED_COLOR = new Color(191, 191, 129);
	/** Error square color. */
	private static final Color ERROR_COLOR = Color.RED;
	/** Selected square color. */
	private static final Color SELECT_COLOR = new Color(229, 255, 204);
	/** Completed board color. */
	private static final Color WIN_COLOR = Color.GREEN;
	/** Font of the 4x4 board text. */
	private static final Font BOARD_NUMBER_4 = new Font("Monospaced", Font.BOLD, 25);
	/** Font of the 9x9 board text. */
	private static final Font BOARD_NUMBER_9 = new Font("Monospaced", Font.BOLD, 15);


    /** Board to be displayed. */
    private Board board;

    /** Width and height of a square in pixels. */
    private int squareSize;

	/** Create a new board panel to display the given board. */
    public BoardPanel(Board board, ClickListener listener) {
        this.board = board;
        addMouseListener(new MouseAdapter() {
            @Override
			public void mouseClicked(MouseEvent e) {
            	int xy = locateSquare(e.getX(), e.getY());
            	if (xy >= 0) {
            		listener.clicked(xy / 100, xy % 100);
            	}
            }
        });
    }
    
    /**
	 * Getter for {@link #squareSize}.
	 * @return {@link #squareSize}
	 */
    public int getSquareSize() {
		return squareSize;
	}

    /**
	 * Setter for {@link #board}.
	 */
    public void setBoard(Board board) {
    	this.board = board;
    }
    
    /**
     * Given a screen coordinate, return the indexes of the corresponding square
     * or -1 if there is no square.
     * The indexes are encoded and returned as x*100 + y, 
     * where x and y are 0-based column/row indexes.
     * 
     * @return index of the corresponding square.
     */
    private int locateSquare(int x, int y) {
    	if (x < 0 || x > board.getSize() * squareSize
    			|| y < 0 || y > board.getSize() * squareSize) {
    		return -1;
    	}
    	int xx = x / squareSize;
    	int yy = y / squareSize;
    	return xx * 100 + yy;
    }

    /** Draw the associated board. */
    @Override
    public void paint(Graphics g) {
        super.paint(g); 
        Dimension dim = getSize();
        squareSize = Math.min(dim.width, dim.height) / board.getSize();

     // Draw the background.
        if(board.isSolved())
        	g.setColor(WIN_COLOR);
        else
        	g.setColor(BOARD_COLOR);
        g.fillRect(0, 0, squareSize * board.getSize(), squareSize * board.getSize());
        
        // Fill the board's squares.
        int[] lastIndex = board.getLastSelected();
        if(lastIndex[0]!=-1) {
        	g.setColor(SELECT_COLOR);
    		g.fillRect(lastIndex[0]*squareSize, lastIndex[1]*squareSize, squareSize, squareSize);
        }
        int x=0, y=0;
        for(Square square : board.getGrid()) {
        	if(square.getState().contains(State.FIXED)) {
        		g.setColor(FIXED_COLOR);
        		g.fillRect(x, y, squareSize, squareSize);
        	}
        	if(square.getState().contains(State.ERROR)) {
        		g.setColor(ERROR_COLOR);
        		g.fillOval(x+squareSize/4, y+squareSize/4,
	        			squareSize/2, squareSize/2);
        	}
        	y += squareSize;
        	if(y==board.getSize()*squareSize) {
        		y = 0;
        		x += squareSize;
        	}
        		
        }     
	    g.setColor(Color.BLACK);                                                            // Fill in numbers with font sizes specific to board size. 
	    FontMetrics metrics;
	    if (board.getSize() == 4) {
        g.setFont(BOARD_NUMBER_4);
        metrics = g.getFontMetrics(BOARD_NUMBER_4);
	    }
	    else {
	        g.setFont(BOARD_NUMBER_9);
	        metrics = g.getFontMetrics(BOARD_NUMBER_9);	    	
	    }             
        int width = metrics.stringWidth("0")/2;
        int height = metrics.getDescent();
        int value;
        for(int i=0; i<board.getSize(); i++) {
        	for(int j=0; j<board.getSize(); j++) {
        		value = board.getValue(i, j);
        		if(value!=0)
        			g.drawString(String.valueOf(value), (i+1)*(squareSize)-squareSize/2-width, (j+1)*(squareSize)-squareSize/2+height);
        	}
        }      
        g.setColor(new Color(183, 215, 147));                                               // Draw the grid and sub-grids.
        int size = board.getSize();
        int subGrid = board.getCellDim();
        for(int i = 0; i<size-1; i++) {
        	g.drawLine(squareSize*(i+1), 0, squareSize*(i+1), squareSize*size);             // columns
        	g.drawLine(0, squareSize*(i+1), squareSize*size, squareSize*(i+1));             // rows
        }
        g.setColor(Color.BLACK);
        for(int i=subGrid-1; i<size-1; i+=subGrid) {
    		g.drawLine(squareSize*(i+1), 0, squareSize*(i+1), squareSize*size);             // columns
    		g.drawLine(0, squareSize*(i+1), squareSize*size, squareSize*(i+1));             // rows
        }        
    }
}