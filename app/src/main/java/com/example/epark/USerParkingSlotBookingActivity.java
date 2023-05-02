package com.example.epark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.epark.Calender.DatePickerFragment;
import com.example.epark.Model.ParkSlot;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class USerParkingSlotBookingActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    TextView select_date, one, two, three, four, five, six, seven, eight, nine, ten, eleven, twelve, thirteen, fourteen, fifteen, sixteen, seventeen, eighteen, nineteen, twenty, twentyOne, twentyTwo, twentyThree, twentyFour;
    LinearLayout one_l, two_l, three_l, four_l, five_l, six_l, seven_l, eight_l, nine_l, ten_l, eleven_l, twelve_l, thirteen_l, fourteen_l, fifteen_l, sixteen_l, seventeen_l, eighteen_l, nineteen_l, twenty_l, twentyOne_l, twentyTwo_l, twentyThree_l, twentyFour_l;
    LinearLayout layer_1, layer_2, layer_3, layer_4, layer_5, layer_6;
    Button continue_btn;
    ArrayList<String> timeList = new ArrayList<String>();
    ArrayList<String> selectedTimeSlot = new ArrayList<String>();
    String dateSelected;
    FirebaseAuth auth;
    FirebaseDatabase database;
    String ownerId, adapterId, latitude, longitude, address, vehicle_selected, total_2_wheeler_slot, total_4_wheeler_slot;
    TextView two_Wheeler_rate, four_wheeler_rate, total_amount;
    String t_wheeler_rate, f_wheeler_rate, vehicleNo, Owner_upi_id, utr_no, bookSlot;
    SupportMapFragment smp;
    private GoogleMap mMap;
    ImageView car, bike;
    MaterialCardView car_card_view, bike_card_view;
    int total;
    LinearLayout payment_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_parking_slot_booking);

        initialize();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        ownerId = getIntent().getStringExtra("OwnerId");
        adapterId = getIntent().getStringExtra("adapterId");
        latitude = getIntent().getStringExtra("latitude").toString();
        longitude = getIntent().getStringExtra("longitude").toString();
        address = getIntent().getStringExtra("selectedAddress").toString();
        two_Wheeler_rate.setText("2-Wheeler Rate : Rs. " + getIntent().getStringExtra("2_wheeler_slot_rate").toString());
        t_wheeler_rate = getIntent().getStringExtra("2_wheeler_slot_rate").toString();
        four_wheeler_rate.setText("4-Wheeler Rate : Rs. " +getIntent().getStringExtra("4_wheeler_slot_rate").toString());
        f_wheeler_rate = getIntent().getStringExtra("4_wheeler_slot_rate").toString();
        total_2_wheeler_slot = getIntent().getStringExtra("total_2_wheeler_slots").toString();
        total_4_wheeler_slot = getIntent().getStringExtra("total_4_wheeler_slots").toString();
        Owner_upi_id = getIntent().getStringExtra("upiId");

        smp = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map1);

        smp.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                mMap = googleMap;

                LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(address);


                googleMap.addMarker(markerOptions);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));

            }
        });

        timeSlotSelection();

        select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date Picker");
            }
        });

        car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(USerParkingSlotBookingActivity.this);
                dialog.setContentView(R.layout.vehicle_no_dialog);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false);

                TextView confirm = dialog.findViewById(R.id.confirm);
                TextView cancel = dialog.findViewById(R.id.cancel);
                EditText vehicle_no = dialog.findViewById(R.id.vehicle_number);

                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vehicleNo = vehicle_no.getText().toString();
                        dialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

                vehicle_selected = "Car";
                bike_card_view.setCardBackgroundColor(Color.WHITE);
                car_card_view.setCardBackgroundColor(Color.parseColor("#bdc3c7"));
            }
        });
        bike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new Dialog(USerParkingSlotBookingActivity.this);
                dialog.setContentView(R.layout.vehicle_no_dialog);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false);

                TextView confirm = dialog.findViewById(R.id.confirm);
                TextView cancel = dialog.findViewById(R.id.cancel);
                EditText vehicle_no = dialog.findViewById(R.id.vehicle_number);

                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vehicleNo = vehicle_no.getText().toString();
                        dialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                vehicle_selected = "Bike";
                car_card_view.setCardBackgroundColor(Color.WHITE);
                bike_card_view.setCardBackgroundColor(Color.parseColor("#bdc3c7"));
            }
        });

        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(String i: selectedTimeSlot)bookSlot += i + "$";
//                for(String it: selectedTimeSlot){
//                    ParkSlot parkSlot = new ParkSlot(auth.getCurrentUser().getUid());
//                    database.getReference().child("Parking Slots Details").child(ownerId).child(adapterId)
//                            .child("Booking User List").child(dateSelected).child(vehicle_selected).child(it).push().setValue(parkSlot);
//
//                }

                Dialog dialog = new Dialog(USerParkingSlotBookingActivity.this);
                dialog.setContentView(R.layout.utr_no_dialog);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false);

                TextView confirm = dialog.findViewById(R.id.confirm_payment_dialog);
                TextView cancel = dialog.findViewById(R.id.cancel_payment_dialog);
                EditText e_utr_no = dialog.findViewById(R.id.utr_number_dialog);
                TextView t_upi_id = dialog.findViewById(R.id.upi_id_dialog);
                t_upi_id.setText(Owner_upi_id);
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        utr_no = e_utr_no.getText().toString();
                        String customer_id = auth.getCurrentUser().getUid();
                        ParkSlot parkSlot = new ParkSlot(utr_no, "No", bookSlot, vehicle_selected, dateSelected, ownerId, adapterId, address);
                        if(!TextUtils.isEmpty(utr_no)){
                            database.getReference().child("Users").child(auth.getCurrentUser().getUid()).child("Booked History")
                                    .push().setValue(parkSlot).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            ParkSlot parkSlot1 = new ParkSlot(utr_no, "No", bookSlot, vehicle_selected, dateSelected);
                                            database.getReference().child("Parking Slots Details").child(ownerId).child(adapterId)
                                                    .child("Customer List").child(customer_id).push().setValue(parkSlot1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Intent i = new Intent(USerParkingSlotBookingActivity.this, UserMainActivity.class);
                                                            startActivity(i);
                                                        }
                                                    });
                                        }
                                    });
                        }else{
                            Toast.makeText(USerParkingSlotBookingActivity.this, "Please Enter the UTR Number", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }

        });

    }

    private void fetchSlotByDate() {

        for(String i: timeList){
            Log.e("TAG" , " h " + i);
        }

        if(timeList.contains("1")){
            one.setTextColor(Color.parseColor("#6f0000"));
            one.setBackgroundColor(Color.parseColor("#F3B7B7"));
            one.setBackgroundResource(R.drawable.text_view_4_bg);
        }
        else{
            one.setTextColor(Color.parseColor("#218124"));
            one.setBackgroundColor(Color.parseColor("#ACEFC7"));
            one.setBackgroundResource(R.drawable.text_view_3_bg);
        }
        if(timeList.contains("2")){
            two.setTextColor(Color.parseColor("#6f0000"));
            two.setBackgroundColor(Color.parseColor("#F3B7B7"));
            two.setBackgroundResource(R.drawable.text_view_4_bg);
        }
        else{
            two.setTextColor(Color.parseColor("#218124"));
            two.setBackgroundColor(Color.parseColor("#ACEFC7"));
            two.setBackgroundResource(R.drawable.text_view_3_bg);
        }

        if(timeList.contains("3")){
            three.setTextColor(Color.parseColor("#6f0000"));
            three.setBackgroundColor(Color.parseColor("#F3B7B7"));
            three.setBackgroundResource(R.drawable.text_view_4_bg);
        }
        else{
            three.setTextColor(Color.parseColor("#218124"));
            three.setBackgroundColor(Color.parseColor("#ACEFC7"));
            three.setBackgroundResource(R.drawable.text_view_3_bg);
        }

        if(timeList.contains("4")){
            four.setTextColor(Color.parseColor("#6f0000"));
            four.setBackgroundColor(Color.parseColor("#F3B7B7"));
            four.setBackgroundResource(R.drawable.text_view_4_bg);
        }
        else{
            four.setTextColor(Color.parseColor("#218124"));
            four.setBackgroundColor(Color.parseColor("#ACEFC7"));
            four.setBackgroundResource(R.drawable.text_view_3_bg);
        }
        if(timeList.contains("5")){
            five.setTextColor(Color.parseColor("#6f0000"));
            five.setBackgroundColor(Color.parseColor("#F3B7B7"));
            five.setBackgroundResource(R.drawable.text_view_4_bg);
        }
        else{
            five.setTextColor(Color.parseColor("#218124"));
            five.setBackgroundColor(Color.parseColor("#ACEFC7"));
            five.setBackgroundResource(R.drawable.text_view_3_bg);
        }
        if(timeList.contains("6")){
            six.setTextColor(Color.parseColor("#6f0000"));
            six.setBackgroundColor(Color.parseColor("#F3B7B7"));
            six.setBackgroundResource(R.drawable.text_view_4_bg);
        }
        else{
            six.setTextColor(Color.parseColor("#218124"));
            six.setBackgroundColor(Color.parseColor("#ACEFC7"));
            six.setBackgroundResource(R.drawable.text_view_3_bg);
        }
        if(timeList.contains("7")){
            seven.setTextColor(Color.parseColor("#6f0000"));
            seven.setBackgroundColor(Color.parseColor("#F3B7B7"));
            seven.setBackgroundResource(R.drawable.text_view_4_bg);
        }
        else{
            seven.setTextColor(Color.parseColor("#218124"));
            seven.setBackgroundColor(Color.parseColor("#ACEFC7"));
            seven.setBackgroundResource(R.drawable.text_view_3_bg);
        }
        if(timeList.contains("8")){
            eight.setTextColor(Color.parseColor("#6f0000"));
            eight.setBackgroundColor(Color.parseColor("#F3B7B7"));
            eight.setBackgroundResource(R.drawable.text_view_4_bg);
        }
        else{
            eight.setTextColor(Color.parseColor("#218124"));
            eight.setBackgroundColor(Color.parseColor("#ACEFC7"));
            eight.setBackgroundResource(R.drawable.text_view_3_bg);
        }
        if(timeList.contains("9")){
            nine.setTextColor(Color.parseColor("#6f0000"));
            nine.setBackgroundColor(Color.parseColor("#F3B7B7"));
            nine.setBackgroundResource(R.drawable.text_view_4_bg);
        }
        else{
            nine.setTextColor(Color.parseColor("#218124"));
            nine.setBackgroundColor(Color.parseColor("#ACEFC7"));
            nine.setBackgroundResource(R.drawable.text_view_3_bg);
        }
        if(timeList.contains("10")){
            ten.setTextColor(Color.parseColor("#6f0000"));
            ten.setBackgroundColor(Color.parseColor("#F3B7B7"));
            ten.setBackgroundResource(R.drawable.text_view_4_bg);
        }
        else{
            ten.setTextColor(Color.parseColor("#218124"));
            ten.setBackgroundColor(Color.parseColor("#ACEFC7"));
            ten.setBackgroundResource(R.drawable.text_view_3_bg);
        }
        if(timeList.contains("11")){
            eleven.setTextColor(Color.parseColor("#6f0000"));
            eleven.setBackgroundColor(Color.parseColor("#F3B7B7"));
            eleven.setBackgroundResource(R.drawable.text_view_4_bg);
        }
        else{
            eleven.setTextColor(Color.parseColor("#218124"));
            eleven.setBackgroundColor(Color.parseColor("#ACEFC7"));
            eleven.setBackgroundResource(R.drawable.text_view_3_bg);
        }
        if(timeList.contains("12")){
            twelve.setTextColor(Color.parseColor("#6f0000"));
            twelve.setBackgroundColor(Color.parseColor("#F3B7B7"));
            twelve.setBackgroundResource(R.drawable.text_view_4_bg);
        }
        else{
            twelve.setTextColor(Color.parseColor("#218124"));
            twelve.setBackgroundColor(Color.parseColor("#ACEFC7"));
            twelve.setBackgroundResource(R.drawable.text_view_3_bg);
        }
        if(timeList.contains("13")){
            thirteen.setTextColor(Color.parseColor("#6f0000"));
            thirteen.setBackgroundColor(Color.parseColor("#F3B7B7"));
            thirteen.setBackgroundResource(R.drawable.text_view_4_bg);
        }
        else{
            thirteen.setTextColor(Color.parseColor("#218124"));
            thirteen.setBackgroundColor(Color.parseColor("#ACEFC7"));
            thirteen.setBackgroundResource(R.drawable.text_view_3_bg);
        }
        if(timeList.contains("14")){
            fourteen.setTextColor(Color.parseColor("#6f0000"));
            fourteen.setBackgroundColor(Color.parseColor("#F3B7B7"));
            fourteen.setBackgroundResource(R.drawable.text_view_4_bg);
        }
        else{
            fourteen.setTextColor(Color.parseColor("#218124"));
            fourteen.setBackgroundColor(Color.parseColor("#ACEFC7"));
            fourteen.setBackgroundResource(R.drawable.text_view_3_bg);
        }
        if(timeList.contains("15")){
            fifteen.setTextColor(Color.parseColor("#6f0000"));
            fifteen.setBackgroundColor(Color.parseColor("#F3B7B7"));
            fifteen.setBackgroundResource(R.drawable.text_view_4_bg);
        }
        else{
            fifteen.setTextColor(Color.parseColor("#218124"));
            fifteen.setBackgroundColor(Color.parseColor("#ACEFC7"));
            fifteen.setBackgroundResource(R.drawable.text_view_3_bg);
        }
        if(timeList.contains("16")){
            sixteen.setTextColor(Color.parseColor("#6f0000"));
            sixteen.setBackgroundColor(Color.parseColor("#F3B7B7"));
            sixteen.setBackgroundResource(R.drawable.text_view_4_bg);
        }
        else{
            sixteen.setTextColor(Color.parseColor("#218124"));
            sixteen.setBackgroundColor(Color.parseColor("#ACEFC7"));
            sixteen.setBackgroundResource(R.drawable.text_view_3_bg);
        }
        if(timeList.contains("17")){
            seventeen.setTextColor(Color.parseColor("#6f0000"));
            seventeen.setBackgroundColor(Color.parseColor("#F3B7B7"));
            seventeen.setBackgroundResource(R.drawable.text_view_4_bg);
        }
        else{
            seventeen.setTextColor(Color.parseColor("#218124"));
            seventeen.setBackgroundColor(Color.parseColor("#ACEFC7"));
            seventeen.setBackgroundResource(R.drawable.text_view_3_bg);
        }
        if(timeList.contains("18")){
            eighteen.setTextColor(Color.parseColor("#6f0000"));
            eighteen.setBackgroundColor(Color.parseColor("#F3B7B7"));
            eighteen.setBackgroundResource(R.drawable.text_view_4_bg);
        }
        else{
            eighteen.setTextColor(Color.parseColor("#218124"));
            eighteen.setBackgroundColor(Color.parseColor("#ACEFC7"));
            eighteen.setBackgroundResource(R.drawable.text_view_3_bg);
        }
        if(timeList.contains("19")){
            nineteen.setTextColor(Color.parseColor("#6f0000"));
            nineteen.setBackgroundColor(Color.parseColor("#F3B7B7"));
            nineteen.setBackgroundResource(R.drawable.text_view_4_bg);
        }
        else{
            nineteen.setTextColor(Color.parseColor("#218124"));
            nineteen.setBackgroundColor(Color.parseColor("#ACEFC7"));
            nineteen.setBackgroundResource(R.drawable.text_view_3_bg);
        }
        if(timeList.contains("20")){
            twenty.setTextColor(Color.parseColor("#6f0000"));
            twenty.setBackgroundColor(Color.parseColor("#F3B7B7"));
            twenty.setBackgroundResource(R.drawable.text_view_4_bg);
        }
        else{
            twenty.setTextColor(Color.parseColor("#218124"));
            twenty.setBackgroundColor(Color.parseColor("#ACEFC7"));
            twenty.setBackgroundResource(R.drawable.text_view_3_bg);
        }
        if(timeList.contains("21")){
            twentyOne.setTextColor(Color.parseColor("#6f0000"));
            twentyOne.setBackgroundColor(Color.parseColor("#F3B7B7"));
            twentyOne.setBackgroundResource(R.drawable.text_view_4_bg);
        }
        else{
            twentyOne.setTextColor(Color.parseColor("#218124"));
            twentyOne.setBackgroundColor(Color.parseColor("#ACEFC7"));
            twentyOne.setBackgroundResource(R.drawable.text_view_3_bg);
        }
        if(timeList.contains("22")){
            twentyTwo.setTextColor(Color.parseColor("#6f0000"));
            twentyTwo.setBackgroundColor(Color.parseColor("#F3B7B7"));
            twentyTwo.setBackgroundResource(R.drawable.text_view_4_bg);
        }
        else{
            twentyTwo.setTextColor(Color.parseColor("#218124"));
            twentyTwo.setBackgroundColor(Color.parseColor("#ACEFC7"));
            twentyTwo.setBackgroundResource(R.drawable.text_view_3_bg);
        }
        if(timeList.contains("23")){
            twentyThree.setTextColor(Color.parseColor("#6f0000"));
            twentyThree.setBackgroundColor(Color.parseColor("#F3B7B7"));
            twentyThree.setBackgroundResource(R.drawable.text_view_4_bg);
        }
        else{
            twentyThree.setTextColor(Color.parseColor("#218124"));
            twentyThree.setBackgroundColor(Color.parseColor("#ACEFC7"));
            twentyThree.setBackgroundResource(R.drawable.text_view_3_bg);
        }
        if(timeList.contains("24")){
            twentyFour.setTextColor(Color.parseColor("#6f0000"));
            twentyFour.setBackgroundColor(Color.parseColor("#F3B7B7"));
            twentyFour.setBackgroundResource(R.drawable.text_view_4_bg);
        }
        else{
            twentyFour.setTextColor(Color.parseColor("#218124"));
            twentyFour.setBackgroundColor(Color.parseColor("#ACEFC7"));
            twentyFour.setBackgroundResource(R.drawable.text_view_3_bg);
        }


    }

    private void fetchSlotDetail() {

        database.getReference().child("Parking Slots Details").child(ownerId).child(adapterId)
                .child("Booking User List").child(dateSelected).child(vehicle_selected).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        timeList.clear();
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                            String slot = dataSnapshot.getKey();
                            int size = (int) dataSnapshot.getChildrenCount();
                            Log.e("TAG", "Size "+ String.valueOf(size));
                            Log.e("TAG", "slot "+ slot);
                            Log.e("TAG", "total "+ total_2_wheeler_slot);
                            if(vehicle_selected.equals("Car")){
                                if(String.valueOf(size).equals(total_4_wheeler_slot))timeList.add(slot);
                            }else{
                                if(String.valueOf(size).equals(total_2_wheeler_slot))timeList.add(slot);
                            }

                            Log.e("TAG", String.valueOf(size));
                        }

                        for(String it: timeList){
                            Log.e("TAG", "q1 " +it);
                        }
                        layer_1.setVisibility(View.VISIBLE);
                        layer_2.setVisibility(View.VISIBLE);
                        layer_3.setVisibility(View.VISIBLE);
                        layer_4.setVisibility(View.VISIBLE);
                        layer_5.setVisibility(View.VISIBLE);
                        layer_6.setVisibility(View.VISIBLE);
                        fetchSlotByDate();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    private void initialize() {
        select_date = findViewById(R.id.select_date_1);
        layer_1 = (LinearLayout) findViewById(R.id.l_1);
        layer_1.setVisibility(View.GONE);
        one = findViewById(R.id.l_1_1_1);
        two = findViewById(R.id.l_1_1_2);
        three = findViewById(R.id.l_1_1_3);
        four = findViewById(R.id.l_1_1_4);

        layer_2 = (LinearLayout) findViewById(R.id.l_2);
        five = findViewById(R.id.l_2_1_1);
        six = findViewById(R.id.l_2_1_2);
        seven = findViewById(R.id.l_2_1_3);
        eight = findViewById(R.id.l_2_1_4);
        layer_2.setVisibility(View.GONE);

        layer_3 = (LinearLayout) findViewById(R.id.l_3);
        nine = findViewById(R.id.l_3_1_1);
        ten = findViewById(R.id.l_3_1_2);
        eleven = findViewById(R.id.l_3_1_3);
        twelve = findViewById(R.id.l_3_1_4);
        layer_3.setVisibility(View.GONE);

        layer_4 = (LinearLayout) findViewById(R.id.l_4);
        thirteen = findViewById(R.id.l_4_1_1);
        fourteen = findViewById(R.id.l_4_1_2);
        fifteen = findViewById(R.id.l_4_1_3);
        sixteen = findViewById(R.id.l_4_1_4);
        layer_4.setVisibility(View.GONE);

        layer_5 = (LinearLayout) findViewById(R.id.l_5);
        seventeen = findViewById(R.id.l_5_1_1);
        eighteen = findViewById(R.id.l_5_1_2);
        nineteen = findViewById(R.id.l_5_1_3);
        twenty = findViewById(R.id.l_5_1_4);
        layer_5.setVisibility(View.GONE);

        layer_6 = (LinearLayout) findViewById(R.id.l_6);
        twentyOne = findViewById(R.id.l_6_1_1);
        twentyTwo = findViewById(R.id.l_6_1_2);
        twentyThree = findViewById(R.id.l_6_1_3);
        twentyFour = findViewById(R.id.l_6_1_4);
        layer_6.setVisibility(View.GONE);

        continue_btn = findViewById(R.id.continue_btn);
        two_Wheeler_rate = findViewById(R.id.t_weeler_rate);
        four_wheeler_rate = findViewById(R.id.f_weeler_rate);
        car = findViewById(R.id.car);
        bike = findViewById(R.id.bike);

        car_card_view = findViewById(R.id.card_view2);
        bike_card_view = findViewById(R.id.card_view3);
        payment_layout = findViewById(R.id.payment_layout);
        payment_layout.setVisibility(View.GONE);
        total_amount = findViewById(R.id.totsl_amount);
    }

    private void timeSlotSelection() {

        one.setBackgroundResource(R.drawable.text_view_3_bg);
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!timeList.contains("1")){
                    if(selectedTimeSlot.contains("1")){
                        one.setTextColor(Color.parseColor("#218124"));
                        one.setBackgroundColor(Color.parseColor("#ACEFC7"));
                        one.setBackgroundResource(R.drawable.text_view_3_bg);
                        selectedTimeSlot.remove("1");
                        if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
                    }else{
                        selectedTimeSlot.add("1");
                        one.setTextColor(Color.parseColor("#373B44"));
                        one.setBackgroundColor(Color.parseColor("#86A8E7"));
                        one.setBackgroundResource(R.drawable.text_view_5_bg);
                    }
                }
                payment_layout.setVisibility(View.VISIBLE);
                if(vehicle_selected.equals("Car")){
                    total = selectedTimeSlot.size() * Integer.parseInt(f_wheeler_rate);
                }else{
                    total = selectedTimeSlot.size() * Integer.parseInt(t_wheeler_rate);
                }
                total_amount.setText("Rs. " + String.valueOf(total));
                if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);

            }
        });

        two.setBackgroundResource(R.drawable.text_view_3_bg);
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!timeList.contains("2")){
                    if(selectedTimeSlot.contains("2")){
                        two.setTextColor(Color.parseColor("#218124"));
                        two.setBackgroundColor(Color.parseColor("#ACEFC7"));
                        two.setBackgroundResource(R.drawable.text_view_3_bg);
                        selectedTimeSlot.remove("2");
                        if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
                    }else{
                        selectedTimeSlot.add("2");
                        two.setTextColor(Color.parseColor("#373B44"));
                        two.setBackgroundColor(Color.parseColor("#86A8E7"));
                        two.setBackgroundResource(R.drawable.text_view_5_bg);
                    }
                }
                payment_layout.setVisibility(View.VISIBLE);
                if(vehicle_selected.equals("Car")){
                    total = selectedTimeSlot.size() * Integer.parseInt(f_wheeler_rate);
                }else{
                    total = selectedTimeSlot.size() * Integer.parseInt(t_wheeler_rate);
                }
                total_amount.setText("Rs. " + String.valueOf(total));
                if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
            }
        });

        three.setBackgroundResource(R.drawable.text_view_3_bg);
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!timeList.contains("3")){
                    if(selectedTimeSlot.contains("3")){
                        three.setTextColor(Color.parseColor("#218124"));
                        three.setBackgroundColor(Color.parseColor("#ACEFC7"));
                        three.setBackgroundResource(R.drawable.text_view_3_bg);
                        selectedTimeSlot.remove("3");
                        if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
                    }else{
                        selectedTimeSlot.add("3");
                        three.setTextColor(Color.parseColor("#373B44"));
                        three.setBackgroundColor(Color.parseColor("#86A8E7"));
                        three.setBackgroundResource(R.drawable.text_view_5_bg);
                    }
                }
                payment_layout.setVisibility(View.VISIBLE);
                if(vehicle_selected.equals("Car")){
                    total = selectedTimeSlot.size() * Integer.parseInt(f_wheeler_rate);
                }else{
                    total = selectedTimeSlot.size() * Integer.parseInt(t_wheeler_rate);
                }
                total_amount.setText("Rs. " + String.valueOf(total));
                if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
            }
        });

        four.setBackgroundResource(R.drawable.text_view_3_bg);
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!timeList.contains("4")){
                    if(selectedTimeSlot.contains("4")){
                        four.setTextColor(Color.parseColor("#218124"));
                        four.setBackgroundColor(Color.parseColor("#ACEFC7"));
                        four.setBackgroundResource(R.drawable.text_view_3_bg);
                        selectedTimeSlot.remove("4");
                        if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
                    }else{
                        selectedTimeSlot.add("4");
                        four.setTextColor(Color.parseColor("#373B44"));
                        four.setBackgroundColor(Color.parseColor("#86A8E7"));
                        four.setBackgroundResource(R.drawable.text_view_5_bg);
                    }
                }
                payment_layout.setVisibility(View.VISIBLE);
                if(vehicle_selected.equals("Car")){
                    total = selectedTimeSlot.size() * Integer.parseInt(f_wheeler_rate);
                }else{
                    total = selectedTimeSlot.size() * Integer.parseInt(t_wheeler_rate);
                }
                total_amount.setText("Rs. " + String.valueOf(total));
                if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
            }
        });

        five.setBackgroundResource(R.drawable.text_view_3_bg);
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!timeList.contains("5")){
                    if(selectedTimeSlot.contains("5")){
                        five.setTextColor(Color.parseColor("#218124"));
                        five.setBackgroundColor(Color.parseColor("#ACEFC7"));
                        five.setBackgroundResource(R.drawable.text_view_3_bg);
                        selectedTimeSlot.remove("5");
                        if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
                    }else{
                        selectedTimeSlot.add("5");
                        five.setTextColor(Color.parseColor("#373B44"));
                        five.setBackgroundColor(Color.parseColor("#86A8E7"));
                        five.setBackgroundResource(R.drawable.text_view_5_bg);
                    }
                }
                payment_layout.setVisibility(View.VISIBLE);
                if(vehicle_selected.equals("Car")){
                    total = selectedTimeSlot.size() * Integer.parseInt(f_wheeler_rate);
                }else{
                    total = selectedTimeSlot.size() * Integer.parseInt(t_wheeler_rate);
                }
                total_amount.setText("Rs. " + String.valueOf(total));
                if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
            }
        });

        six.setBackgroundResource(R.drawable.text_view_3_bg);
        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!timeList.contains("6")){
                    if(selectedTimeSlot.contains("6")){
                        six.setTextColor(Color.parseColor("#218124"));
                        six.setBackgroundColor(Color.parseColor("#ACEFC7"));
                        six.setBackgroundResource(R.drawable.text_view_3_bg);
                        selectedTimeSlot.remove("6");
                        if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
                    }else{
                        selectedTimeSlot.add("6");
                        six.setTextColor(Color.parseColor("#373B44"));
                        six.setBackgroundColor(Color.parseColor("#86A8E7"));
                        six.setBackgroundResource(R.drawable.text_view_5_bg);
                    }
                }
                payment_layout.setVisibility(View.VISIBLE);
                if(vehicle_selected.equals("Car")){
                    total = selectedTimeSlot.size() * Integer.parseInt(f_wheeler_rate);
                }else{
                    total = selectedTimeSlot.size() * Integer.parseInt(t_wheeler_rate);
                }
                total_amount.setText("Rs. " + String.valueOf(total));
                if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
            }
        });

        seven.setBackgroundResource(R.drawable.text_view_3_bg);
        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!timeList.contains("7")){
                    if(selectedTimeSlot.contains("7")){
                        seven.setTextColor(Color.parseColor("#218124"));
                        seven.setBackgroundColor(Color.parseColor("#ACEFC7"));
                        seven.setBackgroundResource(R.drawable.text_view_3_bg);
                        selectedTimeSlot.remove("7");
                        if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
                    }else{
                        selectedTimeSlot.add("7");
                        seven.setTextColor(Color.parseColor("#373B44"));
                        seven.setBackgroundColor(Color.parseColor("#86A8E7"));
                        seven.setBackgroundResource(R.drawable.text_view_5_bg);
                    }
                }
                payment_layout.setVisibility(View.VISIBLE);
                if(vehicle_selected.equals("Car")){
                    total = selectedTimeSlot.size() * Integer.parseInt(f_wheeler_rate);
                }else{
                    total = selectedTimeSlot.size() * Integer.parseInt(t_wheeler_rate);
                }
                total_amount.setText("Rs. " + String.valueOf(total));
                if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
            }
        });

        eight.setBackgroundResource(R.drawable.text_view_3_bg);
        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!timeList.contains("8")){
                    if(selectedTimeSlot.contains("8")){
                        eight.setTextColor(Color.parseColor("#218124"));
                        eight.setBackgroundColor(Color.parseColor("#ACEFC7"));
                        eight.setBackgroundResource(R.drawable.text_view_3_bg);
                        selectedTimeSlot.remove("8");
                        if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
                    }else{
                        selectedTimeSlot.add("8");
                        eight.setTextColor(Color.parseColor("#373B44"));
                        eight.setBackgroundColor(Color.parseColor("#86A8E7"));
                        eight.setBackgroundResource(R.drawable.text_view_5_bg);
                    }
                }
                payment_layout.setVisibility(View.VISIBLE);
                if(vehicle_selected.equals("Car")){
                    total = selectedTimeSlot.size() * Integer.parseInt(f_wheeler_rate);
                }else{
                    total = selectedTimeSlot.size() * Integer.parseInt(t_wheeler_rate);
                }
                total_amount.setText("Rs. " + String.valueOf(total));
                if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
            }
        });

        nine.setBackgroundResource(R.drawable.text_view_3_bg);
        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!timeList.contains("9")){
                    if(selectedTimeSlot.contains("9")){
                        nine.setTextColor(Color.parseColor("#218124"));
                        nine.setBackgroundColor(Color.parseColor("#ACEFC7"));
                        nine.setBackgroundResource(R.drawable.text_view_3_bg);
                        selectedTimeSlot.remove("9");
                        if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
                    }else{
                        selectedTimeSlot.add("9");
                        nine.setTextColor(Color.parseColor("#373B44"));
                        nine.setBackgroundColor(Color.parseColor("#86A8E7"));
                        nine.setBackgroundResource(R.drawable.text_view_5_bg);
                    }
                }
                payment_layout.setVisibility(View.VISIBLE);
                if(vehicle_selected.equals("Car")){
                    total = selectedTimeSlot.size() * Integer.parseInt(f_wheeler_rate);
                }else{
                    total = selectedTimeSlot.size() * Integer.parseInt(t_wheeler_rate);
                }
                total_amount.setText("Rs. " + String.valueOf(total));
                if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
            }
        });

        ten.setBackgroundResource(R.drawable.text_view_3_bg);
        ten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!timeList.contains("10")){
                    if(selectedTimeSlot.contains("10")){
                        ten.setTextColor(Color.parseColor("#218124"));
                        ten.setBackgroundColor(Color.parseColor("#ACEFC7"));
                        ten.setBackgroundResource(R.drawable.text_view_3_bg);
                        selectedTimeSlot.remove("10");
                        if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
                    }else{
                        selectedTimeSlot.add("10");
                        ten.setTextColor(Color.parseColor("#373B44"));
                        ten.setBackgroundColor(Color.parseColor("#86A8E7"));
                        ten.setBackgroundResource(R.drawable.text_view_5_bg);
                    }
                }
                payment_layout.setVisibility(View.VISIBLE);
                if(vehicle_selected.equals("Car")){
                    total = selectedTimeSlot.size() * Integer.parseInt(f_wheeler_rate);
                }else{
                    total = selectedTimeSlot.size() * Integer.parseInt(t_wheeler_rate);
                }
                total_amount.setText("Rs. " + String.valueOf(total));
                if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
            }
        });

        eleven.setBackgroundResource(R.drawable.text_view_3_bg);
        eleven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!timeList.contains("11")){
                    if(selectedTimeSlot.contains("11")){
                        eleven.setTextColor(Color.parseColor("#218124"));
                        eleven.setBackgroundColor(Color.parseColor("#ACEFC7"));
                        eleven.setBackgroundResource(R.drawable.text_view_3_bg);
                        selectedTimeSlot.remove("11");
                        if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
                    }else{
                        selectedTimeSlot.add("11");
                        eleven.setTextColor(Color.parseColor("#373B44"));
                        eleven.setBackgroundColor(Color.parseColor("#86A8E7"));
                        eleven.setBackgroundResource(R.drawable.text_view_5_bg);
                    }
                }
                payment_layout.setVisibility(View.VISIBLE);
                if(vehicle_selected.equals("Car")){
                    total = selectedTimeSlot.size() * Integer.parseInt(f_wheeler_rate);
                }else{
                    total = selectedTimeSlot.size() * Integer.parseInt(t_wheeler_rate);
                }
                total_amount.setText("Rs. " + String.valueOf(total));
                if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
            }
        });

        twelve.setBackgroundResource(R.drawable.text_view_3_bg);
        twelve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!timeList.contains("12")){
                    if(selectedTimeSlot.contains("12")){
                        twelve.setTextColor(Color.parseColor("#218124"));
                        twelve.setBackgroundColor(Color.parseColor("#ACEFC7"));
                        twelve.setBackgroundResource(R.drawable.text_view_3_bg);
                        selectedTimeSlot.remove("12");
                        if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
                    }else{
                        selectedTimeSlot.add("12");
                        twelve.setTextColor(Color.parseColor("#373B44"));
                        twelve.setBackgroundColor(Color.parseColor("#86A8E7"));
                        twelve.setBackgroundResource(R.drawable.text_view_5_bg);
                    }
                }
                payment_layout.setVisibility(View.VISIBLE);
                if(vehicle_selected.equals("Car")){
                    total = selectedTimeSlot.size() * Integer.parseInt(f_wheeler_rate);
                }else{
                    total = selectedTimeSlot.size() * Integer.parseInt(t_wheeler_rate);
                }
                total_amount.setText("Rs. " + String.valueOf(total));
                if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
            }
        });

        thirteen.setBackgroundResource(R.drawable.text_view_3_bg);
        thirteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!timeList.contains("13")){
                    if(selectedTimeSlot.contains("13")){
                        thirteen.setTextColor(Color.parseColor("#218124"));
                        thirteen.setBackgroundColor(Color.parseColor("#ACEFC7"));
                        thirteen.setBackgroundResource(R.drawable.text_view_3_bg);
                        selectedTimeSlot.remove("13");
                        if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
                    }else{
                        selectedTimeSlot.add("13");
                        thirteen.setTextColor(Color.parseColor("#373B44"));
                        thirteen.setBackgroundColor(Color.parseColor("#86A8E7"));
                        thirteen.setBackgroundResource(R.drawable.text_view_5_bg);
                    }
                }
                payment_layout.setVisibility(View.VISIBLE);
                if(vehicle_selected.equals("Car")){
                    total = selectedTimeSlot.size() * Integer.parseInt(f_wheeler_rate);
                }else{
                    total = selectedTimeSlot.size() * Integer.parseInt(t_wheeler_rate);
                }
                total_amount.setText("Rs. " + String.valueOf(total));
                if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
            }
        });

        fourteen.setBackgroundResource(R.drawable.text_view_3_bg);
        fourteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!timeList.contains("14")){
                    if(selectedTimeSlot.contains("14")){
                        fourteen.setTextColor(Color.parseColor("#218124"));
                        fourteen.setBackgroundColor(Color.parseColor("#ACEFC7"));
                        fourteen.setBackgroundResource(R.drawable.text_view_3_bg);
                        selectedTimeSlot.remove("14");
                        if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
                    }else{
                        selectedTimeSlot.add("14");
                        fourteen.setTextColor(Color.parseColor("#373B44"));
                        fourteen.setBackgroundColor(Color.parseColor("#86A8E7"));
                        fourteen.setBackgroundResource(R.drawable.text_view_5_bg);
                    }
                }
                payment_layout.setVisibility(View.VISIBLE);
                if(vehicle_selected.equals("Car")){
                    total = selectedTimeSlot.size() * Integer.parseInt(f_wheeler_rate);
                }else{
                    total = selectedTimeSlot.size() * Integer.parseInt(t_wheeler_rate);
                }
                total_amount.setText("Rs. " + String.valueOf(total));
                if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
            }
        });

        fifteen.setBackgroundResource(R.drawable.text_view_3_bg);
        fifteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!timeList.contains("15")){
                    if(selectedTimeSlot.contains("15")){
                        fifteen.setTextColor(Color.parseColor("#218124"));
                        fifteen.setBackgroundColor(Color.parseColor("#ACEFC7"));
                        fifteen.setBackgroundResource(R.drawable.text_view_3_bg);
                        selectedTimeSlot.remove("15");
                        if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
                    }else{
                        selectedTimeSlot.add("15");
                        fifteen.setTextColor(Color.parseColor("#373B44"));
                        fifteen.setBackgroundColor(Color.parseColor("#86A8E7"));
                        fifteen.setBackgroundResource(R.drawable.text_view_5_bg);
                    }
                }
                payment_layout.setVisibility(View.VISIBLE);
                if(vehicle_selected.equals("Car")){
                    total = selectedTimeSlot.size() * Integer.parseInt(f_wheeler_rate);
                }else{
                    total = selectedTimeSlot.size() * Integer.parseInt(t_wheeler_rate);
                }
                total_amount.setText("Rs. " + String.valueOf(total));
                if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
            }
        });

        sixteen.setBackgroundResource(R.drawable.text_view_3_bg);
        sixteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!timeList.contains("16")){
                    if(selectedTimeSlot.contains("16")){
                        sixteen.setTextColor(Color.parseColor("#218124"));
                        sixteen.setBackgroundColor(Color.parseColor("#ACEFC7"));
                        sixteen.setBackgroundResource(R.drawable.text_view_3_bg);
                        selectedTimeSlot.remove("16");
                        if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
                    }else{
                        selectedTimeSlot.add("16");
                        sixteen.setTextColor(Color.parseColor("#373B44"));
                        sixteen.setBackgroundColor(Color.parseColor("#86A8E7"));
                        sixteen.setBackgroundResource(R.drawable.text_view_5_bg);
                    }
                }
                payment_layout.setVisibility(View.VISIBLE);
                if(vehicle_selected.equals("Car")){
                    total = selectedTimeSlot.size() * Integer.parseInt(f_wheeler_rate);
                }else{
                    total = selectedTimeSlot.size() * Integer.parseInt(t_wheeler_rate);
                }
                total_amount.setText("Rs. " + String.valueOf(total));
                if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
            }
        });

        seventeen.setBackgroundResource(R.drawable.text_view_3_bg);
        seventeen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!timeList.contains("17")){
                    if(selectedTimeSlot.contains("17")){
                        seventeen.setTextColor(Color.parseColor("#218124"));
                        seventeen.setBackgroundColor(Color.parseColor("#ACEFC7"));
                        seventeen.setBackgroundResource(R.drawable.text_view_3_bg);
                        selectedTimeSlot.remove("17");
                        if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
                    }else{
                        selectedTimeSlot.add("17");
                        seventeen.setTextColor(Color.parseColor("#373B44"));
                        seventeen.setBackgroundColor(Color.parseColor("#86A8E7"));
                        seventeen.setBackgroundResource(R.drawable.text_view_5_bg);
                    }
                }
                payment_layout.setVisibility(View.VISIBLE);
                if(vehicle_selected.equals("Car")){
                    total = selectedTimeSlot.size() * Integer.parseInt(f_wheeler_rate);
                }else{
                    total = selectedTimeSlot.size() * Integer.parseInt(t_wheeler_rate);
                }
                total_amount.setText("Rs. " + String.valueOf(total));
                if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
            }
        });

        eighteen.setBackgroundResource(R.drawable.text_view_3_bg);
        eighteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!timeList.contains("18")){
                    if(selectedTimeSlot.contains("18")){
                        eighteen.setTextColor(Color.parseColor("#218124"));
                        eighteen.setBackgroundColor(Color.parseColor("#ACEFC7"));
                        eighteen.setBackgroundResource(R.drawable.text_view_3_bg);
                        selectedTimeSlot.remove("18");
                        if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
                    }else{
                        selectedTimeSlot.add("18");
                        eighteen.setTextColor(Color.parseColor("#373B44"));
                        eighteen.setBackgroundColor(Color.parseColor("#86A8E7"));
                        eighteen.setBackgroundResource(R.drawable.text_view_5_bg);
                    }
                }
                payment_layout.setVisibility(View.VISIBLE);
                if(vehicle_selected.equals("Car")){
                    total = selectedTimeSlot.size() * Integer.parseInt(f_wheeler_rate);
                }else{
                    total = selectedTimeSlot.size() * Integer.parseInt(t_wheeler_rate);
                }
                total_amount.setText("Rs. " + String.valueOf(total));
                if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
            }
        });

        nineteen.setBackgroundResource(R.drawable.text_view_3_bg);
        nineteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!timeList.contains("19")){
                    if(selectedTimeSlot.contains("19")){
                        nineteen.setTextColor(Color.parseColor("#218124"));
                        nineteen.setBackgroundColor(Color.parseColor("#ACEFC7"));
                        nineteen.setBackgroundResource(R.drawable.text_view_3_bg);
                        selectedTimeSlot.remove("19");
                        if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
                    }else{
                        selectedTimeSlot.add("19");
                        nineteen.setTextColor(Color.parseColor("#373B44"));
                        nineteen.setBackgroundColor(Color.parseColor("#86A8E7"));
                        nineteen.setBackgroundResource(R.drawable.text_view_5_bg);
                    }
                }
                payment_layout.setVisibility(View.VISIBLE);
                if(vehicle_selected.equals("Car")){
                    total = selectedTimeSlot.size() * Integer.parseInt(f_wheeler_rate);
                }else{
                    total = selectedTimeSlot.size() * Integer.parseInt(t_wheeler_rate);
                }
                total_amount.setText("Rs. " + String.valueOf(total));
                if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
            }
        });

        twenty.setBackgroundResource(R.drawable.text_view_3_bg);
        twenty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!timeList.contains("20")){
                    if(selectedTimeSlot.contains("20")){
                        twenty.setTextColor(Color.parseColor("#218124"));
                        twenty.setBackgroundColor(Color.parseColor("#ACEFC7"));
                        twenty.setBackgroundResource(R.drawable.text_view_3_bg);
                        selectedTimeSlot.remove("20");
                        if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
                    }else{
                        selectedTimeSlot.add("20");
                        twenty.setTextColor(Color.parseColor("#373B44"));
                        twenty.setBackgroundColor(Color.parseColor("#86A8E7"));
                        twenty.setBackgroundResource(R.drawable.text_view_5_bg);
                    }
                }
                payment_layout.setVisibility(View.VISIBLE);
                if(vehicle_selected.equals("Car")){
                    total = selectedTimeSlot.size() * Integer.parseInt(f_wheeler_rate);
                }else{
                    total = selectedTimeSlot.size() * Integer.parseInt(t_wheeler_rate);
                }
                total_amount.setText("Rs. " + String.valueOf(total));
                if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
            }
        });

        twentyOne.setBackgroundResource(R.drawable.text_view_3_bg);
        twentyOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!timeList.contains("21")){
                    if(selectedTimeSlot.contains("21")){
                        twentyOne.setTextColor(Color.parseColor("#218124"));
                        twentyOne.setBackgroundColor(Color.parseColor("#ACEFC7"));
                        twentyOne.setBackgroundResource(R.drawable.text_view_3_bg);
                        selectedTimeSlot.remove("21");
                        if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
                    }else{
                        selectedTimeSlot.add("21");
                        twentyOne.setTextColor(Color.parseColor("#373B44"));
                        twentyOne.setBackgroundColor(Color.parseColor("#86A8E7"));
                        twentyOne.setBackgroundResource(R.drawable.text_view_5_bg);
                    }
                }
                payment_layout.setVisibility(View.VISIBLE);
                if(vehicle_selected.equals("Car")){
                    total = selectedTimeSlot.size() * Integer.parseInt(f_wheeler_rate);
                }else{
                    total = selectedTimeSlot.size() * Integer.parseInt(t_wheeler_rate);
                }
                total_amount.setText("Rs. " + String.valueOf(total));
                if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
            }
        });

        twentyTwo.setBackgroundResource(R.drawable.text_view_3_bg);
        twentyTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!timeList.contains("22")){
                    if(selectedTimeSlot.contains("22")){
                        twentyTwo.setTextColor(Color.parseColor("#218124"));
                        twentyTwo.setBackgroundColor(Color.parseColor("#ACEFC7"));
                        twentyTwo.setBackgroundResource(R.drawable.text_view_3_bg);
                        selectedTimeSlot.remove("22");
                        if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
                    }else{
                        selectedTimeSlot.add("22");
                        twentyTwo.setTextColor(Color.parseColor("#373B44"));
                        twentyTwo.setBackgroundColor(Color.parseColor("#86A8E7"));
                        twentyTwo.setBackgroundResource(R.drawable.text_view_5_bg);
                    }
                }
                payment_layout.setVisibility(View.VISIBLE);
                if(vehicle_selected.equals("Car")){
                    total = selectedTimeSlot.size() * Integer.parseInt(f_wheeler_rate);
                }else{
                    total = selectedTimeSlot.size() * Integer.parseInt(t_wheeler_rate);
                }
                total_amount.setText("Rs. " + String.valueOf(total));
                if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
            }
        });

        twentyThree.setBackgroundResource(R.drawable.text_view_3_bg);
        twentyThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!timeList.contains("23")) {
                    if(selectedTimeSlot.contains("23")){
                        twentyThree.setTextColor(Color.parseColor("#218124"));
                        twentyThree.setBackgroundColor(Color.parseColor("#ACEFC7"));
                        twentyThree.setBackgroundResource(R.drawable.text_view_3_bg);
                        selectedTimeSlot.remove("23");
                        if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
                    }else{
                        selectedTimeSlot.add("23");
                        twentyThree.setTextColor(Color.parseColor("#373B44"));
                        twentyThree.setBackgroundColor(Color.parseColor("#86A8E7"));
                        twentyThree.setBackgroundResource(R.drawable.text_view_5_bg);
                    }
                }
                payment_layout.setVisibility(View.VISIBLE);
                if(vehicle_selected.equals("Car")){
                    total = selectedTimeSlot.size() * Integer.parseInt(f_wheeler_rate);
                }else{
                    total = selectedTimeSlot.size() * Integer.parseInt(t_wheeler_rate);
                }
                total_amount.setText("Rs. " + String.valueOf(total));
                if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
            }
        });

        twentyFour.setBackgroundResource(R.drawable.text_view_3_bg);
        twentyFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!timeList.contains("24")) {
                    if(selectedTimeSlot.contains("24")){
                        twentyFour.setTextColor(Color.parseColor("#218124"));
                        twentyFour.setBackgroundColor(Color.parseColor("#ACEFC7"));
                        twentyFour.setBackgroundResource(R.drawable.text_view_3_bg);
                        selectedTimeSlot.remove("24");
                        if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
                    }else{
                        selectedTimeSlot.add("24");
                        twentyFour.setTextColor(Color.parseColor("#373B44"));
                        twentyFour.setBackgroundColor(Color.parseColor("#86A8E7"));
                        twentyFour.setBackgroundResource(R.drawable.text_view_5_bg);
                    }
                }
                payment_layout.setVisibility(View.VISIBLE);
                if(vehicle_selected.equals("Car")){
                    total = selectedTimeSlot.size() * Integer.parseInt(f_wheeler_rate);
                }else{
                    total = selectedTimeSlot.size() * Integer.parseInt(t_wheeler_rate);
                }
                total_amount.setText("Rs. " + String.valueOf(total));
                if(selectedTimeSlot.size() == 0)payment_layout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, dayOfMonth);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);

//        Date time = c.getTime();
//        long t = time.getTime();
//        dateSelected = String.valueOf(t);
        dateSelected = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        select_date.setText(dateSelected);

        fetchSlotDetail();
    }
}