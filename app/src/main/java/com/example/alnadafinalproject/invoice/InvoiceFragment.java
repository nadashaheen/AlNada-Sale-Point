package com.example.alnadafinalproject.invoice;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.alnadafinalproject.classes.Category;
import com.example.alnadafinalproject.MainActivity;
import com.example.alnadafinalproject.classes.Person;
import com.example.alnadafinalproject.classes.Product;
import com.example.alnadafinalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class InvoiceFragment extends Fragment {
    RecyclerView rfInvoice;
    ArrayList<Person> sup_list = new ArrayList<>();
    ArrayList<Category> cat_list = new ArrayList<>();
    ArrayList<Product> pro_list = new ArrayList<>() ;
    ArrayList<Product>  pro_cart  = new ArrayList<>();

    public InvoiceFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_invoice, container, false);

        View cartProduct = v.findViewById(R.id.cart);
        rfInvoice = v.findViewById(R.id.rfInvoice);

        cartProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("ProductCart" , pro_cart);
                InvoiceProductFragment invoiceProductFrag = new InvoiceProductFragment();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                invoiceProductFrag.setArguments(bundle);

                ft.addToBackStack(null);
                ft.replace(R.id.fcv, invoiceProductFrag);
                ft.commit();




            }
        });


        return v ;
    }

    @Override
    public void onResume() {
        super.onResume();
        readSupplierFromDB();
        readCategoryFromDB ();

        readProductFromDB();
    }

    public void readProductFromDB(){

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

                    InvoiceAdapter adapter = new InvoiceAdapter(pro_list , sup_list , cat_list, (MainActivity)getActivity());

                    rfInvoice.setAdapter(adapter);
                    rfInvoice.setLayoutManager(new LinearLayoutManager((MainActivity)getActivity()));
                    pro_cart = adapter.pro_cart ;


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