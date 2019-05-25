package com.example.hciapp;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface EventsDAO {
    @Insert
    public void addEvent(Event event);

    @Query("SELECT * FROM event")
    public List<Event> getAllEvents();

    @Query("SELECT * FROM event WHERE eventId == :eventId LIMIT 1")
    public Event getEvent(int eventId);

    @Delete
    public void deleteEvent(Event event);

    @Update
    public void updateEvent(Event event);

    @Delete
    public void deleteAll(List<Event> events);
}
