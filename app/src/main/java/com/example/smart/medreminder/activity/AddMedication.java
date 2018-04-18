package com.example.smart.medreminder.activity;

import android.app.DatePickerDialog;
import android.content.ClipboardManager;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.smart.medreminder.R;
import com.example.smart.medreminder.adapter.MedicineAdapter;
import com.example.smart.medreminder.helper.DatabaseHelper;
import com.example.smart.medreminder.helper.LocalData;
import com.example.smart.medreminder.helper.MedicationHelper;
import com.example.smart.medreminder.helper.NotificationScheduler;
import com.example.smart.medreminder.model.MedicationModel;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddMedication extends AppCompatActivity {

    private int interval;
    private Calendar calendar;
    private String drugName2, description2, drugInterval2, unit2, startDate2, endDate2;
    private MaterialEditText drugName, drugDescription, startDate, endDate;
    private Button proceed;
    private SearchableSpinner drugInterval, drugDosage;
    private SimpleDateFormat simpleDateFormat;
    MedicationHelper medicationHelper;
    MedicationModel medicationModel;

    private FirebaseAnalytics mFirebaseAnalytics;
    private RecyclerView recyclerView;
    private MedicineAdapter medicineAdapter;
    private ArrayList<MedicationModel> mDataList;
    public int hour, minute, year, month, dayOfMonth;
    LocalData localData;
    ClipboardManager myClipboard;
    LinearLayout ll_set_time, ll_end_time, layoutMedicine;
    SwitchCompat reminderSwitch;
    TextInputLayout layoutDescription;
    EditText notificationTitle, notificationContent;
    NotificationScheduler notificationScheduler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication);

        initViews();
        initListeners();
        initObjects();

    }

    private void initViews() {

        drugName = findViewById(R.id.drugName);
        drugDescription = findViewById(R.id.drugDescription);
        startDate = findViewById(R.id.startDate);
        endDate = findViewById(R.id.endDate);
        proceed = findViewById(R.id.btnProceed);
        drugInterval = findViewById(R.id.interval);
        drugDosage = findViewById(R.id.spinnerUnit);
    }

    private void initListeners() {

        final DatePickerDialog.OnDateSetListener dateNotificationStart = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                AddMedication.this.setStartMedication(startDate2);
            }
        };

        final DatePickerDialog.OnDateSetListener dateNotificationEnd = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                AddMedication.this.setEndMedication(endDate2);
            }
        };

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String now = simpleDateFormat.format(calendar.getTime());
        startDate.setText(now);
        endDate.setText(now);

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddMedication.this, dateNotificationStart,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddMedication.this, dateNotificationEnd,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                inputValidation();

                saveDataToDB();
            }
        });

    }

    private void setStartMedication(String dateStart) {
        String date1 = simpleDateFormat.format(calendar.getTime());
        startDate.setText(date1);
        startDate2 = dateStart;
    }

    private void setEndMedication(String dateEnd) {
        String date2 = simpleDateFormat.format(calendar.getTime());
        endDate.setText(date2);
        endDate2 = dateEnd;
    }

    private void initObjects() {

        medicationHelper = new MedicationHelper(this);
        medicationModel = new MedicationModel();
    }

    private void inputValidation() {

        drugName2 = drugName.getText().toString().trim();
        description2 = drugDescription.getText().toString().trim();
        drugInterval2 = drugInterval.getSelectedItem().toString().trim();
        unit2 = drugDosage.getSelectedItem().toString().trim();
        startDate2 = startDate.getText().toString().trim();
        endDate2 = endDate.getText().toString().trim();

        if (TextUtils.isEmpty(drugName2)) {
            drugName.setError("Enter Medicine Name");
            return;
        }

        if (TextUtils.isEmpty(description2)) {
            drugDescription.setError("Enter Medicine Description");
            return;
        }

        if (TextUtils.isEmpty(drugInterval2)) {
            Toast.makeText(this, "Enter Medicine interval", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(unit2)) {
            Toast.makeText(this, "Enter Medicine Dosage", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(startDate2)) {
            startDate.setError("Set reminder start date");
            return;
        }

        if (TextUtils.isEmpty(endDate2)) {
            endDate.setError("Set reminder stop date");
        }


    }

    private void saveDataToDB() {

        medicationModel.setDrugName(drugName2);
        medicationModel.setDescription(description2);
        medicationModel.setInterval(drugInterval2);


        medicationHelper.addReminder(medicationModel);

        Toast.makeText(this, "Medicine successfully added",
                Toast.LENGTH_SHORT).show();

        Intent mainActivity = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();

        bundle.putString("drugName", drugName2);
        bundle.putString("drugDescription", description2);
        bundle.putString("interval", drugInterval2);
        bundle.putString("unit", unit2);
        bundle.putString("startDate", startDate2);
        bundle.putString("endDate", endDate2);

        mainActivity.putExtras(bundle);

        startActivity(mainActivity);
        finish();
    }
}
