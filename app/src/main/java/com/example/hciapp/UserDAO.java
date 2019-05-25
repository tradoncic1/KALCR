package com.example.hciapp;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert
    public void addUser(User user);

    @Query("SELECT * FROM user")
    public List<User> getAllUsers();

    @Query("SELECT * FROM user WHERE userId == :userId LIMIT 1")
    public User getUser(int userId);

    @Delete
    public void deleteUser(User user);

    @Update
    public void updatUser(User user);

    @Delete
    public void deleteAll(List<User> users);
}
