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

import com.bumptech.glide.Glide;
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
import java.util.UUID;

public class UpdateProductActivity extends AppCompatActivity {
    private TextInputEditText pro_name, pro_code, pro_price, pro_unit, pro_description;
    Intent intent;
    String id;
    String cat_id, sup_id;
    String cat_name, sup_name;
    ArrayList<String> cat_items = new ArrayList<>();
    ArrayList<String> sup_items = new ArrayList<>();
Double wholesale ;
    private FirebaseDatabase db;
    private DatabaseReference ref;
    private StorageReference sref;
    private FirebaseStorage storage;

    public static final int PICK_IMAGE = 1021;

    ArrayList<Product> pro_list = new ArrayList<>();
    ArrayList<Person> sup_list = new ArrayList<>();
    ArrayList<Category> cat_list = new ArrayList<>();
    ArrayList<Person> cus_list = new ArrayList<>();
    String productFile;
    Button updateProductbtn;
    Spinner cat_spinner;
    Spinner sup_spinner;
    ImageView ivImage;
    Uri selectedImage;
    String imageUrl;
    FloatingActionButton selectImg_btn;
    TextView txtlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update_product);
        txtlogin = findViewById(R.id.txtlogin);

        Typeface typeface = getResources().getFont(R.font.bungee);
        txtlogin.setTypeface(typeface);
        init();

        Bundle b = getIntent().getExtras();

        if (b != null) {
            Object cus = b.getSerializable("product");

            id = b.getString("id");
            pro_name.setText(b.getString("name"));
            pro_code.setText(b.getString("code"));
            pro_price.setText(b.getString("price"));
            wholesale = Double.parseDouble(b.getString("wholesale"));
            pro_unit.setText(b.getString("unit"));
            pro_description.setText(b.getString("description"));
            cat_id = b.getString("cat_id");
            sup_id = b.getString("sup_id");
            sup_name = b.getString("sup_name");
            cat_name = b.getString("cat_name");
            imageUrl = b.getString("image_product");
            Log.e("eeee", sup_name);
            Log.e("eeee", cat_name);
            Glide.with(getApplicationContext()).load(b.getString("image_product")).into(ivImage);
        } else {
            pro_name.setText("");
            pro_code.setText("");
            pro_price.setText("");
            pro_unit.setText("");
            pro_description.setText("");
        }
        updateProductbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputValidation()) {
                    UpdateProduct();
                    finish();

                }
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



    public boolean UpdateProduct() {
        DatabaseReference refCus = FirebaseDatabase.getInstance().getReference("products").child(id);

        String name = pro_name.getText().toString();
        int code = Integer.parseInt(pro_code.getText().toString());
        double price = Double.parseDouble(pro_price.getText().toString());
        int unit = Integer.parseInt(pro_unit.getText().toString());
        String description = pro_description.getText().toString();


        db = FirebaseDatabase.getInstance();
        ref = db.getReference("products");
        storage = FirebaseStorage.getInstance();
        if (selectedImage != null) {
            sref = storage.getReference("images/" + UUID.randomUUID().toString());
            sref.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        sref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {

                                if (task.isSuccessful()) {
                                    String imageUr = task.getResult().toString();

                                    Product pro = new Product(id, name, code, price, wholesale, unit, description, cat_id, sup_id, imageUr);
                                    refCus.setValue(pro);

//                                Toast.makeText(this, "Updated Successfully ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });
        } else {
            Product pro = new Product(id, name, code, price, wholesale, unit, description, cat_id, sup_id, imageUrl);
            refCus.setValue(pro);
        }

        return true;

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
                    for (DataSnapshot snap : data) {
                        Category cat = snap.getValue(Category.class);
                        cat_items.add(cat.getName());
                        cat_list.add(cat);
                        Log.d("d", "" + cat);

                        if (cat_items != null && cat_items.size() != 0) {
                            ArrayAdapter<String> cat_adapter = new ArrayAdapter<String>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, cat_items);
                            cat_spinner.setAdapter(cat_adapter);
                            cat_adapter.notifyDataSetChanged();
                        }

                        for (int i = 0; i < cat_list.size(); i++) {
                            Log.e("eeee", cat_spinner.getItemAtPosition(i) + "");
                            if (cat_spinner.getItemAtPosition(i).toString().equals(cat_name)) {
                                cat_spinner.setSelection(i);
                                break;
                            }
                        }


                    }
                } else {
                    String errorMessage = task.getException().getMessage();
                    Toast.makeText(UpdateProductActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
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

                    for (int i = 0; i < sup_list.size(); i++) {
                        Log.e("eeee", sup_spinner.getItemAtPosition(i) + "");
                        if (sup_spinner.getItemAtPosition(i).toString().equals(sup_name)) {
                            sup_spinner.setSelection(i);
                            break;
                        }
                    }

                } else {
                    String errorMessage = task.getException().getMessage();
                    Toast.makeText(UpdateProductActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void init() {
        pro_name = findViewById(R.id.pro_update_name);
        pro_code = findViewById(R.id.pro_update_code);
        pro_price = findViewById(R.id.pro_update_price);
        pro_unit = findViewById(R.id.pro_update_unit);
        pro_description = findViewById(R.id.pro_update_description);
        updateProductbtn = findViewById(R.id.updateProductbtn);
        cat_spinner = findViewById(R.id.select_updateCategory);
        sup_spinner = findViewById(R.id.select_updateSupplier);
        ivImage = findViewById(R.id.pro_update_image);
        selectImg_btn = findViewById(R.id.selectImg_btn);

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImage = data.getData();
            ivImage.setImageURI(selectedImage);
        }
    }



}