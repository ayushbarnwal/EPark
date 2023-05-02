package com.example.epark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.epark.Fragment.OwnerHomeFragment;
import com.example.epark.Fragment.OwnerMapFragment;
import com.example.epark.Fragment.OwnerProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class OwnerMainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_main);

        floatingActionButton = findViewById(R.id.owner_add_park);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        toolbar = findViewById(R.id.tool_bar);
        toolbar.setVisibility(View.GONE);

        if (getFragmentManager().findFragmentById(androidx.appcompat.R.id.action_bar_container) == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment,new OwnerHomeFragment()).commit();
        }
        bottomNavigationView.setOnItemSelectedListener(this);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ContextCompat.checkSelfPermission(OwnerMainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    startActivity(new Intent(OwnerMainActivity.this, MapActivity.class));
                    return;
                }else{

                    Dexter.withContext(OwnerMainActivity.this)
                            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                            .withListener(new PermissionListener() {
                                @Override
                                public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                    startActivity(new Intent(OwnerMainActivity.this, MapActivity.class));
                                }

                                @Override
                                public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                                    if(permissionDeniedResponse.isPermanentlyDenied()){
                                        AlertDialog.Builder builder = new AlertDialog.Builder(OwnerMainActivity.this);
                                        builder.setTitle("Permission Denied")
                                                .setMessage("Permission to access device location is Permanently Denied...you need to go setting to allow the permission")
                                                .setNegativeButton("Cancel", null)
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent intent = new Intent();
                                                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                        intent.setData(Uri.fromParts("package", getPackageName(), null));
                                                    }
                                                }).show();
                                    }else{
                                        Toast.makeText(OwnerMainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                    permissionToken.continuePermissionRequest();
                                }
                            }).check();

                }
            }
        });


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.owner_home :
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, new OwnerHomeFragment()).commit();
                toolbar.setVisibility(View.GONE);
                floatingActionButton.setVisibility(View.VISIBLE);
                return true;
            case R.id.owner_map:
                seekPermision();
                //getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, new OwnerMapFragment()).commit();
                floatingActionButton.setVisibility(View.GONE);
                toolbar.setVisibility(View.GONE);
                toolbar.setTitle("Map");
                toolbar.setTitleTextColor(Color.WHITE);
                return true;
            case R.id.owner_history:
                Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
                floatingActionButton.setVisibility(View.GONE);
                toolbar.setVisibility(View.VISIBLE);
                toolbar.setTitle("History");
                toolbar.setTitleTextColor(Color.WHITE);
                return true;
            case R.id.owner_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, new OwnerProfileFragment()).commit();
                floatingActionButton.setVisibility(View.GONE);
                toolbar.setVisibility(View.VISIBLE);
                toolbar.setTitle("Profile");
                toolbar.setTitleTextColor(Color.WHITE);
                return true;
        }

        return true;
    }

    private void seekPermision() {

        if(ContextCompat.checkSelfPermission(OwnerMainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, new OwnerMapFragment()).commit();
            return;
        }else{

            Dexter.withContext(OwnerMainActivity.this)
                    .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, new OwnerMapFragment()).commit();
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                            if(permissionDeniedResponse.isPermanentlyDenied()){
                                AlertDialog.Builder builder = new AlertDialog.Builder(OwnerMainActivity.this);
                                builder.setTitle("Permission Denied")
                                        .setMessage("Permission to access device location is Permanently Denied...you need to go setting to allow the permission")
                                        .setNegativeButton("Cancel", null)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent();
                                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                intent.setData(Uri.fromParts("package", getPackageName(), null));
                                            }
                                        }).show();
                            }else{
                                Toast.makeText(OwnerMainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }
                    }).check();

        }

    }
}