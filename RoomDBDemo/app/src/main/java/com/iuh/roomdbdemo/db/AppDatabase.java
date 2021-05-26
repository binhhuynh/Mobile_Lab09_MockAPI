package com.iuh.roomdbdemo.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.iuh.roomdbdemo.dao.UserDao;
import com.iuh.roomdbdemo.entity.User;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}
