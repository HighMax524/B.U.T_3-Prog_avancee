package TP3_BAL;

class Consommateur extends Thread {
    private Bal bal;

    public Consommateur(Bal bal) {
        this.bal = bal;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String lettre = bal.retirer();
                System.out.println("Consommateur retire: " + lettre);
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
