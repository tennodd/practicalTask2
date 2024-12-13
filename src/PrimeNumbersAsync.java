import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

public class PrimeNumbersAsync {
    public static void main(String[] args) {
        // Start measuring execution time
        long startTime = System.currentTimeMillis();

        Scanner scanner = new Scanner(System.in);

        // Ask user for N
        System.out.print("Enter a number N (0 < N ≤ 1000): ");
        int N = scanner.nextInt();

        // Validate N
        if (N <= 0 || N > 1000) {
            System.out.println("Invalid input. N must be between 1 and 1000.");
            return;
        }

        // Number of threads
        int numThreads = 4;

        // Calculate subranges
        int rangeSize = N / numThreads;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        // Shared CopyOnWriteArrayList for primes
        CopyOnWriteArrayList<Integer> primesList = new CopyOnWriteArrayList<>();

        List<Future<List<Integer>>> futures = new ArrayList<>();

        // Create and submit tasks
        for (int i = 0; i < numThreads; i++) {
            int start = i * rangeSize + 2;
            int end = (i == numThreads - 1) ? N : start + rangeSize - 1;

            PrimeCallable task = new PrimeCallable(start, end);
            Future<List<Integer>> future = executor.submit(task);
            futures.add(future);
        }

        // Check isDone() and collect results
        for (Future<List<Integer>> future : futures) {
            try {
                while (!future.isDone()) {
                    // You can implement timeout or cancellation logic here if needed
                    // For example, cancel the task if it takes too long
                    // future.cancel(true);
                }
                List<Integer> primes = future.get();
                primesList.addAll(primes);
            } catch (CancellationException e) {
                System.out.println("A task was cancelled.");
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Shutdown executor
        executor.shutdown();

        // Sort the primesList
        primesList.sort(Integer::compareTo);

        // Output the primes
        System.out.println("Primes up to " + N + ":");
        for (Integer prime : primesList) {
            System.out.print(prime + " ");
        }
        System.out.println();

        // Output execution time
        long endTime = System.currentTimeMillis();
        System.out.println("Execution time: " + (endTime - startTime) + " ms");
    }
}

class PrimeCallable implements Callable<List<Integer>> {
    private int start;
    private int end;

    public PrimeCallable(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public List<Integer> call() throws Exception {
        List<Integer> primes = new ArrayList<>();
        for (int number = start; number <= end; number++) {
            if (isPrime(number)) {
                primes.add(number);
            }
        }
        return primes;
    }

    private boolean isPrime(int n) {
        if (n <= 1)
            return false;
        if (n <= 3)
            return true;

        // Skip even numbers and multiples of 3
        if (n % 2 == 0 || n % 3 == 0)
            return false;

        // Check for factors up to sqrt(n)
        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0)
                return false;
        }
        return true;
    }
}
