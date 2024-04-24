package com.oisin.fit2plan;

import android.content.Context;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutHandler> {

    ArrayList<Workout> workouts;
    Context context;
    ItemClicked itemClicked;
    ViewGroup parent;
    public WorkoutAdapter(ArrayList<Workout> arrayList, Context context, ItemClicked itemClicked) {
        workouts = arrayList;
        this.context = context;
        this.itemClicked = itemClicked;
    }
    @NonNull
    @Override
    public WorkoutHandler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.workout_holder,parent,false);
        this.parent = parent;

        return new WorkoutHandler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutHandler holder, int position) {
        holder.a1.setText(workouts.get(position).getA1());
        holder.a2.setText(workouts.get(position).getA2());
        holder.b1.setText(workouts.get(position).getB1());
        holder.b2.setText(workouts.get(position).getB2());
        holder.c1.setText(workouts.get(position).getC1());
        holder.c2.setText(workouts.get(position).getC2());
        holder.multiNotes.setText(workouts.get(position).getMultiNotes());

    }

    public void updateWorkouts(ArrayList<Workout> newWorkouts) {
        workouts.clear();
        this.workouts.addAll(newWorkouts);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return workouts.size();
    }

    class WorkoutHandler extends RecyclerView.ViewHolder {

        TextView a1;
        TextView a2;
        TextView b1;
        TextView b2;
        TextView c1;
        TextView c2;
        TextView multiNotes;
        ImageButton editBtn;


        public WorkoutHandler(@NonNull View itemView) {
            super(itemView);

            a1 = itemView.findViewById(R.id.edtTxt_a1);
            a2 = itemView.findViewById(R.id.edtTxt_a2);
            b1 = itemView.findViewById(R.id.edtTxt_b1);
            b2 = itemView.findViewById(R.id.edtTxt_b2);
            c1 = itemView.findViewById(R.id.edtTxt_c1);
            c2 = itemView.findViewById(R.id.edtTxt_c2);
            multiNotes = itemView.findViewById(R.id.edtTxt_multiNotes);

            editBtn = itemView.findViewById(R.id.btnEdit);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (a1.getMaxWidth() == 1) {
                        a1.setMaxLines(Integer.MAX_VALUE);
                    }
                    if (a2.getMaxWidth() == 1) {
                        a2.setMaxLines(Integer.MAX_VALUE);
                    }
                    if (b1.getMaxWidth() == 1) {
                        b1.setMaxLines(Integer.MAX_VALUE);
                    }
                    if (b2.getMaxWidth() == 1) {
                        b2.setMaxLines(Integer.MAX_VALUE);
                    }
                    if (c1.getMaxWidth() == 1) {
                        c1.setMaxLines(Integer.MAX_VALUE);
                    }
                    if (c2.getMaxWidth() == 1) {
                        c2.setMaxLines(Integer.MAX_VALUE);
                    }
                    if (multiNotes.getMaxWidth() == 1) {
                        multiNotes.setMaxLines(Integer.MAX_VALUE);
                    }else {
                        a1.setMaxLines(1);
                        a2.setMaxLines(1);
                        b1.setMaxLines(1);
                        b2.setMaxLines(1);
                        c1.setMaxLines(1);
                        c2.setMaxLines(1);
                        multiNotes.setMaxLines(1);
                    }
                    TransitionManager.beginDelayedTransition(parent);
                }
            });

            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClicked.onClick(getAdapterPosition(),itemView);
                }
            });
        }
    }

    interface ItemClicked {
        void onClick(int position, View view);
    }
}
