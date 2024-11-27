/*
 * Copyright © 2021, Ozone HIS <info@ozone-his.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.ozonehis.eip.security.oauth2;

import static com.ozonehis.eip.security.Constants.BEARER_HTTP_AUTH_SCHEME;
import static com.ozonehis.eip.security.Constants.HEADER_OAUTH2_CLIENT_ID;
import static com.ozonehis.eip.security.Constants.HEADER_OAUTH2_CLIENT_SECRET;
import static com.ozonehis.eip.security.Constants.HEADER_OAUTH2_SCOPE;
import static com.ozonehis.eip.security.Constants.HEADER_OAUTH2_URL;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Setter
@Getter
@Component("eip.oauthProcessor")
public class OAuth2Processor implements Processor {

    private final ProducerTemplate producerTemplate;

    @Autowired
    public OAuth2Processor(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

    @Override
    public void process(Exchange exchange) {
        OAuth2Properties properties = OAuth2Properties.builder()
                .authUrl(exchange.getMessage().getHeader(HEADER_OAUTH2_URL, String.class))
                .clientScope(exchange.getMessage().getHeader(HEADER_OAUTH2_SCOPE, String.class))
                .clientId(exchange.getMessage().getHeader(HEADER_OAUTH2_CLIENT_ID, String.class))
                .clientSecret(exchange.getMessage().getHeader(HEADER_OAUTH2_CLIENT_SECRET, String.class))
                .build();
        validateOAuth2Properties(properties);
        OAuth2Token authToken = callAccessTokenUri(properties.getAuthUrl(), buildOAuth2RequestBody(properties));
        if (authToken == null) {
            throw new IllegalStateException("OAuth2 token is null");
        } else {
            log.info("OAuth2 token is successfully obtained. Expires in {} seconds", authToken.getExpiresIn());
        }
        setAuthorizationHeader(exchange, authToken.getAccessToken());
    }

    private void validateOAuth2Properties(OAuth2Properties properties) {
        if (properties == null || !properties.isValid()) {
            throw new IllegalStateException("OAuth2 properties are not set properly or some properties are missing");
        }
    }

    private String buildOAuth2RequestBody(OAuth2Properties properties) {
        return "grant_type=client_credentials" + "&client_id="
                + properties.getClientId() + "&client_secret="
                + properties.getClientSecret() + "&scope="
                + properties.getClientScope();
    }

    private OAuth2Token callAccessTokenUri(String accessTokenUri, String requestBody) {
        return producerTemplate.requestBodyAndHeader(
                "direct:oauth2", requestBody, HEADER_OAUTH2_URL, accessTokenUri, OAuth2Token.class);
    }

    private void setAuthorizationHeader(Exchange exchange, String accessToken) {
        exchange.getMessage().setHeader("Authorization", BEARER_HTTP_AUTH_SCHEME + " " + accessToken);
    }
}
