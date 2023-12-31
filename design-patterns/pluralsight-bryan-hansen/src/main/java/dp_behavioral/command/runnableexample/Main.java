package dp_behavioral.command.runnableexample;

public class Main {

    public static void main(String[] args) {

        Task task1 = new Task(10, 12);  // encapsulates request
        Task task2 = new Task(11, 13);

        Thread thread1 = new Thread(task1);
        thread1.start();    // invoker

        Thread thread2 = new Thread(task2);
        thread2.start();    // invoker

    }
}

class Task implements Runnable {

    int num1;
    int num2;

    public Task(int num1, int num2) {
        this.num1 = num1;
        this.num2 = num2;
    }

    @Override
    public void run() {
        System.out.println(num1 * num2);    // receiver
    }
}

/*
- encapsulates each request as an object
- decouple sender from processor
- object oriented callback
- often used for "undo" functionality

java.lang.Runnable
javax.swing.Action
 */
