/*
 * Copyright © 2021, Ozone HIS <info@ozone-his.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.ozonehis.eip;

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

        SpringApplication.run(Application.class, args);
    }
}
