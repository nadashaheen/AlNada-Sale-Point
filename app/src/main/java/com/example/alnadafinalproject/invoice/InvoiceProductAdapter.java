package com.example.alnadafinalproject.invoice;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
//import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.alnadafinalproject.classes.Discount;
import com.example.alnadafinalproject.classes.Invoice;
import com.example.alnadafinalproject.classes.Person;
import com.example.alnadafinalproject.classes.Product;
import com.example.alnadafinalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class InvoiceProductAdapter extends RecyclerView.Adapter<InvoiceProductAdapter.ProductHolder> {
    ArrayList<Product> product_List;
    Invoice invoice;
    Context context;
    InvoiceProductFragment ipv;
    String cat_name = "";
    double total;
    ArrayList<Discount> dis_list = new ArrayList<>();
    ArrayList<String> cusNamelist = new ArrayList<>();
    private FirebaseDatabase db;
    private DatabaseReference ref;


    public InvoiceProductAdapter(ArrayList<Product> product_List, Context context, InvoiceProductFragment ipv) {
        this.product_List = product_List;
        this.ipv = ipv;
        this.context = context;
        readDiscountFromDB();
        readCustomerNames();
    }

    @NonNull
    @Override
    public InvoiceProductAdapter.ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.activity_product_cartdes, parent, false);

        ipv.cus_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ipv.selectedItem = parent.getItemAtPosition(position).toString();
                ipv.cus_position = position;
                Log.e("e", "yessssssssssss" + "  " + ipv.selectedItem);
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return new InvoiceProductAdapter.ProductHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceProductAdapter.ProductHolder holder, int position) {
        int i = position;

        String productId = product_List.get(position).getId();
        String name = product_List.get(position).getName();
        String code = product_List.get(position).getCode() + "";
        String price = product_List.get(position).getPrice() + "";


        String image_product = product_List.get(position).getProImg();


        holder.product_name.setText(name);
        holder.product_code.setText(code);
        holder.product_price.setText(price);
        product_List.get(i).setUnit(1);
        holder.unit_product.setText(product_List.get(i).getUnit() + "");

        if (!product_List.get(position).getProImg().isEmpty()) {
            Glide.with(holder.itemView).load(image_product).into(holder.product_img);
        }
        UpdateTotal();
        holder.decreasNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product_List.get(i).getUnit() != 1) {
                    product_List.get(i).setUnit(product_List.get(i).getUnit() - 1);
                    holder.unit_product.setText(product_List.get(i).getUnit() + "");
                    UpdateTotal();

                } else {
                    Toast.makeText(context, "Unit Can not be less than 1", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.increasNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product_List.get(i).setUnit(product_List.get(i).getUnit() + 1);
                holder.unit_product.setText(product_List.get(i).getUnit() + "");
                UpdateTotal();
            }
        });

        holder.delete_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product_List.remove(product_List.get(i));
                notifyDataSetChanged();
            }
        });

        ipv.caldisbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("tag" , dis_list.size()+"");
                for (Discount dis : dis_list) {
                    Log.e("tag" , dis_list.size()+"");
                    Log.e("tag" , dis.getCode()+"");
                    Log.e("tag" , code+"");
                    if (ipv.code_name.getText().toString().equalsIgnoreCase(dis.getCode())) {
                        double to = Double.parseDouble(ipv.total.getText() + "");
                        to -= to * (Double.parseDouble(dis.getPercent()) / 100);
                        ipv.total.setText(to + "");
                        ipv.caldisbtn.setEnabled(false);
                        Drawable img = ipv.caldisbtn.getContext().getResources().getDrawable( R.drawable.accept );
                        ipv.caldisbtn.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
                        ipv.caldisbtn.setTextSize(20);
                        ipv.caldisbtn.setText("%"+dis.getPercent());
                        return;
//                        total -= total*(Double.parseDouble(dis.getPercent())/100);
                    } else if (dis_list.size() < dis_list.size() - 1) {
                        Toast.makeText(ipv.getContext(), "there is no code like this !!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        ipv.checkoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ipv.cus_position > 0) {
                    Invoice inv = new Invoice(ipv.selectedItem, Double.parseDouble(ipv.total.getText() + ""), product_List);
                    addInvoiceToDB(inv);

                } else {
                    Toast.makeText(ipv.getContext(), "choice customer !!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Log.e("tt", product_List.get(i).getUnit() * product_List.get(position).getPrice() + "");
        total += product_List.get(i).getUnit() * product_List.get(position).getPrice();
        Log.e("tt", total + "");

    }

    private void UpdateTotal() {
        double sum = 0;
        for (int i = 0; i < product_List.size(); i++) {
            Log.e("tt", sum + "sum");
            sum += product_List.get(i).getUnit() * product_List.get(i).getPrice();
        }
        ipv.total.setText(sum + "");
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
        TextView unit_product;
        Button delete_product;
        Button increasNumber;
        Button decreasNumber;


        public ProductHolder(@NonNull View itemView) {
            super(itemView);

            product_img = itemView.findViewById(R.id.product_img);
            product_name = itemView.findViewById(R.id.product_name);
            product_code = itemView.findViewById(R.id.product_code);
            product_price = itemView.findViewById(R.id.product_price);
            unit_product = itemView.findViewById(R.id.unit_product);
            delete_product = itemView.findViewById(R.id.delete_product);
            increasNumber = itemView.findViewById(R.id.increasNumber);
            decreasNumber = itemView.findViewById(R.id.decreasNumber);

        }


    }

    public void readDiscountFromDB() {

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

                } else {
                    String errorMessage = task.getException().getMessage();
                    Toast.makeText(ipv.getActivity(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void readCustomerNames() {

        cusNamelist.clear();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("customers");
        Task<DataSnapshot> task = ref.get();
        task.addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    Iterable<DataSnapshot> data = task.getResult().getChildren();
                    cusNamelist.add("Select Customer");
                    for (DataSnapshot snap : data) {
                        Person sup = snap.getValue(Person.class);
                        cusNamelist.add(sup.getName());
                        Log.d("d", "" + sup);
                    }
                    if (cusNamelist != null && cusNamelist.size() != 0) {
                        ArrayAdapter<String> sup_adapter = new ArrayAdapter<String>(ipv.getActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, cusNamelist);
                        ipv.cus_spinner.setAdapter(sup_adapter);
                        sup_adapter.notifyDataSetChanged();
                    }

                } else {
                    String errorMessage = task.getException().getMessage();
                    Toast.makeText(ipv.getActivity(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addInvoiceToDB(Invoice inv) {
        db = FirebaseDatabase.getInstance();
        ref = db.getReference("invoices");
        String id = ref.push().getKey();

        ref.child(id).setValue(inv).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ipv.getContext(), "checked out successfully", Toast.LENGTH_SHORT).show();
                    ipv.getFragmentManager().popBackStackImmediate();
                } else {
                    String errorMessage = task.getException().getMessage();
                    Toast.makeText(ipv.getContext(), "Fail " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}

