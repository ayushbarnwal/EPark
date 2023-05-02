package com.example.epark.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.epark.Fragment.UserUnverifiedBookingSlotFragment;
import com.example.epark.Fragment.UserVerifiedBookingSlotFragment;

public class ViewPagerUserBookingHistoryAdapter extends FragmentPagerAdapter {
    String activity;

    public ViewPagerUserBookingHistoryAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public ViewPagerUserBookingHistoryAdapter(@NonNull FragmentManager fm, String activity) {
        super(fm);
        this.activity = activity;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position==0) return new UserUnverifiedBookingSlotFragment();
        else return new UserVerifiedBookingSlotFragment();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0) return "Unverified";
        else return "Verified";
    }

    @Override
    public int getCount() {
        return 2;
    }
}
