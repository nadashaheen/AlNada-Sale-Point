package com.example.alnadafinalproject.category;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.alnadafinalproject.MainActivity;
import com.example.alnadafinalproject.R;
import com.example.alnadafinalproject.classes.Category;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;

public class CategoryFragment extends Fragment {

    RecyclerView rfCategory;
    ArrayList<Category> cat_list = new ArrayList<>();
    FloatingActionButton addCategory;



    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_category, container, false);
        View addCategory = v.findViewById(R.id.addcategorytbtn);

        rfCategory = v.findViewById(R.id.rfCategory);


        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext() , AddCategoryActivity.class);
                startActivity(intent);
            }
        });
        return v ;
    }

    @Override
    public void onResume () {
        super.onResume();
        readCategoryFromDB();
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

                    CategoryAdapter adapter = new CategoryAdapter(cat_list , (MainActivity)getActivity());

                    rfCategory.setAdapter(adapter);
                    rfCategory.setLayoutManager(new LinearLayoutManager((MainActivity)getActivity()));
//                    Toast.makeText(getActivity(), "" + (MainActivity)getActivity() , Toast.LENGTH_SHORT).show();


                } else {
                    String errorMessage = task.getException().getMessage();
                    Toast.makeText(getActivity(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



}