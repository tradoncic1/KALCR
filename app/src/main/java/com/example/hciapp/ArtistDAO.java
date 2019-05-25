package com.example.hciapp;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ArtistDAO {
    @Insert
    public void addArtist(Artist artist);

    @Query("SELECT * FROM artist")
    public List<Artist> getAllArtists();

    @Query("SELECT * FROM artist WHERE artistId == :artistId LIMIT 1")
    public Artist getArtist(int artistId);

    @Delete
    public void deleteArtist(Artist artist);

    @Update
    public void updateArtist(Artist artist);

    @Delete
    public void deleteAll(List<Artist> artists);
}
