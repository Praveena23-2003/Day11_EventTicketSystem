package org.example.eventsystem.service;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;
import org.example.eventsystem.util.EmailUtil;
import org.example.eventsystem.util.TokenUtil;

public class BookingService {
    private final MongoClient mongoClient;

    public BookingService(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public void bookToken(RoutingContext ctx) {
        JsonObject body = ctx.getBodyAsJson();
        String email = body.getString("email");
        String eventId = body.getString("eventId");

        // Find user
        mongoClient.findOne("users", new JsonObject().put("email", email), null, userRes -> {
            if (userRes.succeeded() && userRes.result() != null) {
                String userId = userRes.result().getString("_id");

                // Find event and update token count
                JsonObject query = new JsonObject().put("_id", eventId);
                mongoClient.findOne("events", query, null, eventRes -> {
                    if (eventRes.succeeded() && eventRes.result() != null) {
                        int availableTokens = eventRes.result().getInteger("availableTokens");

                        if (availableTokens > 0) {
                            String token = TokenUtil.generateToken();

                            JsonObject booking = new JsonObject()
                                    .put("userId", userId)
                                    .put("eventId", eventId)
                                    .put("bookingToken", token);

                            mongoClient.save("bookings", booking, saveRes -> {
                                if (saveRes.succeeded()) {
                                    // Decrease token count
                                    JsonObject update = new JsonObject().put("$inc",
                                            new JsonObject().put("availableTokens", -1));
                                    mongoClient.updateCollection("events", query, update, updateRes -> {
                                        EmailUtil.sendEmail(email, "Your Event Token", "Booking Token: " + token);
                                        ctx.response().end("Token booked successfully. Sent to email.");
                                    });
                                } else {
                                    ctx.response().setStatusCode(500).end("Booking failed");
                                }
                            });
                        } else {
                            ctx.response().setStatusCode(400).end("No tokens left for this event");
                        }
                    } else {
                        ctx.response().setStatusCode(404).end("Event not found");
                    }
                });
            } else {
                ctx.response().setStatusCode(404).end("User not found");
            }
        });
    }
}