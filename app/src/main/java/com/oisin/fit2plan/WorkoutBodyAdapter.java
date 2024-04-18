package com.oisin.fit2plan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class WorkoutBodyAdapter extends RecyclerView.Adapter<WorkoutBodyAdapter.ViewHolder> {
    private List<WorkoutBody> bodyWorkouts;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView workoutTitle;
        private final TextView workoutText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            workoutTitle = itemView.findViewById(R.id.workout_name);
            workoutText = itemView.findViewById(R.id.workout_desc);
        }

    }

    public WorkoutBodyAdapter(List<WorkoutBody> bodyWorkouts) {
        this.bodyWorkouts = bodyWorkouts;
    }

    @NonNull
    @Override
    public WorkoutBodyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chest_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutBodyAdapter.ViewHolder holder, int position) {
        WorkoutBody workoutBody = bodyWorkouts.get(position);
        holder.workoutTitle.setText(workoutBody.getWorkoutName());
        holder.workoutText.setText(workoutBody.getWorkoutDescription());

    }

    @Override
    public int getItemCount() {
        return bodyWorkouts.size();
    }
}
