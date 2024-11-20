/*
 * Copyright © 2021, Ozone HIS <info@ozone-his.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.ozonehis.eip.security.oauth2;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.ozonehis.eip.security.Constants;
import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

public class Oauth2RouteTest extends CamelTestSupport {

    public static final String OAUTH_REQUEST_BODY =
            "grant_type=client_credentials&client_id=testClientId&client_secret=testClientSecret&scope=testClientScope";

    @EndpointInject("mock:result")
    private MockEndpoint mockEndpoint;

    @Produce("direct:oauth2")
    private ProducerTemplate producerTemplate;

    @Override
    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = new DefaultCamelContext();
        context.addRoutes(new Oauth2Route());
        return context;
    }

    @Override
    protected void doPreSetup() throws Exception {
        super.doPreSetup();
    }

    @Test
    public void shouldGetAccessTokenFromAuthProviderGivenOAuthPropsAsRequestBody() throws Exception {
        // Setup
        mockEndpoint.expectedMessageCount(1);
        mockEndpoint.expectedBodiesReceived(OAUTH_REQUEST_BODY);
        mockEndpoint.expectedHeaderReceived("Content-Type", "application/x-www-form-urlencoded");
        mockEndpoint.expectedHeaderReceived("CamelHttpMethod", "POST");

        RouteDefinition routeDefinition = context.getRouteDefinition("oauth2");
        AdviceWith.adviceWith(routeDefinition, context, new RouteBuilder() {

            @Override
            public void configure() {
                // Replace the "toD" endpoint with a mock endpoint
                interceptSendToEndpoint("https://example.com/auth")
                        .skipSendToOriginalEndpoint()
                        .to(mockEndpoint);
            }
        });

        context.start();

        Exchange exchange = createExchangeWithBody(OAUTH_REQUEST_BODY);
        exchange.getMessage().setHeader(Constants.HEADER_OAUTH2_URL, "https://example.com/auth");

        // Replay
        producerTemplate.send("direct:oauth2", exchange);

        // Verify
        mockEndpoint.assertIsSatisfied();

        // Verify
        String oAuthToken = exchange.getMessage().getBody(String.class);
        assertNotNull(oAuthToken);
    }
}
