package ru.fizteh.fivt.students.zerts.threads;

import java.util.List;

/**
 * Created by User on 14.12.2015.
 */
public class QueMain {

    private static SafeQueue<Integer> q = new SafeQueue<>(1);

    static class Add implements Runnable {
        private List<Integer> elements;

        Add(List<Integer> elements) {
            this.elements = elements;
        }

        @Override
        public void run() {
            try {
                q.offer(elements);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class Get implements Runnable {
        private int numberOfElements;

        Get(int n) {
            numberOfElements = n;
        }
        @Override
        public void run() {
            try {
                List<Integer> x = q.take(numberOfElements);
                x.stream().forEach(System.out::println);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

    }
}
