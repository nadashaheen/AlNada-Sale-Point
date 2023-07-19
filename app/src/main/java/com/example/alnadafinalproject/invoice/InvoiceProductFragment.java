package com.example.alnadafinalproject.invoice;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.alnadafinalproject.classes.Discount;
import com.example.alnadafinalproject.MainActivity;
import com.example.alnadafinalproject.classes.Product;
import com.example.alnadafinalproject.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class InvoiceProductFragment extends Fragment {

    Spinner cus_spinner;
    int cus_position;
    ArrayList<String> cusNamelist = new ArrayList<>();
    ArrayList<Discount> dis_list = new ArrayList<>();
    private FirebaseDatabase db;
    private DatabaseReference ref;
    Button checkoutbtn;
    Button caldisbtn;
    RecyclerView rfProductsCart;
    TextView total;
    String selectedItem;
    TextInputEditText code_name;

    public InvoiceProductFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_invoice_product, container, false);

        rfProductsCart = v.findViewById(R.id.rfProductsCart);
        total = v.findViewById(R.id.total);
        cus_spinner = v.findViewById(R.id.selectCustomer);
        checkoutbtn = v.findViewById(R.id.checkoutbtn);
        code_name = v.findViewById(R.id.code_name);
        caldisbtn = v.findViewById(R.id.caldisbtn);
        String code = code_name.getText().toString();


        Bundle bundle = getArguments();
        ArrayList<Product> array_pro = bundle.getParcelableArrayList("ProductCart");

        if (bundle != null) {
            Log.e("eee", "onCreateView: " + array_pro);

        }

        InvoiceProductAdapter adapter = new InvoiceProductAdapter(array_pro, (MainActivity) getActivity(), InvoiceProductFragment.this);

        rfProductsCart.setAdapter(adapter);
        rfProductsCart.setLayoutManager(new LinearLayoutManager((MainActivity) getActivity()));

        return v;

    }



}
