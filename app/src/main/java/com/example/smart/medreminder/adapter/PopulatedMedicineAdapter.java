package com.example.smart.medreminder.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smart.medreminder.R;
import com.example.smart.medreminder.model.MedicationModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PopulatedMedicineAdapter extends RecyclerView.Adapter<PopulatedMedicineAdapter.ViewHolder> {
        ArrayList<MedicationModel> medicationModel = new ArrayList<MedicationModel>();
        Context context;
        private final String TAG = PopulatedMedicineAdapter.class.getSimpleName();

        public PopulatedMedicineAdapter(Context context, ArrayList<MedicationModel> medicationModels) {
            this.context = context;
            this.medicationModel = medicationModels;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.daily_medication_item, parent, false);

            ViewHolder viewHolder = new ViewHolder(view, context, medicationModel);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(PopulatedMedicineAdapter.ViewHolder holder, int position) {

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMM d, yyyy");
            String todaysDate = simpleDateFormat.format(calendar.getTime());

            MedicationModel model = medicationModel.get(position);

            holder.cancel.setImageDrawable(model.getCancel());
            holder.notification.setImageDrawable(model.getNotification());
            holder.check.setImageDrawable(model.getCheck());
            holder.medicineImage.setImageDrawable(model.getMedicine());
            holder.drug.setText(model.getDrugName());
            holder.tvDescription.setText(model.getDescription());
            holder.date.setText(todaysDate);
        }

        @Override
        public int getItemCount() {
            int count;

            if (!medicationModel.isEmpty() || medicationModel != null) {
                count = medicationModel.size();
            } else {
                count = 0;
            }

            return count;
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            ArrayList<MedicationModel> models = new ArrayList<>();
            Context context;
            private ImageView cancel, notification, check, medicineImage;
            private TextView drug, dosage, tvDescription;
            private TextView date;

            public ViewHolder(View itemView, final Context context, ArrayList<MedicationModel> medicationModels) {
                super(itemView);

                this.context = context;
                this.models = medicationModels;

                itemView.setOnClickListener(this);

                cancel = itemView.findViewById(R.id.cancel);
                notification = itemView.findViewById(R.id.notification);
                check = itemView.findViewById(R.id.check);
                medicineImage = itemView.findViewById(R.id.medicine_image);
                drug = itemView.findViewById(R.id.drugName);
                tvDescription = itemView.findViewById(R.id.drugDescription);
                date = itemView.findViewById(R.id.date);


                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
            }
        }
    }
