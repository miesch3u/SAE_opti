import javax.imageio.ImageIO;
import java.awt.*;
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
    public List<int[]> couleursPossibles;

    public void debutDuConstructeur(String path){
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        centroids = new ArrayList<>();
        couleurs = new ArrayList<>();
        clusters = new HashMap<>();
        couleursPossibles = new ArrayList<>();
    }

    public CluteriseurDeCouleur(int nbCouleurs, String path) {
        debutDuConstructeur(path);
        for (int i = 0; i < nbCouleurs; i++) {
            int[] centroid = new int[3];
            Random rand = new Random();
            centroid[0] = rand.nextInt(255);
            centroid[1] = rand.nextInt(255);
            centroid[2] = rand.nextInt(255);
            centroids.add(centroid);
        }
        setCouleursPossiblesEtCouleurs();
        setClusters();
    }

    public CluteriseurDeCouleur(String path, List<int[]> centroids) {
        debutDuConstructeur(path);
        this.centroids = centroids;
        setCouleursPossiblesEtCouleurs();
        setClusters();
    }

    public void setCouleursPossiblesEtCouleurs() {
        for (int i = 0; i<image.getWidth(); i++){
            for (int j = 0; j<image.getHeight(); j++){
                int r = (image.getRGB(i, j) >> 16) & 0xFF;
                int g = (image.getRGB(i, j) >> 8) & 0xFF;
                int b = image.getRGB(i, j) & 0xFF;
                int[] rgb = {r, g, b};
                couleursPossibles.add(rgb);
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

    public static BufferedImage calculerImage(String[] args){
        long startTime = System.currentTimeMillis();
        int nbCouleurs = args.length == 0 ? 64 : Integer.parseInt(args[0]);
        String path = args.length <= 1 ? "./images_diverses_small/animaux/poulpe.png" : args[1];
        String path2 = args.length <= 2 ? "./imgCompressees/copie.jpg" : args[2];
        boolean better = args.length <= 3 ? false : Boolean.parseBoolean(args[3]);
        System.out.println(better);
        CluteriseurDeCouleur cluteriseurDeCouleur;
        BufferedImage image;
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(better){
            double tolerance = 170.48*Math.pow(nbCouleurs,-0.458);
            System.out.println(tolerance);
            Color[] colors = MainQ1Sae.calculerClusters(image,nbCouleurs,tolerance);
            List<int[]> couleurs = new ArrayList<>();
            for (Color color : colors) {
                int[] rgb = {color.getRed(),color.getGreen(),color.getBlue()};
                couleurs.add(rgb);
            }
            cluteriseurDeCouleur = new CluteriseurDeCouleur(path,couleurs);
        }else{
            cluteriseurDeCouleur = new CluteriseurDeCouleur(nbCouleurs,path);
        }
        boolean convergence = false;
        BufferedImage img = cluteriseurDeCouleur.image;
        long cout = Long.MAX_VALUE;
        int i = 0;
        while (!convergence){
            //System.out.println("Iteration : " + i++);
            cluteriseurDeCouleur.setCentroids();
            cluteriseurDeCouleur.setClusters();
            BufferedImage newImg = Mainq1.replaceByClosestColorInt(cluteriseurDeCouleur.image,cluteriseurDeCouleur.centroids);
            long newCout = cluteriseurDeCouleur.getCoutMoyensDesClusters();
            if (cout <= newCout){
                convergence = true;
            }
            cout = newCout;
            //System.out.println("Cout : " + cout);
            img = newImg;
        }
        System.out.println("Temps d'execution : " + (System.currentTimeMillis() - startTime) + "ms");
        if(!path.isEmpty()){
            try {
                ImageIO.write(img, "jpg", new File(path2));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return img;
    }

    public static void main(String[] args) {
        calculerImage(args);
    }
}
