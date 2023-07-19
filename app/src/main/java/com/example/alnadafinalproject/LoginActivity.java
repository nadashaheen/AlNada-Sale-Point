package com.example.alnadafinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    private Button login;
    private TextView txtlogin;
    FirebaseAuth firAuth ;

    private TextInputEditText et_email, et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);


        inti();
        lisner();

        Typeface typeface = getResources().getFont(R.font.cairo);
        txtlogin.setTypeface(typeface);

    }

    private void auth() {
        String email =  et_email.getText().toString();
        String password =  et_password.getText().toString();

        if (email.isEmpty()){
            Toast.makeText(this , "Email Empty !!" , Toast.LENGTH_SHORT).show();
        }else if (password.isEmpty()){
            Toast.makeText(this , "Password Empty !!" , Toast.LENGTH_SHORT).show();
        }else{
            firAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        liseners();
                        finish();
                    }else{
                        Toast.makeText(LoginActivity.this, "email or password wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void lisner() {

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth();
            }
        });

    }

    private void inti() {
        firAuth = FirebaseAuth.getInstance();
        login = findViewById(R.id.login);
        txtlogin = findViewById(R.id.txtlogin);
        et_email = findViewById(R.id.email);
        et_password = findViewById(R.id.password);
    }

    private void liseners() {

        NotificationManager nm = getSystemService(NotificationManager.class);

        NotificationChannel nc = new NotificationChannel("my_id", "nada", NotificationManager.IMPORTANCE_HIGH);

        nm.createNotificationChannel(nc);
        NotificationCompat.Builder nb = new NotificationCompat.Builder(getApplicationContext(), "my_id");
        nb.setContentTitle("login");
        nb.setContentText("admin login to app successfully");
        nb.setSmallIcon(R.drawable.camera_image);

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 100, i, PendingIntent.FLAG_UPDATE_CURRENT);
        nb.setContentIntent(pi);

        nm.notify(new Random().nextInt(), nb.build());

        startActivity(i);

    }
}