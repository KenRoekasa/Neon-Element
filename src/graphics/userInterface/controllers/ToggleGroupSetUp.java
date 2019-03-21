package graphics.userInterface.controllers;

import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class ToggleGroupSetUp {
    // for 3 radio button toggle group
    public static void setToggleGroup(ToggleGroup group, RadioButton rB1, RadioButton rB2, RadioButton rB3){
        rB1.setToggleGroup(group);
        rB2.setToggleGroup(group);
        rB3.setToggleGroup(group);
        rB3.setSelected(true);
        removeGroupRadioCircle(rB1,rB2,rB3);
    }

    // for 2 radio button toggle group
    public static void setToggleGroup(ToggleGroup group,RadioButton rB1,RadioButton rB2){
        rB1.setToggleGroup(group);
        rB2.setToggleGroup(group);
        rB2.setSelected(true);
        removeGroupRadioCircle(rB1,rB2);
    }

    //remove single radio circle
    public static void removeRadioCircle(RadioButton radioButton){
        radioButton.getStyleClass().remove("radio-button");
        radioButton.getStyleClass().add("button");
    }

    // remove same toggle group radio circle
    public static void removeGroupRadioCircle(RadioButton rb1,RadioButton rb2, RadioButton rb3) {
        removeRadioCircle(rb1);
        removeRadioCircle(rb2);
        removeRadioCircle(rb3);
    }

    public static void removeGroupRadioCircle(RadioButton rb1,RadioButton rb2) {
        removeRadioCircle(rb1);
        removeRadioCircle(rb2);
    }

    public static void setUserData(String s,RadioButton rB1,RadioButton rB2,RadioButton rB3){
        rB1.setUserData(s);
        rB2.setUserData(s);
        rB3.setUserData(s);
    }
    
    public static void setToggleGroup(ToggleGroup group, RadioButton rB1, RadioButton rB2, RadioButton rB3, RadioButton rB4){
        rB1.setToggleGroup(group);
        rB2.setToggleGroup(group);
        rB3.setToggleGroup(group);
        rB4.setToggleGroup(group);
        rB3.setSelected(true);
        removeGroupRadioCircle(rB1,rB2,rB3);
    }

}
