package com.oisin.fit2plan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {

    private ArrayList<Message> messages;
    private String imgSender, imgReciever;
    private Context context;

    public MessageAdapter(ArrayList<Message> messages, String imgSender, String imgReciever, Context context) {
        this.messages = messages;
        this.imgSender = imgSender;
        this.imgReciever = imgReciever;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_holder, parent, false);
        return new MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {

        holder.txtMessage.setText(messages.get(position).getContent());

        ConstraintLayout constraintLayout = holder.ccLayout;
        if (messages.get(position).getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
            Glide.with(context).load(imgSender).error(R.drawable.profile_icon).placeholder(R.drawable.profile_icon)
                    .into(holder.profileImage);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.clear(R.id.profileCardView, ConstraintSet.LEFT);
            constraintSet.clear(R.id.txtMessageContent, ConstraintSet.LEFT);
            constraintSet.connect(R.id.profileCardView, ConstraintSet.RIGHT, R.id.messageCCLayout,ConstraintSet.RIGHT,0);
            constraintSet.connect(R.id.txtMessageContent, ConstraintSet.RIGHT, R.id.profileCardView,ConstraintSet.LEFT,0);
            constraintSet.applyTo(constraintLayout);
        } else {
            Glide.with(context).load(imgReciever).error(R.drawable.profile_icon).placeholder(R.drawable.profile_icon)
                    .into(holder.profileImage);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.clear(R.id.profileCardView, ConstraintSet.RIGHT);
            constraintSet.clear(R.id.txtMessageContent, ConstraintSet.RIGHT);
            constraintSet.connect(R.id.profileCardView, ConstraintSet.LEFT, R.id.messageCCLayout,ConstraintSet.LEFT,0);
            constraintSet.connect(R.id.txtMessageContent, ConstraintSet.LEFT, R.id.profileCardView,ConstraintSet.RIGHT,0);
            constraintSet.applyTo(constraintLayout);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class MessageHolder extends RecyclerView.ViewHolder {

        ConstraintLayout ccLayout;
        TextView txtMessage;
        ImageView profileImage;
        public MessageHolder(@NonNull View itemView) {
            super(itemView);

            ccLayout = itemView.findViewById(R.id.messageCCLayout);
            txtMessage = itemView.findViewById(R.id.txtMessageContent);
            profileImage = itemView.findViewById(R.id.profile_img_message);
        }
    }

}
