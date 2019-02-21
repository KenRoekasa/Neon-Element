package graphics.rendering.textures;

import javafx.scene.image.Image;

import java.util.HashMap;

public class TextureLoader {

    public static HashMap<String, Image> loadTextures() {
        HashMap<String, Image> values = new HashMap<>();
        Image cursor = new Image("graphics/rendering/textures/pointer.png");
        // reload forcing no sharpening
        Image hardBackground = new Image("graphics/rendering/textures/pointer.png", cursor.getRequestedWidth(), cursor.getRequestedHeight(), false, false);


        values.put("pointer", hardBackground);

        return  values;
    }


}
