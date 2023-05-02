package com.example.epark;

import androidx.annotation.NonNull;
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

import com.example.epark.Fragment.OwnerProfileFragment;
import com.example.epark.Model.Person;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.EnumMap;
import java.util.HashMap;

public class OwnerProfileUpdateActivity extends AppCompatActivity {

    TextInputEditText name, phoneNo, emailId, upiId;
    Button update;
    FirebaseDatabase database;
    FirebaseAuth auth;
    String Name, PhoneNo, EmailId, UpiId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_profile_update);

        Toolbar toolbar = findViewById(R.id.tool_bar_3);
        toolbar.setTitle("Update Profile");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        Initialize();
        fetchDetail();
        String activity = getIntent().getStringExtra("activity");

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Name = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        phoneNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                PhoneNo = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        emailId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                EmailId = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        upiId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                UpiId = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> obj = new HashMap<>();
                obj.put("name", Name);
                obj.put("phone_no", PhoneNo);
                obj.put("email_Id", EmailId);
                obj.put("UpiId", UpiId);
                Log.e("TAG" , Name + PhoneNo + EmailId + UpiId);
                database.getReference().child("Users").child(auth.getCurrentUser().getUid())
                        .updateChildren(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                if(activity.equals("com.example.epark.UserMainActivity@1691b2d")){
                                    Intent i = new Intent(OwnerProfileUpdateActivity.this,UserMainActivity.class);
                                    startActivity(i);
                                }else {
                                    Intent i = new Intent(OwnerProfileUpdateActivity.this, OwnerMainActivity.class);
                                    startActivity(i);
                                }
                            }
                        });
            }
        });

    }

    private void fetchDetail() {
        database.getReference().child("Users").child(auth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Person person = snapshot.getValue(Person.class);
                        name.setText(person.getName());
                        phoneNo.setText(person.getPhone_no());
                        emailId.setText(person.getEmail_Id());
                        if(person.getUpiId()!=null)upiId.setText(person.getUpiId());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void Initialize() {

        name = findViewById(R.id.e_text_name_u);
        phoneNo = findViewById(R.id.e_text_phone_u);
        emailId = findViewById(R.id.e_text_email_u);
        upiId = findViewById(R.id.e_text_upi_u);
        update = findViewById(R.id.update_profile_owner_btn);

    }

}