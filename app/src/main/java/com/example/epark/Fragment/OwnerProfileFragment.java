package com.example.epark.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.epark.LogInActivity;
import com.example.epark.Model.Person;
import com.example.epark.OwnerProfileUpdateActivity;
import com.example.epark.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class OwnerProfileFragment extends Fragment {

    TextView log_out, name, phone_no, email, upi_id;
    ImageView edit;
    FirebaseDatabase database;
    FirebaseAuth auth;
    String Name, PhoneNo, EmailId, UpiId;

    public OwnerProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_owner_profile, container, false);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        Log.e("TAG", getContext().toString());

        Initialize(view);
        fetchDetail();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), OwnerProfileUpdateActivity.class);
                i.putExtra("activity", getContext().toString());
                startActivity(i);
            }
        });

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                builder.setTitle("Log Out");
                builder.setMessage("Are you sure you want to Log Out!");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences pref = getActivity().getSharedPreferences("LogIn", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("LogInMode", "2");
                        editor.apply();

                        Intent i = new Intent(getActivity(), LogInActivity.class);
                        startActivity(i);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        return view;
    }

    private void fetchDetail() {
        database.getReference().child("Users").child(auth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Person person = snapshot.getValue(Person.class);
                        name.setText(person.getName());
                        phone_no.setText(person.getPhone_no());
                        email.setText(person.getEmail_Id());
                        if(person.getUpiId()!=null)upi_id.setText(person.getUpiId());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void Initialize(View view) {
        edit = view.findViewById(R.id.edit_personal_detail_owner);
        name = view.findViewById(R.id.textView4);
        phone_no = view.findViewById(R.id.textView6);
        email = view.findViewById(R.id.textView8);
        upi_id = view.findViewById(R.id.textView10);
        log_out = view.findViewById(R.id.textView11);
    }
}