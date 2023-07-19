package com.example.alnadafinalproject.supplier;

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
import com.example.alnadafinalproject.classes.Person;
import com.example.alnadafinalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SupplierFragment extends Fragment {
    RecyclerView rfSupplier;
    ArrayList<Person> sup_list = new ArrayList<>();
    FloatingActionButton addSupplier;

    public SupplierFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_supplier, container, false);

         addSupplier = v.findViewById(R.id.addsupplierbtn);

        rfSupplier = v.findViewById(R.id.rfSupplier);


        addSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddSupplierActivity.class);
                startActivity(intent);
            }
        });


        return v;
    }
        @Override
        public void onResume () {
            super.onResume();
            readSupplierFromDB();
        }

        public void readSupplierFromDB () {

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

                        SupplierAdapter adapter = new SupplierAdapter(sup_list , (MainActivity) getActivity());

                        rfSupplier.setAdapter(adapter);
                        rfSupplier.setLayoutManager(new LinearLayoutManager((MainActivity) getActivity()));

                    } else {
                        String errorMessage = task.getException().getMessage();
                        Toast.makeText(getActivity(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }