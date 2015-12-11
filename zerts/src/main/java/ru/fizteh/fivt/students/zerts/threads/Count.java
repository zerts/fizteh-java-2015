package ru.fizteh.fivt.students.zerts.threads;

/**
 * Created by zerts on 03.12.2015.
 */
public class Count {

    private static class Caller {

        private int currentId = 0;

        private class PrintThread implements Runnable {
            private int numberOfThreads;
            private int id;
            PrintThread(int id, int numberOfThreads) {
                this.id = id;
                this.numberOfThreads = numberOfThreads;
            }

            @Override
            public void run() {
                while (true) {
                    synchronized (Caller.this) {
                        while (currentId != this.id) {
                            try {
                                Caller.this.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        System.out.println("Thread-" + this.id);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        currentId = (currentId + 1) % numberOfThreads;
                        Caller.this.notifyAll();
                    }
                }
            }
        }

        public void startCount(int numberOfThreads) {
            for (int i = 0; i < numberOfThreads; i++) {
                (new Thread(new PrintThread(i, numberOfThreads))).start();
            }
        }
    }

    public static void main(String[] argv) {
        (new Caller()).startCount(new Integer(argv[0]));
    }
}
