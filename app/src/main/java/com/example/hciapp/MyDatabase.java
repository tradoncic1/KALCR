package com.example.hciapp;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {User.class, Event.class, Artist.class}, version = 1)
public abstract class MyDatabase extends RoomDatabase {
    private static MyDatabase INSTANCE = null;

    public abstract UserDAO userDAO();
    public abstract EventsDAO eventsDAO();
    public abstract ArtistDAO artistDAO();

    public static MyDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, MyDatabase.class, "kalcr_db").allowMainThreadQueries().build();
        }

        return  INSTANCE;
    }
}
