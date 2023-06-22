import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class MainQ1Sae {

 /* private static double k, dx;
  private static BufferedImage img;

  private static int nbCoul;

  static {
    try {
      img = ImageIO.read(new File("./images_etudiants/copie.png"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static double f(double x) throws IOException {
    System.out.println("calcul de l'image");
    BufferedImage img2 = calculerImage(10,x);
    return Distance.distance(img, img2);
  }

  private static double df(double x) throws IOException {
    return (f(x + dx) - f(x)) / dx;
  }

  public static double descente(double x0) throws IOException {
    double x = x0;
    System.out.println("tolerance: " + x);

    while(true) {
      System.out.println("calcule de fx");
      double fx = f(x);
      System.out.println("calcule de dfx");
      double dfx = df(x);
      x -= k * dfx;
      System.out.println("x: " + x + " f(x): " + fx + " df(x): " + dfx);
      if(Math.abs(df(x)) < dx) break;
    }
    return x;
  }*/

  public static Color getCloseColor(List<Color> colors, Color color, double maxDistance) {
    Color closestColor = null;
    for (Color color1 : colors) {
      double distance = colorDistance(color, color1);
      if (distance < maxDistance) {
        closestColor = color1;
      }
    }
    return closestColor;
  }

  private static double colorDistance(Color color, Color color1) {
    return Math.sqrt(Math.pow(color.getRed() - color1.getRed(), 2) + Math.pow(color.getGreen() - color1.getGreen(), 2) + Math.pow(color.getBlue() - color1.getBlue(), 2));
  }

  public static Color[] getXMoreRepresented(int x, List<Color> colors, List<Integer> counts) {
    Color[] res = new Color[x];
    for (int i = 0; i < x; i++) {
      int max = 0;
      int index = 0;
      for (int j = 0; j < counts.size(); j++) {
        if (counts.get(j) > max) {
          max = counts.get(j);
          index = j;
        }
      }
      res[i] = colors.get(index);
      counts.remove(index);
      colors.remove(index);
    }
    return res;
  }

  public static BufferedImage calculerImage(BufferedImage img, int nbCouleur, double tolerance){
    List<Color> colors = new ArrayList<>();
    List<Integer> counts = new ArrayList<>();
    for (int i = 0; i < img.getHeight(); i++) {
      for (int j = 0; j < img.getWidth(); j++) {
        Color color = new Color(img.getRGB(i, j));
        Color closeColor = getCloseColor(colors, color, tolerance);
        if (closeColor != null) {
          counts.set(colors.indexOf(closeColor), counts.get(colors.indexOf(closeColor)) + 1);
        } else {
          colors.add(color);
          counts.add(1);
        }
      }
    }
    return (Mainq1.replaceByClosestColor(img, getXMoreRepresented(nbCouleur, colors, counts)));
  }

  public static void main(String[] args) throws IOException {
    long start = System.currentTimeMillis();
    BufferedImage img = calculerImage(ImageIO.read(new File("./images_etudiants/copie.png")),31, 37);
    long inter = System.currentTimeMillis();
    ImageIO.write(img, "png", new File("test.png"));
    long end = System.currentTimeMillis();
    System.out.println("temps de calcul: " + (inter - start));
    System.out.println("temps d'ecriture: " + (end - inter));
    System.out.println("temps total: " + (end - start));
  }
}
