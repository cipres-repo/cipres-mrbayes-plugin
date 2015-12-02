package org.suchard.gui.components;

import org.suchard.gui.OptionsPanel;

import javax.swing.*;
import java.util.prefs.Preferences;

public class IntegerOption implements Option {

    protected final String name;
    private final int defaultValue;
    protected final JTextField textField;
    protected JLabel label;
    
    private Preferences PREFS;
    

    public String getName() { return name; }
    
    /**
     *
     * @param name to be displayed and for saving to prefs
     * @param defaultValue which the field will initially have and may be defaulted to
     * if user's input is invalid.
     */
    public IntegerOption(String name, int defaultValue, int min, int max, Preferences PREFS) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.PREFS = PREFS;
        int def = PREFS.getInt(name, defaultValue);
        textField = new JTextField(4);
        textField.setDocument(new IntegerOnlyDocument(min,max));
        textField.setText("" + def);
        textField.setMinimumSize(textField.getPreferredSize());
    }

    public JTextField getTextField() {
        return textField;
    }

    public JLabel getLabel() {
        if (label == null) {
            throw new IllegalStateException("cannot getLabel until option is added to a panel");
        }
        return label;
    }

    public JComponent getExtraComponent() { return null; }
    
    public int getDefaultValue() {
        return defaultValue;
    }

    public int getValue() {
        String text = textField.getText();
        int input;
        //should never throw NumberFormatException because text field is restricted to integer input
        input = Integer.parseInt(text);
//        if (!isInputValid(input)) {
//            throw new InvalidInputException();
//        }
        return input;
    }

    /**
     * This will only save if the input is valid.
     */
    public void saveToPrefs() {
//        try {
            PREFS.putInt(name, getValue());
//        } catch (InvalidInputException e) {
////            invalid input, do nothing.
//        }
    }

    /**
     * Override this if you want to disallow specific values for a specific option.
     *
     * @return false if input is not valid for this option
     */
    public void checkInput() throws InvalidInputException {
       //do nothing for valid input
    }

    public void addTo(OptionsPanel panel) {
        if (label != null) {
            throw new IllegalStateException("IntegerOption cannot be added twice");
        }
        label = panel.addComponentWithLabel(name, textField, false);
    }
}

