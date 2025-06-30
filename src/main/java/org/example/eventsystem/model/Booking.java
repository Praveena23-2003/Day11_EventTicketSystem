package org.example.eventsystem.model;

public class Booking {
    private String id;
    private String userId;
    private String eventId;
    private String bookingToken;

    // Constructors
    public Booking() {}
    public Booking(String id, String userId, String eventId, String bookingToken) {
        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
        this.bookingToken = bookingToken;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getBookingToken() { return bookingToken; }
    public void setBookingToken(String bookingToken) { this.bookingToken = bookingToken; }
}
