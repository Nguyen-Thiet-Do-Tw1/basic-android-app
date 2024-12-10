package com.example.myapplication.ui.Thu;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.myapplication.R;
import com.example.myapplication.adapter.KhoanThuAdapter;
import com.example.myapplication.ui.database.DatabaseHelper;
import android.app.AlertDialog;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.List;

public class KhoanThuFragment extends Fragment {
    private RecyclerView recyclerView;
    private KhoanThuFragment fragment;
    private KhoanThuAdapter adapter;
    private List<KhoanThu> khoanThuList;
    private List<LoaiThu> loaiThuList;
    private DatabaseHelper dbHelper;
    private KhoanThuViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_khoan_thu, container, false);

        // Khởi tạo UI và thiết lập sự kiện cho nút "Thêm"
        initializeUI(view);
        initializeDatabase();

        // Thiết lập RecyclerView và tải dữ liệu ban đầu
        setupRecyclerView();
        loadData();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData(); // Cập nhật lại dữ liệu khi quay lại fragment
    }

    /**
     * Khởi tạo các thành phần giao diện và sự kiện nút "Thêm".
     */
    private void initializeUI(View view) {
        recyclerView = view.findViewById(R.id.rcv_KhoanThu);
        view.findViewById(R.id.fab_add_khoanthu).setOnClickListener(v -> showAddKhoanThuDialog());
    }

    /**
     * Khởi tạo DatabaseHelper để lấy dữ liệu.
     */
    private void initializeDatabase() {
        dbHelper = new DatabaseHelper(getContext());
        khoanThuList = dbHelper.getAllKhoanThu();
        loaiThuList = dbHelper.getAllLoaiThu();
    }

    /**
     * Thiết lập RecyclerView với LinearLayoutManager và adapter.
     */
    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new KhoanThuAdapter(khoanThuList, loaiThuList, getContext(), this);
        recyclerView.setAdapter(adapter);
    }

    /**
     * Tải dữ liệu từ cơ sở dữ liệu và làm mới RecyclerView.
     */
    private void loadData() {
        khoanThuList.clear();
        khoanThuList.addAll(dbHelper.getAllKhoanThu());
        adapter.notifyDataSetChanged();
    }

    /**
     * Hiển thị hộp thoại để thêm mới một KhoanThu.
     */
    private void showAddKhoanThuDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_khoanthu, null);
        dialogBuilder.setView(dialogView);

        // Khởi tạo các thành phần giao diện trong hộp thoại
        EditText edtDateTime = dialogView.findViewById(R.id.edt_date_time);
        Spinner spinnerLoaiThu = dialogView.findViewById(R.id.spinnerLoaiThu);
        EditText editTextNote = dialogView.findViewById(R.id.editTextNote);
        EditText editTextAmount = dialogView.findViewById(R.id.editTextAmount);
        ImageView iconPreview = dialogView.findViewById(R.id.iconPreview);

        setDefaultDate(edtDateTime);
        setupSpinner(spinnerLoaiThu, iconPreview);
        updateLoaiThuList();
        refreshSpinner(spinnerLoaiThu, iconPreview);

        // Thiết lập các nút của hộp thoại
        dialogBuilder.setPositiveButton("Lưu", (dialog, which) -> saveKhoanThu(editTextNote, editTextAmount, edtDateTime, spinnerLoaiThu));
        dialogBuilder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = dialogBuilder.create();
        dialog.setOnDismissListener(d -> loadData()); // Cập nhật dữ liệu sau khi đóng hộp thoại
        dialog.show();
    }

    /**
     * Thiết lập ngày hiện tại vào EditText ngày.
     */
    private void setDefaultDate(EditText edtDateTime) {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        edtDateTime.setText(currentDate);
    }
    private void updateLoaiThuList() {
        loaiThuList.clear();
        loaiThuList.addAll(dbHelper.getAllLoaiThu());
    }
    private void refreshSpinner(Spinner spinner, ImageView iconPreview) {
        ArrayAdapter<LoaiThu> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, loaiThuList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoaiThu selectedLoaiThu = (LoaiThu) parent.getItemAtPosition(position);
                iconPreview.setImageResource(selectedLoaiThu.getIcon());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                iconPreview.setImageResource(R.drawable.ic_launcher_foreground);
            }
        });
    }

    /**
     * Thiết lập Spinner với dữ liệu từ cơ sở dữ liệu và hiển thị biểu tượng dựa trên LoaiThu được chọn.
     */
    private void setupSpinner(Spinner spinner, ImageView iconPreview) {
        ArrayAdapter<LoaiThu> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, loaiThuList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        // Hiển thị biểu tượng của LoaiThu đã chọn
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoaiThu selectedLoaiThu = (LoaiThu) parent.getItemAtPosition(position);
                iconPreview.setImageResource(selectedLoaiThu.getIcon());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                iconPreview.setImageResource(R.drawable.ic_launcher_foreground); // Icon mặc định
            }
        });
    }

    /**
     * Lưu một mục KhoanThu mới vào cơ sở dữ liệu và làm mới RecyclerView.
     */
    private void saveKhoanThu(EditText editTextNote, EditText editTextAmount, EditText edtDateTime, Spinner spinnerLoaiThu) {
        String note = editTextNote.getText().toString();
        double amount = parseAmount(editTextAmount.getText().toString());
        String date = edtDateTime.getText().toString();
        LoaiThu selectedLoaiThu = (LoaiThu) spinnerLoaiThu.getSelectedItem();

        if (selectedLoaiThu != null && amount > 0) {
            dbHelper.addKhoanThu(amount, note, date, selectedLoaiThu.getId());
            Toast.makeText(getContext(), "Khoản thu đã được thêm", Toast.LENGTH_SHORT).show();
            loadData();
        } else {
            Toast.makeText(getContext(), "Vui lòng nhập số tiền hợp lệ và chọn loại thu.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Chuyển đổi chuỗi nhập vào thành số tiền. Trả về 0 nếu không hợp lệ.
     */
    private double parseAmount(String amountStr) {
        try {
            return Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    public void editKhoanThu(KhoanThu khoanThu) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_khoanthu, null);
        dialogBuilder.setView(dialogView);

        // Khởi tạo các thành phần UI trong hộp thoại
        EditText edtDateTime = dialogView.findViewById(R.id.edt_date_time);
        Spinner spinnerLoaiThu = dialogView.findViewById(R.id.spinnerLoaiThu);
        EditText editTextNote = dialogView.findViewById(R.id.editTextNote);
        EditText editTextAmount = dialogView.findViewById(R.id.editTextAmount);
        ImageView iconPreview = dialogView.findViewById(R.id.iconPreview);

        // Thiết lập dữ liệu ban đầu cho hộp thoại sửa
        edtDateTime.setText(khoanThu.getNgay());
        editTextNote.setText(khoanThu.getGhiChu());
        editTextAmount.setText(String.valueOf(khoanThu.getSoTien()));

        // Thiết lập spinner và biểu tượng loại thu
//        setDefaultDate(edtDateTime);
        setupSpinner(spinnerLoaiThu, iconPreview);

        // Chọn đúng loại thu hiện tại trong spinner
        for (int i = 0; i < loaiThuList.size(); i++) {
            if (loaiThuList.get(i).getId() == khoanThu.getLoaiThuId()) {
                spinnerLoaiThu.setSelection(i);
                break;
            }
        }

        dialogBuilder.setPositiveButton("Cập nhật", (dialog, which) -> {
            String note = editTextNote.getText().toString();
            double amount = parseAmount(editTextAmount.getText().toString());
            String date = edtDateTime.getText().toString();
            LoaiThu selectedLoaiThu = (LoaiThu) spinnerLoaiThu.getSelectedItem();

            if (selectedLoaiThu != null && amount > 0) {
                khoanThu.setGhiChu(note);
                khoanThu.setSoTien(amount);
                khoanThu.setNgay(date);
                khoanThu.setLoaiThuId(selectedLoaiThu.getId());

                dbHelper.updateKhoanThu(khoanThu);
                Toast.makeText(getContext(), "Khoản thu đã được cập nhật", Toast.LENGTH_SHORT).show();
                loadData(); // Cập nhật lại danh sách sau khi sửa
            } else {
                Toast.makeText(getContext(), "Vui lòng nhập số tiền hợp lệ và chọn loại thu.", Toast.LENGTH_SHORT).show();
            }
        });

        dialogBuilder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    /**
     * Hàm xóa KhoanThu và làm mới RecyclerView
     */
    public void deleteKhoanThu(KhoanThu khoanThu) {
        new AlertDialog.Builder(getContext())
                .setTitle("Xóa khoản thu")
                .setMessage("Bạn có chắc chắn muốn xóa khoản thu này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    dbHelper.deleteKhoanThu(khoanThu.getId());
                    Toast.makeText(getContext(), "Khoản thu đã được xóa", Toast.LENGTH_SHORT).show();
                    loadData(); // Cập nhật lại danh sách sau khi xóa
                })
                .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(KhoanThuViewModel.class);
    }
}
