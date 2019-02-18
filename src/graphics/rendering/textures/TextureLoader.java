package graphics.rendering.textures;

import javafx.scene.image.Image;

import java.util.HashMap;

public class TextureLoader {

    public static HashMap<String, Image> loadTextures() {
        HashMap<String, Image> values = new HashMap<>();
        Image background = new Image("graphics/rendering/textures/moon.png");
        Image hardBackground = new Image("graphics/rendering/textures/moon.png", background.getRequestedWidth(), background.getRequestedHeight(), false, false);


        values.put("background", hardBackground);

        return  values;
    }


}
