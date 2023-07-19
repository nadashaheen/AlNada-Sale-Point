package com.example.alnadafinalproject.supplier;

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
import com.example.alnadafinalproject.product.UpdateProductActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SupplierAdapter extends RecyclerView.Adapter<SupplierAdapter.SupplierHolder> {
    ArrayList<Person> supplier_List;
    Context context;

    public SupplierAdapter(ArrayList<Person> supplier_List, Context context) {
        this.supplier_List = supplier_List;
        this.context = context;
    }

    @NonNull
    @Override
    public SupplierHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.activity_recycler_des, parent, false);
        return new SupplierAdapter.SupplierHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SupplierAdapter.SupplierHolder holder, int position) {
        int i = position;
        String supplierId = supplier_List.get(position).getId();
        String name = supplier_List.get(position).getName();
        String call = supplier_List.get(position).getCall();
        String email = supplier_List.get(position).getEmail();
        String address = supplier_List.get(position).getAddress();

        holder.txtname.setText(name);
        holder.txtphonenumber.setText(call);
        holder.txtemail.setText(email);
        holder.txtaddress.setText(address);
        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSupplier(supplierId);
                supplier_List.remove(i);
                notifyDataSetChanged();
            }
        });

        holder.edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateSupplierActivity.class);
                Bundle bundle = new Bundle();
                intent.putExtra("id", supplierId);
                intent.putExtra("name" , name );
                intent.putExtra("call" , call );
                intent.putExtra("email" , email );
                intent.putExtra("address" , address );

                context.startActivity(intent);

            }
        });

    }

    private void deleteSupplier(String supplierId) {
        DatabaseReference ref_cus = FirebaseDatabase.getInstance().getReference("suppliers").child(supplierId);
        ref_cus.removeValue();
    }

    @Override
    public int getItemCount() {
        return supplier_List.size();
    }

    public class SupplierHolder extends RecyclerView.ViewHolder {
        TextView txtname;
        TextView txtphonenumber;
        TextView txtemail;
        TextView txtaddress;
        ImageButton edit_btn;
        ImageButton delete_btn;

        public SupplierHolder(@NonNull View itemView) {
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
