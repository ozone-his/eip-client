/*
 * Copyright © 2021, Ozone HIS <info@ozone-his.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.ozonehis.eip.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.camel.Exchange;
import org.apache.camel.builder.ExchangeBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SplitBodyConcatAggregationStrategyTest {

    private SplitBodyConcatAggregationStrategy strategy;

    private Exchange oldExchange, newExchange;

    @BeforeEach
    public void setup() {
        strategy = new SplitBodyConcatAggregationStrategy();
    }

    @Test
    public void aggregate_shouldReturnExchangeWithConcatenateBodyContentsAsCommaSeparated() throws JSONException {
        // Given
        oldExchange = ExchangeBuilder.anExchange(new DefaultCamelContext())
                .withBody("Hello")
                .build();
        newExchange = ExchangeBuilder.anExchange(new DefaultCamelContext())
                .withBody("World")
                .build();

        // When
        Exchange exchange = strategy.aggregate(oldExchange, newExchange);

        // Then
        assertEquals("Hello,World", exchange.getIn().getBody());
    }

    public void aggregate_shouldReturnExchangeWithNewExchangeBodyContentGivenOldExchangeIsNull() throws JSONException {
        // Given
        oldExchange = null;
        newExchange = ExchangeBuilder.anExchange(new DefaultCamelContext())
                .withBody("World")
                .build();

        // When
        Exchange exchange = strategy.aggregate(oldExchange, newExchange);

        // Then
        assertEquals("World", exchange.getIn().getBody());
    }
}
