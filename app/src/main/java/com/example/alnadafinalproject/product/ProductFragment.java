package com.example.alnadafinalproject.product;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import com.example.alnadafinalproject.MainActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Toast;

import com.example.alnadafinalproject.MainActivity;
import com.example.alnadafinalproject.classes.Person;
import com.example.alnadafinalproject.classes.Product;
import com.example.alnadafinalproject.R;
import com.example.alnadafinalproject.classes.Category;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends Fragment {


    RecyclerView rfProduct;
    ArrayList<Product> pro_list = new ArrayList<>() ;
    FloatingActionButton addProduct ;
    ArrayList<Person> sup_list = new ArrayList<>();
    ArrayList<Category> cat_list = new ArrayList<>();

    public ProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_product, container, false);

        View addProduct = v.findViewById(R.id.addproductbtn);

        rfProduct = v.findViewById(R.id.rfProducts);


        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext() , AddProductActivity.class);
                startActivity(intent);
            }
        });

        return  v ;
    }

    @Override
    public void onResume() {
        super.onResume();
        readCategoryFromDB();
        readSupplierFromDB();
        readProductFromDB();
    }


    public void readProductFromDB() {

        pro_list.clear();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("products");
        Task<DataSnapshot> task = ref.get();
        task.addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    Iterable<DataSnapshot> data = task.getResult().getChildren();
                    for (DataSnapshot snap : data) {
                        Product pro = snap.getValue(Product.class);
                        pro_list.add(pro);
                        Log.e("ee" , ""+pro_list);
                    }

                    ProductAdapter adapter = new ProductAdapter(pro_list , sup_list , cat_list, (MainActivity)getActivity());

                    rfProduct.setAdapter(adapter);
                    rfProduct.setLayoutManager(new LinearLayoutManager((MainActivity)getActivity()));

                } else {
                    String errorMessage = task.getException().getMessage();
                    Toast.makeText(getActivity(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void readSupplierFromDB() {

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
                        sup_list.add(sup);
                        Log.e("ee", "" + sup_list);
                    }

                } else {
                    String errorMessage = task.getException().getMessage();
                    Toast.makeText(getContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void readCategoryFromDB () {

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
                        cat_list.add(cat);
                        Log.e("ee", "" + cat_list);
                    }

                } else {
                    String errorMessage = task.getException().getMessage();
                    Toast.makeText(getActivity(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}