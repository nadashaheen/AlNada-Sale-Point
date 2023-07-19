package com.example.alnadafinalproject.discount;

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

import com.example.alnadafinalproject.classes.Discount;
import com.example.alnadafinalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddDiscountActivity extends AppCompatActivity {

    private TextInputEditText dis_perc, dis_code;
    private Button addDiscountbtn;
    private FirebaseDatabase db;
    private DatabaseReference ref;
    TextView txtlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_discount);

        dis_perc = findViewById(R.id.dis_perc);
        dis_code = findViewById(R.id.dis_code);
        addDiscountbtn = findViewById(R.id.addDiscountbtn);
        txtlogin = findViewById(R.id.txtlogin);

        Typeface typeface = getResources().getFont(R.font.bungee);
        txtlogin.setTypeface(typeface);

        addDiscountbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDiscountToBD();
            }
        });
    }

    private void addDiscountToBD() {
        String percent = dis_perc.getText().toString();
        String code = dis_code.getText().toString();

        if (percent.isEmpty()) {
            dis_perc.setError("percent can not be empty !");
        }else if (code.isEmpty()){
            dis_code.setError("code can not be empty !");
        }else {
            db = FirebaseDatabase.getInstance();
            ref = db.getReference("discounts");
            String id = ref.push().getKey();
            Discount c = new Discount(id, code , percent);

            ref.child(id).setValue(c).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(AddDiscountActivity.this, "add discount successfully", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    } else {
                        String errorMessage = task.getException().getMessage();
                        Toast.makeText(AddDiscountActivity.this, "Fail " + errorMessage, Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

}