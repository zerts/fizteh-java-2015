package ru.fizteh.fivt.students.zerts.threads;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zerts on 03.12.2015.
 */
public class ThreadQueue<T> {
    private Queue<T> queue;

    public int getQueueSize() {
        return queueSize;
    }
    private int queueSize;

    private ReentrantLock queueLock = new ReentrantLock();

    public void offer(List<T> elements) {
        queueLock.lock();
        if (elements.size() + queue.size() < queueSize) {
            elements.stream().forEach(queue::add);
        }
        queueLock.unlock();
    }

    public List<T> take(int numberOfElements) {
        queueLock.lock();
        if (queue.size() < numberOfElements) {
            queueLock.unlock();
            return null;
        }
        List<T> result = new ArrayList<>();
        for (int i = 0; i < numberOfElements; i++) {
            result.add(queue.poll());
        }
        queueLock.unlock();
        return result;
    }

    ThreadQueue(int size) {
        this.queueSize = size;
        this.queue = new ArrayDeque<>();
    }
}
