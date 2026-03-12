/*
 * Copyright © 2021, Ozone HIS <info@ozone-his.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.ozonehis.eip.startup;

import java.util.List;
import lombok.Getter;

@Getter
public class StartupAvailabilityState {

    private volatile boolean waiting;

    private volatile boolean timedOut;

    private volatile boolean ready;

    private volatile List<String> blockingEndpoints = List.of();

    public synchronized void markWaiting(List<String> unavailableEndpoints) {
        this.waiting = true;
        this.timedOut = false;
        this.ready = false;
        this.blockingEndpoints = List.copyOf(unavailableEndpoints);
    }

    public synchronized void markTimedOut(List<String> unavailableEndpoints) {
        this.waiting = false;
        this.timedOut = true;
        this.ready = false;
        this.blockingEndpoints = List.copyOf(unavailableEndpoints);
    }

    public synchronized void markReady() {
        this.waiting = false;
        this.timedOut = false;
        this.ready = true;
        this.blockingEndpoints = List.of();
    }

    public synchronized void markDisabled() {
        this.waiting = false;
        this.timedOut = false;
        this.ready = false;
        this.blockingEndpoints = List.of();
    }
}
