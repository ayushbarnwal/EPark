package com.example.epark;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.epark.Model.ParkSlot;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddParkingActivity extends AppCompatActivity {

    Toolbar toolbar;
    String latitude, longitude;
    String address, twoWheelerSlot, fourWheelerSlot, upiId, rateTwoWheeler, rateFourWHeeler, landmark, city, state, activity, adapterId;
    SupportMapFragment smp;
    private  GoogleMap mMap;
    TextView park_address;
    TextInputLayout t_wheeler_layout, f_wheeler_layout, upi_layout;
    TextInputEditText e_t_wheeler_layout_text, e_f_wheeler_layout_text, e_upi_layout_text, e_tWheeler_rate, e_fWheeler_rate;
    Button setParkSlot;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parking);

        toolbar = findViewById(R.id.tool_bar_1);
        toolbar.setTitle("Add Parking Detail");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Initialize();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        latitude = getIntent().getStringExtra("latitude").toString();
        longitude = getIntent().getStringExtra("longitude").toString();
        address = getIntent().getStringExtra("selectedAddress").toString();
        landmark = getIntent().getStringExtra("Landmark").toString();
        city = getIntent().getStringExtra("city").toString();
        state = getIntent().getStringExtra("state").toString();
        activity = getIntent().getStringExtra("activity").toString();

        smp = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

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


        park_address.setText(address);
        e_tWheeler_rate.setText(getIntent().getStringExtra("2_wheeler_slot_rate").toString());
        e_fWheeler_rate.setText(getIntent().getStringExtra("4_wheeler_slot_rate").toString());
        e_upi_layout_text.setText(getIntent().getStringExtra("upiId").toString());
        e_t_wheeler_layout_text.setText(getIntent().getStringExtra("2_wheeler_slot").toString());
        e_f_wheeler_layout_text.setText(getIntent().getStringExtra("4_wheeler_slot").toString());

        e_t_wheeler_layout_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                twoWheelerSlot = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        e_f_wheeler_layout_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fourWheelerSlot = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        e_upi_layout_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                upiId = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        e_tWheeler_rate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                rateTwoWheeler = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        e_fWheeler_rate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                rateFourWHeeler = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setParkSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddParkingActivity.this);
                builder.setTitle("Adding Park Slot");
                builder.setMessage("Please Wait...");
                builder.setCancelable(false);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                if(activity.equals("MapActivity")){
                    ParkSlot parkSlot = new ParkSlot(twoWheelerSlot, fourWheelerSlot, twoWheelerSlot, fourWheelerSlot, upiId, address, rateTwoWheeler, rateFourWHeeler, latitude, longitude, city, landmark, state);
                    database.getReference().child("Parking Slots Details")
                            .child(auth.getCurrentUser().getUid()).push().setValue(parkSlot)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    alertDialog.dismiss();
                                    Intent i = new Intent(AddParkingActivity.this, OwnerMainActivity.class);
                                    startActivity(i);
                                }
                            });
                }else if(activity.equals("OwnerHomeFragment")){
                    HashMap<String, Object> obj = new HashMap<>();
                    if(twoWheelerSlot != null)obj.put("total_2_wheeler_slot", twoWheelerSlot);
                    if(fourWheelerSlot!= null)obj.put("total_4_wheeler_slot", fourWheelerSlot);
                    if(rateTwoWheeler != null)obj.put("rate_2_wheelerPerHrs", rateTwoWheeler);
                    if(rateFourWHeeler != null)obj.put("rate_4_wheelerPerHrs", rateFourWHeeler);
                    if(upiId != null)obj.put("upi_Id", upiId);
                    adapterId = getIntent().getStringExtra("adapterId").toString();
                    database.getReference().child("Parking Slots Details")
                            .child(auth.getCurrentUser().getUid()).child(adapterId)
                            .updateChildren(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            alertDialog.dismiss();
                            Intent i = new Intent(AddParkingActivity.this, OwnerMainActivity.class);
                            startActivity(i);
                        }
                    });
                }
            }
        });

    }

    private void Initialize() {
        park_address = findViewById(R.id.park_address);
        e_t_wheeler_layout_text = findViewById(R.id.e_two_wheeler_slots_owner);
        e_f_wheeler_layout_text = findViewById(R.id.e_four_wheeler_slots_owner);
        e_upi_layout_text = findViewById(R.id.e_upi_id_owner);
        setParkSlot = findViewById(R.id.park_slot_set_btn);
        e_tWheeler_rate = findViewById(R.id.e_two_wheeler_slots_rate_owner);
        e_fWheeler_rate = findViewById(R.id.e_four_wheeler_slots_rate_owner);
    }


}