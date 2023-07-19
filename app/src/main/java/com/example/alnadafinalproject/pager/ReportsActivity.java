package com.example.alnadafinalproject.pager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.alnadafinalproject.R;
import com.example.alnadafinalproject.pager.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ReportsActivity extends AppCompatActivity {
    ViewPagerAdapter viewPagerAdapter ;
    TabLayout tabs ;
    ViewPager2 viewPager ;
    Context context ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        tabs = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        String[] tilteArray  = getApplicationContext().getResources().getStringArray(R.array.title) ;

        Log.e("array", "onCreate: "+tilteArray );
        new TabLayoutMediator(tabs , viewPager,((tab, position) -> tab.setText(tilteArray[position]))).attach();


    }
}