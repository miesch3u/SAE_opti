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

  //la methode getCloseColor est une methode qui permet de trouver une couleur proche d'une autre couleur parmis
  // celles déjà utilisées dans l'image et concervées. si aucune couleur n'est proche, elle renvoie null
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

  // renvoie la distance entre deux couleurs
  private static double colorDistance(Color color, Color color1) {
    return Math.sqrt(Math.pow(color.getRed() - color1.getRed(), 2) + Math.pow(color.getGreen() - color1.getGreen(), 2) + Math.pow(color.getBlue() - color1.getBlue(), 2));
  }

  // renvoie la liste des X couleurs les plus utilisées dans l'image
  public static Color[] getXMoreRepresented(int x, List<Color> colors, List<Integer> counts) {
    if (x >= colors.size()) return colors.toArray(new Color[0]);
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

  // calcule l'image avec les X couleurs les plus utilisées dans l'image
  // prend en parametre l'image, le nombre de couleurs et la tolerance
  // correspond a la distance maximale entre deux couleurs pour qu'elles soient considérées comme proches
  // le reduction de cette valeur entraine une augmentation du temps de calcul, mais est nécessaire pour avoir une image de meilleure qualité
  public static BufferedImage calculerImage(BufferedImage img, int nbCouleur, double tolerance) {
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
    String path = args.length > 0 ? args[0] : "./images_etudiants/copie.png";
    String out = args.length > 1 ? args[1] : "test.jpg";
    int nbCoul = args.length > 2 ? Integer.parseInt(args[2]) : 31;
    int tolerance = args.length > 3 ? Integer.parseInt(args[3]) : 38;
    long start = System.currentTimeMillis();
    BufferedImage img1 = ImageIO.read(new File(path));
    long inter1 = System.currentTimeMillis();
    BufferedImage img = calculerImage(img1, nbCoul, tolerance);
    long inter = System.currentTimeMillis();
    ImageIO.write(img, out.substring(out.length()-3), new File(out));
    long end = System.currentTimeMillis();
    System.out.println("temps de lecture: " + (inter1 - start));
    System.out.println("temps de calcul: " + (inter - inter1));
    System.out.println("temps d'ecriture: " + (end - inter));
    System.out.println("temps total: " + (end - start));
  }
}
