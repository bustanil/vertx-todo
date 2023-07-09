package org.example;

import io.vertx.core.AbstractVerticle;

public class GreetingVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        // respond to greeting requests
        vertx.eventBus().<String>consumer("greeting", event -> event.reply("Howdy: " + event.body()));
    }
}
