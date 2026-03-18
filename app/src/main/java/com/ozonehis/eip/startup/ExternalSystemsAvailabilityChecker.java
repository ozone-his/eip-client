/*
 * Copyright © 2021, Ozone HIS <info@ozone-his.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.ozonehis.eip.startup;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ExternalSystemsAvailabilityChecker {

    private final ExternalSystemsAvailabilityProperties properties;

    private final StartupAvailabilityState startupAvailabilityState;

    public void waitUntilAvailable() throws InterruptedException {
        final List<String> configuredEndpoints = normalizedEndpoints();
        if (configuredEndpoints.isEmpty()) {
            log.warn("No external endpoints configured for startup availability check; continuing startup");
            startupAvailabilityState.markReady();
            return;
        }

        final long startedAt = System.currentTimeMillis();
        int attempt = 1;
        while (true) {
            final List<String> unavailableEndpoints = configuredEndpoints.stream()
                    .filter(endpoint -> !isEndpointAvailable(endpoint))
                    .toList();

            if (unavailableEndpoints.isEmpty()) {
                startupAvailabilityState.markReady();
                log.info("All required external systems are reachable");
                return;
            }

            startupAvailabilityState.markWaiting(unavailableEndpoints);
            final long elapsedMs = System.currentTimeMillis() - startedAt;
            if (properties.getMaxWaitMs() > 0 && elapsedMs >= properties.getMaxWaitMs()) {
                startupAvailabilityState.markTimedOut(unavailableEndpoints);
                throw new IllegalStateException(
                        "Timed out waiting for external systems: " + String.join(", ", unavailableEndpoints));
            }

            log.info(
                    "Attempt {}: waiting for external systems to become reachable: {}",
                    attempt++,
                    String.join(", ", unavailableEndpoints));
            Thread.sleep(Math.max(properties.getRetryIntervalMs(), 1));
        }
    }

    boolean isEndpointAvailable(String endpoint) {
        final URI uri = toUri(endpoint);
        if (uri == null || uri.getScheme() == null) {
            return false;
        }

        return switch (uri.getScheme().toLowerCase()) {
            case "http", "https" -> isHttpAvailable(uri);
            case "tcp" -> isTcpAvailable(uri);
            default -> {
                log.warn("Unsupported startup check endpoint scheme for '{}'; supported: http, https, tcp", endpoint);
                yield false;
            }
        };
    }

    private List<String> normalizedEndpoints() {
        return properties.getEndpoints().stream()
                .map(String::trim)
                .filter(value -> !value.isEmpty())
                .toList();
    }

    private URI toUri(String endpoint) {
        try {
            return new URI(endpoint);
        } catch (URISyntaxException ex) {
            log.warn("Invalid startup check endpoint '{}': {}", endpoint, ex.getMessage());
            return null;
        }
    }

    private boolean isHttpAvailable(URI uri) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) uri.toURL().openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(properties.getConnectTimeoutMs());
            connection.setReadTimeout(properties.getReadTimeoutMs());
            final int responseCode = connection.getResponseCode();
            return responseCode < 500;
        } catch (IOException ex) {
            return false;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private boolean isTcpAvailable(URI uri) {
        final int port = uri.getPort();
        if (uri.getHost() == null || port <= 0) {
            return false;
        }

        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(uri.getHost(), port), properties.getConnectTimeoutMs());
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
}
