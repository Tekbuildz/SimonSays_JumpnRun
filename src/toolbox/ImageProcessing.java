package toolbox;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 *
 * the ImageProcessing class contains a function to flip an image horizontally
 * which is used when flipping an animation to make an object move backwards
 *
 * @author Thomas Bundi
 * @version 0.2
 * @since 2.5
 */
public class ImageProcessing {

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
            //image = toBufferedImage(image.getScaledInstance(image.getWidth(null), image.getHeight(null), Image.SCALE_SMOOTH));
        } catch (IOException e) {
            System.err.println("The image with the name: " + pathName + "could not be loaded with error message: " + e);
        }
        return image;
    }

    /**
     *
     * flips the given image horizontally by scaling and then translating it
     *
     * @param image - the image to be flipped horizontally
     * @return the horizontally flipped image
     */
    public static BufferedImage flipImageHorizontally(BufferedImage image) {
        AffineTransform affineTransform = AffineTransform.getScaleInstance(-1, 1);
        affineTransform.translate(-image.getWidth(null), 0);
        AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        image = affineTransformOp.filter(image, null);
        return image;
    }

    /**
     *
     * Converts a given Image into a BufferedImage
     *
     * @param img - the Image to be converted
     * @return The converted BufferedImage
     */
    public static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bufferedImageGraphics = bufferedImage.createGraphics();
        bufferedImageGraphics.drawImage(img, 0, 0, null);
        bufferedImageGraphics.dispose();

        // Return the buffered image
        return bufferedImage;
    }
}
