package com.example.myapplication.ui.Lich;


import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapter.TransactionAdapter;
import com.example.myapplication.ui.Chi.KhoanChi;
import com.example.myapplication.ui.Thu.KhoanThu;
import com.example.myapplication.ui.database.DatabaseHelper;
import com.example.myapplication.ui.home.Transaction;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class LichFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView textTotalThu, textTotalChi;

    private TransactionAdapter adapter;
    private CalendarView calendarView;
    private String selectedDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        // Kết nối View từ XML
        recyclerView = view.findViewById(R.id.recyclerView_date);
        calendarView = view.findViewById(R.id.calendarView);
        textTotalThu = view.findViewById(R.id.textThu);
        textTotalChi = view.findViewById(R.id.textChi);

        adapter = new TransactionAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Lấy ngày hiện tại và định dạng
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        selectedDate = dateFormat.format(calendar.getTime());

        // Hiển thị dữ liệu cho ngày hiện tại
        filterTransactions();

        // Gán listener cho CalendarView
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            selectedDate = dateFormat.format(calendar.getTime());

            // Cập nhật dữ liệu khi người dùng chọn ngày
            filterTransactions();
        });

        return view;
    }

    private void filterTransactions() {
        if (selectedDate == null) {
            Toast.makeText(requireContext(), "Vui lòng chọn ngày!", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());
        List<Transaction> combinedList = new ArrayList<>();
        double totalThu = databaseHelper.getTotalThuByDateRange(selectedDate, selectedDate);
        double totalChi = databaseHelper.getTotalChiByDateRange(selectedDate, selectedDate);
        double soDu = totalThu - totalChi;

        // Lấy danh sách các khoản chi và chuyển sang Transaction
        List<KhoanChi> khoanChiList = databaseHelper.getKhoanChiByDateRange(selectedDate, selectedDate);
        for (KhoanChi khoanChi : khoanChiList) {
            combinedList.add(new Transaction(khoanChi));
        }

        // Lấy danh sách các khoản thu và chuyển sang Transaction
        List<KhoanThu> khoanThuList = databaseHelper.getKhoanThuByDateRange(selectedDate, selectedDate);
        for (KhoanThu khoanThu : khoanThuList) {
            combinedList.add(new Transaction(khoanThu));
        }

        textTotalThu.setText(String.format("%,.0f", totalThu));
        textTotalThu.setTextColor(Color.parseColor("#FF4CFF00"));
        textTotalChi.setText(String.format("%,.0f", totalChi));
        textTotalChi.setTextColor(Color.parseColor("#FFFF0000"));


        // Cập nhật RecyclerView và giao diện
        adapter.setData(combinedList, databaseHelper);
        adapter.notifyDataSetChanged();
    }
}


