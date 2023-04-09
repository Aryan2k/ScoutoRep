package com.example.scouto.adapters;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.scouto.R;
import com.example.scouto.callbacks.CarCallbackListener;
import com.example.scouto.databinding.ItemCarLayoutBinding;
import com.example.scouto.db.entity.Car;

import java.util.List;


public class CarListAdapter extends RecyclerView.Adapter<CarListAdapter.ViewHolder> {
    private final List<Car> carList;
    private final CarCallbackListener listener;

    public CarListAdapter(List<Car> carList, CarCallbackListener listener) {
        this.carList = carList;
        this.listener = listener;
    }

    @NonNull
    @Override

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCarLayoutBinding binding = ItemCarLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(carList.get(position));
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemCarLayoutBinding binding;

        public ViewHolder(ItemCarLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.addCarImageTxt.setOnClickListener(view -> listener.onCarChangeImageClicked(getAdapterPosition()));
            binding.deleteCarTxt.setOnClickListener(view -> listener.onCarDeleteClicked(getAdapterPosition()));

        }

        void bind(Car car) {
            Log.e("abc", "bind: " + car.getCarImageLink());
            if (car.getCarImageLink().isEmpty()) {
                Glide.with(binding.addCarImg).load(R.drawable.ic_default_car).into(binding.addCarImg);
            } else {
                Glide.with(binding.addCarImg).load(car.getCarImageLink()).error(R.drawable.ic_default_car).centerCrop().into(binding.addCarImg);
            }
            binding.carMakeTxt.setText(car.getCarMake());
            binding.carModelTxt.setText(car.getCarModel());
        }
    }
}
