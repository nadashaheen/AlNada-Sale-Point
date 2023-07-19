package com.example.alnadafinalproject.discount;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alnadafinalproject.classes.Discount;
import com.example.alnadafinalproject.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.DiscountHolder> {

    ArrayList<Discount> discountList;
    Context context;

    public DiscountAdapter(ArrayList<Discount> discountList, Context context) {
        this.discountList = discountList;
        this.context = context;
    }

    @NonNull
    @Override
    public DiscountAdapter.DiscountHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.activity_recycler_discount, parent, false);
        return new DiscountAdapter.DiscountHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscountAdapter.DiscountHolder holder, int position) {
        int i = position;

        String DiscountId = discountList.get(i).getId();
        String code = discountList.get(position).getCode();
        String percent = discountList.get(position).getPercent();
        holder.dis_code.setText(code);
        holder.dis_percent.setText(percent);

        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDiscount(DiscountId);
                discountList.remove(i);
                notifyDataSetChanged();
            }
        });

    }

    private void deleteDiscount(String discountId) {
        DatabaseReference ref_cus = FirebaseDatabase.getInstance().getReference("discounts").child(discountId);
        ref_cus.removeValue();
    }


    @Override
    public int getItemCount() {
        return discountList.size();
    }


    public class DiscountHolder extends RecyclerView.ViewHolder {
        TextView dis_code;
        TextView dis_percent;
        ImageButton delete_btn;

        public DiscountHolder(@NonNull View itemView) {
            super(itemView);
            dis_code = itemView.findViewById(R.id.dis_code);
            dis_percent = itemView.findViewById(R.id.dis_percent);
            delete_btn = itemView.findViewById(R.id.delete_btn);
        }
    }

}
