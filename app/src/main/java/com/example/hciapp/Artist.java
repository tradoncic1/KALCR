package com.example.hciapp;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "artist")
public class Artist {
    @PrimaryKey(autoGenerate = true)
    int artistId;
    String username;
    String fullName;
    String email;
    String password;
    String desc;
    String type;
    byte[] artistImage;

    public Artist(int artistId, String username, String fullName, String email, String password, String desc, String type, byte[] artistImage) {
        this.artistId = artistId;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.desc = desc;
        this.type = type;
        this.artistImage = artistImage;
    }

    @Ignore
    public Artist(String username, String fullName, String email, String password, String desc, String type, byte[] artistImage) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.desc = desc;
        this.type = type;
        this.artistImage = artistImage;
    }

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getArtistImage() {
        return artistImage;
    }

    public void setArtistImage(byte[] artistImage) {
        this.artistImage = artistImage;
    }

}
