package com.example.alnadafinalproject.product;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.alnadafinalproject.classes.Person;
import com.example.alnadafinalproject.classes.Product;
import com.example.alnadafinalproject.R;
import com.example.alnadafinalproject.classes.Category;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {
    ArrayList<Product> product_List;
    Context context;
    ArrayList<Person> sup_list ;
    ArrayList<Category> cat_list ;
    String sup_name = "";
    String cat_name = "";

    public ProductAdapter(ArrayList<Product> product_List,ArrayList<Person> sup_list ,ArrayList<Category> cat_list ,  Context context) {
        this.product_List = product_List;
        this.sup_list = sup_list;
        this.cat_list = cat_list;
        this.context = context;
    }


    @NonNull
    @Override
    public ProductAdapter.ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.activity_product_design, parent, false);
        return new ProductAdapter.ProductHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductHolder holder, int position) {
        int i = position;

        String productId = product_List.get(position).getId();
        String name = product_List.get(position).getName();
        String code = product_List.get(position).getCode() + "";
        String price = product_List.get(position).getPrice() + "";
        String wholesale = product_List.get(position).getWholeSale() + "";
        String unit = product_List.get(position).getUnit() + "";
        String description = product_List.get(position).getDescription();
        String cat_id = product_List.get(position).getCat_id();
        String sup_id = product_List.get(position).getSup_id();

        String image_product = product_List.get(position).getProImg();

        for (Person supp : sup_list){
            if (supp.getId().equals(sup_id)){
                holder.product_supplier.setText(supp.getName());
                sup_name =supp.getName();
            }
        }

        for (Category catt : cat_list){
            if (catt.getId().equals(cat_id)){
                holder.product_cat.setText(catt.getName());
                cat_name = catt.getName();
            }
        }

        holder.product_name.setText(name);
        holder.product_code.setText(code);
        holder.product_price.setText(price);
        holder.product_unit.setText(unit);

        if (!product_List.get(position).getProImg().isEmpty()) {
            Glide.with(holder.itemView).load(image_product).into(holder.product_img);
        }
        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct(productId);
                product_List.remove(i);
                notifyDataSetChanged();
            }
        });

        holder.edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateProductActivity.class);
                Bundle bundle = new Bundle();
                intent.putExtra("id", productId);
                intent.putExtra("name", name);
                intent.putExtra("code", code);
                intent.putExtra("price", price);
                intent.putExtra("wholesale", wholesale);
                intent.putExtra("unit", unit);
                intent.putExtra("description", description);
                intent.putExtra("cat_id", cat_id);
                intent.putExtra("sup_id", sup_id);
                intent.putExtra("sup_name", sup_name);
                intent.putExtra("cat_name", cat_name);
                intent.putExtra("image_product", image_product);

                context.startActivity(intent);

            }
        });

    }

    private void deleteProduct(String productId) {
        DatabaseReference ref_cus = FirebaseDatabase.getInstance().getReference("products").child(productId);
        ref_cus.removeValue();
    }

    @Override
    public int getItemCount() {
        return product_List.size();
    }

    public class ProductHolder extends RecyclerView.ViewHolder {
        ImageView product_img;
        TextView product_name;
        TextView product_code;
        TextView product_price;
        TextView product_unit;
        TextView product_cat;
        TextView product_supplier;
        ImageButton edit_btn;
        ImageButton delete_btn;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);

            product_img = itemView.findViewById(R.id.product_img);
            product_name = itemView.findViewById(R.id.product_name);
            product_code = itemView.findViewById(R.id.product_code);
            product_price = itemView.findViewById(R.id.product_price);
            product_unit = itemView.findViewById(R.id.product_unit);
            product_cat = itemView.findViewById(R.id.product_cat);
            product_supplier = itemView.findViewById(R.id.product_supplier);
            edit_btn = itemView.findViewById(R.id.edit_btn);
            delete_btn = itemView.findViewById(R.id.delete_btn);
        }


    }


}

