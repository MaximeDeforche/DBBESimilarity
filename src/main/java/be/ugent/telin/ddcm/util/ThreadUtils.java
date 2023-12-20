package be.ugent.telin.ddcm.util;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadUtils {

    public static ExecutorService getNameThreadExecutor(int poolSize, String name) {
        BasicThreadFactory threadFactory = new BasicThreadFactory
                .Builder()
                .namingPattern(name + "_%d")
                .build();

        return Executors.newFixedThreadPool(poolSize, threadFactory);
    }
}
