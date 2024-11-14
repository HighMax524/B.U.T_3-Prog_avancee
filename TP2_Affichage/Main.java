import java.io.*;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.BufferedReader;
import java.lang.String;

public class Main {

	public static void main(String[] args) {

		semaphoreBinaire semaphoreBinaire = new semaphoreBinaire(1);
		Affichage TA = new Affichage("AAA", semaphoreBinaire);
		Affichage TB = new Affichage("BB", semaphoreBinaire);
		Affichage TC = new Affichage("CCCC", semaphoreBinaire);
		Affichage TD = new Affichage("DDD", semaphoreBinaire);

		TA.start();
		TC.start();
		TB.start();
		TD.start();

	}

}
