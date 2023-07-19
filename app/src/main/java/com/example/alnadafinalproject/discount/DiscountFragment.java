package com.example.alnadafinalproject.discount;

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

import com.example.alnadafinalproject.classes.Discount;
import com.example.alnadafinalproject.MainActivity;
import com.example.alnadafinalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DiscountFragment extends Fragment {

    FloatingActionButton addDiscountbtn ;
    RecyclerView rfDiscount;

    ArrayList<Discount> dis_list = new ArrayList<>();


    public DiscountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_discount, container, false);
        addDiscountbtn = v.findViewById(R.id.addDiscountbtn);

        rfDiscount = v.findViewById(R.id.rfDiscount);

        addDiscountbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext() , AddDiscountActivity.class);
                startActivity(i);
            }
        });
        return v;
    }


    @Override
    public void onResume () {
        super.onResume();
        readDiscountFromDB();
    }

    public void readDiscountFromDB () {

        dis_list.clear();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("discounts");
        Task<DataSnapshot> task = ref.get();
        task.addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    Iterable<DataSnapshot> data = task.getResult().getChildren();
                    for (DataSnapshot snap : data) {
                        Discount dis = snap.getValue(Discount.class);

                        dis_list.add(dis);
                        Log.e("ee", "" + dis_list);
                    }

                    DiscountAdapter adapter = new DiscountAdapter(dis_list , (MainActivity)getActivity());

                    rfDiscount.setAdapter(adapter);
                    rfDiscount.setLayoutManager(new LinearLayoutManager((MainActivity)getActivity()));
//                    Toast.makeText(getActivity(), "" + (MainActivity)getActivity() , Toast.LENGTH_SHORT).show();


                } else {
                    String errorMessage = task.getException().getMessage();
                    Toast.makeText(getActivity(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}