package graphics.userInterface.controllers;

import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;


/**
 * Set up toggle group for the radio buttons created in fxml.
 */
public class ToggleGroupSetUp {




    /** Set up the toggle group with three radio buttons.
     * @param group the toggle group which is going to be bound to
     * @param rB1 the first radio button
     * @param rB2 the second radio button
     * @param rB3 the third radio button
     */
    public static void setToggleGroup(ToggleGroup group, RadioButton rB1, RadioButton rB2, RadioButton rB3){
        rB1.setToggleGroup(group);
        rB2.setToggleGroup(group);
        rB3.setToggleGroup(group);
        rB3.setSelected(true);
        removeGroupRadioCircle(rB1,rB2,rB3);
    }



    /** Remove a radio circle of a radio button.
     * @param radioButton the radiobutton with radio circle
     */

    public static void removeRadioCircle(RadioButton radioButton){
        radioButton.getStyleClass().remove("radio-button");
        radioButton.getStyleClass().add("button");
    }

    /** Remove radio circles of four radio buttons in same toggle group .
     * @param rb1 the first radio button with radio circle
     * @param rb2 the second radio button with radio circle
     * @param rb3 the third radio button with radio circle
     * @param rb4 the fourth radio button with radio circle
     */
    public static void removeGroupRadioCircle(RadioButton rb1,RadioButton rb2, RadioButton rb3,RadioButton rb4) {
        removeRadioCircle(rb1);
        removeRadioCircle(rb2);
        removeRadioCircle(rb3);
        removeRadioCircle(rb4);
    }

    /** Remove radio circles of three radio buttons in same toggle group.
     * @param rb1 the first radio button with radio circle
     * @param rb2 the second radio button with radio circle
     * @param rb3 the third radio button with radio circle
     */
    public static void removeGroupRadioCircle(RadioButton rb1,RadioButton rb2, RadioButton rb3) {
        removeRadioCircle(rb1);
        removeRadioCircle(rb2);
        removeRadioCircle(rb3);
    }

    /** Remove radio circles of two radio buttons in same toggle group.
     * @param rb1 the first radio button with radio circle
     * @param rb2 the second radio button with radio circle
     */
    public static void removeGroupRadioCircle(RadioButton rb1,RadioButton rb2) {
        removeRadioCircle(rb1);
        removeRadioCircle(rb2);
    }

    /** Set the user data of the radio buttons in the same user data.
     * @param s the user data string
     * @param rB1 the first radio button
     * @param rB2 the second radio button
     * @param rB3 the third radio button
     */
    public static void setUserData(String s,RadioButton rB1,RadioButton rB2,RadioButton rB3){
        rB1.setUserData(s);
        rB2.setUserData(s);
        rB3.setUserData(s);
    }

    /** Set up the toggle group with four radio buttons.
     * @param group the toggle group which is going to be bound to
     * @param rB1 the first radio button
     * @param rB2 the second radio button
     * @param rB3 the third radio button
     * @param rB4 the fourth radio button
     */
    public static void setToggleGroup(ToggleGroup group, RadioButton rB1, RadioButton rB2, RadioButton rB3, RadioButton rB4){
        rB1.setToggleGroup(group);
        rB2.setToggleGroup(group);
        rB3.setToggleGroup(group);
        rB4.setToggleGroup(group);
        rB1.setSelected(true);
        removeGroupRadioCircle(rB1,rB2,rB3,rB4);
    }

}
