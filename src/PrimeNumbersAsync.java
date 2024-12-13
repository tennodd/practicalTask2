import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

public class PrimeNumbersAsync {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter a number N (0 < N ≤ 1000): ");
        int N = scanner.nextInt();

        if (N <= 0 || N > 1000) {
            System.out.println("Invalid input. N must be between 1 and 1000.");
            return;
        }

        long startTime = System.currentTimeMillis();

        int numThreads = 4;

        int rangeSize = N / numThreads;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        CopyOnWriteArrayList<Integer> primesList = new CopyOnWriteArrayList<>();

        List<Future<List<Integer>>> futures = new ArrayList<>();

        for (int i = 0; i < numThreads; i++) {
            int start = i * rangeSize + 2;
            int end = (i == numThreads - 1) ? N : start + rangeSize - 1;

            PrimeCallable task = new PrimeCallable(start, end);
            Future<List<Integer>> future = executor.submit(task);
            futures.add(future);
        }

        for (Future<List<Integer>> future : futures) {
            try {
                List<Integer> primes = future.get();
                primesList.addAll(primes);
            } catch (CancellationException e) {
                System.out.println("A task was cancelled.");
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();

        primesList.sort(Integer::compareTo);

        System.out.println("Primes up to " + N + ":");
        for (Integer prime : primesList) {
            System.out.print(prime + " ");
        }
        System.out.println();

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

        if (n % 2 == 0 || n % 3 == 0)
            return false;

        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0)
                return false;
        }
        return true;
    }
}
