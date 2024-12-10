package com.example.myapplication.ui.Chi;

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
import com.example.myapplication.adapter.KhoanChiAdapter;
import com.example.myapplication.ui.database.DatabaseHelper;
import android.app.AlertDialog;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.List;

public class KhoanChiFragment extends Fragment {
    private RecyclerView recyclerView;
    private KhoanChiFragment fragment;
    private KhoanChiAdapter adapter;
    private List<KhoanChi> khoanChiList;
    private List<LoaiChi> loaiChiList;
    private DatabaseHelper dbHelper;
    private KhoanChiViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_khoan_chi, container, false);

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
        recyclerView = view.findViewById(R.id.rcv_KhoanChi);
        view.findViewById(R.id.fab_add_khoanchi).setOnClickListener(v -> showAddKhoanChiDialog());
    }

    /**
     * Khởi tạo DatabaseHelper để lấy dữ liệu.
     */
    private void initializeDatabase() {
        dbHelper = new DatabaseHelper(getContext());
        khoanChiList = dbHelper.getAllKhoanChi();
        loaiChiList = dbHelper.getAllLoaiChi();
    }

    /**
     * Thiết lập RecyclerView với LinearLayoutManager và adapter.
     */
    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new KhoanChiAdapter(khoanChiList, loaiChiList, getContext(), this);
        recyclerView.setAdapter(adapter);
    }

    /**
     * Tải dữ liệu từ cơ sở dữ liệu và làm mới RecyclerView.
     */
    private void loadData() {
        khoanChiList.clear();
        khoanChiList.addAll(dbHelper.getAllKhoanChi());
        adapter.notifyDataSetChanged();
    }

    private void updateLoaiChiList() {
        loaiChiList.clear();
        loaiChiList.addAll(dbHelper.getAllLoaiChi());
    }
    private void refreshSpinner(Spinner spinner, ImageView iconPreview) {
        ArrayAdapter<LoaiChi> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, loaiChiList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoaiChi selectedLoaiChi = (LoaiChi) parent.getItemAtPosition(position);
                iconPreview.setImageResource(selectedLoaiChi.getIcon());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                iconPreview.setImageResource(R.drawable.ic_launcher_foreground);
            }
        });
    }





    /**
     * Hiển thị hộp thoại để thêm mới một KhoanChi.
     */
    private void showAddKhoanChiDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_khoanchi, null);
        dialogBuilder.setView(dialogView);

        // Khởi tạo các thành phần giao diện trong hộp thoại
        EditText edtDateTime = dialogView.findViewById(R.id.edt_date_time_chi);
        Spinner spinnerLoaiChi = dialogView.findViewById(R.id.spinnerLoai_Chi);
        EditText editTextNote = dialogView.findViewById(R.id.editTextNote_chi);
        EditText editTextAmount = dialogView.findViewById(R.id.editTextAmount_chi);
        ImageView iconPreview = dialogView.findViewById(R.id.iconPreviewLoaiChi);

        setDefaultDate(edtDateTime);
        setupSpinner(spinnerLoaiChi, iconPreview);
        updateLoaiChiList();
        refreshSpinner(spinnerLoaiChi, iconPreview);


        // Thiết lập các nút của hộp thoại
        dialogBuilder.setPositiveButton("Lưu", (dialog, which) -> saveKhoanChi(editTextNote, editTextAmount, edtDateTime, spinnerLoaiChi));
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

    /**
     * Thiết lập Spinner với dữ liệu từ cơ sở dữ liệu và hiển thị biểu tượng dựa trên LoaiChi được chọn.
     */
    private void setupSpinner(Spinner spinner, ImageView iconPreview) {
        ArrayAdapter<LoaiChi> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, loaiChiList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        // Hiển thị biểu tượng của LoaiChi đã chọn
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoaiChi selectedLoaiChi = (LoaiChi) parent.getItemAtPosition(position);
                iconPreview.setImageResource(selectedLoaiChi.getIcon());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                iconPreview.setImageResource(R.drawable.ic_launcher_foreground); // Icon mặc định
            }
        });
    }

    /**
     * Lưu một mục KhoanChi mới vào cơ sở dữ liệu và làm mới RecyclerView.
     */
//    private void saveKhoanChi(EditText editTextNote, EditText editTextAmount, EditText edtDateTime, Spinner spinnerLoaiChi) {
//        String note = editTextNote.getText().toString();
//        double amount = parseAmount(editTextAmount.getText().toString());
//        String date = edtDateTime.getText().toString();
//        LoaiChi selectedLoaiChi = (LoaiChi) spinnerLoaiChi.getSelectedItem();
//
//        if (selectedLoaiChi != null && amount > 0) {
//            dbHelper.addKhoanChi(amount, note, date, selectedLoaiChi.getId());
//            Toast.makeText(getContext(), "Khoản chi đã được thêm", Toast.LENGTH_SHORT).show();
//            loadData();
//        } else {
//            Toast.makeText(getContext(), "Vui lòng nhập số tiền hợp lệ và chọn loại chi.", Toast.LENGTH_SHORT).show();
//        }
//    }
    private String formatToMonthYear(String date) {
        try {
            // Định dạng ban đầu: yyyy-MM-dd
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            // Định dạng đầu ra: MM-yyyy
            SimpleDateFormat outputFormat = new SimpleDateFormat("MM-yyyy", Locale.getDefault());
            return outputFormat.format(inputFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            // Trả về chuỗi trống nếu có lỗi
            return "";
        }
    }

    private void saveKhoanChi(EditText editTextNote, EditText editTextAmount, EditText edtDateTime, Spinner spinnerLoaiChi) {
        String note = editTextNote.getText().toString();
        double amount = parseAmount(editTextAmount.getText().toString());
        String date = edtDateTime.getText().toString(); // Giả sử date có định dạng yyyy-MM-dd
        LoaiChi selectedLoaiChi = (LoaiChi) spinnerLoaiChi.getSelectedItem();

        if (selectedLoaiChi != null && amount > 0) {
            // Chuyển đổi ngày nhập thành định dạng MM-yyyy
            String currentMonth = formatToMonthYear(date);

            if (currentMonth.isEmpty()) {
                Toast.makeText(getContext(), "Định dạng ngày không hợp lệ.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Gọi hàm getRemainingLimit với currentMonth
            double remainingLimit = dbHelper.getRemainingLimit(currentMonth);

            if (amount > remainingLimit) {
                // Nếu vượt hạn mức, hiển thị hộp thoại xác nhận
                double excessAmount = amount - remainingLimit;

                new AlertDialog.Builder(getContext())
                        .setTitle("Vượt hạn mức")
                        .setMessage("Khoản chi vượt hạn mức còn lại (" + remainingLimit + "). Bạn có muốn tiếp tục không?")
                        .setPositiveButton("Có", (dialog, which) -> {
                            // Thêm phần hợp lệ vào tháng hiện tại
                            dbHelper.addKhoanChi(remainingLimit, note, date, selectedLoaiChi.getId());

                            // Thêm phần dư vào tháng sau
                            String nextMonth = getNextMonth(date);
                            dbHelper.addKhoanChi(excessAmount,   " (nợ T."+date.substring(5, 7)+")", nextMonth, selectedLoaiChi.getId());

                            Toast.makeText(getContext(), "Khoản chi đã được thêm", Toast.LENGTH_SHORT).show();
                            loadData();
                        })
                        .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                        .show();
            } else {
                // Nếu trong hạn mức, thêm trực tiếp
                dbHelper.addKhoanChi(amount, note, date, selectedLoaiChi.getId());
                Toast.makeText(getContext(), "Khoản chi đã được thêm", Toast.LENGTH_SHORT).show();
                loadData();
            }
        } else {
            Toast.makeText(getContext(), "Vui lòng nhập số tiền hợp lệ và chọn loại chi.", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Lấy tháng kế tiếp (YYYY-MM).
     */
    private String getNextMonth(String currentDate) {
        // Định dạng ngày đầu vào: yyyy-MM-dd
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        // Định dạng ngày đầu ra: yyyy-MM-dd
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        try {
            // Chuyển đổi ngày đầu vào thành Date
            Date date = inputFormat.parse(currentDate);
            calendar.setTime(date);
            // Thêm một tháng
            calendar.add(Calendar.MONTH, 1);
            // Đặt ngày đầu tiên của tháng
            calendar.set(Calendar.DAY_OF_MONTH, 1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Trả về ngày đầu tiên của tháng sau với định dạng yyyy-MM-dd
        return outputFormat.format(calendar.getTime());
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

//    public void editKhoanChi(KhoanChi khoanChi) {
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
//        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_khoanchi, null);
//        dialogBuilder.setView(dialogView);
//
//        // Khởi tạo các thành phần UI trong hộp thoại
//        EditText edtDateTime = dialogView.findViewById(R.id.edt_date_time_chi);
//        Spinner spinnerLoaiChi = dialogView.findViewById(R.id.spinnerLoai_Chi);
//        EditText editTextNote = dialogView.findViewById(R.id.editTextNote_chi);
//        EditText editTextAmount = dialogView.findViewById(R.id.editTextAmount_chi);
//        ImageView iconPreview = dialogView.findViewById(R.id.iconPreviewLoaiChi);
//
//        // Thiết lập dữ liệu ban đầu cho hộp thoại sửa
//        edtDateTime.setText(khoanChi.getNgay());
//        editTextNote.setText(khoanChi.getGhiChu());
//        editTextAmount.setText(String.valueOf(khoanChi.getSoTien()));
//
//        // Thiết lập spinner và biểu tượng loại chi
//        setupSpinner(spinnerLoaiChi, iconPreview);
//        updateLoaiChiList();
//        refreshSpinner(spinnerLoaiChi, iconPreview);
//
//        // Chọn đúng loại chi hiện tại trong spinner
//        for (int i = 0; i < loaiChiList.size(); i++) {
//            if (loaiChiList.get(i).getId() == khoanChi.getLoaiChiId()) {
//                spinnerLoaiChi.setSelection(i);
//                break;
//            }
//        }
//
//        dialogBuilder.setPositiveButton("Cập nhật", (dialog, which) -> {
//            String note = editTextNote.getText().toString();
//            double amount = parseAmount(editTextAmount.getText().toString());
//            String date = edtDateTime.getText().toString();
//            LoaiChi selectedLoaiChi = (LoaiChi) spinnerLoaiChi.getSelectedItem();
//
//            // Kiểm tra tính hợp lệ của dữ liệu nhập vào
//            if (selectedLoaiChi != null && amount > 0) {
//                // Chuyển đổi ngày nhập thành định dạng MM-yyyy
//                String currentMonth = formatToMonthYear(date);
//
//                if (currentMonth.isEmpty()) {
//                    Toast.makeText(getContext(), "Định dạng ngày không hợp lệ.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                // Kiểm tra số tiền có hợp lệ và không vượt hạn mức
//                double remainingLimit = dbHelper.getRemainingLimit(currentMonth);
//                if (amount > remainingLimit) {
//                    // Nếu vượt hạn mức, hiển thị hộp thoại xác nhận
//                    double excessAmount = amount - remainingLimit;
//
//                    new AlertDialog.Builder(getContext())
//                            .setTitle("Vượt hạn mức")
//                            .setMessage("Khoản chi vượt hạn mức còn lại (" + remainingLimit + "). Bạn có muốn tiếp tục không?")
//                            .setPositiveButton("Có", (dialog1, which1) -> {
//                                // Cập nhật phần hợp lệ vào tháng hiện tại
//                                khoanChi.setGhiChu(note);
//                                khoanChi.setSoTien(remainingLimit);
//                                khoanChi.setNgay(date);
//                                khoanChi.setLoaiChiId(selectedLoaiChi.getId());
//
//                                dbHelper.updateKhoanChi(khoanChi);
//
//                                // Thêm phần dư vào tháng sau
//                                String nextMonth = getNextMonth(date);
//                                dbHelper.addKhoanChi(excessAmount, note + " (nợ T."+nextMonth+")", nextMonth, selectedLoaiChi.getId());
//
//                                Toast.makeText(getContext(), "Khoản chi đã được cập nhật", Toast.LENGTH_SHORT).show();
//                                loadData();
//                            })
//                            .setNegativeButton("Không", (dialog1, which1) -> dialog1.dismiss())
//                            .show();
//                } else {
//                    // Nếu trong hạn mức, cập nhật trực tiếp
//                    khoanChi.setGhiChu(note);
//                    khoanChi.setSoTien(amount);
//                    khoanChi.setNgay(date);
//                    khoanChi.setLoaiChiId(selectedLoaiChi.getId());
//
//                    dbHelper.updateKhoanChi(khoanChi);
//                    Toast.makeText(getContext(), "Khoản chi đã được cập nhật", Toast.LENGTH_SHORT).show();
//                    loadData();
//                }
//            } else {
//                Toast.makeText(getContext(), "Vui lòng nhập số tiền hợp lệ và chọn loại chi.", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        dialogBuilder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
//
//        AlertDialog dialog = dialogBuilder.create();
//        dialog.show();
//    }
public void editKhoanChi(KhoanChi khoanChi) {
    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
    View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_khoanchi, null);
    dialogBuilder.setView(dialogView);

    // Khởi tạo các thành phần UI trong hộp thoại
    EditText edtDateTime = dialogView.findViewById(R.id.edt_date_time_chi);
    Spinner spinnerLoaiChi = dialogView.findViewById(R.id.spinnerLoai_Chi);
    EditText editTextNote = dialogView.findViewById(R.id.editTextNote_chi);
    EditText editTextAmount = dialogView.findViewById(R.id.editTextAmount_chi);
    ImageView iconPreview = dialogView.findViewById(R.id.iconPreviewLoaiChi);

    // Thiết lập dữ liệu ban đầu cho hộp thoại sửa
    edtDateTime.setText(khoanChi.getNgay());
    editTextNote.setText(khoanChi.getGhiChu());
    editTextAmount.setText(String.valueOf(khoanChi.getSoTien()));

    // Thiết lập spinner và biểu tượng loại chi
//        setDefaultDate(edtDateTime);
    setupSpinner(spinnerLoaiChi, iconPreview);
    updateLoaiChiList();
    refreshSpinner(spinnerLoaiChi, iconPreview);

    // Chọn đúng loại chi hiện tại trong spinner
    for (int i = 0; i < loaiChiList.size(); i++) {
        if (loaiChiList.get(i).getId() == khoanChi.getLoaiChiId()) {
            spinnerLoaiChi.setSelection(i);
            break;
        }
    }

    dialogBuilder.setPositiveButton("Cập nhật", (dialog, which) -> {
        String note = editTextNote.getText().toString();
        double amount = parseAmount(editTextAmount.getText().toString());
        String date = edtDateTime.getText().toString();
        LoaiChi selectedLoaiChi = (LoaiChi) spinnerLoaiChi.getSelectedItem();

        if (selectedLoaiChi != null && amount > 0) {
            khoanChi.setGhiChu(note);
            khoanChi.setSoTien(amount);
            khoanChi.setNgay(date);
            khoanChi.setLoaiChiId(selectedLoaiChi.getId());

            dbHelper.updateKhoanChi(khoanChi);
            Toast.makeText(getContext(), "Khoản chi đã được cập nhật", Toast.LENGTH_SHORT).show();
            loadData(); // Cập nhật lại danh sách sau khi sửa
        } else {
            Toast.makeText(getContext(), "Vui lòng nhập số tiền hợp lệ và chọn loại chi.", Toast.LENGTH_SHORT).show();
        }
    });

    dialogBuilder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

    AlertDialog dialog = dialogBuilder.create();
    dialog.show();
}


    /**
     * Hàm xóa KhoanChi và làm mới RecyclerView
     */
    public void deleteKhoanChi(KhoanChi khoanChi) {
        new AlertDialog.Builder(getContext())
                .setTitle("Xóa khoản chi")
                .setMessage("Bạn có chắc chắn muốn xóa khoản chi này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    dbHelper.deleteKhoanChi(khoanChi.getId());
                    Toast.makeText(getContext(), "Khoản chi đã được xóa", Toast.LENGTH_SHORT).show();
                    loadData(); // Cập nhật lại danh sách sau khi xóa
                })
                .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(KhoanChiViewModel.class);
    }
}
