import java.util.concurrent.Semaphore;

// Class representing a philosopher
class Philosopher implements Runnable {
    private final int id;  // Philosopher ID
    private final Semaphore leftFork;  // Left fork semaphore
    private final Semaphore rightFork; // Right fork semaphore
    private final Semaphore maxDiners; // Semaphore to limit the number of diners

    public Philosopher(int id, Semaphore leftFork, Semaphore rightFork, Semaphore maxDiners) {
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.maxDiners = maxDiners;
    }

    // Simulates the philosopher thinking
    private void think() throws InterruptedException {
        System.out.println("Philosopher " + id + " is thinking...");
        Thread.sleep((long) (Math.random() * 1000));  // Sleep for random time
    }

    // Simulates the philosopher eating
    private void eat() throws InterruptedException {
        System.out.println("Philosopher " + id + " is eating...");
        Thread.sleep((long) (Math.random() * 1000));  // Sleep for random time
    }

    // The core logic for each philosopher thread
    @Override
    public void run() {
        try {
            while (true) {
                // Philosopher is thinking
                think();

                // Limit the number of philosophers allowed to pick up forks at once
                maxDiners.acquire();

                // Try to pick up the left fork
                if (leftFork.tryAcquire()) {
                    System.out.println("Philosopher " + id + " picked up the left fork.");

                    // Try to pick up the right fork
                    if (rightFork.tryAcquire()) {
                        System.out.println("Philosopher " + id + " picked up the right fork.");

                        // Philosopher is eating
                        eat();

                        // Put down the right fork after eating
                        rightFork.release();
                        System.out.println("Philosopher " + id + " put down the right fork.");

                        // Put down the left fork after eating
                        leftFork.release();
                        System.out.println("Philosopher " + id + " put down the left fork.");
                    } else {
                        // Could not pick up the right fork, release the left fork
                        leftFork.release();
                        System.out.println("Philosopher " + id + " put down the left fork and will retry.");
                    }
                }

                // Leave the dining room
                maxDiners.release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
