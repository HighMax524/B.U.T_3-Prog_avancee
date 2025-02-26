package TP4_Monte_Carlo;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

class PiMonteCarlo {
	AtomicInteger nAtomSuccess;
	int nThrows;
	double value;

	class MonteCarlo implements Runnable {
		@Override
		public void run() {
			double x = Math.random();
			double y = Math.random();
			if (x * x + y * y <= 1)
				nAtomSuccess.incrementAndGet();
		}
	}

	public PiMonteCarlo(int nThrows) {
		this.nAtomSuccess = new AtomicInteger(0);
		this.nThrows = nThrows;
		this.value = 0;
	}

	public double getPi(int nProcessors) {
		ExecutorService executor = Executors.newWorkStealingPool(nProcessors);
		for (int i = 1; i <= nThrows; i++) {
			Runnable worker = new MonteCarlo();
			executor.execute(worker);
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		value = 4.0 * nAtomSuccess.get() / nThrows;
		return value;
	}
}
public class Assignment102 {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		System.out.println("enter the number of processor");
		int nProcessors = scanner.nextInt();

		System.out.println("enter the number of throws");
		int nThrows = scanner.nextInt();

		PiMonteCarlo PiVal = new PiMonteCarlo(nThrows);
		long startTime = System.currentTimeMillis();
		double value = PiVal.getPi(nProcessors);
		long stopTime = System.currentTimeMillis();
		double singleWorkerTime = stopTime - startTime;
		System.out.println("Approx value: " + value);
		System.out.println("Difference to exact value of pi: " + (value - Math.PI));
		System.out.println("% Error: " + (value - Math.PI) / Math.PI * 100 + " %");
		System.out.println("Error: " + (Math.abs((value - Math.PI)) / Math.PI) + "\n");

		// Ecriture des résultats dans le fichier result_assignement102.txt
		try (FileWriter fileWriter = new FileWriter("result_assignement102.txt", false); // Ouvre le fichier en mode ajout
			 PrintWriter printWriter = new PrintWriter(fileWriter)) {

			// Ecrire le premier résultat (temps avec un seul worker)
			printWriter.printf("Speedup: 1, Temps 1 worker: %.1f\n\n", singleWorkerTime); // Format pour 1 worker

			// Pour les autres processors, nous avons un tableau pour tester la variation
			for (int i = 2; i <= nProcessors; i++) {
				long currentTime = System.currentTimeMillis();
				PiMonteCarlo PiValCurrent = new PiMonteCarlo(nThrows); // Réinstancier l'objet pour tester à nouveau
				PiValCurrent.getPi(i);
				long currentStopTime = System.currentTimeMillis();
				double multiWorkerTime = currentStopTime - currentTime;

				// Calcul du Speedup
				double speedup = singleWorkerTime / multiWorkerTime;
				printWriter.printf("Speedup: %.16f, Temps 1 worker: %.1f, Temps avec %d worker: %.1f\n\n",
						speedup, singleWorkerTime, i, multiWorkerTime);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
