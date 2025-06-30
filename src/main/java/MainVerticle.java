package org.example.eventsystem;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import org.example.eventsystem.config.ConfigLoader;
import org.example.eventsystem.controller.AuthController;
import org.example.eventsystem.controller.BookingController;
import org.example.eventsystem.controller.EventController;

public class MainVerticle extends AbstractVerticle {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MainVerticle());
    }

    @Override
    public void start(Promise<Void> startPromise) {
        MongoClient mongoClient = MongoClient.createShared(vertx, ConfigLoader.loadMongoConfig());

        Router router = Router.router(vertx);

        // ✅ Unified CORS handler with OPTIONS support
        router.route().handler(ctx -> {
            ctx.response()
                    .putHeader("Access-Control-Allow-Origin", "*")
                    .putHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS")
                    .putHeader("Access-Control-Allow-Headers", "Content-Type");

            if (ctx.request().method().name().equals("OPTIONS")) {
                ctx.response().setStatusCode(200).end(); // Preflight success
            } else {
                ctx.next();
            }
        });

        router.route().handler(BodyHandler.create());

        // ✅ Serve static frontend files from "frontend" folder
        router.route("/*").handler(StaticHandler.create("frontend"));

        // ✅ Optional: Health check route
        router.get("/").handler(ctx ->
                ctx.response()
                        .putHeader("Content-Type", "text/plain")
                        .end("🎟️ Welcome to the Event Ticket Token System API")
        );

        // ✅ Register routes
        new AuthController(router, mongoClient);
        new EventController(router, mongoClient);
        new BookingController(router, mongoClient);

        // ✅ Start server on port 9090
        vertx.createHttpServer().requestHandler(router).listen(9090, http -> {
            if (http.succeeded()) {
                startPromise.complete();
                System.out.println("✅ HTTP server started on port 9090");
            } else {
                startPromise.fail(http.cause());
                System.err.println("❌ Failed to start server: " + http.cause().getMessage());
            }
        });
    }
}
