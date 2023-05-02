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

import com.example.epark.Adapter.BookHistoryAdapter;
import com.example.epark.Model.ParkSlot;
import com.example.epark.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserUnverifiedBookingSlotFragment extends Fragment {
    FirebaseAuth auth;
    FirebaseDatabase database;
    RecyclerView recyclerView;
    BookHistoryAdapter bookHistoryAdapter;
    ArrayList<ParkSlot> list = new ArrayList<>();

    public UserUnverifiedBookingSlotFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_unverified_booking_slot, container, false);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        recyclerView = view.findViewById(R.id.recycler_view_3);
        bookHistoryAdapter = new BookHistoryAdapter(getContext(), list, "UserUnVerifiedBookingSlotFragment");
        recyclerView.setAdapter(bookHistoryAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        fetchBookingList(view);

        return view;
    }

    private void fetchBookingList(View view) {
        database.getReference().child("Users").child(auth.getCurrentUser().getUid())
                .child("Booked History").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                            ParkSlot parkSlot = dataSnapshot.getValue(ParkSlot.class);
                            Log.e("TAG", parkSlot.getAddress() + " " + parkSlot.getBooking_date());
                            if(parkSlot.getIsVerified().equals("No")) list.add(parkSlot);
                            bookHistoryAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}