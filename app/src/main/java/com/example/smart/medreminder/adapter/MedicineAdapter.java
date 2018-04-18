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
import com.example.smart.medreminder.model.MedicineModel;

import java.util.ArrayList;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder> {
    ArrayList<MedicineModel> medicineModel = new ArrayList<MedicineModel>();
    Context context;
    private final String TAG = MedicineAdapter.class.getSimpleName();

    public MedicineAdapter(Context context, ArrayList<MedicineModel> medicineModel) {
        this.context = context;
        this.medicineModel = medicineModel;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.daily_medication_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view, context, medicineModel);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MedicineAdapter.ViewHolder holder, int position) {

        MedicineModel model = medicineModel.get(position);

        holder.cancel.setImageDrawable(model.getCancel());
        holder.notification.setImageDrawable(model.getNotification());
        holder.check.setImageDrawable(model.getCheck());
        holder.medicineImage.setImageDrawable(model.getMedicine());
        holder.timer.setText(model.getTime());
        holder.drug.setText(model.getDrug());
        holder.dosage.setText(model.getDosage());
        holder.description.setText(model.getDescription());

        Log.d(TAG, model.getDateTaken());
    }

    @Override
    public int getItemCount() {
        int count;

        if (!medicineModel.isEmpty() || medicineModel != null) {
            count = medicineModel.size();
        } else {
            count = 0;
        }

        return count;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ArrayList<MedicineModel> models = new ArrayList<>();
        Context context;
        private ImageView cancel, notification, check, medicineImage;
        private TextView timer;
        private TextView drug, dosage, description;

        public ViewHolder(View itemView, final Context context, ArrayList<MedicineModel> medicineModels) {
            super(itemView);

            this.context = context;
            this.models = medicineModels;

            itemView.setOnClickListener(this);

            cancel = itemView.findViewById(R.id.cancel);
            notification = itemView.findViewById(R.id.notification);
            check = itemView.findViewById(R.id.check);
            medicineImage = itemView.findViewById(R.id.medicine_image);
            timer = itemView.findViewById(R.id.date);
            drug = itemView.findViewById(R.id.testViewDrugName);
            dosage = itemView.findViewById(R.id.testViewDosage);
            description = itemView.findViewById(R.id.tvDescription);

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
