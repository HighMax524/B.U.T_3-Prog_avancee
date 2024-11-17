package TP3_BAL;

import java.util.Scanner;

class Producteur extends Thread {
    private Bal bal;

    public Producteur(Bal bal) {
        this.bal = bal;
    }

    @Override
    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("Entrez une lettre (Q pour quitter) : ");
                String lettre = scanner.nextLine();
                bal.deposer(lettre);
                if ("Q".equals(lettre)) {
                    break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
