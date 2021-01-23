package com.eeducationgo.markoupdrivers.features.main.view;

import android.os.Bundle;
import android.provider.SyncStateContract;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.eeducationgo.markoupdrivers.R;
import com.eeducationgo.markoupdrivers.features.main.presenter.MainPresenter;
import com.eeducationgo.markoupdrivers.features.registration.model.User;
import com.eeducationgo.markoupdrivers.util.BaseActivity;
import com.eeducationgo.markoupdrivers.util.Constance;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends BaseActivity implements MainView {

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private NavController navController;
    private ImageView imageViewProfile;
    private TextView textViewName, textViewPhone;
    private MainPresenter presenter;
    private Toolbar toolbar;
    private User user;
    private NavigationView navigationView;
    private View hView;
    private FirebaseAuth auth;
    private DatabaseReference databaseReferenceDriver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setUpBars();
        initData();
        sendToken();
    }
    private void sendToken() {
        if (auth.getCurrentUser() != null){
            presenter.sendToken(databaseReferenceDriver);
        }else{

        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }
    private void initData() {

        if (user == null) {
            presenter.initProfiel(databaseReferenceDriver ,imageViewProfile, textViewName, textViewPhone);
        } else {
            presenter.setData(imageViewProfile, textViewName, textViewPhone, user);
        }
    }

    private void init() {
        user = getIntent().getParcelableExtra(Constance.userKey);
        auth = FirebaseAuth.getInstance();
        databaseReferenceDriver = FirebaseDatabase.getInstance().getReference()
                .child(Constance.RootDriver).child(auth.getCurrentUser().getUid());
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.nav_view);
        presenter = new MainPresenter(this, this);
        hView = navigationView.getHeaderView(0);
        imageViewProfile = hView.findViewById(R.id.profile_image_home);
        textViewName = hView.findViewById(R.id.tv_name_home);
        textViewPhone = hView.findViewById(R.id.tv_phone_home);
    }

    private void setUpBars() {

        navigationView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navListener(navigationView);

    }

    private void navListener(NavigationView navigationView) {
        presenter.drawerListener(navigationView, drawer, navController);
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}