package com.example.smart.medreminder.model;


import android.graphics.drawable.Drawable;
import android.widget.TextClock;

public class MedicineModel {


        private String time;
        private String drug;
        private String dosage;
        private String dateTaken;
        private String description;
        private Drawable cancel;
        private Drawable notification;
        private Drawable check;
        private Drawable medicine;
        private TextClock timer;

        public MedicineModel() {

        }

        public MedicineModel(Drawable cancel, Drawable notification, Drawable check,
                               Drawable medicineLogo, String drugName, String description, String drugDate, String drugDosage) {

            this.timer = timer;
            this.cancel = cancel;
            this.notification = notification;
            this.check = check;
            this.medicine = medicineLogo;
            this.drug = drugName;
            this.description = description;
            this.dateTaken = drugDate;
            this.dosage = drugDosage;

        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getDrug() {
            return drug;
        }

        public void setDrug(String drug) {
            this.drug = drug;
        }

        public String getDosage() {
            return dosage;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setDosage(String dosage) {
            this.dosage = dosage;
        }

        public String getDateTaken() {
            return dateTaken;
        }

        public void setDateTaken(String dateTaken) {
            this.dateTaken = dateTaken;
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

