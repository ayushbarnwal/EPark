package com.example.epark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.epark.Adapter.ViewPagerUserBookingHistoryAdapter;
import com.google.android.material.tabs.TabLayout;

public class UserBookedHistoryActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_booked_history);

        toolbar = findViewById(R.id.tool_bar_3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPager viewPager = findViewById(R.id.pager1);
        ViewPagerUserBookingHistoryAdapter adapter = new ViewPagerUserBookingHistoryAdapter(getSupportFragmentManager(), "UserBookedHistoryActivity");
        viewPager.setAdapter(adapter);

        TabLayout t = findViewById(R.id.tab_layout1);
        t.setupWithViewPager(viewPager);
    }
}