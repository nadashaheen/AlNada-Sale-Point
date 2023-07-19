package com.example.alnadafinalproject.pager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private String[] title= new String[]{"Category" , "Customer" , "Supplier" , "Invoice"};
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new PagerCategoryFragment();
            case 1 :
                return new PagerCustomerFragment();
            case 2 :
                return new PagerSupplierFragment();
            case 3 :
                return new PagerInvoiceFragment();
            default:
                return new PagerCategoryFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
