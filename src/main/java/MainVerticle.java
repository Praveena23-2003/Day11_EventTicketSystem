package org.example.eventsystem;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.example.eventsystem.config.ConfigLoader;
import org.example.eventsystem.controller.AuthController;
import org.example.eventsystem.controller.EventController;
import org.example.eventsystem.controller.BookingController;

public class MainVerticle extends AbstractVerticle {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MainVerticle());
    }

    @Override
    public void start(Promise<Void> startPromise) {
        MongoClient mongoClient = MongoClient.createShared(vertx, ConfigLoader.loadMongoConfig());

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        new AuthController(router, mongoClient);
        new EventController(router, mongoClient);
        new BookingController(router, mongoClient);

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8888, http -> {
                    if (http.succeeded()) {
                        startPromise.complete();
                        System.out.println("✅ HTTP server started on port 8888");
                    } else {
                        startPromise.fail(http.cause());
                        System.err.println("❌ Failed to start server: " + http.cause().getMessage());
                    }
                });
    }
}
