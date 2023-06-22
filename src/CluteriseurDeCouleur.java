import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class CluteriseurDeCouleur {
    private BufferedImage image;
    private List<int[]> couleurs;
    private List<int[]> centroids;
    public HashMap<int[],List<int[]>> clusters;

    public CluteriseurDeCouleur(int nbCouleurs) {
        try {
            image = ImageIO.read(new File("./images_etudiants/copie.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        centroids = new ArrayList<>();
        couleurs = new ArrayList<>();
        clusters = new HashMap<>();
        for (int i = 0; i < nbCouleurs; i++) {
            int[] centroid = new int[3];
            Random rand = new Random();
            centroid[0] = rand.nextInt(255);
            centroid[1] = rand.nextInt(255);
            centroid[2] = rand.nextInt(255);
            centroids.add(centroid);
        }
        setCouleurs();
        setClusters();
    }

    public void setCouleurs() {
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int r = (image.getRGB(i, j) >> 16) & 0xFF;
                int g = (image.getRGB(i, j) >> 8) & 0xFF;
                int b = image.getRGB(i, j) & 0xFF;
                int[] rgb = {r, g, b};
                couleurs.add(rgb);
            }
        }
    }

    public int indiceCentroidePlusProche(int r,int g,int b){
        int indice = 0;
        int distance = Integer.MAX_VALUE;
        for (int i = 0; i < centroids.size(); i++) {
            int[] centroid = centroids.get(i);
            int distanceCentroid = (int) Math.sqrt(Math.pow(centroid[0] - r, 2) + Math.pow(centroid[1] - g, 2) + Math.pow(centroid[2] - b, 2));
            if (distanceCentroid < distance) {
                distance = distanceCentroid;
                indice = i;
            }
        }
        return indice;
    }

    public void setClusters(){
        for (int i = 0; i < centroids.size(); i++) {
            clusters.put(centroids.get(i),new ArrayList<>());
        }
        for (int i = 0; i < couleurs.size(); i++) {
            int[] rgb = couleurs.get(i);
            int indice = indiceCentroidePlusProche(rgb[0],rgb[1],rgb[2]);
            clusters.get(centroids.get(indice)).add(rgb);
        }
    }

    public void setCentroids(){
        int[] centroid;
        Random rand = new Random();
        for (int i = 0; i < centroids.size(); i++) {
            centroid = centroids.get(i);
            int[] rgb = {0,0,0};
            if(clusters.get(centroid).size() != 0){
                for (int[] rgbCluster : clusters.get(centroid)) {
                    rgb[0] += rgbCluster[0];
                    rgb[1] += rgbCluster[1];
                    rgb[2] += rgbCluster[2];
                }
                rgb[0] /= clusters.get(centroid).size();
                rgb[1] /= clusters.get(centroid).size();
                rgb[2] /= clusters.get(centroid).size();
                centroids.set(i,rgb);
            }else {
                centroid[0] = rand.nextInt(255);
                centroid[1] = rand.nextInt(255);
                centroid[2] = rand.nextInt(255);
                centroids.set(i,centroid);
            }
        }
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        int nbCouleurs = args.length == 0 ? 31 : Integer.parseInt(args[0]);
        CluteriseurDeCouleur cluteriseurDeCouleur = new CluteriseurDeCouleur(nbCouleurs);
        boolean convergence = false;
        BufferedImage img = cluteriseurDeCouleur.image;
        long distance = Long.MAX_VALUE;
        while (!convergence){
            cluteriseurDeCouleur.setCentroids();
            cluteriseurDeCouleur.setClusters();
            BufferedImage newImg = Mainq1.replaceByClosestColorInt(cluteriseurDeCouleur.image,cluteriseurDeCouleur.centroids);
            long newDistance = Distance.distance(img,newImg);
            if (distance == newDistance){
                convergence = true;
            }
            long arrondi = (long) Math.pow(nbCouleurs,100000);
            distance = (newDistance/arrondi)*arrondi;
            img = newImg;
        }
        System.out.println("Temps d'execution : " + (System.currentTimeMillis() - startTime) + "ms");
        try {
            ImageIO.write(img, "png", new File("./copie.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
