package com.ozonehis.eip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {

    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    private static boolean shuttingDown = false;

    public static void setShuttingDown() {
        shuttingDown = true;
        LOGGER.info("Received application shutting down event");
    }

    public static boolean isShuttingDown() {
        return shuttingDown;
    }

    public static synchronized void shutdown() {
        if (isShuttingDown()) {
            LOGGER.info("Application is already shutting down");
            return;
        }

        setShuttingDown();

        LOGGER.info("Shutting down the application...");

        // Shutdown in a new thread to ensure other background shutdown threads complete too
        new Thread(() -> System.exit(129)).start();
    }
}
