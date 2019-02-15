package textures;

import javafx.scene.image.Image;

import java.util.HashMap;

public class TextureLoader {

    public static HashMap<String, Image> loadTextures() {
        HashMap<String, Image> values = new HashMap<>();
        Image background = new Image("textures/moon.png");
        Image hardBackground = new Image("textures/moon.png", background.getRequestedWidth(), background.getRequestedHeight(), false, false);


        values.put("background", hardBackground);

        return  values;
    }


}
