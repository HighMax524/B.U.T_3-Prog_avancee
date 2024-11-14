/**
 * 
 */
import java.io.*;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.BufferedReader;
import java.lang.String;


public class Affichage extends Thread{
	String texte; 
	semaphoreBinaire semaphoreBinaire;
        
	public Affichage (String txt, semaphoreBinaire semaphoreBinaire){
		texte=txt;
		this.semaphoreBinaire = semaphoreBinaire;
	}
	
	public void run(){
	    try {
            semaphoreBinaire.syncWait(); // Décrémente le sémaphore pour obtenir l'accès
            for (int i = 0; i < texte.length(); i++) {
                System.out.print(texte.charAt(i));
                try {
                    sleep(100);
                } catch (InterruptedException e) {}
            }
        } finally {
            semaphoreBinaire.syncSignal(); // Incrémente le sémaphore pour libérer l'accès
        }
    }
}

