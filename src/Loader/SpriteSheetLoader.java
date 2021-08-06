package Loader;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SpriteSheetLoader {

    /**
     *
     * gets the size of the sprite sheet measured in tiles
     * the function gets the width and height of the sprite sheet
     * and divides them by 40 to acquire the width and height in tiles
     * it then returns those values in an integer array of length 2
     *
     * @param spriteSheet - image containing all the individual sprites to be split up
     * @param spriteWidth - the width of an individual sprite
     * @param spriteHeight - the height of an individual sprite
     * @return the amount of sprites horizontally and vertically contained in the sprite sheet as an array
     */
    public static int[] getSpriteSheetSize(BufferedImage spriteSheet, int spriteWidth, int spriteHeight) {
        // the size array contains values NOT for the number of pixels but the number of individual sprite-images
        // hence a sprite sheet of width 80 pixels will have a size of 2 in the array
        // the size array contains first the width and then the height
        int[] size = new int[2];
        int width = spriteSheet.getWidth()/spriteWidth;
        int height = spriteSheet.getHeight()/spriteHeight;
        size[0] = width;
        size[1] = height;

        return size;
    }

    /**
     *
     * gets all the sprites in a sprite sheet and puts them in an array
     * using the sprite sheet size calculated in the function
     * getSpriteSheetSize(), the total amount of sprites can be calculated.
     * This number is used to create a new Image array in which all the
     * individual sprites are stored using the Image.getSumbimage() function
     *
     * @param spriteSheet - image containing all the individual sprites to be split up
     * @param size - array containing width and height as the number of sprites horizontally and vertically
     * @param spriteWidth - the width of an individual sprite
     * @param spriteHeight - the height of an individual sprite
     * @return an array containing all sprites from this sprite sheet
     */
    public static Image[] getSprites(BufferedImage spriteSheet, int[] size, int spriteWidth, int spriteHeight) {
        Image[] sprites = new Image[(size[0] * size[1])];
        int totalImgCount = 0;
        for (int i = 0; i < size[1]; i++) {
            for (int j = 0; j < size[0]; j++) {
                sprites[totalImgCount] = spriteSheet.getSubimage(j * spriteWidth, i * spriteHeight, spriteWidth, spriteHeight);
                totalImgCount++;
            }
        }

        return sprites;
    }
}
