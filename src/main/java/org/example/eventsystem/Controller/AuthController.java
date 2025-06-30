package org.example.eventsystem.controller;

import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.core.json.JsonObject;
import org.example.eventsystem.util.EmailUtil;
import org.example.eventsystem.util.PasswordUtil;

public class AuthController {

    private final MongoClient mongoClient;

    public AuthController(Router router, MongoClient mongoClient) {
        this.mongoClient = mongoClient;

        router.post("/register").handler(this::handleRegister);
        router.post("/login").handler(this::handleLogin);
    }

    private void handleRegister(RoutingContext ctx) {
        JsonObject body = ctx.getBodyAsJson();
        String name = body.getString("name");
        String email = body.getString("email");

        String password = PasswordUtil.generateRandomPassword(8);

        JsonObject user = new JsonObject()
                .put("name", name)
                .put("email", email)
                .put("password", password);

        mongoClient.save("users", user, res -> {
            if (res.succeeded()) {
                EmailUtil.sendEmail(email, "Your Password", "Password: " + password);
                ctx.response().end("User registered. Password sent to email.");
            } else {
                ctx.response().setStatusCode(500).end("Failed to register user");
            }
        });
    }

    private void handleLogin(RoutingContext ctx) {
        JsonObject body = ctx.getBodyAsJson();
        String email = body.getString("email");
        String password = body.getString("password");

        JsonObject query = new JsonObject().put("email", email).put("password", password);

        mongoClient.findOne("users", query, null, res -> {
            if (res.succeeded() && res.result() != null) {
                ctx.response().end("Login successful");
            } else {
                ctx.response().setStatusCode(401).end("Invalid credentials");
            }
        });
    }
}
