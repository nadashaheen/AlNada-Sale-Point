package com.example.alnadafinalproject.supplier;

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

public class AddSupplierActivity extends AppCompatActivity {

    private TextInputEditText sup_name, sup_phone, sup_email , sup_address;
    private Button addsupplierbtn;
    private FirebaseDatabase db ;
    private DatabaseReference ref ;
    TextView txtlogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_supplier);
        init();
        Typeface typeface = getResources().getFont(R.font.bungee);
        txtlogin.setTypeface(typeface);


        addsupplierbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSupplierToBD();
            }
        });
    }

    public void init(){
        sup_name = findViewById(R.id.sup_name);
        sup_phone = findViewById(R.id.sup_phonenumber);
        sup_email = findViewById(R.id.sup_email);
        sup_address = findViewById(R.id.sup_address);
        addsupplierbtn = findViewById(R.id.addsupplier);
        txtlogin = findViewById(R.id.txtlogin);
    }


    private void addSupplierToBD() {
        String name = sup_name.getText().toString();
        String phone = sup_phone.getText().toString();
        String email = sup_email.getText().toString();
        String address = sup_address.getText().toString();

        if (name.isEmpty()){
            sup_name.setError("name can not be empty !");
        }else if (phone.isEmpty()){
            sup_phone.setError("phone can not be empty !");
        } else if (email.isEmpty()){
            sup_email.setError("email can not be empty !");
        }else if (address.isEmpty()){
            sup_address.setError("address can not be empty !");
        }else{
            db = FirebaseDatabase.getInstance();
            ref = db.getReference("suppliers");
            String id = ref.push().getKey();
            Person sup = new Person(id , name , phone , email , address);

            ref.child(id).setValue(sup).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(AddSupplierActivity.this, "add suppliers successfully", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }else{
                        String errorMessage = task.getException().getMessage();
                        Toast.makeText(AddSupplierActivity.this, "Fail "+errorMessage, Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

}