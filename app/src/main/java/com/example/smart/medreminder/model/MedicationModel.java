package com.example.smart.medreminder.model;

import android.graphics.drawable.Drawable;
import android.widget.TextClock;

import java.util.Date;

/**
 * Created by smart on 10/04/2018, 3:05 PM.
 */

public class MedicationModel {

    private long medicineId;
    private String drugName;
    private String description;
    private String interval;
    private boolean hasNotification;
    private Date startDate;
    private Date endDate;
    private Date currentDate;
    private Drawable cancel;
    private Drawable notification;
    private Drawable check;
    private Drawable medicine;

    public MedicationModel() {

    }

    public MedicationModel(String name, String description, String interval, boolean hasNotification, Drawable cancel, Drawable notification, Drawable check,
                           Drawable medicineLogo, Date startDate, Date endDate, Date currentDate) {

        this.drugName = name;
        this.description = description;
        this.interval = interval;
        this.hasNotification = hasNotification;
        this.cancel = cancel;
        this.notification = notification;
        this.check = check;
        this.medicine = medicineLogo;
        this.startDate = startDate;
        this.endDate = endDate;
        this.currentDate = currentDate;

    }


    public long getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(long medicineId) {
        this.medicineId = medicineId;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getDescription() {
        return description;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isHasNotification() {
        return hasNotification;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public void setHasNotification(boolean hasNotification) {
        this.hasNotification = hasNotification;
    }
    public Drawable getCancel() {
        return cancel;
    }

    public void setCancel(Drawable cancel) {
        this.cancel = cancel;
    }

    public Drawable getNotification() {
        return notification;
    }

    public void setNotification(Drawable notification) {
        this.notification = notification;
    }

    public Drawable getCheck() {
        return check;
    }

    public void setCheck(Drawable check) {
        this.check = check;
    }

    public Drawable getMedicine() {
        return medicine;
    }

    public void setMedicine(Drawable medicine) {
        this.medicine = medicine;
    }
}

