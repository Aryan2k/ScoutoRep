package com.example.scouto.db;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.scouto.db.dao.CarDao;
import com.example.scouto.db.entity.Car;

@Database(entities = Car.class, exportSchema = false, version = 1)
public abstract class ScoutoDatabase extends RoomDatabase {
    public abstract CarDao getCarDao();
}
