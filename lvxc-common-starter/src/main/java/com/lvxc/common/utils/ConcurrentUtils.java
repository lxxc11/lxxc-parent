package com.lvxc.common.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author caoyq
 * @Date 2024/4/7 14:02
 * @PackageName:com.lvxc.common.utils
 * @ClassName: ConcurrentUtils
 * @Version 1.0
 */
public class ConcurrentUtils {

    /**
     * 原子计数器
     */
    private static final AtomicInteger count = new AtomicInteger(0);

    /**
     * 线程安全锁
     */
    private static final Lock lock = new ReentrantLock();

    /**
     * 原子计数器自增1
     */
    public static void increment() {
        count.incrementAndGet();
    }

    /**
     * 获取当前原子计数器值
     */
    public static int getCount() {
        return count.get();
    }

    /**
     * 线程安全加锁计数器自增1
     */
    public static void incrementWithLock() {
        lock.lock();
        try {
            count.incrementAndGet();
        } finally {
            lock.unlock();
        }
    }
}
