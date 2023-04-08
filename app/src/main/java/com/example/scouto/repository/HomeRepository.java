package com.example.scouto.repository;

import com.example.scouto.db.entity.Car;
import com.example.scouto.network.response.manage_car.MakeDetailsResponseData;
import com.example.scouto.network.response.manage_car.ModelDetailsResponseData;

import java.util.List;

public interface HomeRepository {
    long insertCar(Car car);

    int updateCar(Car car);

    int deleteCar(Car car);

    List<Car> getAllCarList();

    MakeDetailsResponseData getMakeDetails();
    ModelDetailsResponseData getModelDetails(long makeId);
}
