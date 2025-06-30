package org.example.eventsystem.controller;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class BookingController {

    private final MongoClient mongoClient;

    public BookingController(Router router, MongoClient mongoClient) {
        this.mongoClient = mongoClient;
        router.post("/bookings").handler(this::bookToken);
    }

    private void bookToken(RoutingContext context) {
        JsonObject body = context.getBodyAsJson();
        if (body == null || !body.containsKey("eventId") || !body.containsKey("email")) {
            context.response()
                    .setStatusCode(400)
                    .putHeader("Content-Type", "application/json")
                    .end(new JsonObject().put("status", "error").put("message", "Missing eventId or email").encode());
            return;
        }

        String eventId = body.getString("eventId");
        String email = body.getString("email");

        JsonObject query = new JsonObject().put("_id", eventId);

        mongoClient.findOne("events", query, null, res -> {
            if (res.succeeded() && res.result() != null) {
                int availableTokens = res.result().getInteger("availableTokens", 0);

                if (availableTokens <= 0) {
                    context.response()
                            .setStatusCode(400)
                            .putHeader("Content-Type", "application/json")
                            .end(new JsonObject().put("status", "error").put("message", "No tokens available").encode());
                    return;
                }

                JsonObject update = new JsonObject().put("$inc", new JsonObject().put("availableTokens", -1));
                mongoClient.findOneAndUpdate("events", query, update, updateRes -> {
                    if (updateRes.succeeded()) {
                        context.response()
                                .putHeader("Content-Type", "application/json")
                                .end(new JsonObject()
                                        .put("status", "success")
                                        .put("message", "Token booked successfully")
                                        .put("email", email)
                                        .put("remainingTokens", availableTokens - 1)
                                        .encode());
                    } else {
                        context.response()
                                .setStatusCode(500)
                                .putHeader("Content-Type", "application/json")
                                .end(new JsonObject().put("status", "error").put("message", "Failed to update tokens").encode());
                    }
                });
            } else {
                context.response()
                        .setStatusCode(404)
                        .putHeader("Content-Type", "application/json")
                        .end(new JsonObject().put("status", "error").put("message", "Event not found").encode());
            }
        });
    }
}
