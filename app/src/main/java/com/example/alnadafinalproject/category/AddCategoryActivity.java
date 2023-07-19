package com.example.alnadafinalproject.category;

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

import com.example.alnadafinalproject.classes.Category;
import com.example.alnadafinalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCategoryActivity extends AppCompatActivity {

    private TextInputEditText cat_name;
    private Button addCategory;
    private FirebaseDatabase db ;
    private DatabaseReference ref ;
    TextView txtlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_category);

        txtlogin = findViewById(R.id.txtlogin);


        Typeface typeface = getResources().getFont(R.font.bungee);
        txtlogin.setTypeface(typeface);
        cat_name = findViewById(R.id.cat_name);
        addCategory = findViewById(R.id.addCategory);
        addCategory = findViewById(R.id.addCategory);
        txtlogin = findViewById(R.id.txtlogin);


        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCategoryToBD();
            }
        });


    }

    private void addCategoryToBD() {
        String name = cat_name.getText().toString();

        if (name.isEmpty()){
            cat_name.setError("name can not be empty !");
        }else{
             db = FirebaseDatabase.getInstance();
             ref = db.getReference("categories");
            String id = ref.push().getKey();
            Category c = new Category(id , name);

            ref.child(id).setValue(c).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(AddCategoryActivity.this, "add category successfully", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }else{
                        String errorMessage = task.getException().getMessage();
                        Toast.makeText(AddCategoryActivity.this, "Fail "+errorMessage, Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

}