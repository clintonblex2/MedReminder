package com.example.smart.medreminder.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.MenuItem;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.smart.medreminder.R;
import com.example.smart.medreminder.adapter.PopulatedMedicineAdapter;
import com.example.smart.medreminder.helper.MedicationHelper;
import com.example.smart.medreminder.helper.NotificationScheduler;
import com.example.smart.medreminder.model.MedicationModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private GoogleSignInClient mGoogleSignInClient;
    private final AppCompatActivity activity = MainActivity.this;
    private final String TAG = MainActivity.class.getSimpleName();
    private TextView profileName, personGName, pEmail, userId;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private GoogleSignInOptions gso;
    private GoogleSignInAccount currentUser;
    private CircleImageView profilePicture;
    private TextView profileEmail;
    private CardView cardView;
    private SimpleDateFormat simpleDateFormat;
//    private List<MedicationModel> medicationList;
    NotificationScheduler notificationScheduler;
    private FirebaseAnalytics mFirebaseAnalytics;
    Toolbar toolbar;
    private Calendar calendar;
    private String date;
    private RecyclerView recyclerView;
    private LinearLayout linearLayout;
    private RelativeLayout relativeLayout;
    MedicationHelper medicationHelper;
    PopulatedMedicineAdapter medicineAdapter;
    ArrayList<MedicationModel> medicationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();
        initListeners();
        initObjects();

    }

    private void initViews() {

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        cardView = findViewById(R.id.cardView);
        linearLayout = findViewById(R.id.layoutEmptyMedication);
        relativeLayout = findViewById(R.id.relativeLayout2);
        View header = navigationView.getHeaderView(0);

        profilePicture = header.findViewById(R.id.profile_image);
        profileEmail = header.findViewById(R.id.textViewEmail);
        profileName = header.findViewById(R.id.textViewName);

        recyclerView = findViewById(R.id.recyclerView);
    }

    private void initListeners() {

        drawer.setOnClickListener(this);
        cardView.setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        currentUser = GoogleSignIn.getLastSignedInAccount(activity);
        if (currentUser != null) {
            String personName = currentUser.getDisplayName();
            String personEmail = currentUser.getEmail();
            Uri personPhoto = currentUser.getPhotoUrl();

            profileName.setText(personName);
            profileEmail.setText(personEmail);

            if (personPhoto != null) {
                Glide.with(this).load(personPhoto).into(profilePicture);

            } else {
                profilePicture.setImageDrawable(getResources().getDrawable(R.drawable.user));
            }


        }

    }

    private void initObjects() {

        medicationList = new ArrayList<>();
        medicationHelper = new MedicationHelper(this);
        medicineAdapter = new PopulatedMedicineAdapter(this, medicationList);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,  false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));


        if (MainActivity.this.medicationList == null) {
            setListData(medicationList);

            getDataFromSQLite();

        }

    }

    private void setListData(final List<MedicationModel> medicationList) {

        if (medicationList.isEmpty()) {
            linearLayout.setVisibility(View.VISIBLE);

        } else {

            relativeLayout.setVisibility(View.VISIBLE);





             }

    }

    @SuppressLint("StaticFieldLeak")
    private void getDataFromSQLite() {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                medicationList.clear();
                medicationList.addAll(medicationHelper.getAllReminders());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                medicineAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home:
                break;

            case R.id.nav_profile:
                startActivity(new Intent(activity, UserProfile.class));
                break;

            case R.id.nav_medication:
                break;

            case R.id.nav_reminder:
                break;

            case R.id.nav_logout:
                signOut();
                startActivity(new Intent(activity, LoginActivity.class));
                finish();
                break;
        }
        return true;
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }
                });
    }

    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }
                });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.cardView:
                Toast.makeText(activity, "Clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(activity, AddMedication.class));
        }
    }
}
