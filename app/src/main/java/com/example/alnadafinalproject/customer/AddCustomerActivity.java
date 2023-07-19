package com.example.alnadafinalproject.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alnadafinalproject.classes.Person;
import com.example.alnadafinalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCustomerActivity extends AppCompatActivity {

    private TextInputEditText cus_name, cus_phone, cus_email , cus_address;
    TextView txtlogin;

    private Button addcustomerbtn;
    private FirebaseDatabase db ;
    private DatabaseReference ref ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_customer);
        init();

        Typeface typeface = getResources().getFont(R.font.bungee);
        txtlogin.setTypeface(typeface);

        addcustomerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCustomerToBD();
            }
        });
    }
    public void init(){
        cus_name = findViewById(R.id.cus_name);
        cus_phone = findViewById(R.id.cus_phonenumber);
        cus_email = findViewById(R.id.cus_email);
        cus_address = findViewById(R.id.cus_address);
        addcustomerbtn = findViewById(R.id.addcustomer);
        txtlogin = findViewById(R.id.txtlogin);
    }

    private void addCustomerToBD() {
        String name = cus_name.getText().toString();
        String phone = cus_phone.getText().toString();
        String email = cus_email.getText().toString();
        String address = cus_address.getText().toString();

        if (name.isEmpty()){
            cus_name.setError("name can not be empty !");
        }else if (phone.isEmpty()){
            cus_phone.setError("phone can not be empty !");
        } else if (email.isEmpty()){
            cus_email.setError("email can not be empty !");
        }else if (address.isEmpty()){
            cus_address.setError("address can not be empty !");
        }else{
            db = FirebaseDatabase.getInstance();
            ref = db.getReference("customers");
            String id = ref.push().getKey();
            Person cus = new Person(id , name , phone , email , address);

            ref.child(id).setValue(cus).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(AddCustomerActivity.this, "add customer successfully", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }else{
                        String errorMessage = task.getException().getMessage();
                        Toast.makeText(AddCustomerActivity.this, "Fail "+errorMessage, Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

}