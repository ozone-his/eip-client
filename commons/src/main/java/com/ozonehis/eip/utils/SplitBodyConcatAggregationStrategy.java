/*
 * Copyright © 2023, Ozone HIS <info@ozone-his.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.ozonehis.eip.utils;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

@Component("splitBodyConcatAggregationStrategy")
public class SplitBodyConcatAggregationStrategy implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {

        if (oldExchange == null) {
            return newExchange;
        }

        String combinedBodyString = oldExchange.getIn().getBody(String.class) + ","
                + newExchange.getIn().getBody(String.class);
        oldExchange.getIn().setBody(combinedBodyString);

        return oldExchange;
    }
}
