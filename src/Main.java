import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {
        int numPhilosophers = 5;  // Number of philosophers
        Semaphore[] forks = new Semaphore[numPhilosophers];  // Forks as semaphores
        Semaphore maxDiners = new Semaphore(numPhilosophers - 1);  // Allow up to n-1 philosophers to pick forks at the same time

        // Initialize forks as semaphores with 1 permit each
        for (int i = 0; i < numPhilosophers; i++) {
            forks[i] = new Semaphore(1);
        }

        // Create and start philosopher threads
        Philosopher[] philosophers = new Philosopher[numPhilosophers];
        Thread[] philosopherThreads = new Thread[numPhilosophers];

        for (int i = 0; i < numPhilosophers; i++) {
            Semaphore leftFork = forks[i];
            Semaphore rightFork = forks[(i + 1) % numPhilosophers];

            // For even philosophers, pick up the left fork first; for odd philosophers, pick up the right fork first
            if (i % 2 == 0) {
                philosophers[i] = new Philosopher(i, leftFork, rightFork, maxDiners);
            } else {
                philosophers[i] = new Philosopher(i, rightFork, leftFork, maxDiners);
            }

            // Start the philosopher thread
            philosopherThreads[i] = new Thread(philosophers[i], "Philosopher " + i);
            philosopherThreads[i].start();
        }

        // Optionally, we can join the threads if we want to wait for them to complete (here, we let them run indefinitely)
        for (Thread t : philosopherThreads) {
            try {
                t.join();  // Ensure the main thread waits for the philosopher threads
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
