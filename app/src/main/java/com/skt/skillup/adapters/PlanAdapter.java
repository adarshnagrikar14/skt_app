package com.skt.skillup.adapters;

import static com.skt.skillup.SharedPreferencesHelper.savePlans;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skt.skillup.R;
import com.skt.skillup.models.PlanModel;
import com.skt.skillup.utils.DateTimeUtils;

import java.util.ArrayList;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> {

    private final ArrayList<PlanModel> planList;
    Context context;

    public PlanAdapter(ArrayList<PlanModel> planList, Context context) {
        this.planList = planList;
        this.context = context;
    }

    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan, parent, false);
        return new PlanViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
        PlanModel plan = planList.get(position);

        int positionText = position + 1;
        holder.posText.setText(positionText + "");

        if (positionText == planList.size()) {
            holder.arrowImage.setVisibility(View.INVISIBLE);
        }

        String pDate = plan.getDate();
        String pTime = plan.getTime();

        long timestamp = DateTimeUtils.calculateTimestamp(pDate, pTime);

        if (timestamp < System.currentTimeMillis()) {
            holder.layout.setAlpha(0.3f);
        }

        holder.icDelete.setOnClickListener(view -> {

            PlanModel planToDelete = planList.get(holder.getAdapterPosition());
            planList.remove(planToDelete);

            savePlans(view.getContext(), planList);

            ((Activity) view.getContext()).recreate();

        });

        holder.bind(plan);
    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

    static class PlanViewHolder extends RecyclerView.ViewHolder {

        private final TextView textTask;
        private final TextView textDate;
        private final TextView textTime;
        private final TextView posText;

        private final ImageView icDelete;
        private final ImageView arrowImage;

        LinearLayout layout;

        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);
            textTask = itemView.findViewById(R.id.textTask);
            textDate = itemView.findViewById(R.id.textDate);
            textTime = itemView.findViewById(R.id.textTime);
            icDelete = itemView.findViewById(R.id.deleteTask);
            arrowImage = itemView.findViewById(R.id.arrowImage);
            posText = itemView.findViewById(R.id.positionText);

            layout = itemView.findViewById(R.id.linearViewPlanner);
        }

        @SuppressLint("SetTextI18n")
        public void bind(PlanModel plan) {
            textTask.setText(plan.getPlan());
            textDate.setText("\uD83D\uDCC5 \t\t" + plan.getDate());
            textTime.setText("⏳ \t\t" + plan.getTime());
        }

    }

}
