package ru.fizteh.fivt.students.zerts.threads;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Zerts on 14.12.2015.
 */

public class SafeQueue<T> {
    private Queue<T> queue;

    public int getQueueSize() {
        return queueSize;
    }
    private int queueSize;

    synchronized void offer(List<T> elements) throws InterruptedException {
        System.out.println("offer");
        while (elements.size() + queue.size() > queueSize) {
            wait();
        }
        elements.stream().forEach(queue::add);
        notifyAll();
    }

    synchronized List<T> take(int numberOfElements) throws InterruptedException {
        while (queue.size() < numberOfElements) {
            wait();
        }
        List<T> result = new ArrayList<>();
        for (int i = 0; i < numberOfElements; i++) {
            result.add(queue.poll());
        }
        notifyAll();
        return result;
    }

    synchronized void offer(List<T> elements, long time) throws InterruptedException {
        long startTume = System.currentTimeMillis();
        while (elements.size() + queue.size() > queueSize && System.currentTimeMillis() - startTume < time) {
            wait();
        }
        if (System.currentTimeMillis() - startTume > time) {
            return;
        }
        elements.stream().forEach(queue::add);
        notifyAll();
    }
    synchronized List<T> take(int numberOfElements, long time) throws InterruptedException {
        long startTume = System.currentTimeMillis();
        while (queue.size() < numberOfElements && System.currentTimeMillis() - startTume < time) {
            wait();
        }
        if (System.currentTimeMillis() - startTume > time) {
            return null;
        }
        List<T> result = new ArrayList<>();
        for (int i = 0; i < numberOfElements; i++) {
            result.add(queue.poll());
        }
        notifyAll();
        return result;
    }

    SafeQueue(int size) {
        this.queueSize = size;
        this.queue = new ArrayDeque<>();
    }
}
