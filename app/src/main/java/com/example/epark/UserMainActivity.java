package com.example.epark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.epark.Fragment.OwnerHomeFragment;
import com.example.epark.Fragment.OwnerProfileFragment;
import com.example.epark.Fragment.UserHomeFragment;
import com.example.epark.Fragment.UserProfileFragment;
import com.example.epark.Model.Person;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class UserMainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton floatingActionButton;
    Toolbar toolbar;
    ImageView headerProfileImage;
    TextView user_name;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        String id = auth.getCurrentUser().getUid();
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/" + id);

        floatingActionButton = findViewById(R.id.owner_add_park);
        floatingActionButton.setVisibility(View.INVISIBLE);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        toolbar = findViewById(R.id.tool_bar);
        toolbar.setTitle("Home");
        toolbar.setTitleTextColor(Color.WHITE);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        setSupportActionBar(toolbar);

        View header = navigationView.getHeaderView(0);
        user_name = (TextView) header.findViewById(R.id.navigation_user_name);
        headerProfileImage = (ImageView) header.findViewById(R.id.profile_picture_navigation);

        fetchHeaderData();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.OpenDrawer,R.string.CloseDrawer);
        toggle.getDrawerArrowDrawable().setColor(getColor(R.color.white));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (getFragmentManager().findFragmentById(androidx.appcompat.R.id.action_bar_container) == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment,new UserHomeFragment()).commit();
        }

        bottomNavigationView.setOnItemSelectedListener(this);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.navigation_logOut){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(UserMainActivity.this);
                    builder1.setMessage("Are you Sure you want to Log Out");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    auth.signOut();

                                    SharedPreferences pref = getSharedPreferences("LogIn", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("LogInMode", "2");
                                    editor.apply();

                                    Intent i = new Intent(UserMainActivity.this,LogInActivity.class);
                                    startActivity(i);
                                    finishAffinity();
                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
                if(id == R.id.navigation_profile){
                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, new OwnerProfileFragment()).commit();
                    toolbar.setTitle("Profile");
                    toolbar.setVisibility(View.VISIBLE);
                    toolbar.setTitleTextColor(Color.WHITE);
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    private void fetchHeaderData() {

        database.getReference().child("Users").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Person person = snapshot.getValue(Person.class);
                user_name.setText(person.getName());
                //Glide.with(UserMainActivity.this).load(person.getProfile_image()).placeholder(R.drawable.user).into(headerProfileImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.owner_home :
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, new UserHomeFragment()).commit();
                toolbar.setTitle("Home");
                toolbar.setVisibility(View.VISIBLE);
                toolbar.setTitleTextColor(Color.WHITE);
                return true;
            case R.id.owner_history:
                Intent i = new Intent(UserMainActivity.this, UserBookedHistoryActivity.class);
                startActivity(i);
                return true;
            case R.id.owner_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, new OwnerProfileFragment()).commit();
                toolbar.setTitle("Profile");
                toolbar.setVisibility(View.VISIBLE);
                toolbar.setTitleTextColor(Color.WHITE);
                return true;
        }

        return true;
    }
}