package org.example.eventsystem.service;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;

public class EventService {
    private final MongoClient mongoClient;

    public EventService(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public void listEvents(RoutingContext ctx) {
        mongoClient.find("events", new JsonObject(), res -> {
            if (res.succeeded()) {
                ctx.response().putHeader("Content-Type", "application/json")
                        .end(res.result().toString());
            } else {
                ctx.response().setStatusCode(500).end("Failed to fetch events");
            }
        });
    }
}
