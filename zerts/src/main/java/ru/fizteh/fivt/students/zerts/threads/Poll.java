package ru.fizteh.fivt.students.zerts.threads;

import java.util.Random;

/**
 * Created by zerts on 03.12.2015.
 */
public class Poll {
    private static volatile int numberOfThreads;

    private static class Voice extends Thread {
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
            Random result = new Random();
            if (result.nextInt(100) < 90) {
                answer = true;
                System.out.println("Yes");
            } else {
                answer = false;
                System.out.println("No");
            }
        }
    }

    public static void main(String[] argv) {
        numberOfThreads = new Integer(argv[0]);
        Voice[] voices = new Voice[numberOfThreads];
        boolean endOfPoll = false;
        while (!endOfPoll) {
            System.out.println("Are you ready?");
            for (int i = 0; i < numberOfThreads; i++) {
                voices[i] = new Voice(i);
                voices[i].start();
            }
            endOfPoll = true;
            for (int i = 0; i < numberOfThreads; i++) {
                try {
                    voices[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!voices[i].isAnswer()) {
                    endOfPoll = false;
                }
            }
        }
    }
}
