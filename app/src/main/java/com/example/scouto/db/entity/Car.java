package com.example.scouto.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@SuppressWarnings("unused")
@Entity
public class Car {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String carImageLink;
    private String carMake;
    private String carModel;

    public Car(long id, String carImageLink, String carMake, String carModel) {
        this.id = id;
        this.carImageLink = carImageLink;
        this.carMake = carMake;
        this.carModel = carModel;
    }

    public long getId() {
        return id;
    }

    public String getCarImageLink() {
        return carImageLink;
    }

    public void setCarImageLink(String carImageLink) {
        this.carImageLink = carImageLink;
    }

    public void setCarMake(String carMake) {
        this.carMake = carMake;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarMake() {
        return carMake;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCarModel() {
        return carModel;
    }

    @NonNull
    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", carImageLink='" + carImageLink + '\'' +
                ", carMake='" + carMake + '\'' +
                ", carModel='" + carModel + '\'' +
                '}';
    }
}
