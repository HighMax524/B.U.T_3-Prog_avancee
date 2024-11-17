package TP3_BAL;

public class Main {
    public static void main(String[] args) {
        Bal bal = new Bal();
        Producteur producteur = new Producteur(bal);
        Consommateur consommateur = new Consommateur(bal);

        producteur.start();
        consommateur.start();
    }
}

