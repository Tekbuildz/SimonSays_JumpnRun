package Loader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 *
 * the ImageLoader class contains a function which returns an image when given
 * a path to the image
 *
 * @author Thomas Bundi
 * @version 0.5
 * @since 1.3
 */
public class ImageLoader {

    /**
     *
     * loads an image, with the given path, to memory
     *
     * @param pathName - the name of the image file to be loaded
     * @return the loaded image
     */
    public static BufferedImage loadImage(String pathName) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(pathName));
        } catch (IOException e) {
            System.err.println("The image with the name: " + pathName + "could not be loaded with error message: " + e);
        }

        return image;
    }
}
