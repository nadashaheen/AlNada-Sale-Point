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

import com.example.alnadafinalproject.classes.Category;
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


public class PagerCategoryFragment extends Fragment {

    private FirebaseDatabase db;
    private DatabaseReference ref;
    private StorageReference sref;
    private StorageReference refStorage;
    private FirebaseStorage storage;
    final Context c = getContext();
    TextView countCategory;
    ArrayList<Category> cat_list = new ArrayList<>();
    String categoryFile;

    Button downloadCat_btn;

    public PagerCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pager_category, container, false);
        downloadCat_btn = v.findViewById(R.id.downloadCat_btn);
        countCategory = v.findViewById(R.id.countCategory);
        readCategoryFromDB();
        downloadCat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download();
            }
        });
        return v;

    }

    public void download() {
        sref = storage.getInstance().getReference();
        refStorage = sref.child("history/category.txt");

        refStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess (Uri uri){

                String url = uri.toString();
                downloadFile(getContext(), "category", ".txt", DIRECTORY_DOWNLOADS, url);
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

    public void readCategoryFromDB() {

        cat_list.clear();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("categories");
        Task<DataSnapshot> task = ref.get();
        task.addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    Iterable<DataSnapshot> data = task.getResult().getChildren();

                    for (DataSnapshot snap : data) {
                        Category cat = snap.getValue(Category.class);
                        cat_list.add(cat);
                        Log.e("ee", "" + cat_list);
                    }
                    countCategory.setText(cat_list.size() + "");
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    categoryFile = gson.toJson(cat_list);
                    sref = FirebaseStorage.getInstance().getReference().child("history");
                    sref.child("category.txt").putBytes(categoryFile.toString().getBytes()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

//                            Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
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