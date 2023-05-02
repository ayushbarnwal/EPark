package com.example.epark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

import com.example.epark.Adapter.CustomerListAdapter;
import com.example.epark.Model.ParkSlot;
import com.example.epark.Model.Person;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class OwnerCustomerListActivity extends AppCompatActivity {

    String adapterId;
    FirebaseAuth auth;
    FirebaseDatabase database;
    SearchView searchView;
    RecyclerView recyclerView;
    Toolbar toolbar;
    ArrayList<ParkSlot> list = new ArrayList<>();
    CustomerListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_customer_list);

        adapterId = getIntent().getStringExtra("adapterId");

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        toolbar = findViewById(R.id.tool_bar_4);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recycler_view_5);
        adapter = new CustomerListAdapter(this, list, "OwnerCustomerActivity");
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        fetchCustomerList();

    }

    private void fetchCustomerList() {

        database.getReference().child("Parking Slots Details").child(auth.getCurrentUser().getUid())
                .child(adapterId).child("Customer List").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                            String root = dataSnapshot.getKey();
                            Set<String> keySet = map.keySet();
                            for(String key: keySet){
                                database.getReference().child("Parking Slots Details").child(auth.getCurrentUser().getUid())
                                        .child(adapterId).child("Customer List").child(root).child(key).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                ParkSlot parkSlot = snapshot.getValue(ParkSlot.class);
                                                database.getReference().child("Users").child(root).addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                        Person person = snapshot.getValue(Person.class);
                                                        parkSlot.setCustomerId(root);
                                                        parkSlot.setCustomer_adapter(key);
                                                        parkSlot.setCustomer_name(person.getName());
                                                        parkSlot.setCustomer_phone(person.getPhone_no());
                                                        parkSlot.setOwnerId(auth.getCurrentUser().getUid());
                                                        parkSlot.setAdapterId(adapterId);
                                                        list.add(parkSlot);
                                                        adapter.notifyDataSetChanged();
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

}