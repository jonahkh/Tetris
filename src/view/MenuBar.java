/*
 * TCSS 305 
 * Assignment 6 - Tetris
 */
package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 * This class represents the menu bar for this game of Tetris.
 * 
 * @author Jonah Howard
 * @version 25 November 2015
 */
public class MenuBar extends JMenuBar {

    /** A generated serial version UID. */
    private static final long serialVersionUID = 8173753399843262172L;
    
    /** Represents the name of the property change event for 

    /** The current JFrame for this game. */
    private final GUI myFrame;
    
    /** The current timer for this game. */
    private final Timer myTimer;
    
    /** A JMenu item that represents the New Game button. */
    private final JMenuItem myNewGameButton;
    
    /** Represents the action listener for the end game button. */
    private final EndGameButton myEndGameButton;
    
    /** The sound player for this game. */
    private final SoundPlayer myPlayer;
    
    /** The current game board for this game. */
    private final GameBoard myPanel;
    
    /**
     * Initializes a new menu bar.
     * 
     * @param theFrame The GUI for this game
     * @param theTimer  The timer for this game
     * @param theNewGameAction the action listener for the NewGame button
     * @param thePlayer the sound player for this game
     * @param thePanel the current game board for this game
     */
    public MenuBar(final GUI theFrame, final Timer theTimer,
                   final NewGameAction theNewGameAction, final SoundPlayer thePlayer,
                   final GameBoard thePanel) {
        super();
        myPanel = thePanel;
        myPlayer = thePlayer;
        myFrame = theFrame;
        myTimer = theTimer;
        myNewGameButton = new JMenuItem(theNewGameAction);
        myEndGameButton = new EndGameButton();
        setUpComponents();
    }

    /**
     * Set up the components for the menu bar.
     */
    private void setUpComponents() {
        myNewGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                myNewGameButton.setEnabled(false);
                myEndGameButton.setEnabled(true);
            }
        });
        addGameMenu();
        addOptionsMenu();  
        addHelpMenu();
    }
    
    /**
     * Create the game menu and add it to the menu bar. 
     */
    private void addGameMenu() {
        final JMenu gameMenu = new JMenu("Game");
        final PauseButton pause = new PauseButton();
        gameMenu.add(pause);
        gameMenu.addSeparator();
        gameMenu.add(myNewGameButton);
        gameMenu.add(myEndGameButton);
        myNewGameButton.setEnabled(false);
        add(gameMenu);
    }
    
    /**
     * Create the options menu and add it to the menu bar. 
     */
    private void addOptionsMenu() {
        final JMenu optionsMenu = new JMenu("Options");
        final JCheckBoxMenuItem grid = new JCheckBoxMenuItem("Enable Grid");
        final JCheckBoxMenuItem mute = new JCheckBoxMenuItem("Mute");
        
        grid.addActionListener(new GridAction(myPanel));
        mute.addActionListener(new MuteAction());
        optionsMenu.add(new ControlsButton(myFrame, myTimer));
        optionsMenu.add(grid);
        optionsMenu.add(mute);
        add(optionsMenu);
    }
    
    /**
     * Add the help menu to the menu bar.
     */
    private void addHelpMenu() {
        final JMenu helpMenu = new JMenu("Help");
        final String about = "About";
        final JMenuItem help = new JMenuItem(about);
        help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                myPlayer.pause(SoundPlayer.THEME_SONG);
                myTimer.stop();
                JOptionPane.showMessageDialog(myPanel, getAboutInformation(), about,
                                              JOptionPane.INFORMATION_MESSAGE,
                                              (Icon) new ImageIcon("./extras/mushroom.png"));
                myPlayer.loop(SoundPlayer.THEME_SONG);
                myTimer.start();
            }
        });
        helpMenu.add(help);
        add(helpMenu);
    }
    
    /**
     * Reads the sources file and returns the text as a String.
     * 
     * @return the text of the sources text file
     */
    private String getAboutInformation() {
        final StringBuilder result = new StringBuilder();
        try {
            final Scanner input = new Scanner(new File("./extras/sources.txt"));
            Scanner line;
            final String space = " ";
            final String newLine = "\n";
            while (input.hasNextLine()) {
                line = new Scanner(input.nextLine());
                while (line.hasNext()) {
                    result.append(line.next());
                    result.append(space);
                }
                result.append(newLine);
            }
            input.close();
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
    
    // Inner classes
    /**
     * This class represents the pause button in the game menu.
     */
    private final class PauseButton extends AbstractAction {

        /** A generated serial version UID. */
        private static final long serialVersionUID = -165758256765881230L;

        /**
         * Initialize a new pause button.
         */
        protected PauseButton() {
            super("Pause");
        }
        
        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            if (myTimer.isRunning()) {
                myTimer.stop();
                myPlayer.pause(SoundPlayer.THEME_SONG);
            } else {
                myTimer.start();
                myPlayer.loop(SoundPlayer.THEME_SONG);
            }
        }
    }
    
    /**
     * This class represents the end game button in the game menu.
     */
    private final class EndGameButton extends AbstractAction {
        
        /** A generated serial version UID. */
        private static final long serialVersionUID = -6430299361856630499L;

        /**
         * Initialize a new NewGameButton.
         */
        protected EndGameButton() {
            super("End Game");
        }
        
        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            myTimer.stop();
            myNewGameButton.setEnabled(true);
            this.setEnabled(false);
            myPlayer.stop(SoundPlayer.THEME_SONG);
            
        }
    }
    
    /**
     * This class represents the action listener for the mute button.
     */
    private final class MuteAction extends AbstractAction {
        
        /** A generated serial version UID. */
        private static final long serialVersionUID = 6928962346052582101L;
        
        /** Tracks whether the mute button has been selected or not. */
        private int myCounter;
        
        /** Initialize a new MuteAction. */
        protected MuteAction() {
            super();
            myCounter = 0;
        }
        
        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            if (myCounter % 2 == 0) {
                myPlayer.mute(true);
                myPlayer.stopAll();
            } else {
                myPlayer.mute(false);
                myPlayer.loop(SoundPlayer.THEME_SONG);
            }
            myCounter++;
        }
    }
}
