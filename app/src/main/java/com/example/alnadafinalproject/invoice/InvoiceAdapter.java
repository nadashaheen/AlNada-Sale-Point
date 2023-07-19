package com.example.alnadafinalproject.invoice;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.alnadafinalproject.classes.Category;
import com.example.alnadafinalproject.classes.Person;
import com.example.alnadafinalproject.classes.Product;
import com.example.alnadafinalproject.R;

import java.util.ArrayList;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.ProductHolder> {
    ArrayList<Product> product_List;
    Context context;
    ArrayList<Person> sup_list ;
    ArrayList<Category> cat_list ;
    ArrayList<Product>  pro_cart  = new ArrayList<>();

    String sup_name = "";
    String cat_name = "";

    public InvoiceAdapter(ArrayList<Product> product_List, ArrayList<Person> sup_list , ArrayList<Category> cat_list , Context context) {
        this.product_List = product_List;
        this.sup_list = sup_list;
        this.cat_list = cat_list;
        this.context = context;
    }


    @NonNull
    @Override
    public InvoiceAdapter.ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.activity_invoice_design, parent, false);
        return new InvoiceAdapter.ProductHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceAdapter.ProductHolder holder, int position) {
        int i = position;

        String productId = product_List.get(position).getId();
        String name = product_List.get(position).getName();
        String code = product_List.get(position).getCode() + "";
        String price = product_List.get(position).getPrice() + "";
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

        holder.addToCartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pro_cart.add(product_List.get(i));
                Log.e("TAG", "onClick: " + pro_cart);
                holder.addToCartbtn.setEnabled(false);

                Drawable img = holder.addToCartbtn.getContext().getResources().getDrawable( R.drawable.accept );
                holder.addToCartbtn.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
                holder.addToCartbtn.setText("Added");
            }
        });
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
        Button addToCartbtn;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);

            product_img = itemView.findViewById(R.id.product_img);
            product_name = itemView.findViewById(R.id.product_name);
            product_code = itemView.findViewById(R.id.product_code);
            product_price = itemView.findViewById(R.id.product_price);
            product_unit = itemView.findViewById(R.id.product_unit);
            product_cat = itemView.findViewById(R.id.product_cat);
            product_supplier = itemView.findViewById(R.id.product_supplier);
            addToCartbtn = itemView.findViewById(R.id.addToCartbtn);
        }


    }


}

