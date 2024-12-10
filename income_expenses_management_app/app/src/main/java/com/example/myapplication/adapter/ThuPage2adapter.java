package com.example.myapplication.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.ui.Chi.KhoanChiFragment;
import com.example.myapplication.ui.Chi.LoaiChiFragment;
import com.example.myapplication.ui.Thu.KhoanThuFragment;
import com.example.myapplication.ui.Thu.LoaiThuFragment;

public class ThuPage2adapter extends FragmentStateAdapter {

    public ThuPage2adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        if (position == 0) {
            return new KhoanThuFragment();
        } else {
            return new KhoanChiFragment();
        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
