import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Mainq1 {

  public static void main(String[] args) throws IOException {
    //q1
    BufferedImage img = ImageIO.read(new File("./images_etudiants/copie.png"));
    ImageIO.write(img, "png", new File("fleur.png"));

    //q2
    BufferedImage img2 = ImageIO.read(new File("./images_etudiants/copie.png"));
    BufferedImage img3 = new BufferedImage(img2.getWidth(), img2.getHeight(), BufferedImage.TYPE_INT_RGB);
    for (int i = 0; i < img2.getWidth(); i++) {
      for (int j = 0; j < img2.getHeight(); j++) {
        img3.setRGB(i, j, img2.getRGB(i, j));
      }
    }
    ImageIO.write(img3, "png", new File("fleur2.png"));

    //q3
    BufferedImage img4 = ImageIO.read(new File("./images_etudiants/copie.png"));
    for (int i = 0; i < img2.getWidth(); i++) {
      for (int j = 0; j < img2.getHeight(); j++) {
        int[] rgb = rgbToInt(img4.getRGB(i, j));
        int gray = (rgb[0] + rgb[1] + rgb[2]) / 3;
        img4.setRGB(i, j, (gray << 16) + (gray << 8) + gray);
      }
    }
    ImageIO.write(img4, "png", new File("fleur3.png"));
    //q4
    BufferedImage img5 = ImageIO.read(new File("./images_etudiants/copie.png"));
    img5 = storeComponent(img5, 1);
    ImageIO.write(img5, "png", new File("fleur4.png"));

    //q5
    BufferedImage img6 = ImageIO.read(new File("./images_etudiants/copie.png"));
    img6 = storeComponent(img6, 2);
    ImageIO.write(img6, "png", new File("fleur5.png"));

    BufferedImage img7 = ImageIO.read(new File("./images_etudiants/copie.png"));
    img7 = storeComponent(img7, 3);
    ImageIO.write(img7, "png", new File("fleur6.png"));

    //q6
    BufferedImage img8 = ImageIO.read(new File("./images_etudiants/copie.png"));
    Color[] colors = {Color.YELLOW, Color.GREEN};
    img8 = replaceByClosestColor(img8, colors);
    ImageIO.write(img8, "png", new File("fleur7.png"));

    //q7
    BufferedImage img9 = ImageIO.read(new File("./images_etudiants/copie.png"));
    Color[] colors2 = {Color.YELLOW, Color.GREEN, Color.WHITE, Color.ORANGE};
    img9 = replaceByClosestColor(img9, colors2);
    ImageIO.write(img9, "png", new File("fleur8.png"));

    //q8
    BufferedImage img10 = ImageIO.read(new File("./images_etudiants/copie.png"));
    Color[] colors3 = {Color.YELLOW, Color.GREEN, Color.WHITE, Color.ORANGE, Color.RED, Color.BLUE, Color.PINK, Color.MAGENTA};
    img10 = replaceByClosestColor(img10, colors3);
    ImageIO.write(img10, "png", new File("fleur9.png"));
  }

  public static int[] rgbToInt(int rgb) {
    int[] res = new int[3];
    res[0] = (rgb >> 16) & 0xFF;
    res[1] = (rgb >> 8) & 0xFF;
    res[2] = rgb & 0xFF;
    return res;
  }

  private static BufferedImage storeComponent(BufferedImage img, int component) {
    BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
    for (int i = 0; i < img.getWidth(); i++) {
      for (int j = 0; j < img.getHeight(); j++) {
        int[] rgb = rgbToInt(img.getRGB(i, j));
        int [] comp = {0,0,0,0};
        if (component >0) comp[component] = 1;
        res.setRGB(i, j, (rgb[0]*comp[1] << 16) + (rgb[1]*comp[2] << 8) + rgb[2]*comp[3]);
      }
    }
    return res;
  }

  private static BufferedImage replaceByClosestColor(BufferedImage img, Color[] colors) {
    BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
    for (int i = 0; i < img.getWidth(); i++) {
      for (int j = 0; j < img.getHeight(); j++) {
        int [] rgb = rgbToInt(img.getRGB(i, j));
        Color closest = colors[0];
        for(Color c : colors) {
          if (Math.abs(c.getRed() - rgb[0]) + Math.abs(c.getGreen() - rgb[1]) + Math.abs(c.getBlue() - rgb[2]) <
              Math.abs(closest.getRed() - rgb[0]) + Math.abs(closest.getGreen() - rgb[1]) + Math.abs(closest.getBlue() - rgb[2])) {
            closest = c;
          }
        }
        res.setRGB(i, j, closest.getRGB());
      }
    }
    return res;
  }
}
