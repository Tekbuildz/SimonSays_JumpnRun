package toolbox;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class ImageProcessing {

    private static AffineTransform affineTransform;
    private static AffineTransformOp affineTransformOp;

    /**
     *
     * flips the given image horizontally by scaling and then translating it
     *
     * @param image - the image to be flipped horizontally
     * @return the horizontally flipped image
     */
    public static BufferedImage flipImageHorizontally(BufferedImage image) {
        affineTransform = AffineTransform.getScaleInstance(-1, 1);
        affineTransform.translate(-image.getWidth(null), 0);
        affineTransformOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        image = affineTransformOp.filter(image, null);
        return image;
    }

}