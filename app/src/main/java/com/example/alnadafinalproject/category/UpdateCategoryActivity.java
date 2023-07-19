package com.example.alnadafinalproject.category;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alnadafinalproject.R;
import com.example.alnadafinalproject.classes.Category;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateCategoryActivity extends AppCompatActivity {
    public TextInputEditText name;
    String id ;
    Button updateCategoryrbtn ;
    TextView txtlogin ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update_category);
        txtlogin = findViewById(R.id.txtlogin);

        Typeface typeface = getResources().getFont(R.font.bungee);
        txtlogin.setTypeface(typeface);

        name = findViewById(R.id.edit_cat_name);
        updateCategoryrbtn = findViewById(R.id.updateCategoryrbtn);

        Bundle b = getIntent().getExtras();

        if (b != null) {
            Object cus =  b.getSerializable("category");

            id = b.getString("id");
            name.setText(b.getString("name"));

        } else {
            name.setText("");
        }
        updateCategoryrbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputValidation()) {
                    if (UpdateCategory()) {
                        finish();
                    }
                }
            }
        });
    }

public boolean UpdateCategory(){
    DatabaseReference refCus = FirebaseDatabase.getInstance().getReference("categories").child(id);

    String cat_name = name.getText().toString();

    Category cat  = new Category(id , cat_name);
    refCus.setValue(cat);
    Toast.makeText(this, "Updated Successfully " , Toast.LENGTH_SHORT).show();

    return  true ;
}


    public Boolean inputValidation() {
        boolean flag = true;

        if (name.getText().toString().isEmpty()) {
            name.setError("Can't be Empty");
            return false;
        }

        return flag;
    }
}