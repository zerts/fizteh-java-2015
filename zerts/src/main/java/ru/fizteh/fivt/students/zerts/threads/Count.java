package ru.fizteh.fivt.students.zerts.threads;

/**
 * Created by zerts on 03.12.2015.
 */
public class Count {
    private static volatile int currentId;
    private static volatile int numberOfThreads;
    private static class PrintThread extends Thread {
        private int id;
        PrintThread(int id) {
            this.id = id;
        }
        @Override
        public void run() {
            while (true) {
                if (currentId == this.id) {
                    System.out.println("Thread-" + this.id);
                    currentId = (currentId + 1) % numberOfThreads;
                    /*try {
                        Thread.sleep(TimeUnit.SECONDS.toMillis(1L));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                } else {
                    Thread.yield();
                }
            }
        }
    }

    public static void main(String[] argv) {
        numberOfThreads = new Integer(argv[0]);
        currentId = 0;
        for (int i = 0; i < numberOfThreads; i++) {
            PrintThread thread = new PrintThread(i);
            thread.start();
        }
    }
}
