package minecraft_mapmaker.logic;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PixelTest {

    @Test
    public void test(){
        try {
            System.out.println(readPixelBits("C:\\Users\\larss\\Downloads\\t3.png", 64, 64));
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readPixelBits(String imagePath, int x, int y) throws IOException {
        // Load the image from the file
        BufferedImage image = ImageIO.read(new File(imagePath));
        // Get the pixel at the specified coordinates
        int pixel = image.getRGB(x, y);
        // Convert the pixel value to a binary string
        String binaryString = Integer.toBinaryString(pixel);
        // Return the binary string
        return binaryString;
    }
}
