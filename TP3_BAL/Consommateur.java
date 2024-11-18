package TP3_BAL;

class Consommateur extends Thread {
    private Tampon tampon;

    // Constructeur
    public Consommateur(Tampon tampon) {
        this.tampon = tampon;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String lettre = tampon.retirer();
                System.out.println("Consommateur retire: " + lettre);
                if ("*".equals(lettre)) { // Arrêt si le caractère spécial est retiré
                    break;
                }
                Thread.sleep(200); // Simulation de temps
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

