package com.example.epark;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.epark.Model.Person;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    TextInputLayout name_layout, phone_layout, email_layout, pass_layout;
    TextInputEditText name_text, phone_text, email_text, pass_text;
    String selected_mode;
    RadioGroup radioGroup;
    String name, phone_no, email_id, password;
    Button register;
    FirebaseAuth auth;
    FirebaseDatabase database;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initialize();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        name_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                name = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        phone_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                phone_no = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        email_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                email_id = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        pass_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton rb1 = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
                selected_mode = rb1.getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                builder.setTitle("Register");
                builder.setMessage("Please Wait....");
                builder.setCancelable(false);
                dialog = builder.create();
                dialog.show();
                RegisterUser();
            }
        });

    }

    private void RegisterUser() {

        auth.createUserWithEmailAndPassword(email_id, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Person person = new Person(name, phone_no, email_id, password, selected_mode);
                    database.getReference().child("Users").child(auth.getCurrentUser().getUid()).setValue(person).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dialog.dismiss();
                            Intent i = new Intent(SignUpActivity.this, LogInActivity.class);
                            startActivity(i);
                            finish();
                        }
                    });
                }else if(task.getException() instanceof FirebaseAuthUserCollisionException){
                    dialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "Account already exists!!", Toast.LENGTH_SHORT).show();
                }else{
                    dialog.dismiss();
                    Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void initialize() {
        name_layout = findViewById(R.id.name_sign_up);
        name_text = findViewById(R.id.e_text_name_sign_up);
        phone_layout = findViewById(R.id.phone_sign_up);
        phone_text = findViewById(R.id.e_text_phone_sign_up);
        email_layout = findViewById(R.id.email_sign_up);
        email_text = findViewById(R.id.e_text_email_sign_up);
        pass_layout = findViewById(R.id.password_sign_up);
        pass_text = findViewById(R.id.e_text_password_sign_up);
        radioGroup = findViewById(R.id.radio_grp1);
        register = findViewById(R.id.register_btn);
    }
}