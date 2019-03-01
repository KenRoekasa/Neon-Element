package graphics.userInterface.resources.style;

import graphics.enums.UIColour;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;

public class Shadow {

    public static Blend loadShadow(){

        Blend blend = new Blend();
        blend.setMode(BlendMode.MULTIPLY);

        DropShadow ds = new DropShadow();
        ds.setColor(Color.WHITE);
        ds.setOffsetX(5);
        ds.setOffsetY(5);
        ds.setRadius(5);
        ds.setSpread(0.2);

        blend.setBottomInput(ds);

        DropShadow ds1 = new DropShadow();
        ds1.setColor((Color) UIColour.NEON_BORDER.getColor());
        ds1.setRadius(10);
        ds1.setSpread(0.2);

        Blend blend2 = new Blend();
        blend2.setMode(BlendMode.MULTIPLY);

        InnerShadow is = new InnerShadow();
        is.setColor((Color) UIColour.NEON_BORDER.getColor());
        is.setRadius(2);
        is.setChoke(0.8);
        blend2.setBottomInput(is);

        InnerShadow is1 = new InnerShadow();
        is1.setColor((Color) UIColour.NEON_BORDER.getColor());
        //	#55185F dark purple

        is1.setRadius(2);
        is1.setChoke(0.4);
        blend2.setTopInput(is1);

        Blend blend1 = new Blend();
        blend1.setMode(BlendMode.MULTIPLY);
        blend1.setBottomInput(ds1);
        blend1.setTopInput(blend2);
        blend.setTopInput(blend1);
        return blend;
    }
}
