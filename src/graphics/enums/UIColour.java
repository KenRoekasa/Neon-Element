package graphics.enums;

import javafx.scene.paint.Color;

public enum UIColour {

    BACKGROUND(Color.BLACK),
    HEAD_TEXT(Color.rgb(3,45,20)),
    NEON_BORDER(Color.web("1b03a3"));

    private Color color;

    UIColour(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
