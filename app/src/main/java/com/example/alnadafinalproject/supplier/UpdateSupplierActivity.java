package com.example.alnadafinalproject.supplier;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alnadafinalproject.R;
import com.example.alnadafinalproject.classes.Person;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public  class UpdateSupplierActivity extends AppCompatActivity {
    public TextInputEditText name, phone, email , address;
    Intent intent ;
    String id ;
    Button updatesupplierbtn ;
    TextView txtlogin ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update_supplier);
        init();
        txtlogin = findViewById(R.id.txtlogin);


        Typeface typeface = getResources().getFont(R.font.bungee);
        txtlogin.setTypeface(typeface);

        Bundle b = getIntent().getExtras();

        if (b != null) {
            Object cus =  b.getSerializable("supplier");

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
        updatesupplierbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputValidation()) {
                    if (UpdateSupplier()) {
                        finish();
                    }
                }
            }
        });

    }
    public void init(){
        name = findViewById(R.id.edit_name);
        phone = findViewById(R.id.edit_phonenumber);
        email = findViewById(R.id.edit_email);
        address = findViewById(R.id.edit_address);
        updatesupplierbtn = findViewById(R.id.updatesupplierbtn);
    }
    public boolean UpdateSupplier(){
        DatabaseReference refCus = FirebaseDatabase.getInstance().getReference("suppliers").child(id);

        String sup_name = name.getText().toString();
        String sup_phone = phone.getText().toString();
        String sup_email = email.getText().toString();
        String sup_address = address.getText().toString();

        Person supplier  = new Person(id , sup_name , sup_phone , sup_email , sup_address);
        refCus.setValue(supplier);
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