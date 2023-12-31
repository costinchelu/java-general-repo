package lesson_24;


class Worker implements Runnable {

    private volatile boolean terminated;

    @Override
    public void run() {

        while (!terminated) {
            System.out.println("Working class is running...");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public boolean isTerminated() {
        return terminated;
    }

    public void setTerminated(boolean terminated) {
        this.terminated = terminated;
    }
}


public class Volatile {

    public static void main(String[] args) {

        Worker worker = new Worker();

        Thread t1 = new Thread(worker);
        t1.start();

        try {
            Thread.sleep(900);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        worker.setTerminated(true);
        System.out.println("Algorithm is terminated...");

    }
}
