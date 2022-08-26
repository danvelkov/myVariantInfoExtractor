package org.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Singleton {
    private static Singleton single_instance = null;

    public static ThreadPool threadPool;

    private Singleton()
    {
        threadPool = new ThreadPool(1, 380_000_000);
//        Runtime.addShutdownHook();
    }

    public static Singleton getInstance()
    {
        if (single_instance == null)
            single_instance = new Singleton();

        return single_instance;
    }
}
