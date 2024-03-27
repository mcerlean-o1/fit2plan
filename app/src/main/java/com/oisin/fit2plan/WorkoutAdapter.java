package com.oisin.fit2plan;

import android.content.Context;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutHolder> {

    ArrayList<Workout> workouts;
    Context context;
    ItemClicked itemClicked;
    public WorkoutAdapter(ArrayList<Workout> workoutList, Context context) {
        this.workouts = workoutList;
        this.context = context;
    }

    class WorkoutHolder extends RecyclerView.ViewHolder {

        EditText date;
        TextView a1, a2, b1, b2, c1, c2, multiNotes;
        Button btnEdit;
        public WorkoutHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.editTextDate);
            a1 = itemView.findViewById(R.id.edt_a1);
            a2 = itemView.findViewById(R.id.edt_a2);
            b1 = itemView.findViewById(R.id.edt_b1);
            b2 = itemView.findViewById(R.id.edt_b2);
            c1 = itemView.findViewById(R.id.edt_c1);
            c2 = itemView.findViewById(R.id.edt_c2);
            multiNotes = itemView.findViewById(R.id.edt_multiNotes);

            btnEdit = itemView.findViewById(R.id.btnEdit);

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClicked.onClick(getAdapterPosition(),itemView);
                }
            });

        }
    }

    @NonNull
    @Override
    public WorkoutHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.workout_handler, parent, false);
        return new WorkoutHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutHolder holder, int position) {

        holder.date.setText(workouts.get(position).getDates());
        holder.a1.setText(workouts.get(position).getA1());
        holder.a2.setText(workouts.get(position).getA2());
        holder.b1.setText(workouts.get(position).getB1());
        holder.b2.setText(workouts.get(position).getB2());
        holder.c1.setText(workouts.get(position).getC1());
        holder.c2.setText(workouts.get(position).getC2());
        holder.multiNotes.setText(workouts.get(position).getMultiNotes());
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    interface ItemClicked {
        void onClick(int position, View v);
    }
}

