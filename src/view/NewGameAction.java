package view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Timer;

import model.Board;

/**
 * This glass represents the JDialog that appears when the user loses a game.
 * 
 * @author Jonah Howard
 * @version 6 December 2015
 */
public final class NewGameAction extends AbstractAction {
    
    /** A generated serial version UID. */
    private static final long serialVersionUID = 2345492325759899174L;

    /** The current board for this game. */
    private final Board myBoard;
    
    /** The current scoring panel for this game. */
    private final ScorePanel myScorePanel;
    
    /** The current timer for this game. */
    private final Timer myTimer;
    
    /** The sound player for this game. */
    private final SoundPlayer myPlayer;
    
    /**
     * Initializes a new new game action.
     * 
     * @param theBoard the current board for this game
     * @param theScorePanel the current score panel for this game
     * @param theTimer the current timer for this game
     * @param thePlayer the current sound player
     */
    public NewGameAction(final Board theBoard, final ScorePanel theScorePanel,
                         final Timer theTimer, final SoundPlayer thePlayer) {
        super("New Game");
        myPlayer = thePlayer;
        myTimer = theTimer;
        myBoard = theBoard;
        myScorePanel = theScorePanel;
    }

    @Override
    public void actionPerformed(final ActionEvent theEvent) {
        startNewGame();
    }
    
    /**
     * Resets values necessary to restart a game.
     */
    protected void startNewGame() {
        myBoard.newGame(GUI.DEFAULT_WIDTH, GUI.DEFAULT_HEIGHT, null);
        myScorePanel.resetValues();
        myTimer.restart();
        myTimer.setDelay(GUI.DEFAULT_DELAY);
        myPlayer.loop(SoundPlayer.THEME_SONG);
    }
}