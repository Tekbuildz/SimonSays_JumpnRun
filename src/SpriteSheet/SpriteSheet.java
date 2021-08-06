package SpriteSheet;

import Loader.ImageLoader;
import Loader.SpriteSheetLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SpriteSheet {

    private final Image[] spriteImages;

    /**
     *
     * basic constructor of the sprite sheet
     * loads the sprite sheet using the ImageLoader.loadImage() function and
     * passing it the fileName
     * <p>
     * getting the sprite sheet size using the
     * SpriteSheetLoader.getSpriteSheetSize() function
     * <p>
     * getting all the sprite images as an array by passing the
     * SpriteSheetLoader.getSprites() function both the sprite sheet size and
     * the sprite sheet itself
     *
     * @param fileName - name of the sprite sheet to be loaded
     * @param spriteWidth - the width of an individual sprite
     * @param spriteHeight - the height of an individual sprite
     */
    public SpriteSheet(String fileName, int spriteWidth, int spriteHeight) {
        // loading the sprite sheet as a buffered image
        BufferedImage spriteSheet = ImageLoader.loadImage(fileName);
        // getting the number of sprites horizontally and vertically contained in the sprite sheet
        int[] spriteSheetSize = SpriteSheetLoader.getSpriteSheetSize(spriteSheet, spriteWidth, spriteHeight);
        // getting the individual sprites as an array
        spriteImages = SpriteSheetLoader.getSprites(spriteSheet, spriteSheetSize, spriteWidth, spriteHeight);
    }

    /**
     *
     * @return an array containing the individual sprites of the sprite sheet
     */
    public Image[] getSpriteImages() {
        return spriteImages;
    }
}
