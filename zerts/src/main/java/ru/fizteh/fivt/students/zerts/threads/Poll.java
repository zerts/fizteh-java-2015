package ru.fizteh.fivt.students.zerts.threads;

import java.util.Random;

/**
 * Created by zerts on 03.12.2015.
 */
public class Poll {
    private static final int FULL_PROBABILITY = 100;
    private static final int NO_PROBABILITY = 50;
    private static class Caller {
        private int numberOfThreads;
        private boolean endOfPoll = false;
        private int numberOfReadyVoices = 0;
        private volatile int lastID;
        private Random random;

        private class Voice implements Runnable {
            private int id;
            public boolean isAnswer() {
                return answer;
            }

            private boolean answer;

            Voice(int id) {
                this.id = id;
            }

            @Override
            public void run() {
                while (true) {
                    random = new Random();
                    synchronized (Caller.this) {
                        //System.out.println(id);
                        //System.out.println(lastID);
                        while (id != lastID) {
                            if (numberOfReadyVoices == numberOfThreads && endOfPoll) {
                                return;
                            }
                            try {
                                Caller.this.wait();
                            } catch (InterruptedException e) {
                                System.err.printf("Interrupted\n");
                                return;
                            }
                        }
                        lastID = id + 1;
                        //System.out.println("rand = " + random.nextInt());
                        if (random.nextInt(FULL_PROBABILITY) < NO_PROBABILITY) {
                            System.out.println("NO");
                            endOfPoll = false;
                        } else {
                            System.out.println("YES");
                        }
                        numberOfReadyVoices++;
                        Caller.this.notifyAll();
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //System.out.println("Ready = " + numberOfReadyVoices);
                    }
                }
            }
        }

        public void start(int numberOfThreads) {
            this.numberOfThreads = numberOfThreads;
            numberOfReadyVoices = 0;
            for (int i = 0; i < numberOfThreads; i++) {
                (new Thread(new Voice(i))).start();
            }
            numberOfReadyVoices = numberOfThreads;
            while (true) {
                synchronized (this) {
                    while (numberOfReadyVoices != numberOfThreads) {
                        try {
                            this.wait();
                        } catch (InterruptedException e) {
                            System.err.printf("Interrupted\n");
                            return;
                        }
                    }
                    if (endOfPoll) {
                        System.out.println("end");
                        return;
                    }

                    System.out.println("Are you ready?");
                    numberOfReadyVoices = 0;
                    lastID = 0;
                    endOfPoll = true;
                    this.notifyAll();
                }
            }

        }
    }


    public static void main(String[] argv) {
        (new Caller()).start(Integer.parseInt(argv[0]));
    }
}
