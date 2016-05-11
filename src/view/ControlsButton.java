/*
 * TCSS 305 
 * Assignment 6 - Tetris
 */
package view;

import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.Timer;

/**
 * This class represents the customize controls button in the options menu.
 * 
 * @author Jonah Howard
 * @version 2 December 2015
 */
public class ControlsButton extends AbstractAction {

    /** A generated serial version UID. */
    private static final long serialVersionUID = -3873996141083705037L;

    /** Number of elements for the controls list. */
    private static final int ELEMENTS = 12;

    /** Stores the current controls. */
    private final Map<String, Integer> myCurrentControls;

    /** Links the name of the the text field to the text field. */
    private final Map<String, JTextField> myTextFields;

    /** Links the text field to the name of the text field. */
    private final Map<JTextField, String> myTextFieldsNames;

    /** Holds the list of controls and their names. */
    private final Object[] myControlsList;

    /** The current GUI for this Tetris game. */
    private final GUI myFrame;

    /** The current timer for this Tetris game. */
    private final Timer myTimer;

    /**
     * Initializes a new controls button.
     * 
     * @param theFrame the GUI for this Tetris game
     * @param theTimer the current timer for this Tetris game
     */
    public ControlsButton(final GUI theFrame, final Timer theTimer) {
        super("Controls");
        myTextFields = new LinkedHashMap<String, JTextField>();
        myControlsList = new Object[ELEMENTS];
        myFrame = theFrame;
        myCurrentControls = myFrame.getControls();
        myTextFieldsNames = new LinkedHashMap<JTextField, String>();
        myTimer = theTimer;
        setUpComponents();
    }

    /** Set up the components. */
    private void setUpComponents() {
        final KeyboardListener listener = new KeyboardListener();
        int i = 0;
        for (final String controls : myFrame.getControlNames()) {
            myTextFields.put(controls, new JTextField());
            myTextFieldsNames.put(myTextFields.get(controls), controls);
            myTextFields.get(controls).addKeyListener(listener);
            myTextFields.get(controls).
                             setText(KeyEvent.getKeyText(myCurrentControls.get(controls)));
            myControlsList[i] = controls;
            myControlsList[i + 1] = myTextFields.get(controls);
            i += 2;
        }
    }

    @Override
    public void actionPerformed(final ActionEvent theEvent) {
        myTimer.stop();
        JOptionPane.showConfirmDialog(null, myControlsList, "Customize Controls",
                                      JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        myTimer.start();
    }

    /**
     * The KeyListener for the change controls menu option.
     */
    private class KeyboardListener extends KeyAdapter {

        @Override
        public void keyPressed(final KeyEvent theEvent) {
            if (myFrame.getControls().containsValue((int) theEvent.getKeyCode())) {
                JOptionPane.showMessageDialog(myTextFields.get(myTextFieldsNames.
                                              get((JTextField) theEvent.getSource())),
                                              "That control key already exists!\nThat control "
                                                              + "will remain its old value.");
            } else {
                myFrame.setControls(myTextFieldsNames.get((JTextField) theEvent.getSource()),
                                    (int) theEvent.getKeyCode());
                myTextFields.get(myTextFieldsNames.get((JTextField) theEvent.getSource())).
                                    setText(KeyEvent.getKeyText((int) theEvent.getKeyCode()));
            }
        }
    }
}
