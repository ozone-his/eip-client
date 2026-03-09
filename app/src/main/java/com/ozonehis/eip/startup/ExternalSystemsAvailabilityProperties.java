/*
 * Copyright © 2021, Ozone HIS <info@ozone-his.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.ozonehis.eip.startup;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "eip.startup.wait")
public class ExternalSystemsAvailabilityProperties {

    private boolean enabled = false;

    private List<String> endpoints = new ArrayList<>();

    private long retryIntervalMs = 10000;

    private long maxWaitMs = 0;

    private int connectTimeoutMs = 3000;

    private int readTimeoutMs = 3000;
}
