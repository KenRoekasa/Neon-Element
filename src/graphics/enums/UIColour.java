package graphics.enums;

import javafx.scene.paint.*;

public enum UIColour {

    BACKGROUND(Color.BLACK),
    HEAD_TEXT(Color.rgb(3,45,20)),
    NEON_BORDER(Color.web("1b03a3"));

    private Paint color;



    UIColour(Color color) {
        this.color = color;
    }

    public Paint getColor() {
        return color;
    }
}
