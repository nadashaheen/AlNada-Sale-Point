package com.example.alnadafinalproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alnadafinalproject.category.CategoryFragment;
import com.example.alnadafinalproject.customer.CustomerFragment;
import com.example.alnadafinalproject.discount.DiscountFragment;
import com.example.alnadafinalproject.invoice.InvoiceFragment;
import com.example.alnadafinalproject.pager.ReportsActivity;
import com.example.alnadafinalproject.product.ProductFragment;
import com.example.alnadafinalproject.supplier.SupplierFragment;


public class DashBoardFragment extends Fragment {


    public DashBoardFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dash_board, container, false);
        View customer = v.findViewById(R.id.customer_btn);
        View supplier = v.findViewById(R.id.sypplier_btn);
        View product = v.findViewById(R.id.productbtn);
        View invoice = v.findViewById(R.id.invoice_btn);
        View report = v.findViewById(R.id.reportbtn);
        View category = v.findViewById(R.id.category_btn);
        View discount = v.findViewById(R.id.discount_btn);

        DashBoardFragment dashFraq = new DashBoardFragment();




        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CustomerFragment customerFrag = new CustomerFragment();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.fcv, customerFrag);
                ft.commit();
//
//                FileAllData file = new FileAllData();
//                file.readProductFromDB();

//                Log.e("file", "onCreate: "+ file.productFile);
//                Log.e("file", "onCreate: "+ file.pro_list);
            }
        });

        supplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SupplierFragment supplierFrag = new SupplierFragment();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.addToBackStack(null);

                ft.replace(R.id.fcv, supplierFrag);
                ft.commit();
            }
        });


        product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductFragment productFrag = new ProductFragment();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.addToBackStack(null);

                ft.replace(R.id.fcv, productFrag);
                ft.commit();
            }
        });

        invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InvoiceFragment invoiceFrag = new InvoiceFragment();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.addToBackStack(null);

                ft.replace(R.id.fcv, invoiceFrag);
                ft.commit();
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext() , ReportsActivity.class);
                startActivity(intent);
            }
        });

        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CategoryFragment categoryFrag = new CategoryFragment();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.addToBackStack(null);

                ft.replace(R.id.fcv, categoryFrag);
                ft.commit();
            }
        });

        discount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                DiscountFragment discountFrag = new DiscountFragment();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.addToBackStack(null);

                ft.replace(R.id.fcv, discountFrag);
                ft.commit();
            }
        });
        return v;


    }
}