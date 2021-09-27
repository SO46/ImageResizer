import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.util.ArrayList;

public class ImageResizer implements Runnable {

    private File[] files;
    private int newWidth;
    private String dstFolder;
    private long start;

    public ImageResizer(File[] files, int newWidth, String dstFolder, long start) {
        this.files = files;
        this.newWidth = newWidth;
        this.dstFolder = dstFolder;
        this.start = start;
    }

    @Override
    public void run() {
        try {
            for (File file : files) {
                BufferedImage image = ImageIO.read(file);
                if (image == null) {
                    continue;
                }

                int newHeight = (int) Math.round(
                        image.getHeight() / (image.getWidth() / (double) newWidth));

//                BufferedImage newImage = ImageResizer.resize(image, newWidth, newHeight);
                BufferedImage newImage = Scalr.resize(image, newWidth, newHeight, Scalr.OP_ANTIALIAS);

                File newFile = new File(dstFolder + "/" + file.getName());
                ImageIO.write(newImage, "jpg", newFile);


            }
            System.out.println("Duration: "
                    + (System.currentTimeMillis() - start)
                    + " files count:" + files.length);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    public static BufferedImage resize(BufferedImage src, int targetWidth,
                                       int targetHeight){
        BufferedImage newImage = new BufferedImage(
                targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);

        int widthStep = src.getWidth() / targetWidth;
        int heightStep = src.getHeight() / targetHeight;

        for (int x = 0; x < targetWidth; x++) {
            for (int y = 0; y < targetHeight; y++) {
                int rgb = src.getRGB(x * widthStep, y * heightStep);
                newImage.setRGB(x, y, rgb);
            }
        }
        return newImage;
    }
}
