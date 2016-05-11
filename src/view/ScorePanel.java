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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Board;

/**
 * This class stores and calculates information regarding the scoring.
 * 
 * @author Jonah Howard
 * @version 25 November 2015
 */
public final class ScorePanel extends JPanel implements Observer {
    
    /** How much the timer is adjusted per level up. */
    public static final double TIMER_OFFSET = 0.9;  // For grading, .9 should be default value
    
    /** A generated serial version UID. */
    private static final long serialVersionUID = -7189622300297775065L;

    /** The number of lines needed to progress to level 2. */
    private static final int INITIAL_LINES = 10; //For grading, 15 should be default vale
    
    /** The score level increase factor. */
    private static final int LEVEL_UP_FACTOR = 40;
    
    /** The score increment factor. */
    private static final int INCREMENT_FACTOR = 5;
    
    /** Refers to the score label. */
    private static final String SCORE = "Score ";
    
    /** Refers to the level label. */
    private static final String LEVEL = "Level ";
    
    /** Refers to the lines cleared label. */
    private static final String LINES = "Lines Cleared ";
    
    /** Refers to the lines left to next level label. */
    private static final String LINES_LEFT = "Lines Until Next Level ";
    
    /** The current score. */
    private int myScore;
    
    /** The current level. */
    private int myLevel;
    
    /** The count of how many lines have been cleared. */
    private int myLines;
    
    /** The number of lines needed to clear until a level up. */
    private int myLinesToNextLevel;
    
    /** Holds the scoring JLabels and their names. */
    private final Map<String, JLabel> myScores; 
    
    /** The current board. */
    private final Board myBoard;
    
    /** The current frame for this game. */
    private final GUI myFrame;
    
    /** The current sound player for this game. */
    private final SoundPlayer myPlayer;
    
    private final JLabel myScoreLabel;
    
    
    /**
     * Initializes a new Score Panel.
     * 
     * @param theBoard the current board
     * @param theFrame the current frame
     * @param thePlayer the sound player for this game
     */
    protected ScorePanel(final Board theBoard, final GUI theFrame, 
                         final SoundPlayer thePlayer) {
        super();
        myBoard = theBoard;
        myFrame = theFrame;
        myLevel = 1;
        myLinesToNextLevel = INITIAL_LINES;
        myScores = new LinkedHashMap<String, JLabel>();
        myPlayer = thePlayer;
        myScoreLabel = new JLabel();
        setUpComponents();
    }
    
    /**
     * Resets the score when a new game is started.
     */
    protected void resetValues() {
        myLevel = 1;
        myLinesToNextLevel = INITIAL_LINES;
        myScore = 0;
        myLines = 0;
        repaint();
    }
    
    /**
     * Sets up the score panel.
     */
    private void setUpComponents() {
        myBoard.addObserver(this);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        myScores.put(SCORE, new JLabel("<html><center>" + SCORE + myScore + "<html>"));
        myScores.put(LEVEL, new JLabel("<html><center>" + LEVEL + myLevel + "<center><html>"));
        myScores.put(LINES, new JLabel("<html><center>" + LINES + myLines + "<center><html>"));
        myScores.put(LINES_LEFT, new JLabel("<html><center>" + LINES_LEFT + myLinesToNextLevel 
                                            + "<center><html>"));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setMaximumSize(new Dimension(160, 65));
        add(myScoreLabel);
        this.repaint();
    }

    @Override
    public void update(final Observable theObservable, final Object theObject) {
        if (theObject != null) {
            String sound = null;
            if ("move down".equals(theObject)) {
                myScore++;
            } else if ("clear line".equals(theObject)) {
                sound = SoundPlayer.CLEAR_LINE;
                myScore += INCREMENT_FACTOR * myLevel;
                myLines++;
                myLinesToNextLevel--;
            } else if (theObject instanceof Integer) {
                myScore += (int) theObject * INCREMENT_FACTOR;
            }
            if (myLinesToNextLevel == 0) {
                sound = SoundPlayer.LEVEL_UP;
                myLevel++;
                myScore += myLevel * LEVEL_UP_FACTOR;
                myFrame.speedUp();
                myLinesToNextLevel = INITIAL_LINES + INCREMENT_FACTOR * (myLevel - 1);
            }
            if (sound != null && !myPlayer.isMuted()) {
                myPlayer.play(sound);
            }
        }
    }
    

    @Override
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        final Graphics2D g2d = (Graphics2D) theGraphics;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        myScoreLabel.setText("<html><center><font size='3'>Score: " + myScore + "<br>Level: " + myLevel 
                             + "<br>Lines Cleared: " + myLines + "<br> Lines Until Next Level: "
                             + myLinesToNextLevel + "</font></center><html>");
        myScoreLabel.setFont(GUI.MARIO_FONT);
    }

//    /**
//     * Returns the value corresponding to the passed label name. 
//     * 
//     * @param theValue the name of the label being considered
//     * @return the value corresponding to the passed label
//     */
//    private int getValue(final String theValue) {
//        int result = 0;
//        switch (theValue) {
//            case SCORE:
//                result = myScore;
//                break;
//            case LEVEL:
//                result = myLevel;
//                break;
//            case LINES:
//                result = myLines;
//                break;
//            case LINES_LEFT:
//                result = myLinesToNextLevel;
//                break;
//            default:
//                break;
//        }
//        return result;
//    }
}
