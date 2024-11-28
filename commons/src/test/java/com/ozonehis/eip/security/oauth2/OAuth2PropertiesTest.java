/*
 * Copyright © 2021, Ozone HIS <info@ozone-his.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.ozonehis.eip.security.oauth2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class OAuth2PropertiesTest {

    @Test
    public void shouldSetPropertiesCorrectlyWithBuilder() {
        // Set the properties using the builder
        OAuth2Properties properties = OAuth2Properties.builder()
                .authUrl("https://example.com/auth")
                .clientId("testClientId")
                .clientSecret("testClientSecret")
                .clientScope("testClientScope")
                .build();

        // Verify
        assertEquals("https://example.com/auth", properties.getAuthUrl());
        assertEquals("testClientId", properties.getClientId());
        assertEquals("testClientSecret", properties.getClientSecret());
        assertEquals("testClientScope", properties.getClientScope());
    }

    @Test
    public void shouldSetPropertiesCorrectlyWithDefaults() {
        // set default properties
        OAuth2Properties properties = OAuth2Properties.builder().build();

        // Verify
        assertNull(properties.getAuthUrl());
        assertNull(properties.getClientId());
        assertNull(properties.getClientSecret());
        assertNull(properties.getClientScope());
    }

    @Test
    public void shouldSetPropertiesCorrectlyWithSetter() {
        OAuth2Properties properties = new OAuth2Properties();

        // Set the properties using the setter methods
        properties.setAuthUrl("https://example.com/auth");
        properties.setClientId("testClientId");
        properties.setClientSecret("testClientSecret");
        properties.setClientScope("testClientScope");

        // Verify
        assertEquals("https://example.com/auth", properties.getAuthUrl());
        assertEquals("testClientId", properties.getClientId());
        assertEquals("testClientSecret", properties.getClientSecret());
        assertEquals("testClientScope", properties.getClientScope());
    }
}
