package com.example.alnadafinalproject.pager;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.app.DownloadManager;
import android.content.Context;
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

import com.example.alnadafinalproject.classes.Person;
import com.example.alnadafinalproject.R;
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


public class PagerCustomerFragment extends Fragment {

    private FirebaseDatabase db;
    private DatabaseReference ref;
    private StorageReference sref;
    private StorageReference refStorage;
    private FirebaseStorage storage;
    final Context c = getContext();
    TextView countCustomer;
    ArrayList<Person> cus_list = new ArrayList<>();
    String customerFile;

    Button downloadCus_btn;


    public PagerCustomerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pager_customer, container, false);
        downloadCus_btn = v.findViewById(R.id.downloadCus_btn);
        countCustomer = v.findViewById(R.id.countCustomer);
        readCustomerFromDB();
        downloadCus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download();
            }
        });
        return v;
    }

    public void download() {
        sref = storage.getInstance().getReference();
        refStorage = sref.child("history/customer.txt");

        refStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {


            @Override
            public void onSuccess(Uri uri) {

                String url = uri.toString();
                downloadFile(getContext(), "customer", ".txt", DIRECTORY_DOWNLOADS, url);
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

    public void readCustomerFromDB() {

        cus_list.clear();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("customers");
        Task<DataSnapshot> task = ref.get();
        task.addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    Iterable<DataSnapshot> data = task.getResult().getChildren();

                    for (DataSnapshot snap : data) {
                        Person cus = snap.getValue(Person.class);
                        cus_list.add(cus);
                        Log.e("ee", "" + cus_list);
                    }
                    countCustomer.setText(cus_list.size() + "");
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    customerFile = gson.toJson(cus_list);
//                    customerFile = new Gson().toJson(CustomerAdapter.class).toString();

                    sref = FirebaseStorage.getInstance().getReference().child("history");
                    sref.child("customer.txt").putBytes(customerFile.toString().getBytes()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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