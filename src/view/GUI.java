/*
 * TCSS 305 
 * Assignment 6 - Tetris
 */
package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.io.File;
import java.io.IOException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.Timer;


import model.Board;

/**
 * @author Jonah Howard
 * @version 22 November 2015
 */
public final class GUI implements Observer, ActionListener {
    
    /** The name of this game. */
    public static final String TETRIS = "Tetris";
    
    /** Represents the left movement. */
    public static final String LEFT = "Left";
    
    /** Represents the right movement. */
    public static final String RIGHT = "Right";
    
    /** Represents the down movement. */
    public static final String DOWN = "Down";
    
    /** Represents the hard drop movement. */
    public static final String HARD_DROP = "Hard Drop";
    
    /** Represents the pause action. */
    public static final String PAUSE = "Pause";
    
    /** Represents the rotate movement. */
    public static final String ROTATE = "Rotate CW";
    
    /** The Mario Themed font. */
    public static final Font MARIO_FONT = createFont();
    
    /** The default width for the game board. */
    public static final int DEFAULT_WIDTH = 10;
    
    /** The default height for the game board. */
    public static final int DEFAULT_HEIGHT = 20;
    
    /** The default delay for the timer. */
    public static final int DEFAULT_DELAY = 500;

    /** The default icon for this game. */
    public static final ImageIcon DEFAULT_ICON = new ImageIcon("./extras/mario.png");
    
    /** The default size for the frame. */
    private static final Dimension DEFAULT_SIZE = new Dimension(550, 500);
    
    /** The timer used to update the state of the game. */
    private final Timer myTimer;
    
    /** The frame used to host the graphical user interface for the game. */
    private final JFrame myFrame;
    
    /** The current Tetris game board. */
    private final Board myBoard;
    
    /** The current panel for the Tetris game. */
    private final GameBoard myGameBoard;
    
    /** Holds all of controls and their associated keys. */
    private final Map<String, Integer> myControls;
    
    /** The current scoring panel for this game. */
    private final ScorePanel myScorePanel;
    
    /** Represents the actions performed when the new game button is pressed. */
    private final NewGameAction myNewGameAction;
    
    /** The sound manager for this game. */
    private final SoundPlayer myPlayer;
    
    /** True if the game is currently paused. */
    private boolean isPaused = false;
    
    
    /**
     * Initializes the GUI for this Tetris game.
     */
    public GUI() {
        myFrame = new JFrame(TETRIS);
        myControls = new LinkedHashMap<String, Integer>();
        fillDefaultControls();
        myPlayer = new SoundPlayer();
        myBoard = new Board(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        myGameBoard = new GameBoard(myBoard);
        myTimer = new Timer(DEFAULT_DELAY, this);
        myScorePanel = new ScorePanel(myBoard, this, myPlayer);
        myNewGameAction = new NewGameAction(myBoard, myScorePanel, myTimer, myPlayer);
        setUpComponents();
    }
    
    /**
     * Initializes the font for the east panel. 
     * 
     * @return the font created from a third party
     */
    private static Font createFont() {
        Font font = null;
            try {
                final File file = new File("./extras/font2.ttf");
                font = Font.createFont(Font.TRUETYPE_FONT, file);
                final GraphicsEnvironment ge = GraphicsEnvironment
                                .getLocalGraphicsEnvironment();
                ge.registerFont(font);
                
            }
            catch (FontFormatException | IOException e) {
                e.printStackTrace();
            }
            return font;
    }
    
    /**
     * Set up the components.
     */
    private void setUpComponents() {
        final KeyboardListener listener = new KeyboardListener();
        final EastPanel eastPanel = new EastPanel(this, myBoard, myScorePanel);
        final JPanel panel = new JPanel();
        final JPanel game = new JPanel();
        final JPanel north = new JPanel();
        final JPanel west = new JPanel();
        final JPanel east = new JPanel();
        final MenuBar menu = new MenuBar(this, myTimer, myNewGameAction, myPlayer, 
                                         myGameBoard);
        west.setBackground(Color.WHITE);
        north.setBackground(Color.WHITE);
        east.setBackground(Color.WHITE);
        
        game.setLayout(new BoxLayout(game, BoxLayout.Y_AXIS));
        game.setBackground(Color.WHITE);
        game.add(myGameBoard);
        
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(Color.WHITE);
        panel.add(game);
        panel.add(Box.createHorizontalStrut(80));
        panel.add(eastPanel);
        
        myFrame.setIconImage(DEFAULT_ICON.getImage());
        myFrame.setLocationRelativeTo(null);
        myFrame.setMinimumSize(DEFAULT_SIZE);
        myFrame.setResizable(false);
        myFrame.setJMenuBar(menu);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.addKeyListener(listener);
        myFrame.add(panel);
        myFrame.add(getControlsButtons(), BorderLayout.SOUTH);
        myFrame.add(north, BorderLayout.NORTH);
        myFrame.add(west, BorderLayout.WEST);
//        myFrame.add(east, BorderLayout.EAST);
        myBoard.addObserver(this);
        myBoard.newGame(DEFAULT_WIDTH, DEFAULT_HEIGHT, null);
        myFrame.pack();
    }
    
    /**
     * Starts the game. Prompts user to select whether they want to play or not.
     */
    private void start() {
        final int option = JOptionPane.showConfirmDialog(myFrame, 
                        "Welcome to Tetris!\nAre you ready to play?\n"
                        + "Click no to exit.", TETRIS, 
                        JOptionPane.YES_NO_OPTION, 
                        JOptionPane.INFORMATION_MESSAGE, DEFAULT_ICON);
        if (option == JOptionPane.YES_OPTION) {
            myFrame.setVisible(true);
            myTimer.start();
            myPlayer.loop(SoundPlayer.THEME_SONG);
            myFrame.pack();
        } else if (option == JOptionPane.NO_OPTION || option == JOptionPane.CLOSED_OPTION) {
            myPlayer.play(SoundPlayer.EXIT_GAME);
            myFrame.dispose();
        }

    }
    
    /**
     * Return the collection of control keys.
     * 
     * @return the current controls
     */
    protected Map<String, Integer> getControls() {
        return myControls;
        
    }
    
    /**
     * Set the controls for this game.
     * 
     * @param theControl The name of the control
     * @param theNewValue The name of the new key
     */
    protected void setControls(final String theControl, final int theNewValue) {
        if (myControls.containsKey(theControl)) {
            myControls.put(theControl, theNewValue);
        }
    }
    
    /** 
     * Speeds up the rate which pieces fall.
     */
    protected void speedUp() {
        myTimer.setDelay((int) (myTimer.getDelay() * ScorePanel.TIMER_OFFSET));
    }
    
    /**
     * Register the controls to the default keys.
     */
    private void fillDefaultControls() {
        myControls.put(LEFT, KeyEvent.VK_LEFT);
        myControls.put(RIGHT, KeyEvent.VK_RIGHT);
        myControls.put(DOWN, KeyEvent.VK_DOWN);
        myControls.put(HARD_DROP, KeyEvent.VK_SPACE);
        myControls.put(ROTATE, KeyEvent.VK_UP);
        myControls.put(PAUSE, KeyEvent.VK_ENTER);
    }
    
    /**
     * Returns the names of the controls.
     * 
     * @return the names of the controls
     */
    protected String[] getControlNames() {
        return new String[] {LEFT, RIGHT, DOWN, HARD_DROP, ROTATE, PAUSE};
    }
    
    @Override
    public void update(final Observable theObserver, final Object theObject) {
        myGameBoard.repaint();
        if (myBoard.isGameOver() && myFrame.isActive()) {
            myPlayer.stop(SoundPlayer.THEME_SONG);
            myPlayer.play(SoundPlayer.GAME_OVER);
            final int option = JOptionPane.showConfirmDialog(myFrame, 
                                               "You lose!\nWould you like to play again?", 
                                               "Game Over!", JOptionPane.YES_NO_OPTION, 
                                               JOptionPane.PLAIN_MESSAGE, 
                                               new ImageIcon("./extras/bowser.png"));
            if (option == JOptionPane.YES_OPTION) {
                myNewGameAction.startNewGame();
                if (!myPlayer.isMuted()) {
                    myPlayer.loop(SoundPlayer.THEME_SONG);
                }
            } else if (option == JOptionPane.NO_OPTION 
                            || option == JOptionPane.CLOSED_OPTION) {
                myPlayer.play(SoundPlayer.EXIT_GAME_END);
                myTimer.stop();
                myFrame.dispose();
            }
        }
    }


    @Override
    public void actionPerformed(final ActionEvent theEvent) {
        myBoard.moveDown();
        myGameBoard.setCurrentPiece(myBoard.getCurrentPiece());
    }

    
    /**
     * Initializes the GUI and starts the game.
     * 
     * @param theArgs Command Line parameters to be ignored in this program
     */
    public static void main(final String... theArgs) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI().start();
            }
        });
    }

    // Inner Classes
    /**
     *The KeyListener for the game.
     */
    private class KeyboardListener extends KeyAdapter {

        @Override
        public void keyPressed(final KeyEvent theEvent) {
            final int keyCode = theEvent.getKeyCode();
            final String sound = getSound(keyCode);
            if (myControls.containsValue(keyCode) && !myPlayer.isMuted()) {
                myPlayer.play(sound);
            }
        }
        
        /**
         * Moves the current piece according to the passed keyCode and gets the sound 
         * associated with that movement.
         * 
         * @param theKeyCode the current key that was pressed
         * @return the name of the sound associated with the passed keyCode
         */
        private String getSound(final int theKeyCode) {
            String sound = SoundPlayer.MOVE;
            if (theKeyCode == myControls.get(PAUSE)) {
                if (myTimer.isRunning()) {
                    myPlayer.pause(SoundPlayer.THEME_SONG);
                    sound = SoundPlayer.PAUSE_SOUND;
                    myTimer.stop();
                    isPaused = true;
                } else {
                    myTimer.start();
                    isPaused = false;
                    if (!myPlayer.isMuted()) {
                        myPlayer.loop(SoundPlayer.THEME_SONG);
                    }
                }
            } else if (!isPaused) {       
                if (theKeyCode == myControls.get(LEFT)) {
                    myBoard.moveLeft();
                } else if (theKeyCode == myControls.get(RIGHT)) {
                    myBoard.moveRight();
                } else if (theKeyCode == myControls.get(DOWN)) {
                    myBoard.moveDown();
                } else if (theKeyCode == myControls.get(HARD_DROP)) {
                    myBoard.hardDrop();
                    sound = SoundPlayer.HARD_DROP;
                } else if (theKeyCode == myControls.get(ROTATE)) {
                    myBoard.rotate();
                    sound = SoundPlayer.ROTATE;
                } 
            }
            return sound;
        }
    }
    
    /**
     * Builds and returns the panel that contains the controls buttons. 
     * 
     * @return the panel that contains the controls buttons
     */
    private JPanel getControlsButtons() { 
        final JPanel panel = new JPanel();
        final JPanel top = new JPanel();
        final JPanel bottom = new JPanel();
        final JPanel middle = new JPanel(new FlowLayout());
        final BoxLayout box = new BoxLayout(panel, BoxLayout.Y_AXIS);
        final JButton rotate = new JButton("<html><font size='3'>Rotate</font><html>");
        final JButton down =  new JButton("<html><font size='3'>Down</font><html>");
        final JButton right = new JButton("<html><font size='3'>Right</font><html>");
        final JButton left = new JButton("<html><font size='3'>Left</font><html>");
        final JButton drop = new JButton("<html><font size='3'>Hard Drop</font><html>");
        
        addListener(rotate, ROTATE);
        addListener(drop, HARD_DROP);
        addListener(down, DOWN);
        addListener(left, LEFT);
        addListener(right, RIGHT);
       
        left.setFocusable(false);
        right.setFocusable(false);
        rotate.setFocusable(false);
        drop.setFocusable(false);
        down.setFocusable(false);

        left.setFont(MARIO_FONT);
        right.setFont(MARIO_FONT);
        rotate.setFont(MARIO_FONT);
        drop.setFont(MARIO_FONT);
        down.setFont(MARIO_FONT);
        
        middle.setBackground(Color.WHITE);
        middle.add(left);
        middle.add(down);
        middle.add(right);
        
        top.setBackground(Color.WHITE);
        top.add(rotate);
        
        bottom.setBackground(Color.WHITE);
        bottom.add(drop);
        
        panel.setLayout(box);
        panel.setBackground(Color.WHITE);
        panel.add(Box.createVerticalStrut(10));
        panel.add(new JSeparator(JSeparator.HORIZONTAL));
        panel.add(Box.createVerticalStrut(10));
        panel.add(top);
        panel.add(middle);
        panel.add(bottom);
        
        return panel;
    }
    
    /**
     * Adds listener to the passed button.
     * 
     * @param theButton the button to add the listener to
     * @param theName Determines which button is being assigned
     */
    private void addListener(final JButton theButton, final String theName) {
        theButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                if (!isPaused) {
                    switch(theName) {
                        case LEFT: 
                            myBoard.moveLeft();
                            break;
                        case RIGHT: 
                            myBoard.moveRight();
                            break;
                        case DOWN: 
                            myBoard.moveDown();
                            break;
                        case ROTATE:
                            myBoard.rotate();
                            break;
                        case HARD_DROP:
                            myBoard.hardDrop();
                            break;
                        default:
                            break;
                    }
                    
                }
            }
                
        });
        
    }
    
}
