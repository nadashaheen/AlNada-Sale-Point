package com.example.alnadafinalproject.pager;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alnadafinalproject.classes.Product;
import com.example.alnadafinalproject.R;
import com.example.alnadafinalproject.classes.Invoice;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class PagerInvoiceFragment extends Fragment {

    private FirebaseDatabase db;
    private DatabaseReference ref;
    private StorageReference sref;
    private StorageReference refStorage;
    private FirebaseStorage storage;
    final Context c = getContext();
    ArrayList<Invoice> inv_list = new ArrayList<>();
    String invoiceFile;

    Button downloadInv_btn;


    private TextView TotalProfit, countInvoice;
    double profit;
    double total_wholesale;
    double total_price;



    public PagerInvoiceFragment() {
        readWholeFromDB();
        readInvoiceFromDB();
        readPriceFromDB();
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pager_invoice, container, false);

        countInvoice = v.findViewById(R.id.countInvoice);
        TotalProfit = v.findViewById(R.id.TotalProfit);
        downloadInv_btn = v.findViewById(R.id.downloadInv_btn);


        downloadInv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download();
            }
        });
        return v;
    }

    public void readWholeFromDB() {

        total_wholesale = 0;

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("products");
        Task<DataSnapshot> taskk = ref.get();
        taskk.addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (taskk.isSuccessful()) {
                    Iterable<DataSnapshot> data = taskk.getResult().getChildren();
                    for (DataSnapshot snap : data) {
                        Product pro = snap.getValue(Product.class);
                        total_wholesale += pro.getWholeSale();
                    }
                } else {
                    String errorMessage = taskk.getException().getMessage();
                    Toast.makeText(getActivity(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void readPriceFromDB() {

        total_price = 0;

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("invoices");
        Task<DataSnapshot> task = ref.get();

        task.addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    Iterable<DataSnapshot> data = task.getResult().getChildren();
                    int counter = 0;
                    for (DataSnapshot snap : data) {
                        Invoice inv = snap.getValue(Invoice.class);
                        total_price += inv.getTotal();
                        counter++;
                    }
                    countInvoice.setText(counter + " ");

                    Log.e("tt", total_price + "total_price");
                    Log.e("tt", total_wholesale + "total_wholesale");
                    profit = total_price - total_wholesale;
                    if (profit > 0) {
                        TotalProfit.setTextColor(Color.GREEN);
                    } else {
                        TotalProfit.setTextColor(Color.RED);
                    }
                    TotalProfit.setText(profit + "");

                } else {
                    String errorMessage = task.getException().getMessage();
                    Toast.makeText(getActivity(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void download() {
        sref = storage.getInstance().getReference();
        refStorage = sref.child("history/invoice.txt");

        refStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {


            @Override
            public void onSuccess(Uri uri) {

                String url = uri.toString();
                downloadFile(getContext(), "invoice", ".txt", DIRECTORY_DOWNLOADS, url);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void downloadFile(Context context, String fileName, String fileEx, String Directory, String url) {
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setDestinationInExternalFilesDir(context, Directory, fileName + fileEx);
        manager.enqueue(request);

    }

    public void readInvoiceFromDB() {

        inv_list.clear();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("invoices");
        Task<DataSnapshot> task = ref.get();
        task.addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    Iterable<DataSnapshot> data = task.getResult().getChildren();

                    for (DataSnapshot snap : data) {
                        Invoice inv = snap.getValue(Invoice.class);
                        inv_list.add(inv);
                        Log.e("ee", "" + inv_list);
                    }
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();

                    invoiceFile = gson.toJson(inv_list);
                    sref = FirebaseStorage.getInstance().getReference().child("history");
                    sref.child("invoice.txt").putBytes(invoiceFile.toString().getBytes()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                            String errorMessage = task.getException().getMessage();
                            Log.e("error", "onFailure: " + errorMessage);
                        }
                    });
                } else {
                    String errorMessage = task.getException().getMessage();
//                    Toast.makeText(getActivity(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

}