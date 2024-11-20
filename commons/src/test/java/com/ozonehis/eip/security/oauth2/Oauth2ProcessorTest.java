/*
 * Copyright © 2021, Ozone HIS <info@ozone-his.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.ozonehis.eip.security.oauth2;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class Oauth2ProcessorTest extends CamelTestSupport {

    public static final String TEST_ACCESS_TOKEN = "testAccessToken";

    public static final String KEYCLOAK_SERVER_URL_AUTH = "https://keycloak-server-url/auth";

    @Mock
    private ProducerTemplate producerTemplate;

    @InjectMocks
    private Oauth2Processor oauth2Processor;

    private static AutoCloseable mocksCloser;

    @Mock
    private Exchange exchange;

    @Mock
    private Message message;

    @BeforeEach
    public void setUp() {
        mocksCloser = openMocks(this);
        when(message.getHeader("oauth2.url", String.class)).thenReturn(KEYCLOAK_SERVER_URL_AUTH);
        when(message.getHeader("oauth2.client.id", String.class)).thenReturn("clientId");
        when(message.getHeader("oauth2.client.secret", String.class)).thenReturn("clientSecret");
        when(message.getHeader("oauth2.client.scope", String.class)).thenReturn("scope");
        when(exchange.getMessage()).thenReturn(message);
    }

    @AfterAll
    public static void closeMocks() throws Exception {
        mocksCloser.close();
    }

    @Test
    public void shouldAddAuthorizationHeaderGivenClientIdAndSecret() {
        // Setup
        when(producerTemplate.requestBodyAndHeader(
                        anyString(), anyString(), anyString(), anyString(), eq(Oauth2Token.class)))
                .thenReturn(getOauthToken());

        // Replay
        oauth2Processor.process(exchange);

        // Verify
        verify(exchange.getMessage(), times(1)).setHeader(eq("Authorization"), eq("Bearer " + TEST_ACCESS_TOKEN));
    }

    @Test
    public void shouldThrowEIPAuthenticationExceptionGivenNullClientScope() {
        // Setup
        String accessTokenUri = "https://test.auth.com/token";
        when(producerTemplate.requestBodyAndHeader(
                        anyString(), anyString(), eq("authUrl"), eq(accessTokenUri), eq(Oauth2Token.class)))
                .thenReturn(getOauthToken());

        // Replay & Verify
        assertThrows(IllegalStateException.class, () -> oauth2Processor.process(exchange));
    }

    @Test
    public void shouldThrowEIPAuthenticationExceptionGivenNullProperties() {
        // Setup
        String accessTokenUri = "https://test.auth.com/token";
        when(producerTemplate.requestBodyAndHeader(
                        anyString(), anyString(), eq("authUrl"), eq(accessTokenUri), eq(Oauth2Token.class)))
                .thenReturn(getOauthToken());

        // Replay & Verify
        assertThrows(IllegalStateException.class, () -> oauth2Processor.process(exchange));
    }

    private static Oauth2Token getOauthToken() {
        Oauth2Token oAuthToken = new Oauth2Token();
        oAuthToken.setAccessToken(TEST_ACCESS_TOKEN);
        oAuthToken.setExpiresIn(3600);
        oAuthToken.setRefreshExpiresIn(3600);
        oAuthToken.setTokenType("Bearer");
        oAuthToken.setNotBeforePolicy(0);
        return oAuthToken;
    }
}
