package TP3_BAL;

class Producteur extends Thread {
    private Bal bal;

    public Producteur(Bal bal) {
        this.bal = bal;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 6; i++) {
                String lettre = "Lettre " + i;
                System.out.println("Producteur dépose: " + lettre);
                bal.deposer(lettre);
                Thread.sleep(100); 
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

