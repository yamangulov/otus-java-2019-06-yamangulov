package ru.otus;

import java.util.concurrent.atomic.AtomicInteger;

public class Sequence {

    private final Thread thread1;
    private final Thread thread2;

    private final AtomicInteger counter1 = new AtomicInteger(1);
    private final AtomicInteger counter2 = new AtomicInteger(1);
    private AtomicInteger flag = new AtomicInteger(0); // флаг инкремента/декремента

    public Sequence() {
        thread1 = new Thread(()-> this.action(counter1));
        thread2 = new Thread(()-> this.action(counter2));

        thread1.setName("Поток №1 ");
        thread2.setName("Поток №2 ");
    }

    public void sequenceStart() {
        thread1.start();
        thread2.start();
    }
    private synchronized void action(AtomicInteger counter) {
        while (true) {
            if (flag.get() == 0) {
                System.out.println(Thread.currentThread().getName() + (counter.incrementAndGet() - 1));
                sleep(1000);
                notifyWaitingThreads();
                toWait();
                if (counter.get() == 11) {
                    flag = new AtomicInteger(1);
                }
            } else {
                System.out.println(Thread.currentThread().getName() + (counter.decrementAndGet() - 1));
                sleep(1000);
                notifyWaitingThreads();
                toWait();
                if (counter.get() == 2) {
                    flag = new AtomicInteger(0);
                }
            }
        }
    }

    private void notifyWaitingThreads() {
        if (thread1.getState().equals(Thread.State.WAITING) || thread2.getState().equals(Thread.State.WAITING)) {
            notifyAll();
        }
    }

    private void sleep(int sleepingPeriod) {
        try {
            Thread.sleep(sleepingPeriod);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void toWait() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

