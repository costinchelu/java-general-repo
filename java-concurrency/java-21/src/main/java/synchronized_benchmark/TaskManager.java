package synchronized_benchmark;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class TaskManager {

    private ThreadFactory threadFactory;

    private final int threads;

    public TaskManager(ThreadType threadType, int threads) {
        this.threads = threads;
        switch (threadType) {
            case VIRTUAL -> this.threadFactory = Thread.ofVirtual().factory();
            case PLATFORM -> this.threadFactory = Thread.ofPlatform().factory();
        }
    }

    public void start() {
        try (ExecutorService executor = Executors.newFixedThreadPool(this.threads, this.threadFactory)) {
            for (int j = 0; j < this.threads; j++) {
                TaskExecutor taskExecutor = new TaskExecutor();
                executor.submit(
                        () -> {
                            try {
                                taskExecutor.execute();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        });
            }
        }
    }
}
