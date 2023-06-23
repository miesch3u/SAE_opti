import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

public class CluteriseurDeCouleur {
    private BufferedImage image;
    private List<int[]> couleurs;
    private List<int[]> centroids;
    public HashMap<int[],List<int[]>> clusters;
    public List<int[]> couleursPossibles;

    public CluteriseurDeCouleur(int nbCouleurs, String path) {
        try {
            if (path.isEmpty()){
                path = "./images_etudiants/originale.jpg";
            }
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        centroids = new ArrayList<>();
        couleurs = new ArrayList<>();
        clusters = new HashMap<>();
        couleursPossibles = new ArrayList<>();
        for (int i = 0; i < nbCouleurs; i++) {
            int[] centroid = new int[3];
            Random rand = new Random();
            centroid[0] = rand.nextInt(255);
            centroid[1] = rand.nextInt(255);
            centroid[2] = rand.nextInt(255);
            centroids.add(centroid);
        }
        setCouleursPossibles();
        setCouleurs();
        setClusters();
    }

    public void setCouleursPossibles() {
        for (int i = 0; i<image.getWidth(); i++){
            for (int j = 0; j<image.getHeight(); j++){
                int r = (image.getRGB(i, j) >> 16) & 0xFF;
                int g = (image.getRGB(i, j) >> 8) & 0xFF;
                int b = image.getRGB(i, j) & 0xFF;
                int[] rgb = {r, g, b};
                couleursPossibles.add(rgb);
            }
        }
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
                int limit = couleursPossibles.size();
                centroid = couleursPossibles.get(rand.nextInt(limit));
                centroids.set(i,centroid);
            }
        }
    }

    public long getCoutMoyensDesClusters(){
        long cout = 0;
        for (int i = 0; i < centroids.size(); i++) {
            int[] centroid = centroids.get(i);
            for (int[] rgb : clusters.get(centroid)) {
                cout += Math.sqrt(Math.pow(centroid[0] - rgb[0], 2) + Math.pow(centroid[1] - rgb[1], 2) + Math.pow(centroid[2] - rgb[2], 2));
            }
        }
        return cout;
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        String path = args.length == 0 ? "./images_diverses_small/animaux/ours.png" : args[0];
        int nbCouleurs = args.length <= 1 ? 32 : Integer.parseInt(args[2]);
        String path2 = args.length <= 2 ? "./imgCompressees/copie.jpg" : args[1];
        CluteriseurDeCouleur cluteriseurDeCouleur = new CluteriseurDeCouleur(nbCouleurs,path);
        boolean convergence = false;
        BufferedImage img = cluteriseurDeCouleur.image;
        long cout = Long.MAX_VALUE;
        while (!convergence){
            cluteriseurDeCouleur.setCentroids();
            cluteriseurDeCouleur.setClusters();
            BufferedImage newImg = Mainq1.replaceByClosestColorInt(cluteriseurDeCouleur.image,cluteriseurDeCouleur.centroids);
            long newCout = cluteriseurDeCouleur.getCoutMoyensDesClusters();
            //long newCout = Distance.distance(cluteriseurDeCouleur.image,newImg)*arrondi/arrondi;
            if (cout <= newCout){
                convergence = true;
            }
            cout = newCout;
            //System.out.println("Cout : " + cout);
            img = newImg;
        }
        System.out.println("Temps d'execution : " + (System.currentTimeMillis() - startTime) + "ms");
        try {
            ImageIO.write(img, "jpg", new File(path2));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
