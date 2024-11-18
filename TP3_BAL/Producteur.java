package TP3_BAL;

class Producteur extends Thread {
    private Tampon tampon;

    // Constructeur qui accepte un objet Tampon
    public Producteur(Tampon tampon) {
        this.tampon = tampon;
    }

    @Override
    public void run() {
        try {
            for (char lettre = 'A'; lettre <= 'Z'; lettre++) {
                System.out.println("Producteur dépose: " + lettre);
                tampon.deposer(String.valueOf(lettre));
                Thread.sleep(100);
            }
            tampon.deposer("*");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

