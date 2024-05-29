/*
 * Copyright © 2021, Ozone HIS <info@ozone-his.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.ozonehis.eip.utils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

public class ShutdownHandlerTest {

    private ShutdownHandler shutdownHandler;
    private ApplicationContext applicationContext;
    private Logger logger;

    @BeforeEach
    public void setUp() {
        shutdownHandler = new ShutdownHandler();
        applicationContext = mock(ConfigurableApplicationContext.class);
        shutdownHandler.setApplicationContext(applicationContext);
        logger = mock(Logger.class);
        ShutdownHandler.LOGGER = logger;
    }

    @Test
    public void testShutdownCloseApplicationContext() {
        shutdownHandler.shutdown();

        Mockito.verify((ConfigurableApplicationContext) applicationContext, times(1))
                .close();
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
    public void testExitApplicationWhenAlreadyShuttingDown() {
        ShutdownHandler.shuttingDown = true;

        ShutdownHandler.exitApplication();

        Mockito.verify(logger, times(1)).info("Application is already shutting down");
    }
}
