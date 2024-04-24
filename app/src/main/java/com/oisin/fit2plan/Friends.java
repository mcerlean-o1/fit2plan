package com.oisin.fit2plan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Friends extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<User> users;
    private ProgressBar progressUsers;
    private SwipeRefreshLayout swipeRefreshLayout;
    private UserAdapter userAdapter;
    UserAdapter.OnUserClickListener onUserClickListener;
    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        progressUsers = findViewById(R.id.friendsProgressBar);
        users = new ArrayList<>();
        recyclerView = findViewById(R.id.friendsRecycler);
        swipeRefreshLayout = findViewById(R.id.swipeLayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUsers();
                swipeRefreshLayout.setRefreshing(false);

            }
        });

        onUserClickListener = new UserAdapter.OnUserClickListener() {
            @Override
            public void onUserClicked(int position) {
                startActivity(new Intent(Friends.this, ChatRoom.class)
                        .putExtra("username_of_friend",users.get(position).getUsername())
                        .putExtra("email_of_friend", users.get(position).getEmail())
                        .putExtra("image_of_friend", users.get(position).getProfilePicture())
                        .putExtra("my_img", imageUrl)
                );
            }
        };

        getUsers();
    }


    public void getUsers() {
        users.clear();
        FirebaseDatabase.getInstance().getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    users.add(dataSnapshot.getValue(User.class));
                }
                userAdapter = new UserAdapter(users,Friends.this,onUserClickListener);
                recyclerView.setLayoutManager(new LinearLayoutManager(Friends.this));
                recyclerView.setAdapter(userAdapter);

                progressUsers.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                for (User user : users) {
                    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                    if (user.getEmail().equals(currentUser)) {
                        imageUrl = user.getProfilePicture();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}