package TP3_BAL;

public class Main {
    public static void main(String[] args) {
        Tampon tampon = new Tampon(5); // Tampon de taille 5
        Producteur producteur = new Producteur(tampon);
        Consommateur consommateur = new Consommateur(tampon);

        producteur.start();
        consommateur.start();
    }
}


