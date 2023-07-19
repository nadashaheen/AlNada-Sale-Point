package com.example.alnadafinalproject.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateCustomerActivity extends AppCompatActivity {
    public TextInputEditText name, phone, email , address;
    Intent intent ;
    String id ;
    Button updateCustomerbtn ;
    TextView txtlogin ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_update_customer);

        txtlogin = findViewById(R.id.txtlogin);

        Typeface typeface = getResources().getFont(R.font.bungee);
        txtlogin.setTypeface(typeface);

        super.onCreate(savedInstanceState);
        init();

        Bundle b = getIntent().getExtras();

        if (b != null) {
        Object cus =  b.getSerializable("customer");

             id = b.getString("id");
            name.setText(b.getString("name"));
            phone.setText(b.getString("call"));
            email.setText(b.getString("email"));
            address.setText(b.getString("address"));
        } else {
            name.setText("");
            phone.setText("");
            email.setText("");
            address.setText("");
        }
        updateCustomerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputValidation()) {
                    if (UpdateCustomer()) {
                        finish();
                    }
                }
            }
        });

    }
    public void init(){
        name = findViewById(R.id.edit_cus_name);
        phone = findViewById(R.id.edit_cus_phonenumber);
        email = findViewById(R.id.edit_cus_email);
        address = findViewById(R.id.edit_cus_address);
        updateCustomerbtn = findViewById(R.id.updateCustomerbtn);
    }
    public boolean UpdateCustomer(){
        DatabaseReference refCus = FirebaseDatabase.getInstance().getReference("customers").child(id);

        String cus_name = name.getText().toString();
        String cus_phone = phone.getText().toString();
        String cus_email = email.getText().toString();
        String cus_address = address.getText().toString();

        Person customer  = new Person(id , cus_name , cus_phone , cus_email , cus_address);
        refCus.setValue(customer);
        Toast.makeText(this, "Updated Successfully " , Toast.LENGTH_SHORT).show();

        return  true ;
    }


    public Boolean inputValidation() {
        boolean flag = true;

        if (name.getText().toString().isEmpty()) {
            name.setError("Can't be Empty");
            return false;
        }

        if (phone.getText().toString().isEmpty()) {
            phone.setError("Can't be Empty");
            return false;
        }

        if (email.getText().toString().isEmpty()) {
            email.setError("Can't be Empty");
            return false;
        }

        if (address.getText().toString().isEmpty()) {
            address.setError("Can't be Empty");
            return false;
        }

        return flag;
    }

}