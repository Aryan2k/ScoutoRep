package com.example.scouto.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.scouto.R;
import com.example.scouto.adapters.CarListAdapter;
import com.example.scouto.callbacks.CarCallbackListener;
import com.example.scouto.databinding.FragmentManageCarBinding;
import com.example.scouto.db.entity.Car;
import com.example.scouto.network.response.manage_car.MakeDetails;
import com.example.scouto.network.response.manage_car.MakeDetailsResponseData;
import com.example.scouto.network.response.manage_car.ModelDetails;
import com.example.scouto.network.response.manage_car.ModelDetailsResponseData;
import com.example.scouto.ui.authentication.AuthenticationActivity;
import com.example.scouto.utils.Data;
import com.example.scouto.utils.FunctionUtils;
import com.example.scouto.utils.Resource;
import com.example.scouto.viewmodel.home.ManageCarViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ManageCarFragment extends Fragment implements View.OnClickListener, CarCallbackListener {

    private FragmentManageCarBinding binding;
    private ManageCarViewModel viewModel;

    private CarListAdapter carListAdapter;

    private int pos;
    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getData() != null) {
                        Uri uri = data.getData();
                        String uriString = uri.toString();
                        Car car = viewModel.carList.get(pos);
                        car.setCarImageLink(uriString);
                        viewModel.updateCar(car, pos);
                    }
                }
            });

    public ManageCarFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentManageCarBinding.inflate(inflater);
        viewModel = new ViewModelProvider(this).get(ManageCarViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpClickListeners();
        setUpObservers();
        viewModel.getAllCar();
        viewModel.getMakeDetails();
    }

    private void setUpObservers() {
        observeCarList();
        observeInsertCar();
        observeDeleteCar();
        observeChangeCarImage();
        observeMakeDetails();
        observeModelDetails();
        observeAddCarBtn();
    }

    private void observeAddCarBtn() {
        Observer<Boolean> observer = value -> {
            if (value) {
                binding.addCarBtn.setAlpha(1f);
            } else
                binding.addCarBtn.setAlpha(.4f);

        };
        viewModel.addCarBtnActivateLiveData.observe(getViewLifecycleOwner(), observer);
    }

    private void observeModelDetails() {
        Observer<Resource<Data<ModelDetailsResponseData>>> observer = resource -> {
            switch (resource.getStatus()) {
                case LOADING: {
                    binding.modelContainer.setVisibility(View.INVISIBLE);
                    binding.shimmerModelDetails.getRoot().setVisibility(View.VISIBLE);
                    binding.shimmerModelDetails.root.startShimmer();
                    break;
                }
                case SUCCESS: {
                    binding.modelContainer.setVisibility(View.VISIBLE);
                    binding.shimmerModelDetails.getRoot().setVisibility(View.GONE);
                    binding.shimmerModelDetails.root.stopShimmer();
                    ArrayAdapter<ModelDetails> modelAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, viewModel.modelDetailsArrayList);
                    binding.modelTxt.setAdapter(modelAdapter);
                    binding.modelTxt.setOnItemClickListener((adapterView, view, i, l) -> viewModel.activateBtn(true, true));
                    break;
                }
                case EXCEPTION: {
                    binding.modelContainer.setVisibility(View.VISIBLE);
                    binding.shimmerModelDetails.getRoot().setVisibility(View.GONE);
                    binding.shimmerModelDetails.root.stopShimmer();
                    FunctionUtils.toast(requireContext(), getString(R.string.some_error_occurred_while_fetching_make_details), Toast.LENGTH_SHORT);
                    break;
                }
            }
        };
        viewModel.modelDetailsResponseData.observe(getViewLifecycleOwner(), observer);
    }

    private void observeMakeDetails() {
        Observer<Resource<Data<MakeDetailsResponseData>>> observer = resource -> {
            switch (resource.getStatus()) {
                case LOADING: {
                    binding.manufacturerContainer.setVisibility(View.INVISIBLE);
                    binding.shimmerMakeDetails.getRoot().setVisibility(View.VISIBLE);
                    binding.shimmerMakeDetails.root.startShimmer();
                    break;
                }
                case SUCCESS: {
                    binding.manufacturerContainer.setVisibility(View.VISIBLE);
                    binding.shimmerMakeDetails.getRoot().setVisibility(View.GONE);
                    binding.shimmerMakeDetails.root.stopShimmer();
                    ArrayAdapter<MakeDetails> makeAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, viewModel.makeDetailsArrayList);
                    binding.manufacturerTxt.setAdapter(makeAdapter);
                    binding.manufacturerTxt.setOnItemClickListener((adapterView, view, i, l) -> {
                        viewModel.activateBtn(true, false);
                        binding.modelContainer.setAlpha(1f);
                        binding.modelTxt.setText("");
                        viewModel.getModelDetails(viewModel.makeDetailsArrayList.get(i).getMakeID());
                    });
                    break;
                }
                case EXCEPTION: {
                    binding.manufacturerContainer.setVisibility(View.VISIBLE);
                    binding.shimmerMakeDetails.getRoot().setVisibility(View.GONE);
                    binding.shimmerMakeDetails.root.stopShimmer();
                    FunctionUtils.toast(requireContext(), getString(R.string.some_error_occurred_while_fetching_make_details), Toast.LENGTH_SHORT);
                    break;
                }
            }
        };
        viewModel.makeDetailsResponseData.observe(getViewLifecycleOwner(), observer);
    }

    private void observeChangeCarImage() {
        Observer<Resource<Data<Integer>>> observer = resource -> {
            switch (resource.getStatus()) {
                case LOADING: {
                    Objects.requireNonNull(binding.carListRv.findViewHolderForAdapterPosition(resource.getData().get())).itemView.setAlpha(.4f);
                    break;
                }
                case SUCCESS: {
                    Objects.requireNonNull(binding.carListRv.findViewHolderForAdapterPosition(resource.getData().get())).itemView.setAlpha(1f);
                    int pos = resource.getData().get();
                    carListAdapter.notifyItemChanged(pos);
                    break;
                }
                case EXCEPTION: {
                    Objects.requireNonNull(binding.carListRv.findViewHolderForAdapterPosition(resource.getData().get())).itemView.setAlpha(1f);
                    FunctionUtils.toast(requireContext(), getString(R.string.some_error_occurred), Toast.LENGTH_SHORT);
                    break;
                }
            }
        };
        viewModel.updateCarLiveData.observe(getViewLifecycleOwner(), observer);
    }

    private void observeDeleteCar() {
        Observer<Resource<Data<Integer>>> observer = resource -> {
            switch (resource.getStatus()) {
                case LOADING: {
                    Objects.requireNonNull(binding.carListRv.findViewHolderForAdapterPosition(resource.getData().get())).itemView.setAlpha(.4f);
                    break;
                }
                case SUCCESS: {
                    int pos = resource.getData().get();
                    viewModel.carList.remove(pos);
                    carListAdapter.notifyItemRemoved(pos);
                    break;
                }
                case EXCEPTION: {
                    Objects.requireNonNull(binding.carListRv.findViewHolderForAdapterPosition(resource.getData().get())).itemView.setAlpha(1f);
                    FunctionUtils.toast(requireContext(), getString(R.string.some_error_occurred), Toast.LENGTH_SHORT);
                    break;
                }
            }
        };
        viewModel.deleteCarLiveData.observe(getViewLifecycleOwner(), observer);
    }

    private void observeCarList() {
        Observer<Resource<Data<List<Car>>>> observer = resource -> {
            switch (resource.getStatus()) {
                case LOADING: {
                    binding.shimmerContainer.getRoot().setVisibility(View.VISIBLE);
                    binding.shimmerContainer.shimmerLayout.startShimmer();
                    binding.carListRv.setVisibility(View.GONE);
                    break;
                }
                case SUCCESS: {
                    binding.shimmerContainer.shimmerLayout.startShimmer();
                    binding.shimmerContainer.getRoot().setVisibility(View.GONE);
                    binding.carListRv.setVisibility(View.VISIBLE);
                    carListAdapter = new CarListAdapter(viewModel.carList, this);
                    binding.carListRv.setAdapter(carListAdapter);
                    break;
                }
            }
        };
        viewModel.getCarLiveData.observe(getViewLifecycleOwner(), observer);
    }

    private void observeInsertCar() {
        Observer<Resource<Data<Car>>> observer = resource -> {
            switch (resource.getStatus()) {
                case LOADING: {
                    binding.addCarBtnTxt.setText(R.string.adding_car);
                    binding.addNewCarProgress.setVisibility(View.VISIBLE);
                    break;
                }
                case SUCCESS: {
                    FunctionUtils.toast(requireContext(), getString(R.string.car_added_successfully), Toast.LENGTH_SHORT);
                    binding.addCarBtnTxt.setText(R.string.add_car);
                    binding.addNewCarProgress.setVisibility(View.INVISIBLE);
                    viewModel.carList.add(0, resource.getData().get());
                    carListAdapter.notifyItemInserted(0);
                    binding.carListRv.scrollToPosition(0);
                    break;
                }
                case EXCEPTION: {
                    FunctionUtils.toast(requireContext(), getString(R.string.some_error_occured), Toast.LENGTH_SHORT);
                    binding.addCarBtnTxt.setText(R.string.add_car);
                    binding.addNewCarProgress.setVisibility(View.INVISIBLE);
                    break;
                }
            }
        };
        viewModel.insertCarLiveData.observe(getViewLifecycleOwner(), observer);
    }

    private void setUpClickListeners() {

        binding.signoutBtn.setOnClickListener(this);
        binding.addCarBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view != null) {
            if (binding.signoutBtn.getId() == view.getId()) {
                showSignoutDialog();
            } else if (binding.addCarBtn.getId() == view.getId()) {
                if (binding.manufacturerTxt.getText().toString().isEmpty()) {
                    FunctionUtils.toast(requireContext(), getString(R.string.please_select_make_first), Toast.LENGTH_SHORT);
                } else if (binding.modelTxt.getText().toString().isEmpty()) {
                    FunctionUtils.toast(requireContext(), getString(R.string.please_select_model_first), Toast.LENGTH_SHORT);
                } else {
                    viewModel.insertCar(new Car(0, "", binding.manufacturerTxt.getText().toString(), binding.modelTxt.getText().toString()));
                }
            }
        }
    }

    private void showSignoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage(R.string.do_you_want_to_signout);
        builder.setTitle(R.string.alert);
        builder.setIcon(R.drawable.baseline_warning_24);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.yes, (dialog, which) -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(requireActivity(), AuthenticationActivity.class));
            FunctionUtils.toast(requireContext(), getString(R.string.signed_out_successfully), Toast.LENGTH_SHORT);
            requireActivity().finish();
        });
        builder.setNegativeButton(R.string.no, (dialog, which) -> dialog.cancel());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @Override
    public void onCarChangeImageClicked(int pos) {
        this.pos = pos;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        launcher.launch(intent);
    }

    @Override
    public void onCarDeleteClicked(int pos) {
        viewModel.deleteCar(pos);
    }

}