package graphics.rendering.textures;

import javafx.scene.image.Image;

import java.util.HashMap;

public class TextureLoader {

    public static HashMap<Sprites, Image> loadTextures() {
        HashMap<Sprites, Image> values = new HashMap<>();

        for (Sprites sprite: Sprites.values()) {
            Image initial = new Image(sprite.getLocation());
            // reload forcing no sharpening
            Image finalImg = new Image(sprite.getLocation(), initial.getRequestedWidth(), initial.getRequestedHeight(), false, false);

            values.put(sprite, finalImg);
        }

        return  values;
    }


}
