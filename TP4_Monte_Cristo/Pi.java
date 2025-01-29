package TP4_Monte_Cristo;
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

	for(int i = 0; i <repetition; i ++){
		long singleWorkerTime = master.doRun(nThrows, 1);
		long multiWorkerTime = master.doRun(nThrows, nWorkers);

		totalSingleWorkerTime += singleWorkerTime;
		totalMultiWorkerTime += multiWorkerTime;
	}

	System.out.println("Total tps 1 worker : " + totalSingleWorkerTime);
	System.out.println("Total tps plusieurs worker : " + totalMultiWorkerTime);

	double moySingleWorkerTime = totalSingleWorkerTime / repetition;
	System.out.println("Temps moyen pour 1 processeur: " + moySingleWorkerTime);

	double moyMultiWorkerTime = totalMultiWorkerTime / repetition;
	System.out.println("Temps moyen pour " + nWorkers + " processeur: " + moyMultiWorkerTime);

	double speedup = (double) moySingleWorkerTime/ moyMultiWorkerTime;
	System.out.println("Speedup : " + speedup);

	try (FileWriter fileWriter = new FileWriter("result_pi.txt", true);
	PrintWriter printWriter = new PrintWriter(fileWriter)){
		printWriter.printf("Speedup: " + speedup +", Temps 1 worker: " + moySingleWorkerTime + ", Temps avec " + nWorkers +" worker: "+ moyMultiWorkerTime + "\n");
	} catch(IOException e){
		e.printStackTrace();
	}
    }
}

/**
 * Creates workers to run the Monte Carlo simulation
 * and aggregates the results.
 */
class Master {
    public long doRun(int totalCount, int numWorkers) throws InterruptedException, ExecutionException 
    {

	long startTime = System.currentTimeMillis();

	// Create a collection of tasks
	List<Callable<Long>> tasks = new ArrayList<Callable<Long>>();
	for (int i = 0; i < numWorkers; ++i) 
	    {
		tasks.add(new Worker(totalCount));
	    }
    
	// Run them and receive a collection of Futures
	ExecutorService exec = Executors.newFixedThreadPool(numWorkers);
	List<Future<Long>> results = exec.invokeAll(tasks);
	long total = 0;
    
	// Assemble the results.
	for (Future<Long> f : results)
	    {
		// Call to get() is an implicit barrier.  This will block
		// until result from corresponding worker is ready.
		total += f.get();
	    }
	double pi = 4.0 * total / totalCount / numWorkers;

	long stopTime = System.currentTimeMillis();

	double errRelative = (Math.abs((pi - Math.PI)) / Math.PI);
	long duration = stopTime - startTime;

	System.out.println("Approx value: : " + pi );
	System.out.println("Difference to exact value of pi: " + (pi - Math.PI));
	System.out.println("% Error: " + (pi - Math.PI) / Math.PI * 100 + " %");
	System.out.println("Error: " + errRelative +"\n");

	System.out.println("Ntot: " + totalCount*numWorkers);
	System.out.println("Available processors: " + numWorkers);
	System.out.println("Time Duration (ms): " + duration + "\n");

	try (FileWriter fileWriter = new FileWriter("result_pi.txt", true);
	PrintWriter printWriter = new PrintWriter(fileWriter)){
		printWriter.printf("Erreur relative: %.6f, nthrows: %d, nombre processus: %d, temps : %dms%n", errRelative, totalCount, numWorkers, duration);
	} catch(IOException e){
		e.printStackTrace();
	}

	exec.shutdown();
	return duration;
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
