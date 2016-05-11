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

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import model.AbstractPiece;
import model.Board;

/**
 * This class represents the JPanel that shows the next piece for the current game.
 * 
 * @author Jonah Howard
 * @version 24 November 2015
 */
public final class NextPiecePanel extends JPanel {

    /** The generated serial version UID. */
    private static final long serialVersionUID = -2805804895730166250L;
    
    /** The preferred size for this panel. */
    private static final Dimension PREFERRED_SIZE = new Dimension(100, 100);
    
    /** The maximum size for this panel. */
    private static final Dimension MAXIMUM_SIZE = new Dimension(200, 100);
    
    /** Horizontal offset for the next piece. */
    private static final int HORIZONTAL_OFFSET = 15;
    
    /** Vertical offset for the next piece. */
    private static final int VERTICAL_OFFSET = 75;
    
    /** The current board for this game. */
    private final Board myBoard;
    
    /**
     * Initialize a new NextPiecePanel.
     * 
     * @param theBoard the current board
     */
    public NextPiecePanel(final Board theBoard) {
        super();
        myBoard = theBoard;
        setUpPanel();
    }
    
    /**
     * Set up the panel.
     */
    private void setUpPanel() {
        setPreferredSize(PREFERRED_SIZE);
        setMaximumSize(MAXIMUM_SIZE);
        setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }
    
    @Override
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        final Graphics2D g2d = (Graphics2D) theGraphics;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        final int[][] nextPiece = ((AbstractPiece) myBoard.getNextPiece()).getRotation();
        // Paint the next piece
        for (int i = 0; i < GameBoard.BLOCKS; i++) {
            g2d.setColor(GameBoard.getBlockColor(
                        ((AbstractPiece) myBoard.getNextPiece()).getBlock()));
            g2d.fillRect(nextPiece[i][0] * GameBoard.WIDTH + HORIZONTAL_OFFSET, 
                         VERTICAL_OFFSET - (nextPiece[i][1] * GameBoard.WIDTH), 
                         GameBoard.WIDTH, GameBoard.WIDTH);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(nextPiece[i][0] * GameBoard.WIDTH + HORIZONTAL_OFFSET, 
                         VERTICAL_OFFSET - (nextPiece[i][1] * GameBoard.WIDTH), 
                         GameBoard.WIDTH, GameBoard.WIDTH);
        }
    }
}
