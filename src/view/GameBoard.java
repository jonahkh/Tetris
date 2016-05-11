/*
 * TCSS 305 
 * Assignment 6 - Tetris
 */
package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import model.AbstractPiece;
import model.Block;
import model.Board;
import model.Piece;

/**
 * This class represents the panel that holds the visual display for the Tetris game board.
 * 
 * @author Jonah Howard
 * @version 23 November 2015
 */
public final class GameBoard extends JPanel {

    /** Number of blocks in a piece. */
    public static final int BLOCKS = 4;
    
    /** The current width of the game. */
    public static final int WIDTH = 20;

    /** A generated serial version UID. */
    private static final long serialVersionUID = 7943648261879456841L;

    /** The current size of the game board. */
    private static final Dimension BOARD_SIZE = new Dimension(201, 401);

    /** Contains the respective colors for the blocks. */
    private static final Map<Block, Color> COLORS = new HashMap<Block, Color>();
    static {
        COLORS.put(Block.I, Color.CYAN);
        COLORS.put(Block.J, Color.RED);
        COLORS.put(Block.L, Color.GREEN);
        COLORS.put(Block.O, Color.YELLOW);
        COLORS.put(Block.S, Color.ORANGE);
        COLORS.put(Block.T, Color.MAGENTA);
        COLORS.put(Block.Z, Color.BLUE);
    }
    
    
    /** The current piece. */
    private Piece myCurrentPiece;
    
    /** The current board. */
    private final Board myBoard;
    
    /**
     * Initializes a new game board.
     * 
     * @param theBoard The current board for this Tetris game
     */
    public GameBoard(final Board theBoard) {
        super(true);
        setBackground(Color.WHITE);
        setSize(BOARD_SIZE);
        setPreferredSize(BOARD_SIZE);
        setMaximumSize(BOARD_SIZE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        myBoard = theBoard;
    }
    
    @Override
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        final Graphics2D g2d = (Graphics2D) theGraphics;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
        final List<Block[]> frozenBlocks = myBoard.getFrozenBlocks();
        // Paint frozen blocks
        for (int rows = 0; rows < frozenBlocks.size(); rows++) {
            for (int columns = 0; columns < frozenBlocks.get(rows).length; columns++) {
                if (frozenBlocks.get(rows)[columns] != Block.EMPTY) {
                    paintBlock(g2d, frozenBlocks.get(rows)[columns], columns, rows);
                }
            }
        }
        // Paint current piece
        if (myCurrentPiece != null) {
            final int[][] piece = ((AbstractPiece) myCurrentPiece).getBoardCoordinates();
            for (int i = 0; i < BLOCKS; i++) {
                paintBlock(g2d, ((AbstractPiece) myCurrentPiece).getBlock(), 
                           piece[i][0], piece[i][1]);
            }
        }
    }
    
    /**
     * Paints the passed block at the passed x and y coordinates using the passed graphics.
     * 
     * @param theGraphics the graphics to draw to
     * @param theBlock the current block being drawn
     * @param theX the x coordinate of the block being drawn
     * @param theY the y coordinate of the block being drawn
     */
    private void paintBlock(final Graphics2D theGraphics, final Block theBlock,
                            final int theX, final int theY) {
        theGraphics.setColor(getBlockColor(theBlock));
        theGraphics.fillRect(theX * WIDTH, WIDTH * (WIDTH - theY) - WIDTH, WIDTH, WIDTH);
        theGraphics.setColor(Color.BLACK);
        theGraphics.drawRect(theX * WIDTH, WIDTH * (WIDTH - theY) - WIDTH, WIDTH, WIDTH);
    }
    
    /**
     * Returns the color of the passed block.
     * 
     * @param theBlock the passed block
     * @return the color of the passed block
     */
    protected static Color getBlockColor(final Block theBlock) {
        return COLORS.get(theBlock);
        
    }
        
    /**
     * Updates the current piece.
     * 
     * @param thePiece the current piece
     */
    protected void setCurrentPiece(final Piece thePiece) {
        myCurrentPiece = thePiece;
    }
}
