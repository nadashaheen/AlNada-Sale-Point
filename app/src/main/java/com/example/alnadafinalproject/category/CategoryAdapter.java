package com.example.alnadafinalproject.category;

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

import com.example.alnadafinalproject.classes.Category;
import com.example.alnadafinalproject.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {
    ArrayList<Category> categoryList;
    Context context ;
    public CategoryAdapter(ArrayList<Category> categoryList , Context context) {
        this.categoryList = categoryList;
        this.context = context ;
    }


    @NonNull
    @Override
    public CategoryAdapter.CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.activity_recycler_category, parent, false);
        return new CategoryAdapter.CategoryHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        int i = position;

        String categoryId = categoryList.get(i).getId();
        String name = categoryList.get(position).getName();
        holder.txtname.setText(name);

        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCategory(categoryId);
                categoryList.remove(i);
                notifyDataSetChanged();
            }

        });
        holder.edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , UpdateCategoryActivity.class);
                Bundle b = new Bundle();
                intent.putExtra("id" , categoryId );
                intent.putExtra("name" , name );


                context.startActivity(intent);

            }
        });
    }

    private void deleteCategory(String categoryId) {
        DatabaseReference ref_cus = FirebaseDatabase.getInstance().getReference("categories").child(categoryId);
        ref_cus.removeValue();
    }


    @Override
    public int getItemCount() {
        return categoryList.size();
    }


    public class CategoryHolder extends RecyclerView.ViewHolder {
        TextView txtname;
        ImageButton edit_btn ;
        ImageButton delete_btn;
        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            txtname = itemView.findViewById(R.id.catNameDesign);
            edit_btn = itemView.findViewById(R.id.edit_btn);
            delete_btn = itemView.findViewById(R.id.delete_btn);
        }
    }

}

