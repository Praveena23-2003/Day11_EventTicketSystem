package org.example.eventsystem.model;

public class Event {
    private String id;
    private String name;
    private String date;
    private int availableTokens;

    // Constructors
    public Event() {}
    public Event(String id, String name, String date, int availableTokens) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.availableTokens = availableTokens;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public int getAvailableTokens() { return availableTokens; }
    public void setAvailableTokens(int availableTokens) { this.availableTokens = availableTokens; }
}
