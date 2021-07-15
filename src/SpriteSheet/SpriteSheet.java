package SpriteSheet;

import Loader.ImageLoader;
import Loader.SpriteSheetLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SpriteSheet {

    private static Image[] spriteImages;

    /**
     *
     * @param fileName - name of the sprite sheet to be loaded
     */
    public SpriteSheet(String fileName) {
        // loading the sprite sheet as a buffered image
        BufferedImage spriteSheet = ImageLoader.loadImage(fileName);
        // getting the number of sprites horizontally and vertically contained in the sprite sheet
        int[] spriteSheetSize = SpriteSheetLoader.getSpriteSheetSize(spriteSheet);
        // getting the individual sprites as an array
        spriteImages = SpriteSheetLoader.getSprites(spriteSheet, spriteSheetSize);
    }

    /**
     *
     * @return an array containing the individual sprites of the sprite sheet
     */
    public static Image[] getSpriteImages() {
        return spriteImages;
    }
}
