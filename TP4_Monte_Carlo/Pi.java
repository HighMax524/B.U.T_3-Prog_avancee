package TP4_Monte_Carlo;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Approximates PI using the Monte Carlo method.  Demonstrates
 * use of Callables, Futures, and thread pools.
 */
public class Pi 
{
    public static void main(String[] args) throws Exception 
    {
	Scanner scanner = new Scanner(System.in);

	System.out.println("enter the number of workers");
	int nWorkers = scanner.nextInt();

	System.out.println("enter the number of throws");
	int nThrows = scanner.nextInt();

	Master master = new Master();

	int repetition = 10;
	long totalSingleWorkerTime = 0;
	long totalMultiWorkerTime = 0;

	for(int j = 0; j <repetition; j ++){
		long singleWorkerTime = master.doRun(nThrows, 1);

		totalSingleWorkerTime += singleWorkerTime;
	}

	double moySingleWorkerTime = (double) totalSingleWorkerTime / repetition;
		System.out.println("Temps moyen pour 1 processeur: " + moySingleWorkerTime);

	try (FileWriter fileWriter = new FileWriter("result_pi.txt", false);
		PrintWriter printWriter = new PrintWriter(fileWriter)){
			printWriter.printf("Speedup: " + 1 +", Temps 1 worker: " + moySingleWorkerTime + "\n \n");
		} catch(IOException e){
			e.printStackTrace();
		}

	for (int nworkersCours = 2; nworkersCours <= nWorkers; nworkersCours +=2){
		totalMultiWorkerTime = 0;

		for(int j = 0; j <repetition; j ++){
			long multiWorkerTime = master.doRun(nThrows / nworkersCours, nworkersCours);

			totalMultiWorkerTime += multiWorkerTime;
		}

		System.out.println("Total tps 1 worker : " + totalSingleWorkerTime);
		System.out.println("Total tps plusieurs worker : " + totalMultiWorkerTime);


		double moyMultiWorkerTime = (double)totalMultiWorkerTime / repetition;
		System.out.println("Temps moyen pour " + nworkersCours + " processeur: " + moyMultiWorkerTime);

		double speedup = moySingleWorkerTime/ moyMultiWorkerTime;
		System.out.println("Speedup : " + speedup);

		try (FileWriter fileWriter = new FileWriter("result_pi.txt", true);
		PrintWriter printWriter = new PrintWriter(fileWriter)){
			printWriter.printf("Speedup: " + speedup +", Temps 1 worker: " + moySingleWorkerTime + ", Temps avec " + nworkersCours +" worker: "+ moyMultiWorkerTime + "\n \n");
		} catch(IOException e){
			e.printStackTrace();
		}
	}


    }
}

/**
 * Task for running the Monte Carlo simulation.
 */
class Worker implements Callable<Long> 
{   
    private int numIterations;
    public Worker(int num) 
	{ 
	    this.numIterations = num; 
	}

  @Override
      public Long call() 
      {
	  long circleCount = 0;
	  Random prng = new Random ();
	  for (int j = 0; j < numIterations; j++) 
	      {
		  double x = prng.nextDouble();
		  double y = prng.nextDouble();
		  if ((x * x + y * y) < 1)  ++circleCount;
	      }
	  return circleCount;
      }
}
