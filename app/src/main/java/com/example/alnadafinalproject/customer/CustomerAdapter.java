package com.example.alnadafinalproject.customer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alnadafinalproject.classes.Person;
import com.example.alnadafinalproject.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerHolder> {
    ArrayList<Person> customer_List;
    CustomerFragment cus ;
    Context context ;

    public CustomerAdapter(ArrayList<Person> customer_List , Context context) {
        this.customer_List = customer_List;
        this.context = context ;
    }


    @NonNull
    @Override
    public CustomerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.activity_recycler_des, parent, false);
        return new CustomerAdapter.CustomerHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerHolder holder, int position) {
        int i = position;

        String customerId = customer_List.get(i).getId();
        String name = customer_List.get(i).getName();
        String call = customer_List.get(i).getCall() ;
        String email = customer_List.get(i).getEmail() ;
        String address = customer_List.get(i).getAddress();

        holder.txtname.setText(name);
        holder.txtphonenumber.setText(call);
        holder.txtemail.setText(email);
        holder.txtaddress.setText(address);

        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCustomer(customerId);
                customer_List.remove(i);
                notifyDataSetChanged();
               }

        });
        holder.edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , UpdateCustomerActivity.class);
               Bundle b = new Bundle();
//                b.putSerializable("customer",customer_List.get(i));
                intent.putExtra("id" , customerId );
                intent.putExtra("name" , name );
                intent.putExtra("call" , call );
                intent.putExtra("email" , email );
                intent.putExtra("address" , address );

                context.startActivity(intent);

            }
        });

    }

    private void deleteCustomer(String customerId) {
        DatabaseReference ref_cus = FirebaseDatabase.getInstance().getReference("customers").child(customerId);
        ref_cus.removeValue();
    }

    @Override
    public int getItemCount() {
        return customer_List.size();
    }

    public class CustomerHolder extends RecyclerView.ViewHolder {
        TextView txtname;
        TextView txtphonenumber;
        TextView txtemail;
        TextView txtaddress;
        ImageButton edit_btn ;
        ImageButton delete_btn;

        public CustomerHolder(@NonNull View itemView) {
            super(itemView);
            txtname = itemView.findViewById(R.id.per_name);
            txtphonenumber = itemView.findViewById(R.id.per_phonenumber);
            txtemail = itemView.findViewById(R.id.per_email);
            txtaddress = itemView.findViewById(R.id.per_address);
            edit_btn = itemView.findViewById(R.id.edit_btn);
            delete_btn = itemView.findViewById(R.id.delete_btn);
        }
    }
}