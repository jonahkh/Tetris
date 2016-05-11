/*
 * TCSS 305 Assignment 6 - Tetris
 */
package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Board;

/**
 * This class represents the panel that displays the next piece, shows scoring information,
 * and shows the current controls.
 * 
 * @author Jonah Howard
 * @version 24 November 2015
 */
public final class EastPanel extends JPanel implements Observer {
    
    /** Represents the spacing between panels. */
    public static final int GAP = 65;

    /** A generated serial version id. */
    private static final long serialVersionUID = -6995958782741712503L;

    /** The current layout for this panel. */
    private final BoxLayout myBoxLayout;
    
    /** The current JFrame for this game. */
    private final GUI myFrame;
    
    /** The current board for this game. */
    private final Board myBoard;
    
    /** The current scoring panel for this game. */
    private final ScorePanel myScorePanel;
    
    /**
     * Initializes a new EastPanel.
     * 
     * @param theFrame the current frame for the game
     * @param theBoard the current board for the game
     * @param theScorePanel the current scoring panel for this game
     */
    public EastPanel(final GUI theFrame, final Board theBoard, 
                     final ScorePanel theScorePanel) {
        super();
        myBoxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        myFrame = theFrame;
        myBoard = theBoard;
        myScorePanel = theScorePanel;
        setUpComponents();
    }

    /**
     * Set up the components for the east panel.
     */
    private void setUpComponents() {
        myBoard.addObserver(this);
        setBackground(Color.WHITE);
        setLayout(myBoxLayout);
//        add(Box.createVerticalStut(15));
//        add(Box.createVerticalGlue());
        addNextPiecePanel();
        add(Box.createHorizontalGlue());
        add(Box.createVerticalStrut(GAP));
//        add(Box.createVerticalGlue());
        addScorePanel();
        add(Box.createVerticalStrut(GAP));
//        add(Box.createVerticalGlue());
        addControlsPanel();
//        add(Box.createVerticalStrut(GAP));
//        add(Box.createVerticalGlue());
    }
    
    /**
     * Add the panel that displays the next piece.
     */
    private void addNextPiecePanel() {
        final NextPiecePanel panel = new NextPiecePanel(myBoard);
        add(panel);
    }
    
    /**
     * Add the panel that displays the current scoring information.
     */
    private void addScorePanel() {
        add(myScorePanel);
        myScorePanel.repaint();
    }
    
    /**
     * Add the panel that displays the current controls.
     */
    private void addControlsPanel() {
        final ControlsPanel panel = new ControlsPanel();
        add(panel);
        panel.repaint();
    }

    
    @Override
    public void update(final Observable theOberver, final Object theObject) {
        repaint();        
        if ("reset game".equals(theObject)) {
            myScorePanel.resetValues();
        }
    }
    
    // Inner classes
    /**
     * This class represents the controls panel. It displays the current controls and their 
     * respective keys.
     */
    private final class ControlsPanel extends JPanel {
        /** A generated serial version UID. */
        private static final long serialVersionUID = 3009714352711514580L;
        
        /** Stores the labels for all of the controls. */
        private final Map<String, JLabel> myLabels;
        
        /**
         * Initialize a new ControlsPanel.
         */
        protected ControlsPanel() {
            super();
            myLabels = new LinkedHashMap<String, JLabel>();
            setUpComponents();
        }
        
        /**
         * Set up the components.
         */
        private void setUpComponents() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(Color.BLACK));
            for (final String current : myFrame.getControls().keySet()) {
                final JLabel newLabel = new JLabel(current + ": %-10s" 
                                + KeyEvent.getKeyText(myFrame.getControls().get(current)));
                myLabels.put(current, newLabel);
                add(newLabel);
            }
        }
        
        @Override
        public void paintComponent(final Graphics theGraphics) {
            super.paintComponent(theGraphics);
            final Graphics2D g2d = (Graphics2D) theGraphics;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                 RenderingHints.VALUE_ANTIALIAS_ON);
            for (final String current : myFrame.getControls().keySet()) {
                myLabels.get(current).setText(current + ": " 
                                + KeyEvent.getKeyText(myFrame.getControls().get(current)));
            }
        }
    }
}
