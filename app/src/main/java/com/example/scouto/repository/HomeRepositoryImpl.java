package com.example.scouto.repository;

import com.example.scouto.db.ScoutoDatabase;
import com.example.scouto.db.entity.Car;
import com.example.scouto.network.api.CarApi;
import com.example.scouto.network.response.manage_car.MakeDetailsResponseData;
import com.example.scouto.network.response.manage_car.ModelDetailsResponseData;

import java.util.List;

import javax.inject.Inject;

public class HomeRepositoryImpl implements HomeRepository {

    private final ScoutoDatabase database;

    private final CarApi carApi;

    @Inject
    public HomeRepositoryImpl(ScoutoDatabase database, CarApi apiService) {
        this.database = database;
        this.carApi = apiService;
    }

    @Override
    public long insertCar(Car car) {
        return database.getCarDao().insertCar(car);
    }

    @Override
    public int updateCar(Car car) {
        return database.getCarDao().updateCar(car);
    }

    @Override
    public int deleteCar(Car car) {
        return database.getCarDao().deleteCar(car);
    }

    @Override
    public List<Car> getAllCarList() {
        return database.getCarDao().getCarList();
    }

    @Override
    public MakeDetailsResponseData getMakeDetails() {
        final MakeDetailsResponseData[] details = {null};
        try {
            MakeDetailsResponseData responseData = carApi.getMakeDetailsResponseData().execute().body();
            if (responseData != null)
                details[0] = responseData;
        } catch (Exception ignored) {

        }
        return details[0];
    }

    @Override
    public ModelDetailsResponseData getModelDetails(long makeId) {
        final ModelDetailsResponseData[] details = {null};
        try {
            ModelDetailsResponseData responseData = carApi.getModelDetailsResponseData(makeId).execute().body();
            if (responseData != null)
                details[0] = responseData;
        } catch (Exception ignored) {

        }
        return details[0];
    }
}
