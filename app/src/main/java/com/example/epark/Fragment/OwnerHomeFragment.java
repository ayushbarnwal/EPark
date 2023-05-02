package com.example.epark.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.epark.Adapter.ParkingSlotDetailAdapter;
import com.example.epark.Model.ParkSlot;
import com.example.epark.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OwnerHomeFragment extends Fragment {

    RecyclerView recyclerView;
    ParkingSlotDetailAdapter adapter;
    ArrayList<ParkSlot> list = new ArrayList<>();
    FirebaseDatabase database;
    FirebaseAuth auth;

    public OwnerHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_owner_home, container, false);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        fetchParkPlace();

        recyclerView = view.findViewById(R.id.recycler_view_1);
        adapter = new ParkingSlotDetailAdapter(getActivity(), list, "OwnerHomeFragment");
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        return view;
    }

    private void fetchParkPlace() {

        database.getReference().child("Parking Slots Details").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    ParkSlot parkSlot = dataSnapshot.getValue(ParkSlot.class);
                    parkSlot.setAdapterId(dataSnapshot.getKey());
                    parkSlot.setOwnerId(auth.getCurrentUser().getUid());
                    list.add(parkSlot);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}