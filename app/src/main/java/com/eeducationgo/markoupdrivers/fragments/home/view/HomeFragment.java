package com.eeducationgo.markoupdrivers.fragments.home.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.eeducationgo.markoupdrivers.R;
import com.eeducationgo.markoupdrivers.databinding.FragmentHomeBinding;
import com.eeducationgo.markoupdrivers.features.confirem_trip.view.ConfiremTripActivity;
import com.eeducationgo.markoupdrivers.fragments.home.presenter.HomeFragmentPresenter;
import com.eeducationgo.markoupdrivers.util.BaseFragment;
import com.eeducationgo.markoupdrivers.util.Constance;
import com.eeducationgo.markoupdrivers.util.ProgressBarDialog;
import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeFragment extends BaseFragment implements HomeFragmentView {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentHomeBinding binding;
    private SimpleDateFormat mFormatterTime = new SimpleDateFormat("MMMM dd yyyy");
    private SimpleDateFormat mFormatterDate = new SimpleDateFormat("hh:mm aa");
    private static final String TAG = "HomeFragment";
    private HomeFragmentPresenter presenter;
    private FirebaseAuth auth;
    private DatabaseReference databaseReferenceDriver;
    private ProgressBarDialog dialog;
    private String station, destination, tripTime, tripDate;

    public HomeFragment() {
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        init();
        initData();
        initListener();
        return binding.getRoot();
    }

    private void initData() {
        presenter.setData(binding.tvDate);
    }

    private void init() {
        dialog = new ProgressBarDialog(getContext());
        presenter = new HomeFragmentPresenter(this, this);
        auth = FirebaseAuth.getInstance();
        databaseReferenceDriver = FirebaseDatabase.getInstance().getReference()
                .child(Constance.RootDriver).child(auth.getCurrentUser().getUid());
        databaseReferenceDriver.keepSynced(true);
    }

    private void initListener() {
        binding.cardDate.setOnClickListener(v -> showPickDate());
        spinnerStation();
        spinnerDestination();
        startBtn();
        skipBtn();
    }

    private void skipBtn() {
        binding.buttonSkipEmailRegesteration.setOnClickListener(v -> startActivity(new Intent(getContext(), ConfiremTripActivity.class)));
    }

    private void startBtn() {
        binding.buttonFindDriver.setOnClickListener(v -> presenter.updateDestination(databaseReferenceDriver, destination, station, tripTime, tripDate));
    }

    private void spinnerDestination() {
        binding.spinnerDestination.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                destination = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void spinnerStation() {
        binding.spinnerStation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                station = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private SlideDateTimeListener listener = new SlideDateTimeListener() {
        @SuppressLint("NewApi")
        @Override
        public void onDateTimeSet(Date date) {

            Log.d(TAG, "onDateTimeSet: " + mFormatterTime.format(date));
            //Log.d(TAG, "onDateTimeSet: " + mFormatterTime.format(date));
            tripTime = mFormatterTime.format(date).toString();
            tripDate = mFormatterDate.format(date).toString();

        }

        @Override
        public void onDateTimeCancel() {

        }
    };


    public void showPickDate() {
        new SlideDateTimePicker.Builder(getActivity().getSupportFragmentManager())
                .setListener(listener)
                .setInitialDate(new Date())
                .setIs24HourTime(false)
                .build()
                .show();
    }

    @Override
    public void showErrorMessage(String message) {
        super.showErrorMessage(message);
        snackErrorShow(binding.getRoot(), message);
    }

    @Override
    public void showSuccessMessage(String message) {
        super.showSuccessMessage(message);
        snackSuccessShow(binding.getRoot(), message);
    }

    @Override
    public void showProgress() {
        super.showProgress();
        dialog.show();
    }

    @Override
    public void hideProgress() {
        super.hideProgress();
        dialog.dismiss();
    }
}