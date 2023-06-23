import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MainTests {

  public static void main(String[] args) throws IOException {
    String pathName = "./images_diverses_small/animaux/ours.png";
    String outFolder = "./imgSorties/";
    int[] couleurs = {2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096};
    int[] tolerances = {110, 75, 70, 56, 43, 30, 20, 12, 8, 6, 5, 4};
    BufferedImage img = ImageIO.read(new java.io.File(pathName));
    for (int i = 0; i < couleurs.length; i++) {
      System.out.println("calcul de l'image " + couleurs[i] + " couleurs");
      long start = System.currentTimeMillis();
      BufferedImage img2 = MainQ1Sae.calculerImage(img, couleurs[i], tolerances[i]);
      long interval = System.currentTimeMillis();
      BufferedImage img3 = CluteriseurDeCouleur.calculerImage(new String[]{pathName, outFolder + "ours" + couleurs[i] + ".png", ""+couleurs[i]});
      ImageIO.write(img2, "png", new java.io.File(outFolder + "ours" + couleurs[i] + "_" + tolerances[i] + ".png"));
      ImageIO.write(img3, "png", new java.io.File(outFolder + "ours" + couleurs[i] +".png"));
      System.out.println("temps de calcul methode 1 : " + (interval - start) + "ms");
      long dist1 = Distance.distance(img, img2);
      long dist2 = Distance.distance(img, img3);
      System.out.println("distance methode 1 : " + dist1);
      System.out.println("distance methode 2 : " + dist2 + "\n");
    }
  }
}
