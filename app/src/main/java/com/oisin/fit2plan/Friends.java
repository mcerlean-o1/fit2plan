package com.oisin.fit2plan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
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

    ImageView profilePicture;
    TextView usernameProfile;
    GestureDetector gesture;
    DrawerLayout drawLayout;
    NavigationView navigationView;
    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        progressUsers = findViewById(R.id.friendsProgressBar);
        users = new ArrayList<>();
        recyclerView = findViewById(R.id.friendsRecycler);
        swipeRefreshLayout = findViewById(R.id.swipeLayout);

        drawLayout = findViewById(R.id.friendNavLayout);

        navigationView = findViewById(R.id.navigationFriendView);


        View headerView = navigationView.getHeaderView(0);
        profilePicture = headerView.findViewById(R.id.profilePic);
        usernameProfile = headerView.findViewById(R.id.proUsername);


        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            handleNavigationItemClick(id);
            return true;
        });


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid());

        databaseReference.child("profilePicture").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String profilePictureUrl = snapshot.getValue(String.class);
                if (profilePictureUrl != null) {
                    Glide.with(Friends.this).load(profilePictureUrl).into(profilePicture);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.w("Activity Error", "loadPost:onCancelled", error.toException() );
            }
        });

        databaseReference.child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String usernameData = snapshot.getValue(String.class);
                if (usernameData != null) {
                    usernameProfile.setText(usernameData);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.w("Activity Error", "loadPost:onCancelled", error.toException() );
            }
        });

        setUpGestureHandler();

        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Friends.this, Profile.class));

                drawLayout.closeDrawer(GravityCompat.START);
            }
        });
        drawLayout.closeDrawer(GravityCompat.START);

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

    private void handleNavigationItemClick(int id) {
        Intent intent = null;
        if (id == R.id.navHome) {
            intent = new Intent(Friends.this, MainActivity.class);
        } else if (id == R.id.foodDiaryPage) {
            intent = new Intent(Friends.this, FoodDiary.class);
        } else if (id == R.id.workoutDiaryPage) {
            intent = new Intent(Friends.this, WorkoutDiary.class);
        } else if (id == R.id.chatRoomPage) {
            intent = new Intent(Friends.this, Friends.class);
        } else if (id == R.id.nutritionCalculatorPage) {
            intent = new Intent(Friends.this, NutritionCalculator.class);
        }

        if (intent != null) {
            startActivity(intent);
            drawLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void setUpGestureHandler() {

        gesture = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(@NonNull MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(@NonNull MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(@NonNull MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(@NonNull MotionEvent e) {

            }

            @Override
            public boolean onFling(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
                if (e2.getX() - e1.getX() > 100) { //swipe left to right
                    drawLayout.openDrawer(GravityCompat.START);
                    return true;
                } else if (e1.getX() - e2.getX() > 100) {
                    if (drawLayout.isDrawerOpen(GravityCompat.START)) {
                        drawLayout.closeDrawer(GravityCompat.START);
                    }
                }
                return false;
            }
        });
    }
}