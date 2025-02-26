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
		System.out.println("Approx value:" + value);
		System.out.println("Difference to exact value of pi: " + (value - Math.PI));
		System.out.println("% Error: " + (value - Math.PI) / Math.PI * 100 + " %");
		System.out.println("Error: " + (Math.abs((value - Math.PI)) / Math.PI) + "\n");

		System.out.println("\nAvailable processors: " + nProcessors);
		System.out.println("Time Duration: " + (stopTime - startTime) + "ms");

		System.out.println((Math.abs((value - Math.PI)) / Math.PI) + " " + nThrows + " " + nProcessors + " " + (stopTime - startTime));

		// Writing results to result_assignement102.txt
		try (FileWriter fileWriter = new FileWriter("result_assignement102.txt", false); // Open file in append mode
			 PrintWriter printWriter = new PrintWriter(fileWriter)) {
			printWriter.printf("Approx value: %.6f\n", value); // Format with 6 decimal places
			printWriter.printf("Difference to exact value of pi: %.6f\n", (value - Math.PI)); // Same for difference
			printWriter.printf("%% Error: %.6f %%\n", (value - Math.PI) / Math.PI * 100); // Format percentage error
			printWriter.printf("Error: %.6f\n", (Math.abs((value - Math.PI)) / Math.PI)); // Format error
			printWriter.printf("N Throws: %d, Available processors: %d\n", nThrows, nProcessors); // Integer formatting
			printWriter.printf("Time Duration: %d ms\n\n", (stopTime - startTime)); // Time in ms
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
