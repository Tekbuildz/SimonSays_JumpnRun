package Loader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SpriteSheetLoader {

    private static final int CUBE_PIXEL_SIZE = 40;

    /**
     *
     * @param spriteSheet - image containing all the individual sprites to be split up
     * @return the amount of sprites horizontally and vertically contained in the sprite sheet as an array
     */
    public static int[] getSpriteSheetSize(BufferedImage spriteSheet) {
        // the size array contains values NOT for the number of pixels but the number of individual sprite-images
        // hence a sprite sheet of width 80 pixels will have a size of 2 in the array
        // the size array contains first the width and then the height
        int[] size = new int[2];
        int width = spriteSheet.getWidth()/40;
        int height = spriteSheet.getHeight()/40;
        size[0] = width;
        size[1] = height;

        return size;
    }

    /**
     *
     * @param spriteSheet - image containing all the individual sprites to be split up
     * @param size - array containing width and height as the number of sprites horizontally and vertically
     * @return an array containing all sprites from this sprite sheet
     */
    public static Image[] getSprites(BufferedImage spriteSheet, int[] size) {
        int numberOfSprites = size[0] * size[1];
        Image[] sprites = new Image[numberOfSprites];
        int totalImgCount = 0;
        for (int i = 0; i < size[0]; i++) {
            for (int j = 0; j < size[1]; j++) {
                sprites[totalImgCount] = spriteSheet.getSubimage(i * CUBE_PIXEL_SIZE, j * CUBE_PIXEL_SIZE, CUBE_PIXEL_SIZE, CUBE_PIXEL_SIZE);
                totalImgCount++;
            }
        }

        return sprites;
    }
}
