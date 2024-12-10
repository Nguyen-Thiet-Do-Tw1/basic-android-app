package com.example.myapplication.ui.BaoCao;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.ExpenseIncomeAdapter;
import com.example.myapplication.adapter.LoaiChiAdapter;
import com.example.myapplication.ui.Chi.LoaiChi;
import com.example.myapplication.ui.Thu.KhoanThu;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.ui.Chi.KhoanChi;
import com.example.myapplication.ui.database.DatabaseHelper;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BaoCaoFragment extends Fragment {

    private PieChart barChart_thu, barChart;
    private List<KhoanChi> expenseList;
    private List<KhoanThu> incomeList;
    private DatabaseHelper databaseHelper;
    private Spinner spinner_thang, spinner_nam;
    private ImageButton btnAddLimit;
    private TextView tvCurrentLimit;

    CombinedChart combinedChart;


    private double TongChi = 0;



    public BaoCaoFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(getContext()); // Khởi tạo DatabaseHelper
        loadDataFromDatabase(); // Lấy dữ liệu thực tế từ cơ sở dữ liệu

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bao_cao, container, false);

        // Ánh xạ các thành phần giao diện
        barChart = view.findViewById(R.id.pieChart_Chi);
        barChart_thu = view.findViewById(R.id.pieChart_Thu);
        spinner_thang = view.findViewById(R.id.spinner_thang);
        spinner_nam = view.findViewById(R.id.spinner_nam);
        btnAddLimit =view.findViewById(R.id.btnAddLimit);
        tvCurrentLimit = view.findViewById(R.id.tvCurrentLimit);
        combinedChart = view.findViewById(R.id.pieChart_Chi_Thu);

        setupSpinnerthang();
        setupSpinnernam();

        updateCurrentLimit(tvCurrentLimit, spinner_thang);
        // Sự kiện khi nhấn nút
        btnAddLimit.setOnClickListener(v -> showAddLimitDialog());

        initRecyclerView(view);
        initRecyclerView_Thu(view);

        return view;
    }
    private void initRecyclerView(View view) {
        RecyclerView recyclerViewExpense = view.findViewById(R.id.recyclerViewExpense);
        recyclerViewExpense.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseHelper = new DatabaseHelper(getContext());
        List<ExpenseIncomeItem> expenseList = databaseHelper.getExpenseIncomeByMonthh(getSelectedMonth(), getSelectedYear(), true);
        Log.d("ExpenseList", "Size: " + expenseList.size() + " - Data: " + expenseList.toString());

        calculatePercentages(expenseList);
        // Khởi tạo Adapter và gắn vào RecyclerView
        ExpenseIncomeAdapter expenseIncomeAdapter = new ExpenseIncomeAdapter(expenseList);
        recyclerViewExpense.setAdapter(expenseIncomeAdapter);
        expenseIncomeAdapter.notifyDataSetChanged();

    }
    private void initRecyclerView_Thu(View view) {
        RecyclerView recyclerViewExpense = view.findViewById(R.id.recyclerViewIncome);
        recyclerViewExpense.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseHelper = new DatabaseHelper(getContext());
        List<ExpenseIncomeItem> incomelist = databaseHelper.getExpenseIncomeByMonthh(getSelectedMonth(), getSelectedYear(), false);
        Log.d("Income", "Size: " + expenseList.size() + " - Data: " + expenseList.toString());

        calculatePercentages(incomelist);
        // Khởi tạo Adapter và gắn vào RecyclerView
        ExpenseIncomeAdapter expenseIncomeAdapter = new ExpenseIncomeAdapter(incomelist);
        recyclerViewExpense.setAdapter(expenseIncomeAdapter);
        expenseIncomeAdapter.notifyDataSetChanged();

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupCombinedChart(view);  // Gọi sau khi view đã được khởi tạo
    }
    private void setupSpinnerthang() {
            // Tạo danh sách dữ liệu từ 1 đến 12
            String[] monthList = {"Tháng Một", "Tháng Hai", "Tháng Ba", "Tháng Tư", "Tháng Năm", "Tháng Sáu",
                    "Tháng Bảy", "Tháng Tám", "Tháng Chín", "Tháng Mười", "Tháng Mười Một", "Tháng Mười Hai"};

            // Tạo ArrayAdapter và liên kết với Spinner
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_spinner_item, // Giao diện mặc định của Spinner
                    monthList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Giao diện khi nhấn vào Spinner
            spinner_thang.setAdapter(adapter);

            // Lấy tháng hiện tại (tháng từ 0 - 11)
            int currentMonth = Calendar.getInstance().get(Calendar.MONTH); // Calendar.MONTH trả về tháng từ 0 đến 11

            // Đặt giá trị mặc định của Spinner là tháng hiện tại
            spinner_thang.setSelection(currentMonth); // Cập nhật spinner chọn tháng hiện tại

            spinner_thang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // position từ 0 -> 11 (tương ứng Tháng Một -> Tháng Mười Hai)
                    int selectedMonth = position + 1; // Chuyển về 1 -> 12
                    updateReportForMonth(selectedMonth, getSelectedYear()); // Cập nhật báo cáo cho tháng được chọn
                    updateCurrentLimit(tvCurrentLimit, spinner_thang);
                    initRecyclerView(getView());  // Refresh RecyclerView
                    initRecyclerView_Thu(getView());


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Không làm gì khi không có mục nào được chọn
                }
            });
        }

    private void setupSpinnernam() {
        // Tạo danh sách năm từ 2020 đến 2030 (hoặc theo yêu cầu của bạn)
        List<String> yearList = new ArrayList<>();
        for (int i = 2024; i <= 2030; i++) {
            yearList.add(String.valueOf(i));
        }

        // Tạo ArrayAdapter và liên kết với Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, yearList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_nam.setAdapter(adapter);

        // Lấy năm hiện tại
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        // Tìm vị trí của năm hiện tại trong danh sách và đặt giá trị mặc định
        int defaultPosition = yearList.indexOf(String.valueOf(currentYear));
        spinner_nam.setSelection(defaultPosition);

        // Lắng nghe sự kiện khi chọn năm
        spinner_nam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedYear = parent.getItemAtPosition(position).toString();
                // Cập nhật báo cáo cho tháng và năm đã chọn
                updateReportForMonth(getSelectedMonth(), Integer.parseInt(selectedYear));
                updateCurrentLimit(tvCurrentLimit, spinner_thang);
                initRecyclerView(getView());  // Refresh RecyclerView
                initRecyclerView_Thu(getView());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không làm gì
            }
        });
    }


    private int getSelectedYear() {
        String selectedYearText = spinner_nam.getSelectedItem().toString();
        return Integer.parseInt(selectedYearText);
    }
    private int getSelectedMonth() {
        String selectedMonthText = spinner_thang.getSelectedItem().toString();
        return getMonthFromString(selectedMonthText); // Convert month name to number (1-12)
    }

    private void setupCombinedChart(View view) {

        // Khởi tạo các danh sách để lưu tổng thu và tổng chi
        List<BarEntry> entriesChi = new ArrayList<>();
        List<BarEntry> entriesThu = new ArrayList<>();
        List<String> months = new ArrayList<>();

        // Lấy tháng đã chọn từ spinner
        String selectedMonthText = spinner_thang.getSelectedItem().toString();
        int selectedMonth = getMonthFromString(selectedMonthText);
        int year = getSelectedYear();

        // Lấy dữ liệu cho tháng đã chọn và 2 tháng trước đó
        for (int i = 0; i < 3; i++) {
            int month = selectedMonth - (2 - i);
            if (month <= 0) {
                month += 12;
                year--;
            }
            months.add("Tháng " + month);

            // Lấy tổng chi và thu từ cơ sở dữ liệu
            double totalChi = databaseHelper.getTotalChi(month, year);
            double totalThu = databaseHelper.getTotalThu(month, year);

            entriesChi.add(new BarEntry(i - 0.25f, (float) totalChi));
            entriesThu.add(new BarEntry(i + 0.25f, (float) totalThu));
        }

        // Tạo BarDataSet cho chi và thu
        BarDataSet chiDataSet = new BarDataSet(entriesChi, "Tổng Chi");
        chiDataSet.setColor(Color.parseColor("#FF0000"));
        chiDataSet.setDrawValues(true);

        BarDataSet thuDataSet = new BarDataSet(entriesThu, "Tổng Thu");
        thuDataSet.setColor(Color.GREEN);
        thuDataSet.setDrawValues(true);

        // Tạo BarData từ các BarDataSet
        BarData barData = new BarData(chiDataSet, thuDataSet);

        // Điều chỉnh kích thước cột và khoảng cách giữa các nhóm cột
        barData.setBarWidth(0.3f);

        // Tạo CombinedData và thêm BarData vào CombinedData
        CombinedData combinedData = new CombinedData();
        combinedData.setData(barData);

        // Cấu hình biểu đồ kết hợp
        combinedChart.setData(combinedData);
        combinedChart.invalidate();

        // Cấu hình trục X
        XAxis xAxis = combinedChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(months));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(3);
        xAxis.setGranularityEnabled(true);
        xAxis.setSpaceMax(0.2f);

        // Ẩn trục Y bên trái
        YAxis leftAxis = combinedChart.getAxisLeft();
        leftAxis.setEnabled(false);

        // Ẩn trục Y bên phải
        YAxis rightAxis = combinedChart.getAxisRight();
        rightAxis.setEnabled(false);

        // Tùy chỉnh cho biểu đồ
        combinedChart.getDescription().setEnabled(false);
        combinedChart.getLegend().setEnabled(true);
    }

    // Hàm để chuyển đổi tháng từ tên tháng (ví dụ: "Tháng Một") sang số tháng (1 - 12)
    private int getMonthFromString(String monthText) {
        switch (monthText) {
            case "Tháng Một": return 1;
            case "Tháng Hai": return 2;
            case "Tháng Ba": return 3;
            case "Tháng Tư": return 4;
            case "Tháng Năm": return 5;
            case "Tháng Sáu": return 6;
            case "Tháng Bảy": return 7;
            case "Tháng Tám": return 8;
            case "Tháng Chín": return 9;
            case "Tháng Mười": return 10;
            case "Tháng Mười Một": return 11;
            case "Tháng Mười Hai": return 12;
            default: return 1; // Default to January if not recognized
        }
    }

    private void updateReportForMonth(int month, int year) {
        // Lấy dữ liệu thu/chi theo tháng và năm từ cơ sở dữ liệu
        List<KhoanChi> expensesForMonth = databaseHelper.getKhoanChiByMonth(month, year);
        List<KhoanThu> incomesForMonth = databaseHelper.getKhoanThuByMonth(month, year);

        // Tính tổng thu/chi
        double totalExpenseForMonth = 0;
        double totalIncomeForMonth = 0;

        for (KhoanChi expense : expensesForMonth) {
            totalExpenseForMonth += expense.getSoTien();
        }

        for (KhoanThu income : incomesForMonth) {
            totalIncomeForMonth += income.getSoTien();
        }

        // Cập nhật tổng thu/chi trên giao diện
        TextView txtTongChi = getView().findViewById(R.id.txtTongChi);
        TongChi = totalExpenseForMonth ;
        TextView txtTongThu = getView().findViewById(R.id.txtTongThu);

        txtTongChi.setText("Tổng Chi: " + String.format("%,.0f", totalExpenseForMonth) + " VND");
        txtTongThu.setText("Tổng Thu: " + String.format("%,.0f", totalIncomeForMonth) + " VND");

        // Cập nhật dữ liệu biểu đồ
        updatePieChart(barChart, expensesForMonth, "");
        updatePieChart(barChart_thu, incomesForMonth, "");
        setupCombinedChart(getView());
    }



    private void loadDataFromDatabase() {
        // Lấy danh sách chi tiêu từ cơ sở dữ liệu
        expenseList = databaseHelper.getAllKhoanChi(); // Hàm này lấy tất cả chi tiêu từ database
        incomeList = databaseHelper.getAllKhoanThu();
    }

    private void updatePieChart(PieChart pieChart, List<?> dataList, String label) {
        List<PieEntry> entries = new ArrayList<>();
        Map<String, Double> dataByCategory = new HashMap<>();

        // Duyệt qua danh sách dữ liệu
        for (Object item : dataList) {
            String categoryName;
            double amount;

            // Phân loại và xử lý từng đối tượng
            if (item instanceof KhoanThu) {
                KhoanThu thu = (KhoanThu) item;
                categoryName = databaseHelper.getLoaiThuNameById(thu.getLoaiThuId());
                amount = thu.getSoTien();
            } else if (item instanceof KhoanChi) {
                KhoanChi chi = (KhoanChi) item;
                categoryName = databaseHelper.getLoaiChiNameById(chi.getLoaiChiId());
                amount = chi.getSoTien();
            } else {
                // Bỏ qua nếu không phải KhoanThu hoặc KhoanChi
                continue;
            }

            // Tổng hợp dữ liệu theo danh mục
            dataByCategory.put(categoryName, dataByCategory.getOrDefault(categoryName, 0.0) + amount);
        }

        // Chuyển đổi dữ liệu tổng hợp thành PieEntry
        for (Map.Entry<String, Double> entry : dataByCategory.entrySet()) {
            entries.add(new PieEntry(entry.getValue().floatValue(), entry.getKey()));
        }

        // Tạo PieDataSet và cấu hình biểu đồ tròn
        PieDataSet dataSet = new PieDataSet(entries, label);
        dataSet.setColors(generateRandomColors(entries.size())); // Tạo màu ngẫu nhiên
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setSliceSpace(0.5f); // Khoảng cách giữa các lát cắt

        PieData pieData = new PieData(dataSet);

        // Thiết lập PieChart
        pieChart.setData(pieData);
        pieChart.setDrawHoleEnabled(true); // Bật chế độ có lỗ ở giữa
        pieChart.setHoleRadius(40f); // Bán kính lỗ
        pieChart.setTransparentCircleRadius(45f); // Bán kính vòng trong trong suốt
        pieChart.setCenterText(null); // Văn bản ở giữa biểu đồ
        pieChart.setCenterTextSize(14f);
        pieChart.getDescription().setEnabled(false); // Tắt mô tả biểu đồ
        pieChart.getLegend().setEnabled(true); // Hiển thị chú thích
        pieChart.setDrawEntryLabels(false); // Không hiển thị nhãn
        pieChart.setRotationEnabled(false); // Tắt xoay biểu đồ
        dataSet.setDrawValues(false); // Ẩn giá trị trên biểu đồ


        pieChart.invalidate(); // Làm mới biểu đồ

    }


    private List<Integer> generateRandomColors(int count) {
        List<Integer> colors = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            int color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            colors.add(color);
        }

        return colors;
    }

    private void showAddLimitDialog() {
        // Inflate layout dialog
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_limit, null);

        // Tạo Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);
        builder.setTitle("Thêm Hạn mức Chi tiêu");

        // Liên kết các view trong dialog
        NumberPicker npMonth = dialogView.findViewById(R.id.npMonth);
        NumberPicker npYear = dialogView.findViewById(R.id.npYear);
        EditText etAmount = dialogView.findViewById(R.id.etAmount);

        // Cấu hình NumberPicker cho tháng
        npMonth.setMinValue(1);
        npMonth.setMaxValue(12);
        npMonth.setValue(getSelectedMonth());

        // Cấu hình NumberPicker cho năm
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        npYear.setMinValue(currentYear);
        npYear.setMaxValue(currentYear + 10); // Tăng 10 năm
        npYear.setValue(getSelectedYear()); // Năm hiện tại

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            int selectedMonth = npMonth.getValue(); // Lấy tháng được chọn
            int selectedYear = npYear.getValue(); // Lấy năm được chọn

            String month = String.format("%02d-%d", selectedMonth, selectedYear); // Định dạng mm-yyyy
            String amountText = etAmount.getText().toString().trim();

            if (!amountText.isEmpty()) {
                double limitAmount = Double.parseDouble(amountText); // Số tiền hạn mức

                // Lưu hạn mức vào cơ sở dữ liệu
                databaseHelper.addMonthlyLimit(month, limitAmount);
                updateCurrentLimit(tvCurrentLimit, spinner_thang); // Cập nhật lại hạn mức hiện tại trên giao diện
                
                // Hiển thị thông báo
                Toast.makeText(getContext(), "Đã lưu hạn mức cho tháng " + month + "!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Vui lòng nhập số tiền.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Hủy", null);
        // Hiển thị Dialog
        builder.create().show();
    }
    private void updateCurrentLimit(TextView tvCurrentLimit, Spinner spinnerMonthYear) {
        // Lấy tên tháng từ Spinner
        String selectedMonthText = spinnerMonthYear.getSelectedItem().toString();
        int month = getSelectedMonth(); // Chuyển về số tháng (1-12)
        int year = getSelectedYear(); // Lấy năm hiện tại

        // Tạo chuỗi tháng-năm theo định dạng "mm-yyyy"
        String monthYear = String.format("%02d-%d", month, year);

        // Lấy hạn mức từ cơ sở dữ liệu
        double limit = databaseHelper.getMonthlyLimit(monthYear);



        if (limit > 0) {
            tvCurrentLimit.setText("Hạn mức: " + String.format("%,.0f VNĐ", limit));
        } else {
            tvCurrentLimit.setText("Chưa có hạn mức cho tháng " + monthYear);
        }
    }
    public void calculatePercentages(List<ExpenseIncomeItem> list) {
        double totalAmount = 0;
        for (ExpenseIncomeItem item : list) {
            totalAmount += item.getTotal();
        }

        for (ExpenseIncomeItem item : list) {
            if (totalAmount > 0) {
                double percentage = (item.getTotal() / totalAmount) * 100;
                item.setPercentage(percentage);
            } else {
                item.setPercentage(0);
            }
        }
    }



}
