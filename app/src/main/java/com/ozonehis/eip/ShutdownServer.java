package com.ozonehis.eip;

import org.springframework.stereotype.Component;

@Component
public class ShutdownServer {
    public void shutdown() {
        System.exit(0);
    }
}