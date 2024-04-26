package com.oisin.fit2plan;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatRoom extends AppCompatActivity {

    private RecyclerView recyclerView;

    private EditText edtMessage;
    GestureDetector gesture;
    private TextView messageUsername;
    private String friendUsername, friendEmail, chatRoomId;
    private ProgressBar progressBar;
    private ArrayList<Message> messages;
    private MessageAdapter messageAdapter;
    private ImageView profilePicture, imgSend;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room);

        friendUsername = getIntent().getStringExtra("username_of_friend");
        friendEmail = getIntent().getStringExtra("email_of_friend");

        recyclerView = findViewById(R.id.recyclerMessages);
        edtMessage = findViewById(R.id.edtMessageTxt);
        messageUsername = findViewById(R.id.txtUser);
        progressBar = findViewById(R.id.progressMessages);
        profilePicture = findViewById(R.id.profilePicMessage);
        imgSend = findViewById(R.id.messageSend);

        messageUsername.setText(friendUsername);

        messages = new ArrayList<>();

        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("messages/"+chatRoomId).push()
                        .setValue(new Message(FirebaseAuth.getInstance().getCurrentUser()
                                .getEmail(),friendEmail,edtMessage.getText().toString()));
                edtMessage.setText("");
            }
        });
        messageAdapter = new MessageAdapter(messages,getIntent()
                .getStringExtra("my_img"), getIntent().getStringExtra("image_of_friend"),ChatRoom.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        Glide.with(ChatRoom.this).load(getIntent().getStringExtra("image_of_friend"))
                .placeholder(R.drawable.profile_icon).error(R.drawable.profile_icon).into(profilePicture);


        setUpChat();


    }

    private void setUpChat() {
        FirebaseDatabase.getInstance().getReference("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username = snapshot.getValue(User.class).getUsername();
                if (friendUsername.compareTo(username) > 0) {
                    chatRoomId = username + friendUsername;
                } else if (friendUsername.compareTo(username) == 0) {
                    chatRoomId = username + friendUsername;
                } else {
                    chatRoomId = friendUsername + username;
                }
                attachMessageListener(chatRoomId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void attachMessageListener(String chatRoomId) {
        FirebaseDatabase.getInstance().getReference("messages/"+chatRoomId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    messages.add(dataSnapshot.getValue(Message.class));
                }
                messageAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messages.size()-1);
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
