package TP3_BAL;

class Tampon {
    private final String[] buffer;
    private int tete = 0, queue = 0, nbLettres = 0;

    public Tampon(int taille) {
        buffer = new String[taille];
    }

    public synchronized void deposer(String lettre) throws InterruptedException {
        while (nbLettres == buffer.length) { // Tampon plein
            wait();
        }
        buffer[queue] = lettre;
        queue = (queue + 1) % buffer.length;
        nbLettres++;
        notifyAll();
    }

    public synchronized String retirer() throws InterruptedException {
        while (nbLettres == 0) { // Tampon vide
            wait();
        }
        String lettre = buffer[tete];
        tete = (tete + 1) % buffer.length;
        nbLettres--;
        notifyAll();
        return lettre;
    }
}
