package com.oisin.fit2plan;

import android.content.Context;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {

    ArrayList<Note> notes;
    Context context;
    ItemClicked itemClicked;
    ViewGroup parent;
    public NoteAdapter(ArrayList<Note> arrayList, Context context, ItemClicked itemClicked) {
        notes = arrayList;
        this.context = context;
        this.itemClicked = itemClicked;
    }
    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_holder,parent,false);
        this.parent = parent;

        return new NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        holder.title.setText(notes.get(position).getTitle());
        holder.breakfast.setText(notes.get(position).getBreakfast());
        holder.snack1.setText(notes.get(position).getSnack1());
        holder.lunch.setText(notes.get(position).getLunch());
        holder.snack2.setText(notes.get(position).getSnack2());
        holder.dinner.setText(notes.get(position).getDinner());

    }


    @Override
    public int getItemCount() {
        return notes.size();
    }

    class NoteHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView breakfast;
        TextView snack1;
        TextView lunch;
        TextView snack2;
        TextView dinner;
        Button edit_button;


        public NoteHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.txt_note_day);
            breakfast = itemView.findViewById(R.id.breakfast);
            snack1 = itemView.findViewById(R.id.snack1);
            lunch = itemView.findViewById(R.id.lunch);
            snack2 = itemView.findViewById(R.id.snack2);
            dinner = itemView.findViewById(R.id.dinner);
            edit_button = itemView.findViewById(R.id.edit_button);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (breakfast.getMaxWidth() == 1) {
                        breakfast.setMaxLines(Integer.MAX_VALUE);
                    }
                    if (snack1.getMaxWidth() == 1) {
                        snack1.setMaxLines(Integer.MAX_VALUE);
                    }
                    if (lunch.getMaxWidth() == 1) {
                        lunch.setMaxLines(Integer.MAX_VALUE);
                    }
                    if (snack2.getMaxWidth() == 1) {
                        snack2.setMaxLines(Integer.MAX_VALUE);
                    }
                    if (dinner.getMaxWidth() == 1) {
                        dinner.setMaxLines(Integer.MAX_VALUE);
                    } else {
                        breakfast.setMaxLines(1);
                        snack1.setMaxLines(1);
                        lunch.setMaxLines(1);
                        snack2.setMaxLines(1);
                        dinner.setMaxLines(1);
                    }
                    TransitionManager.beginDelayedTransition(parent);
                }
            });

            edit_button.setOnClickListener(new View.OnClickListener() {
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
