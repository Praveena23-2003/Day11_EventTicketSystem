package org.example.eventsystem.controller;

import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import org.example.eventsystem.service.BookingService;

public class BookingController {
    public BookingController(Router router, MongoClient mongoClient) {
        BookingService bookingService = new BookingService(mongoClient);

        router.post("/book").handler(ctx -> bookingService.bookToken(ctx));
    }
}