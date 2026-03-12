/*
 * Copyright © 2021, Ozone HIS <info@ozone-his.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.ozonehis.eip;

import com.ozonehis.eip.startup.ExternalSystemsAvailabilityChecker;
import com.ozonehis.eip.startup.ExternalSystemsAvailabilityProperties;
import com.ozonehis.eip.startup.StartupAvailabilityState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@Slf4j
@PropertySource("classpath:eip-client.properties")
@SpringBootApplication(scanBasePackages = "com.ozonehis.eip.*, ${eip.app.scan.packages}")
public class Application {

    public static void main(final String[] args) {
        log.info("Starting EIP Client Application . . .");

        waitForExternalSystems();

        SpringApplication.run(Application.class, args);
    }

    private static void waitForExternalSystems() {
        ExternalSystemsAvailabilityProperties properties = ExternalSystemsAvailabilityProperties.fromEnvironment();

        if (!properties.isEnabled()) {
            log.info("Startup availability check is disabled; proceeding to start the application immediately");
            return;
        }

        log.info("Startup availability check is enabled; waiting for external systems before starting the application");
        StartupAvailabilityState state = new StartupAvailabilityState();
        ExternalSystemsAvailabilityChecker checker = new ExternalSystemsAvailabilityChecker(properties, state);

        try {
            checker.waitUntilAvailable();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while waiting for external systems", ex);
        }
    }
}
