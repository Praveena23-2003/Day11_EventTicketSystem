package org.example.eventsystem.service;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;
import org.example.eventsystem.util.EmailUtil;
import org.example.eventsystem.util.PasswordUtil;

public class AuthService {
    private final MongoClient mongoClient;

    public AuthService(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public void register(RoutingContext ctx) {
        JsonObject body = ctx.getBodyAsJson();
        String email = body.getString("email");
        String name = body.getString("name");
        String password = PasswordUtil.generateRandomPassword(8);

        JsonObject user = new JsonObject()
                .put("email", email)
                .put("name", name)
                .put("password", password);

        mongoClient.save("users", user, res -> {
            if (res.succeeded()) {
                EmailUtil.sendEmail(email, "Your Event System Password", "Password: " + password);
                ctx.response().setStatusCode(200).end("Registered Successfully. Password sent to email.");
            } else {
                ctx.response().setStatusCode(500).end("Registration Failed");
            }
        });
    }

    public void login(RoutingContext ctx) {
        JsonObject body = ctx.getBodyAsJson();
        String email = body.getString("email");
        String password = body.getString("password");

        JsonObject query = new JsonObject().put("email", email).put("password", password);
        mongoClient.findOne("users", query, null, res -> {
            if (res.succeeded() && res.result() != null) {
                ctx.response().setStatusCode(200).end("Login successful");
            } else {
                ctx.response().setStatusCode(401).end("Invalid credentials");
            }
        });
    }
}