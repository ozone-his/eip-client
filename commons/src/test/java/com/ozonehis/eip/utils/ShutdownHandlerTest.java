package com.ozonehis.eip.utils;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import static org.mockito.Mockito.*;

public class ShutdownHandlerTest {

    private ShutdownHandler shutdownHandler;
    private ApplicationContext applicationContext;
    private Logger logger;

    @BeforeEach
    public void setUp() {
        shutdownHandler = new ShutdownHandler();
        applicationContext = mock(ConfigurableApplicationContext.class);
        shutdownHandler.setApplicationContext(applicationContext);
        logger = Mockito.mock(Logger.class);
        ShutdownHandler.LOGGER = logger;
    }

    @Test
    public void testShutdown() {
        shutdownHandler.shutdown();

        Mockito.verify((ConfigurableApplicationContext) applicationContext, times(1)).close();
    }

    @Test
    public void testExitApplication() {
        ShutdownHandler.shuttingDown = false;

        ShutdownHandler.exitApplication();

        // Since System.exit() cannot be directly tested, we can't validate that it was invoked.
        // We can only verify that the logger is invoked.
        Mockito.verify(logger, times(1)).info("Shutting down the application...");
    }

    @Test
    public void testExitApplication_ShuttingDown() {
        ShutdownHandler.shuttingDown = true;

        ShutdownHandler.exitApplication();

        Mockito.verify(logger, times(1)).info("Application is already shutting down");
    }
}
