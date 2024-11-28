/*
 * Copyright © 2021, Ozone HIS <info@ozone-his.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.ozonehis.eip.security.oauth2;

import static com.ozonehis.eip.security.Constants.HEADER_OAUTH2_URL;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class OAuth2Route extends RouteBuilder {

    @Override
    public void configure() {
        from("direct:oauth2")
                .routeId("oauth2")
                .setHeader("Content-Type", constant("application/x-www-form-urlencoded"))
                .setHeader("CamelHttpMethod", constant("POST"))
                .toD("${header." + HEADER_OAUTH2_URL + "}")
                .unmarshal()
                .json(JsonLibrary.Jackson, OAuth2Token.class);
    }
}
