package com.example.alnadafinalproject.product;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alnadafinalproject.classes.Person;
import com.example.alnadafinalproject.classes.Product;
import com.example.alnadafinalproject.R;
import com.example.alnadafinalproject.classes.Category;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class AddProductActivity extends AppCompatActivity {

    public static final int PICK_IMAGE = 1021;

    private TextInputEditText pro_name, pro_code, pro_price, pro_wholesale,pro_unit, pro_description;
    TextView txtlogin;

    String cat_id, sup_id;
    ArrayList<String> cat_items = new ArrayList<>();
    ArrayList<String> sup_items = new ArrayList<>();
    ImageView ivImage;
    Uri selectedImage;
    FloatingActionButton selectImg_btn;

    private Button addProductbtn;
    Spinner cat_spinner;
    Spinner sup_spinner;
    private FirebaseDatabase db;
    private DatabaseReference ref;
    private StorageReference sref;
    private FirebaseStorage storage;

    ArrayList<Category> cat_list = new ArrayList<>();
    ArrayList<Person> sup_list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_product);

        init();
        Typeface typeface = getResources().getFont(R.font.bungee);
        txtlogin.setTypeface(typeface);


        addProductbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProductToBD();
            }
        });

        selectImg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                getIntent().setType("image/*");
                startActivityForResult(i, PICK_IMAGE);
            }
        });

        cat_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                cat_id = getIdByCatName(selectedItem);
                Log.e("e", "yessssssssssss" + cat_id + "  " + selectedItem);
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sup_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                sup_id = getIdBySupName(selectedItem);
                Log.e("e", "yessssssssssss" + sup_id + "  " + selectedItem);
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        readCategories();
        readSuppliers();

    }

    private String getIdByCatName(String selectedItem) {
        for (Category c : cat_list) {
            if (selectedItem.equalsIgnoreCase(c.getName())) {
                return c.getId();
            }
        }
        return null;
    }

    private String getIdBySupName(String selectedItem) {
        for (Person p : sup_list) {
            if (selectedItem.equalsIgnoreCase(p.getName())) {
                return p.getId();
            }
        }
        return null;
    }

    public void init() {
        pro_name = findViewById(R.id.pro_name);
        pro_code = findViewById(R.id.pro_code);
        pro_price = findViewById(R.id.pro_price);
        pro_wholesale = findViewById(R.id.pro_wholesale);
        pro_unit = findViewById(R.id.pro_unit);
        pro_description = findViewById(R.id.pro_description);
        addProductbtn = findViewById(R.id.addProductbtn);
        cat_spinner = findViewById(R.id.selectCategory);
        sup_spinner = findViewById(R.id.selectSupplier);
        ivImage = findViewById(R.id.pro_image);
        selectImg_btn = findViewById(R.id.selectImg_btn);
        txtlogin = findViewById(R.id.txtlogin);

    }

    private void addProductToBD() {

        if (inputValidation()) {

            String name = pro_name.getText().toString();
            int code = Integer.parseInt(pro_code.getText().toString());
            double price = Double.parseDouble(pro_price.getText().toString());
            double wholesale = Double.parseDouble(pro_wholesale.getText().toString());
            int unit = Integer.parseInt(pro_unit.getText().toString());
            String desc = pro_description.getText().toString();

            db = FirebaseDatabase.getInstance();
            ref = db.getReference("products");
            String id = ref.push().getKey();

            if (selectedImage != null) {
                storage = FirebaseStorage.getInstance();
                sref = storage.getReference("images/" + id);
                sref.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            sref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        String imageUrl = task.getResult().toString();
                                        Product pro = new Product(id, name, code, price, wholesale, unit, desc, cat_id, sup_id, imageUrl);

                                        Toast.makeText(AddProductActivity.this, "add product successfully", Toast.LENGTH_SHORT).show();

                                        ref.child(id).setValue(pro).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(AddProductActivity.this, "add product successfully", Toast.LENGTH_SHORT).show();
                                                    onBackPressed();
                                                } else {
                                                    String errorMessage = task.getException().getMessage();
                                                    Toast.makeText(AddProductActivity.this, "Fail " + errorMessage, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        } else {

                        }

                    }
                });
            } else {
                Toast.makeText(AddProductActivity.this, "please choice image", Toast.LENGTH_SHORT);
            }
        }
    }

    public void readCategories() {

        cat_list.clear();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("categories");
        Task<DataSnapshot> task = ref.get();
        task.addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    Iterable<DataSnapshot> data = task.getResult().getChildren();
                    cat_items.add("Select Category");
                    for (DataSnapshot snap : data) {
                        Category cat = snap.getValue(Category.class);
                        cat_items.add(cat.getName());
                        cat_list.add(cat);
                        Log.d("d", "" + cat);

                        ArrayAdapter<String> cat_adapter = new ArrayAdapter<String>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, cat_items);
                        cat_spinner.setAdapter(cat_adapter);
                        cat_adapter.notifyDataSetChanged();
                    }
                } else {
                    String errorMessage = task.getException().getMessage();
                    Toast.makeText(AddProductActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void readSuppliers() {

        sup_list.clear();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("suppliers");
        Task<DataSnapshot> task = ref.get();
        task.addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    Iterable<DataSnapshot> data = task.getResult().getChildren();
                    sup_items.add("Select Supplier");
                    for (DataSnapshot snap : data) {
                        Person sup = snap.getValue(Person.class);
                        sup_items.add(sup.getName());
                        sup_list.add(sup);
                        Log.d("d", "" + sup);
                    }
                    if (sup_items != null && sup_items.size() != 0) {
                        ArrayAdapter<String> sup_adapter = new ArrayAdapter<String>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, sup_items);
                        sup_spinner.setAdapter(sup_adapter);
                        sup_adapter.notifyDataSetChanged();
                    }

                } else {
                    String errorMessage = task.getException().getMessage();
                    Toast.makeText(AddProductActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImage = data.getData();
            ivImage.setImageURI(selectedImage);
        }
    }

    public Boolean inputValidation() {
        boolean flag = true;

        if (pro_name.getText().toString().isEmpty()) {
            pro_name.setError("Can't be Empty");
            return false;
        }

        if (pro_code.getText().toString().isEmpty()) {
            pro_code.setError("Can't be Empty");
            return false;
        }

        if (pro_price.getText().toString().isEmpty()) {
            pro_price.setError("Can't be Empty");
            return false;
        }
        if (pro_wholesale.getText().toString().isEmpty()) {
            pro_wholesale.setError("Can't be Empty");
            return false;
        }


        if (pro_unit.getText().toString().isEmpty()) {
            pro_unit.setError("Can't be Empty");
            return false;
        }

        if (pro_description.getText().toString().isEmpty()) {
            pro_description.setError("Can't be Empty");
            return false;
        }

        if (cat_id == null) {
            Toast.makeText(this, "choose category !!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (sup_id == null) {
            Toast.makeText(this, "choose supplier !!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return flag;
    }

}