package com.idontknow.anochatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    TextView txt_signup, signin_btn;
    EditText login_pass,login_email;
    FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait ... ");
        progressDialog.setCancelable(false);


        auth = FirebaseAuth.getInstance();

        txt_signup = findViewById(R.id.txt_signup);
        signin_btn = findViewById(R.id.signin_btn);
        login_pass = findViewById(R.id.login_pass);
        login_email = findViewById(R.id.login_email);

        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ;
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));

            }
        });

        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String email = login_email.getText().toString();
                String pass = login_pass.getText().toString();


                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Enter Valid Data", Toast.LENGTH_SHORT).show();
                } else if (!email.matches(emailPattern)) {
                    progressDialog.dismiss();
                    login_email.setError("Invalid Email");
                    Toast.makeText(LoginActivity.this, "Invalid Email Data", Toast.LENGTH_SHORT).show();
                } else if (pass.length() <= 6) {
                    progressDialog.dismiss();
                    login_pass.setError("Invalid Password");
                    Toast.makeText(LoginActivity.this, "Invalid Password Data", Toast.LENGTH_SHORT).show();
                } else{
                    auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Error In Login", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


    }
}