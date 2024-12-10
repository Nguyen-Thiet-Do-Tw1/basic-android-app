package com.example.myapplication.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.ui.Chi.KhoanChiFragment;
import com.example.myapplication.ui.Chi.LoaiChiFragment;
import com.example.myapplication.ui.Thu.LoaiThuFragment;

public class ChiPage2adapter extends FragmentStateAdapter {
    public ChiPage2adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new LoaiThuFragment();
        } else {
            return new LoaiChiFragment();
        }
    }


    @Override
    public int getItemCount() {
        return 2;
    }
}
