package com.example.hciapp;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.drawable.Drawable;
import android.media.Image;

@Entity(tableName = "event")
public class Event {
    @PrimaryKey(autoGenerate = true)
    int eventId;

    String title;
    String description;
    String location;
    String dateTime;
    String price;
    String type;
    String venue;
    byte[] eventImage;

    public Event(int eventId, String title, String description, String location, String dateTime, String price, String type, String venue, byte[] eventImage) {
        this.eventId = eventId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.dateTime = dateTime;
        this.price = price;
        this.type = type;
        this.venue = venue;
        this.eventImage = eventImage;
    }

    @Ignore
    public Event(String title, String description, String location, String dateTime, String price, String type, String venue) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.dateTime = dateTime;
        this.price = price;
        this.type = type;
        this.venue = venue;
    }

    @Ignore
    public Event(String title, String description, String location, String dateTime, String price, String type, String venue, byte[] eventImage) {
        this.eventId = eventId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.dateTime = dateTime;
        this.price = price;
        this.type = type;
        this.venue = venue;
        this.eventImage = eventImage;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public byte[] getEventImage() {
        return eventImage;
    }

    public void setEventImage(byte[] eventImage) {
        this.eventImage = eventImage;
    }
}
