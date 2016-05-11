/*
 * TCSS 305 
 * Assignment 6 - Tetris
 */

package view;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JPanel;

/**
 * This class creates the grid. The width of the grid lines is adjustable.
 * 
 * @author Jonah Howard
 * @version 9 December 2015
 *
 */
public final class GridAction extends AbstractAction {

    /** The width of the grid lines. */ 
    private static final int GRID_SIZE = GameBoard.WIDTH;
    
    /** A generated serialization ID. */
    private static final long serialVersionUID = -6344110433836816369L;

    /** Keeps track of if the grid check box has been selected. */
    private int myCounter;
    
    /** Represents the drawing panel. */
    private JPanel myPanel;
    
    /** The panel for the grid. */
    private JPanel myGridPanel;
    
    /**
     * Initializes a new GridAction.
     * 
     * @param thePanel the current drawing panel
     */
    public GridAction(final JPanel thePanel) {
        super("Grid");
        myPanel = thePanel;
        myCounter = 0;
    }
    
    // Draws a grid based on the size of the drawing panel
    @Override
    public void actionPerformed(final ActionEvent theEvent) {
        if (myCounter % 2 == 0) {   // Turn on the grid
            /** {@inheritDoc} */
            class Grid extends JPanel {
                
                /** A generated serialization ID. */
                private static final long serialVersionUID = 6809270684237916045L;
                
                /** Initializes a new Grid. */
                public Grid() {
                    super();
                    setSize(myPanel.getSize());
                    setOpaque(false);  
                }
                @Override
                public void paintComponent(final Graphics theGraphics) {
                    setSize(myPanel.getSize());
                    setLocation(myPanel.getLocation());
                    super.paintComponent(theGraphics);
                    final Graphics2D g2d = (Graphics2D) theGraphics;
                    g2d.setStroke(new BasicStroke(1));
                    // Draw the vertical lines
                    for (int i = 0; i < myPanel.getWidth(); i += GRID_SIZE) {
                        g2d.drawLine(i, 0, i, myPanel.getHeight());
                    }
                    // Draw the horizontal lines
                    for (int i = 0; i < myPanel.getHeight(); i += GRID_SIZE) {
                        g2d.drawLine(0, i, myPanel.getWidth(), i);
                    }
                }
            }
            myGridPanel = new Grid();
            myPanel.add(myGridPanel);
        } else {    // Turn off the grid
            myPanel.remove(myGridPanel);
        }
        myPanel.repaint();
        myCounter++;
    }
}