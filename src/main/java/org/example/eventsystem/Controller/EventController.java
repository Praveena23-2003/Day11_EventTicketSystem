package org.example.eventsystem.controller;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class EventController {

    private final MongoClient mongoClient;

    public EventController(Router router, MongoClient mongoClient) {
        this.mongoClient = mongoClient;

        // Route to add new events
        router.post("/addEvent").handler(this::handleAddEvent);

        // Route to list all events
        router.get("/events").handler(this::handleListEvents);
    }

    // POST /addEvent
    private void handleAddEvent(RoutingContext ctx) {
        JsonObject body = ctx.getBodyAsJson();

        String name = body.getString("name");
        String date = body.getString("date");
        String location = body.getString("location");
        Integer availableTokens = body.getInteger("availableTokens");

        if (name == null || date == null || location == null || availableTokens == null) {
            ctx.response().setStatusCode(400).end("Missing event fields");
            return;
        }

        JsonObject event = new JsonObject()
                .put("name", name)
                .put("date", date)
                .put("location", location)
                .put("availableTokens", availableTokens);

        mongoClient.save("events", event, res -> {
            if (res.succeeded()) {
                ctx.response()
                        .putHeader("Content-Type", "text/plain")
                        .end("✅ Event added successfully");
            } else {
                ctx.response().setStatusCode(500).end("❌ Failed to add event");
            }
        });
    }

    // GET /events
    private void handleListEvents(RoutingContext ctx) {
        mongoClient.find("events", new JsonObject(), res -> {
            if (res.succeeded()) {
                ctx.response()
                        .putHeader("Content-Type", "application/json")
                        .end(res.result().toString());
            } else {
                ctx.response().setStatusCode(500).end("❌ Failed to fetch events");
            }
        });
    }
}
