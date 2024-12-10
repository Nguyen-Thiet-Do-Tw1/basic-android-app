package com.example.myapplication.ui.home;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.TransactionAdapter;
import com.example.myapplication.ui.Chi.KhoanChi;
import com.example.myapplication.ui.Thu.KhoanThu;
import com.example.myapplication.ui.database.DatabaseHelper;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {
    private TextView ngaybatdau, ngayketthuc , textView6, textView7, textView8;
    private RecyclerView recyclerView;
    private TransactionAdapter adapter;
    private TextView emptyTextView, textTotalThu, textTotalChi, textSoDu;
    private ConstraintLayout summaryLayout;
    private PieChart pieChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Kết nối View từ XML
        ngaybatdau = view.findViewById(R.id.Ngaybatdau);
        ngayketthuc = view.findViewById(R.id.ngayketthuc);
        recyclerView = view.findViewById(R.id.recyclerViewTransactions);
        emptyTextView = view.findViewById(R.id.emptyTextView);
        summaryLayout = view.findViewById(R.id.summaryLayout);
        textTotalThu = view.findViewById(R.id.textTotalThu);
        textTotalChi = view.findViewById(R.id.textTotalChi);
        textSoDu = view.findViewById(R.id.textSoDu);
        textView6 = view.findViewById(R.id.textView6);
        textView7 = view.findViewById(R.id.textView7);
        textView8 = view.findViewById(R.id.textView8);

        pieChart = view.findViewById(R.id.pieChart_Chi);

        adapter = new TransactionAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Thiết lập ngày mặc định là ngày đầu và cuối của tháng
        setDefaultDates();

        ngaybatdau.setOnClickListener(v -> showDatePickerDialog(ngaybatdau));
        ngayketthuc.setOnClickListener(v -> showDatePickerDialog(ngayketthuc));

        // Cập nhật danh sách tự động khi thay đổi ngày
        ngaybatdau.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                filterTransactions(); // Cập nhật khi ngày bắt đầu thay đổi
            }
        });

        ngayketthuc.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                filterTransactions(); // Cập nhật khi ngày kết thúc thay đổi
            }
        });

        return view;
    }

    private void setDefaultDates() {
        Calendar calendar = Calendar.getInstance();

        // Ngày đầu tháng
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String startDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.getTime());
        ngaybatdau.setText(startDate);

        // Ngày cuối tháng
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        String endDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.getTime());
        ngayketthuc.setText(endDate);

        // Cập nhật dữ liệu ngay khi khởi tạo
        filterTransactions();
    }

    private String convertDateFormat(String date) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return outputFormat.format(inputFormat.parse(date));
        } catch (Exception e) {
            e.printStackTrace();
            return date;
        }
    }

    private void filterTransactions() {
        String startDate = convertDateFormat(ngaybatdau.getText().toString());
        String endDate = convertDateFormat(ngayketthuc.getText().toString());

        if (startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng chọn ngày bắt đầu và kết thúc!", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());
        double totalThu = databaseHelper.getTotalThuByDateRange(startDate, endDate);
        double totalChi = databaseHelper.getTotalChiByDateRange(startDate, endDate);
        double soDu = totalThu - totalChi;

        List<Transaction> combinedList = new ArrayList<>();

        // Lấy danh sách các khoản chi và chuyển sang Transaction
        List<KhoanChi> khoanChiList = databaseHelper.getKhoanChiByDateRange(startDate, endDate);
        for (KhoanChi khoanChi : khoanChiList) {
            combinedList.add(new Transaction(khoanChi));
        }

        // Lấy danh sách các khoản thu và chuyển sang Transaction
        List<KhoanThu> khoanThuList = databaseHelper.getKhoanThuByDateRange(startDate, endDate);
        for (KhoanThu khoanThu : khoanThuList) {
            combinedList.add(new Transaction(khoanThu));
        }

        // Sắp xếp danh sách theo ngày
        Collections.sort(combinedList, (t1, t2) -> t1.getNgay().compareTo(t2.getNgay()));

        if (combinedList.isEmpty()) {
            // Không có khoản thu chi
            recyclerView.setVisibility(View.GONE);
            summaryLayout.setVisibility(View.GONE);
            textSoDu.setVisibility(View.GONE);
            textTotalChi.setVisibility(View.GONE);
            textTotalThu.setVisibility(View.GONE);
            textView6.setVisibility(View.GONE);
            textView7.setVisibility(View.GONE);
            textView8.setVisibility(View.GONE);

            emptyTextView.setVisibility(View.VISIBLE);
            emptyTextView.setText("Không có thu chi phát sinh!");
            pieChart.setVisibility(View.GONE); // Ẩn biểu đồ khi không có dữ liệu
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            summaryLayout.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.GONE);
            pieChart.setVisibility(View.VISIBLE); // Hiển thị biểu đồ nếu có dữ liệu

            adapter.setData(combinedList, databaseHelper);
            adapter.notifyDataSetChanged();

            updatePieChart(totalThu, totalChi); // Cập nhật biểu đồ nếu có dữ liệu
            textView6.setVisibility(View.VISIBLE);
            textView7.setVisibility(View.VISIBLE);
            textView8.setVisibility(View.VISIBLE);
            textTotalThu.setVisibility(View.VISIBLE);
            textTotalChi.setVisibility(View.VISIBLE);
            textSoDu.setVisibility(View.VISIBLE);
            textTotalThu.setText(String.format("%,.0f", totalThu));
            textTotalThu.setTextColor(Color.parseColor("#FF4CFF00"));
            textTotalChi.setText(String.format("%,.0f", totalChi));
            textTotalChi.setTextColor(Color.parseColor("#FFFF0000"));
            textSoDu.setText(String.format("%,.0f", soDu));
            textSoDu.setTextColor(Color.parseColor("#FFF6E33F"));

        }
    }



    private void updatePieChart(double income, double expense) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry((float) income, "Tổng Thu"));
        entries.add(new PieEntry((float) expense, "Tổng Chi"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(new int[]{Color.parseColor("#00FF00"), Color.parseColor("#FF0000")}); // Xanh lá và đỏ
//        dataSet.setValueTextColor(Color.TRANSPARENT); // Ẩn giá trị trên biểu đồ
        dataSet.setValueTextSize(12f);

        dataSet.setSliceSpace(3f); // Khoảng cách giữa các lát cắt

        PieData data = new PieData(dataSet);
        pieChart.setData(data);

        // Hiển thị số dư ở giữa biểu đồ
//        float balance = (float) (income - expense);
//        pieChart.setCenterText("Số Dư\n" + String.format("%,.0f", balance) +"\nVND"); // Hiển thị số dư
//        pieChart.setCenterTextSize(16f); // Kích thước chữ ở giữa
//        pieChart.setCenterTextColor(Color.BLACK); // Màu chữ ở giữa

        // Cấu hình thêm cho PieChart
        pieChart.setDrawHoleEnabled(false); // Lỗ ở giữa

//        pieChart.setHoleRadius(80f); // Kích thước lỗ
//        pieChart.setTransparentCircleRadius(85f); // Kích thước vòng tròn trong suốt
        pieChart.setDrawEntryLabels(false); // Không hiển thị nhãn
        pieChart.setRotationEnabled(false); // Tắt xoay biểu đồ
        pieChart.getLegend().setEnabled(true); // Ẩn chú thích
        pieChart.setDescription(null); // Xóa mô tả

        // Thêm văn bản thu chi bên trái và bên phải
//        pieChart.setRenderer(new CustomPieChartRenderer(pieChart, income, expense));

        // Làm mới biểu đồ
        pieChart.invalidate();

        if (income == 0 && expense == 0) {
            pieChart.clear(); // Xóa biểu đồ nếu không có dữ liệu
            pieChart.invalidate(); // Làm mới biểu đồ
            return;
        }
    }



    private void showDatePickerDialog(TextView textView) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    textView.setText(selectedDate);
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}
