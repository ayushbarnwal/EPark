package com.example.epark;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;  //for fetching current location
    private Location mLastKnownLocation;
    private LocationCallback locationCallback;
    private Geocoder geocoder;
    private List<Address> addresses;
    private View mapView;
    private final float DEFAULT_ZOOM = 18;
    private ImageView marker;
    private double selectedLat, selectedLong;
    private String selectedAddress, Landmark, city, state;
    private Button setAddress;
    private TextView campAddress;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();

        marker = findViewById(R.id.marker);
        setAddress= findViewById(R.id.btn_find);
        setAddress.setVisibility(View.GONE);
        campAddress = findViewById(R.id.camp_address);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapActivity.this);

        setAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedAddress!=null){
                    Intent i = new Intent(MapActivity.this, AddParkingActivity.class);
                    i.putExtra("selectedAddress", selectedAddress);
                    i.putExtra("latitude", String.valueOf(selectedLat));
                    i.putExtra("longitude", String.valueOf(selectedLong));
                    i.putExtra("2_wheeler_slot", "");
                    i.putExtra("4_wheeler_slot", "");
                    i.putExtra("2_wheeler_slot_rate", "");
                    i.putExtra("4_wheeler_slot_rate", "");
                    i.putExtra("upiId", "");
                    i.putExtra("city", city);
                    i.putExtra("Landmark", Landmark);
                    i.putExtra("state", state);
                    i.putExtra("activity", "MapActivity");
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                    startActivity(i);
                }else Toast.makeText(MapActivity.this, "Select Valid Address", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);  // to show myLocation Button

        if(mapView != null && mapView.findViewById(Integer.parseInt("1")) != null){
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 40, 180);
        }

        //Before fetching location check whether gps is on or not....
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(MapActivity.this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        //if gps enabled
        task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getDeviceLocation();
            }
        });

        //if gps didn't enabled
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(e instanceof ResolvableApiException){
                    ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                    try {
                        resolvableApiException.startResolutionForResult(MapActivity.this, 51);  // this line show dialog
                    } catch (IntentSender.SendIntentException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude, latLng.longitude), DEFAULT_ZOOM));
                selectedLat = latLng.latitude;
                selectedLong = latLng.longitude;

                GetAddress(selectedLat, selectedLong);
            }
        });

//        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
//            @Override
//            public boolean onMyLocationButtonClick() {
//                if(materialSearchBar.isSuggestionsVisible()){
//                    materialSearchBar.clearSuggestions();
//                }
//                if(materialSearchBar.isSearchOpened()){
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//                        materialSearchBar.setAllowClickWhenDisabled(true);
//                    }
//                }
//                return false;
//            }
//        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 51){
            if(resultCode == RESULT_OK){
                getDeviceLocation();
            }
        }

    }

    @SuppressLint("MissingPermission")
    private void getDeviceLocation(){
        mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if(task.isSuccessful()){
                    mLastKnownLocation = task.getResult();
                    if(mLastKnownLocation != null){
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                    }else{
                        LocationRequest locationRequest = LocationRequest.create();
                        locationRequest.setInterval(10000);
                        locationRequest.setFastestInterval(5000);
                        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                        locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                super.onLocationResult(locationResult);
                                if(locationResult == null)return;
                                mLastKnownLocation = locationResult.getLastLocation();
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
                            }
                        };
                        mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
                    }
                }else{
                    Toast.makeText(MapActivity.this, "Unable to get last Location...", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void GetAddress(double mLat, double mLong){
        geocoder = new Geocoder(MapActivity.this, Locale.getDefault());

        if(mLat != 0){
            try {
                addresses= geocoder.getFromLocation(mLat, mLong, 10);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if(addresses != null){
                String mAddress  = addresses.get(0).getAddressLine(0);

                Landmark = addresses.get(0).getSubLocality();  // landmark will be zero for some locations
                city = addresses.get(0).getLocality();
                state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String dis = addresses.get(0).getSubAdminArea();

                Log.e("TAG", "h1 " + Landmark + "h2 " + city  + "h3 " + state + "h4 " + country + "h5 " + postalCode);

                selectedAddress = mAddress;

                Log.e("TAG", " h6 " + selectedAddress);


                if(mAddress != null){
                    Log.e("TAG", selectedAddress);
                    campAddress.setText(selectedAddress);
                    setAddress.setVisibility(View.VISIBLE);
                    setAddress.setText("Set Address");
                    setAddress.setTextColor(Color.WHITE);
                }else Toast.makeText(this, "Something went Wrong", Toast.LENGTH_SHORT).show();

            }else Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }else Toast.makeText(this, "Latlng null", Toast.LENGTH_SHORT).show();
    }
}