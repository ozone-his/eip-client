/*
 * Copyright © 2021, Ozone HIS <info@ozone-his.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.ozonehis.eip.startup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.ServiceStatus;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CamelStartupAvailabilityGate implements ApplicationRunner {

    private final CamelContext camelContext;

    private final ExternalSystemsAvailabilityChecker availabilityChecker;

    private final ExternalSystemsAvailabilityProperties properties;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (ServiceStatus.Started.equals(camelContext.getStatus())) {
            return;
        }

        if (properties.isEnabled()) {
            log.info(
                    "Startup availability check is enabled; waiting for external systems before starting Camel routes");
            availabilityChecker.waitUntilAvailable();
        } else {
            log.info("Startup availability check is disabled; starting Camel routes immediately");
        }

        camelContext.start();
        log.info("Camel routes started");
    }
}
