/*
 * Copyright © 2021, Ozone HIS <info@ozone-his.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.ozonehis.eip.startup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Configuration for the pre-startup availability check.
 * <p>
 * Properties are read from environment variables before the Spring context starts:
 * <ul>
 *   <li>{@code EIP_STARTUP_WAIT_ENABLED} – enable/disable (default {@code false})</li>
 *   <li>{@code EIP_STARTUP_WAIT_ENDPOINTS} – comma-separated list of endpoints</li>
 *   <li>{@code EIP_STARTUP_WAIT_RETRY_INTERVAL_MS} – retry interval (default 10 000 ms)</li>
 *   <li>{@code EIP_STARTUP_WAIT_MAX_WAIT_MS} – max wait time (default 1 800 000 ms / 30 min)</li>
 *   <li>{@code EIP_STARTUP_WAIT_CONNECT_TIMEOUT_MS} – per-attempt connect timeout (default 3 000 ms)</li>
 *   <li>{@code EIP_STARTUP_WAIT_READ_TIMEOUT_MS} – per-attempt read timeout (default 3 000 ms)</li>
 * </ul>
 */
@Setter
@Getter
public class ExternalSystemsAvailabilityProperties {

    /**
     * Enable or disable the waiting for external systems to be available.
     */
    private boolean enabled = false;

    /**
     * Endpoints to wait for. Each endpoint is a URL.
     */
    private List<String> endpoints = new ArrayList<>();

    /**
     * Interval between retries when waiting for endpoints to be available. Default is 10 seconds.
     */
    private long retryIntervalMs = 10000;

    /**
     * Maximum time to wait for all endpoints to be available. Default is 30 mins.
     */
    private long maxWaitMs = 30 * 60 * 1000;

    /**
     * Connect timeout for each endpoint. Default is 3 seconds.
     */
    private int connectTimeoutMs = 3000;

    /**
     * Read timeout for each endpoint. Default is 3 seconds.
     */
    private int readTimeoutMs = 3000;

    /**
     * Creates an instance populated from environment variables (with sensible defaults).
     */
    public static ExternalSystemsAvailabilityProperties fromEnvironment() {
        ExternalSystemsAvailabilityProperties props = new ExternalSystemsAvailabilityProperties();
        props.setEnabled(Boolean.parseBoolean(envOrDefault("EIP_STARTUP_WAIT_ENABLED", "false")));

        String endpoints = envOrDefault("EIP_STARTUP_WAIT_ENDPOINTS", "");
        if (!endpoints.isBlank()) {
            props.setEndpoints(new ArrayList<>(Arrays.asList(endpoints.split(","))));
        }

        props.setRetryIntervalMs(Long.parseLong(envOrDefault("EIP_STARTUP_WAIT_RETRY_INTERVAL_MS", "10000")));
        props.setMaxWaitMs(Long.parseLong(envOrDefault("EIP_STARTUP_WAIT_MAX_WAIT_MS", "1800000")));
        props.setConnectTimeoutMs(Integer.parseInt(envOrDefault("EIP_STARTUP_WAIT_CONNECT_TIMEOUT_MS", "3000")));
        props.setReadTimeoutMs(Integer.parseInt(envOrDefault("EIP_STARTUP_WAIT_READ_TIMEOUT_MS", "3000")));
        return props;
    }

    private static String envOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        return value != null && !value.isBlank() ? value : defaultValue;
    }
}
