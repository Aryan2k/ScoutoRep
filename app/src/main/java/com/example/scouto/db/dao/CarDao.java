package com.example.scouto.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.scouto.db.entity.Car;

import java.util.List;

@Dao
public interface CarDao {

    //returns the id of inserted item
    @Insert
    long insertCar(Car car);

    //return the no of rows deleted
    @Delete
    int deleteCar(Car car);

    //return the no of rows updated
    @Update
    int updateCar(Car car);

    @Query("SELECT * FROM Car")
    List<Car> getCarList();


}
