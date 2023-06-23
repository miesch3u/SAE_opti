import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Distance {

  public static void main(String[] args) throws IOException {
    String[] noms = {"coul_2.png", "coul_3.png", "coul_5.png", "coul_10.png", "coul_20.png", "copie_pixels.png", "copie.png","copie_proche_OB.png","copie_proche_WB.png"
            ,"copie_proche_YG.png","copie_proche_YGW.png","copie_proche_YGWO.png","copie_proche_YGWOP.png","copie_proche_YGWOPB.png","copie_rouge.png","copie_vert_bleu.png"};
    BufferedImage original = ImageIO.read(new File("./images_etudiants/originale.jpg"));
    for (String nom : noms) {
      BufferedImage img = ImageIO.read(new File("./images_etudiants/" + nom));
      System.out.println(nom + " : " + distance(original, img));
    }

    String[] noms2 = {"fleur.png", "fleur2.png", "fleur3.png", "fleur4.png", "fleur5.png", "fleur6.png", "fleur7.png", "fleur8.png"};
    for (String nom : noms2) {
      BufferedImage img = ImageIO.read(new File(nom));
      System.out.println(nom + " : " + distance(original, img));
    }

  }

  public static long distance(BufferedImage img1, BufferedImage img2) {
    long res = 0;
    for (int i = 0; i< img1.getHeight(); i++){
      for (int j = 0; j< img1.getWidth(); j++){
        int[] rgb1 = Mainq1.rgbToInt(img1.getRGB(j, i));
        int[] rgb2 = Mainq1.rgbToInt(img2.getRGB(j, i));
        res += Math.pow(rgb1[0] - rgb2[0],2) + Math.pow(rgb1[1] - rgb2[1],2) + Math.pow(rgb1[2] - rgb2[2],2);
      }
    }
    return res;
  }
}
