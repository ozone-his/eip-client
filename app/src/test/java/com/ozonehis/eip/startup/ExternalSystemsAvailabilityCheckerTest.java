/*
 * Copyright © 2021, Ozone HIS <info@ozone-his.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.ozonehis.eip.startup;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.List;
import org.junit.jupiter.api.Test;

class ExternalSystemsAvailabilityCheckerTest {

    @Test
    void shouldReportHttpEndpointAsAvailable() throws IOException {
        final HttpServer server = HttpServer.create(new InetSocketAddress(0), 0);
        server.createContext("/health", new OkHandler());
        server.start();

        try {
            final ExternalSystemsAvailabilityProperties properties = propertiesWithEndpoints(
                    List.of("http://127.0.0.1:" + server.getAddress().getPort() + "/health"));
            final ExternalSystemsAvailabilityChecker checker = new ExternalSystemsAvailabilityChecker(properties);

            assertTrue(checker.isEndpointAvailable(properties.getEndpoints().get(0)));
        } finally {
            server.stop(0);
        }
    }

    @Test
    void shouldFailWhenTimeoutReachedBeforeSystemsAreAvailable() throws IOException {
        final int unavailablePort;
        try (ServerSocket socket = new ServerSocket(0)) {
            unavailablePort = socket.getLocalPort();
        }

        final ExternalSystemsAvailabilityProperties properties =
                propertiesWithEndpoints(List.of("tcp://127.0.0.1:" + unavailablePort));
        properties.setMaxWaitMs(150);
        properties.setRetryIntervalMs(50);

        final ExternalSystemsAvailabilityChecker checker = new ExternalSystemsAvailabilityChecker(properties);

        assertThrows(IllegalStateException.class, checker::waitUntilAvailable);
        assertFalse(checker.isEndpointAvailable(properties.getEndpoints().get(0)));
    }

    private ExternalSystemsAvailabilityProperties propertiesWithEndpoints(List<String> endpoints) {
        final ExternalSystemsAvailabilityProperties properties = new ExternalSystemsAvailabilityProperties();
        properties.setEndpoints(endpoints);
        properties.setConnectTimeoutMs(100);
        properties.setReadTimeoutMs(100);
        properties.setRetryIntervalMs(50);
        properties.setMaxWaitMs(500);
        return properties;
    }

    private static class OkHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.sendResponseHeaders(200, 0);
            exchange.getResponseBody().close();
        }
    }
}
