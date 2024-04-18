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

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodHandler> {

    ArrayList<Food> foods;
    Context context;
    ItemClicked itemClicked;
    ViewGroup parent;
    public FoodAdapter(ArrayList<Food> arrayList, Context context, ItemClicked itemClicked) {
        foods = arrayList;
        this.context = context;
        this.itemClicked = itemClicked;
    }
    @NonNull
    @Override
    public FoodHandler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.food_holder,parent,false);
        this.parent = parent;

        return new FoodHandler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodHandler holder, int position) {
        //holder.date.setText(foods.get(position).getDate());
        holder.mealType.setText(foods.get(position).getMealType());
        holder.mealDescription.setText(foods.get(position).getDescription());

    }

    public void updateFoods(ArrayList<Food> newFoods) {
        foods.clear();
        this.foods.addAll(newFoods);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    class FoodHandler extends RecyclerView.ViewHolder {

        TextView date;
        TextView mealType;
        TextView mealDescription;

        ImageButton editButton;


        public FoodHandler(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.txt_date);
            mealType = itemView.findViewById(R.id.title_meal);
            mealDescription = itemView.findViewById(R.id.meal_description);
            editButton = itemView.findViewById(R.id.edit_button);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mealDescription.getMaxWidth() == 1) {
                        mealDescription.setMaxLines(Integer.MAX_VALUE);
                    } else {
                        mealDescription.setMaxLines(1);
                    }
                    TransitionManager.beginDelayedTransition(parent);
                }
            });

            editButton.setOnClickListener(new View.OnClickListener() {
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
