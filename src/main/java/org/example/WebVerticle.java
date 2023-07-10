package org.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.SqlClient;

import java.util.UUID;

public class WebVerticle extends AbstractVerticle {

    private SqlClient client;

    @Override
    public void start() throws Exception {
        // setup DB connection
        MySQLConnectOptions connectOptions = new MySQLConnectOptions()
                .setPort(3306)
                .setHost("127.0.0.1")
                .setDatabase("todo")
                .setUser("root")
                .setPassword("root");

        // Pool options
        PoolOptions poolOptions = new PoolOptions().setMaxSize(5);

        // Create the client pool
        client = MySQLPool.pool(vertx, connectOptions, poolOptions);
        TodoService todoService = new TodoService(client);

        // setup the HTTP routes
        Router router = Router.router(vertx);
        router
                .get("/hello")
                .respond(ctx -> ctx
                        .vertx()
                        .eventBus()
                        .request("greeting", ctx.queryParam("name").get(0))
                        .map(message -> (String) message.body()));

        router
                .get("/todo")
                .respond(ctx -> todoService.getAll());

        router
                .post("/todo")
                .handler(BodyHandler.create())
                .respond(ctx -> {
                    Todo todo = ctx.body().asPojo(Todo.class);
                    todo.setId(UUID.randomUUID().toString());
                    return todoService.addTodo(todo);
                });

        router
                .put("/todo")
                .respond(ctx -> Future.succeededFuture());

        // create and start HTTP server
        HttpServer httpServer = vertx.createHttpServer(new HttpServerOptions().setLogActivity(true));
        httpServer.requestHandler(router);
        httpServer.listen(8080, http -> {
            if (http.succeeded()) {
                System.out.println("HTTP server started on port 8080");
            } else {
                System.out.println("failed");
            }
        });
    }

    @Override
    public void stop() throws Exception {
        client.close(event -> {
            if (event.succeeded()) {
                System.out.println("Connection is successfully closed");
            } else {
                System.out.println("Failed to close connection");
                event.cause().printStackTrace();
            }
        });
    }
}
