package org.example.eventsystem.config;

import io.vertx.core.json.JsonObject;

public class ConfigLoader {
    public static JsonObject loadMongoConfig() {
        return new JsonObject()
                .put("connection_string", "mongodb://localhost:27017")  // Replace with your MongoDB URI
                .put("db_name", "eventsystem");                         // Database name
    }
}
