package TP3_BAL;

public class Bal {
    private String lettre = null;

    public void deposer(String lettre) throws InterruptedException {
        synchronized(this){
        while (this.lettre != null) {
            wait();
        }
        this.lettre = lettre;
        notifyAll();
    }
    }

    public String retirer() throws InterruptedException {
        synchronized(this){
        while (this.lettre == null) {
            wait();
        }
        String lettreRetiree = this.lettre;
        this.lettre = null;
        notifyAll();
        return lettreRetiree;
    }
    }
}
