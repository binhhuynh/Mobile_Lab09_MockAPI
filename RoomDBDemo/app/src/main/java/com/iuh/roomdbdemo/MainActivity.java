package com.iuh.roomdbdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;

import com.iuh.roomdbdemo.dao.UserDao;
import com.iuh.roomdbdemo.db.AppDatabase;
import com.iuh.roomdbdemo.entity.User;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppDatabase db = Room.databaseBuilder(this,
                AppDatabase.class, "MyDatabase").allowMainThreadQueries().build();

        UserDao userDao = db.userDao();
        List<User> users = userDao.getAll();
    }
}