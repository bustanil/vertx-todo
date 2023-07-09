package org.example;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.jackson.DatabindCodec;

public class App
{
    public static void main( String[] args )
    {
        DatabindCodec.mapper().registerModule(new JavaTimeModule());
        DatabindCodec.prettyMapper().registerModule(new JavaTimeModule());
        DatabindCodec.mapper().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        DatabindCodec.prettyMapper().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);


        Vertx vertx = Vertx.vertx();
        Handler<AsyncResult<String>> deployHandler = event -> {
            System.out.println(event.succeeded());
            if (event.failed() && event.cause() != null) {
                event.cause().printStackTrace();
            }
        };

        vertx.deployVerticle(new WebVerticle(), deployHandler);
        vertx.deployVerticle(new GreetingVerticle(), deployHandler);
    }
}
