package TP4_Monte_Carlo;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

class PiMonteCarloScalFaible {
	AtomicInteger nAtomSuccess;
	int nThrows;
	double value;
	AtomicInteger throwsCompleted;  // Compteur de lancers effectués

	class MonteCarlo implements Runnable {
		@Override
		public void run() {
			// Effectuer le calcul du Monte Carlo
			double x = Math.random();
			double y = Math.random();
			if (x * x + y * y <= 1)
				nAtomSuccess.incrementAndGet();

			// Incrémenter le compteur de lancers
			throwsCompleted.incrementAndGet();
		}
	}

	public PiMonteCarloScalFaible(int nThrows) {
		this.nAtomSuccess = new AtomicInteger(0);
		this.nThrows = nThrows;
		this.value = 0;
		this.throwsCompleted = new AtomicInteger(0); // Initialisation du compteur de lancers
	}

	public double getPi(int nProcessors) {
		int totalThrows = nThrows * nProcessors;  // Nombre total de lancers à effectuer

		ExecutorService executor = Executors.newWorkStealingPool(nProcessors);
		for (int i = 1; i <= totalThrows; i++) {
			Runnable worker = new MonteCarlo();
			executor.execute(worker);
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		value = 4.0 * nAtomSuccess.get() / totalThrows;
		return value;
	}
}

public class Assignment102ScalFaible {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Enter the number of processors:");
		int nProcessors = scanner.nextInt();  // Nombre de processeurs

		System.out.println("Enter the number of throws per processor:");
		int nThrows = scanner.nextInt();  // Nombre de lancers par processeur

		PiMonteCarloScalFaible PiVal = new PiMonteCarloScalFaible(nThrows);
		long startTime = System.currentTimeMillis();
		double value = PiVal.getPi(nProcessors);  // Calcul de Pi avec nProcessors processeurs
		long stopTime = System.currentTimeMillis();
		double singleWorkerTime = stopTime - startTime;

		// Affichage des résultats
		System.out.println("Approx value: " + value);
		System.out.println("Difference to exact value of pi: " + (value - Math.PI));
		System.out.println("% Error: " + (value - Math.PI) / Math.PI * 100 + " %");
		System.out.println("Error: " + (Math.abs((value - Math.PI)) / Math.PI) + "\n");

		// Ecriture des résultats dans un fichier
		try (FileWriter fileWriter = new FileWriter("result_assignement102.txt", false); // Ouvre le fichier en mode ajout
			 PrintWriter printWriter = new PrintWriter(fileWriter)) {

			// Ecrire le premier résultat (temps avec un seul worker)
			printWriter.printf("Speedup: 1, Temps 1 worker: %.1f, Total Throws: %d\n\n", singleWorkerTime, nThrows);

			// Pour les autres processors, nous avons un tableau pour tester la variation
			for (int i = 2; i <= nProcessors; i++) {
				long currentTime = System.currentTimeMillis();
				PiMonteCarloScalFaible PiValCurrent = new PiMonteCarloScalFaible(nThrows); // Réinstancier l'objet pour tester à nouveau
				PiValCurrent.getPi(i);
				long currentStopTime = System.currentTimeMillis();
				double multiWorkerTime = currentStopTime - currentTime;

				// Calcul du Speedup
				double speedup = singleWorkerTime / multiWorkerTime;

				// Ecrire les résultats dans le fichier, avec le nombre de lancers effectués
				int totalThrows = nThrows * i;
				printWriter.printf("Speedup: %.16f, Temps 1 worker: %.1f, Temps avec %d worker: %.1f, Total Throws: %d\n\n",
						speedup, singleWorkerTime, i, multiWorkerTime, totalThrows);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
