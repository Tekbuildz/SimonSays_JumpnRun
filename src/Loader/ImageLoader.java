package Loader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageLoader {

    /**
     *
     * @param fileName - the name of the image file to be loaded
     * @return the loaded image
     */
    public static BufferedImage loadImage(String fileName) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("res/" + fileName + ".png"));
        } catch (IOException e) {
            System.err.println("The image with the name: " + fileName + "could not be loaded with error message: " + e);
        }

        return image;
    }
}
