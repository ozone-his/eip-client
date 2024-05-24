/*
 * Copyright © 2021, Ozone HIS <info@ozone-his.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.ozonehis.eip.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ShutdownHandler implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    public static boolean shuttingDown = false;

    public static Logger LOGGER = LoggerFactory.getLogger(ShutdownHandler.class);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void shutdown() {
        ConfigurableApplicationContext configurableApplicationContext =
                (ConfigurableApplicationContext) applicationContext;
        configurableApplicationContext.close();
        exitApplication();
    }

    public static synchronized void exitApplication() {
        if (shuttingDown) {
            LOGGER.info("Application is already shutting down");
            return;
        }
        shuttingDown = true;
        LOGGER.info("Shutting down the application...");

        // Shutdown in a new thread to ensure other background shutdown threads complete too
        new Thread(() -> System.exit(0)).start();
    }
}
