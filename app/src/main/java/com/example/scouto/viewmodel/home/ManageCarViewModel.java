package com.example.scouto.viewmodel.home;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.scouto.db.entity.Car;
import com.example.scouto.network.response.manage_car.MakeDetails;
import com.example.scouto.network.response.manage_car.MakeDetailsResponseData;
import com.example.scouto.network.response.manage_car.ModelDetails;
import com.example.scouto.network.response.manage_car.ModelDetailsResponseData;
import com.example.scouto.repository.HomeRepository;
import com.example.scouto.utils.Data;
import com.example.scouto.utils.RequestStatus;
import com.example.scouto.utils.Resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ManageCarViewModel extends ViewModel {
    private final HomeRepository homeRepository;

    public MutableLiveData<Resource<Data<Car>>> insertCarLiveData = new MutableLiveData<>();
    public MutableLiveData<Resource<Data<Integer>>> deleteCarLiveData = new MutableLiveData<>();
    public MutableLiveData<Resource<Data<Integer>>> updateCarLiveData = new MutableLiveData<>();
    public MutableLiveData<Resource<Data<List<Car>>>> getCarLiveData = new MutableLiveData<>();
    public MutableLiveData<Resource<Data<MakeDetailsResponseData>>> makeDetailsResponseData = new MutableLiveData<>();
    public MutableLiveData<Resource<Data<ModelDetailsResponseData>>> modelDetailsResponseData = new MutableLiveData<>();
    public MutableLiveData<Boolean> addCarBtnActivateLiveData = new MutableLiveData<>();

    public List<Car> carList;
    public ArrayList<MakeDetails> makeDetailsArrayList;
    public ArrayList<ModelDetails> modelDetailsArrayList;

    @Inject

    public ManageCarViewModel(HomeRepository homeRepository) {
        this.homeRepository = homeRepository;
    }


    public void insertCar(Car car) {
        insertCarLiveData.postValue(new Resource<>(RequestStatus.LOADING, null, null));
        AsyncTask.execute(() -> {
            long id = homeRepository.insertCar(car);
            if (id != -1) {
                Data<Car> data = new Data<>();
                car.setId(id);
                data.set(car);
                insertCarLiveData.postValue(new Resource<>(RequestStatus.SUCCESS, data, null));
            } else {
                insertCarLiveData.postValue(new Resource<>(RequestStatus.EXCEPTION, null, null));
            }
        });
    }

    public void updateCar(Car car, int pos) {
        Data<Integer> data = new Data<>();
        data.set(pos);
        updateCarLiveData.postValue(new Resource<>(RequestStatus.LOADING, data, null));
        AsyncTask.execute(() -> {
            long id = homeRepository.updateCar(car);
            if (id != 0) {
                updateCarLiveData.postValue(new Resource<>(RequestStatus.SUCCESS, data, null));
            } else {
                updateCarLiveData.postValue(new Resource<>(RequestStatus.EXCEPTION, data, null));
            }
        });
    }

    public void deleteCar(int pos) {
        Data<Integer> data = new Data<>();
        data.set(pos);
        deleteCarLiveData.postValue(new Resource<>(RequestStatus.LOADING, data, null));
        AsyncTask.execute(() -> {
            Log.e("abc", "deleteCar: "+carList.get(pos) );
            long id = homeRepository.deleteCar(carList.get(pos));
            Log.e("abc", "deleteCar: "+id);
            if (id != 0) {
                deleteCarLiveData.postValue(new Resource<>(RequestStatus.SUCCESS, data, null));
            } else {
                deleteCarLiveData.postValue(new Resource<>(RequestStatus.EXCEPTION, data, null));
            }
        });
    }

    public void getAllCar() {
        getCarLiveData.postValue(new Resource<>(RequestStatus.LOADING, null, null));
        AsyncTask.execute(() -> {
            this.carList = homeRepository.getAllCarList();
            Collections.reverse(this.carList);
            getCarLiveData.postValue(new Resource<>(RequestStatus.SUCCESS, null, null));
        });
    }

    public void getMakeDetails() {
        makeDetailsResponseData.postValue(new Resource<>(RequestStatus.LOADING, null, null));
        AsyncTask.execute(() -> {
            Data<MakeDetailsResponseData> data = new Data<>();
            data.set(homeRepository.getMakeDetails());
            if (data.get() != null) {
                makeDetailsArrayList = data.get().getResults();
                makeDetailsResponseData.postValue(new Resource<>(RequestStatus.SUCCESS, data, null));
            } else {
                makeDetailsArrayList = null;
                makeDetailsResponseData.postValue(new Resource<>(RequestStatus.EXCEPTION, null, null));
            }
        });
    }

    public void getModelDetails(long makeId) {
        modelDetailsResponseData.postValue(new Resource<>(RequestStatus.LOADING, null, null));
        AsyncTask.execute(() -> {
            Data<ModelDetailsResponseData> data = new Data<>();
            data.set(homeRepository.getModelDetails(makeId));
            if (data.get() != null) {
                modelDetailsArrayList = data.get().getResults();
                modelDetailsResponseData.postValue(new Resource<>(RequestStatus.SUCCESS, data, null));
            } else {
                modelDetailsResponseData.postValue(new Resource<>(RequestStatus.EXCEPTION, null, null));
            }
        });
    }

    public void activateBtn(Boolean isMakeSelected, Boolean isModelSelected) {
        if (isMakeSelected && isModelSelected)
            addCarBtnActivateLiveData.postValue(true);
        else
            addCarBtnActivateLiveData.postValue(false);
    }

}

