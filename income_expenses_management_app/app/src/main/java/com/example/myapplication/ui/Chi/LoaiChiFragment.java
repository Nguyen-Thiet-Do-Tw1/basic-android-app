package com.example.myapplication.ui.Chi;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.LoaiChiAdapter;
import com.example.myapplication.ui.Thu.LoaiThu;
import com.example.myapplication.ui.database.DatabaseHelper;

import java.util.List;

public class LoaiChiFragment extends Fragment {
    private RecyclerView recyclerView; // Danh sách hiển thị loại chi
    private LoaiChiAdapter adapter; // Adapter kết nối dữ liệu với RecyclerView
    private DatabaseHelper databaseHelper; // Cơ sở dữ liệu
    private List<LoaiChi> loaiChiList; // Danh sách loại chi
    private int selectedIcon = R.drawable.ic_menu_camera; // Icon mặc định khi thêm mới

    /**
     * Khởi tạo giao diện của Fragment.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loai_chi, container, false);

        // Khởi tạo RecyclerView và DatabaseHelper
        initRecyclerView(view);

        // Thiết lập nút thêm loại chi mới
        setupAddButton(view);

        return view;
    }

    /**
     * Khởi tạo RecyclerView và DatabaseHelper.
     */
    private void initRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewLoaiChi);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseHelper = new DatabaseHelper(getContext());
        loaiChiList = databaseHelper.getAllLoaiChi();

        // Khởi tạo Adapter và gắn vào RecyclerView
        adapter = new LoaiChiAdapter(loaiChiList, new LoaiChiAdapter.OnLoaiChiClickListener() {
            @Override
            public void onEditClick(LoaiChi loaiChi) {
                showEditDialog(loaiChi); // Hiển thị dialog chỉnh sửa
            }

            @Override
            public void onDeleteClick(LoaiChi loaiChi) {
                deleteLoaiChi(loaiChi); // Xóa loại chi
            }
        });
        recyclerView.setAdapter(adapter);
    }

    /**
     * Thiết lập nút thêm loại chi mới.
     */
    private void setupAddButton(View view) {
        view.findViewById(R.id.fab_add_chi).setOnClickListener(v -> showAddCategoryDialog());
    }

    /**
     * Hiển thị dialog thêm loại chi mới.
     */
    private void showAddCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_loaichi, null);
        builder.setView(dialogView);

        EditText editTextName = dialogView.findViewById(R.id.editTextName);
        ImageView imageView2 = dialogView.findViewById(R.id.imageView2);
        Button btnChooseIcon = dialogView.findViewById(R.id.btnChooseIcon);

        // Khi nhấn vào Button, hiển thị Dialog chọn biểu tượng
        btnChooseIcon.setOnClickListener(v -> showIconSelectionDialog(imageView2));

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String name = editTextName.getText().toString().trim();
            if (!name.isEmpty()) {
                addNewCategory(name);
            } else {
                Toast.makeText(getContext(), "Vui lòng nhập tên", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", null);
        builder.create().show();
    }




    /**
     * Cài đặt Spinner để hiển thị danh sách icon.
     */
    private void setupIconGridView(GridView iconGridView, AlertDialog dialog, ImageView imageView2) {
        Integer[] icons = {
                R.drawable.salary, R.drawable.ic_bonus, R.drawable.ic_clothes,
                R.drawable.ic_cosmetics, R.drawable.ic_donation, R.drawable.ic_home,
                R.drawable.ic_investment, R.drawable.medicine, R.drawable.piggy,
                R.drawable.smartphone, R.drawable.water, R.drawable.write
        };

        ArrayAdapter<Integer> iconAdapter = new ArrayAdapter<Integer>(getContext(), R.layout.grid_item_icon, icons) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_icon, parent, false);
                }
                ImageView imageView = (ImageView) convertView;
                imageView.setImageResource(icons[position]);
                return convertView;
            }
        };

        iconGridView.setAdapter(iconAdapter);

        // Xử lý sự kiện chọn biểu tượng
        iconGridView.setOnItemClickListener((parent, view, position, id) -> {
            selectedIcon = icons[position]; // Lưu biểu tượng được chọn
            imageView2.setImageResource(selectedIcon); // Hiển thị biểu tượng trên ImageView2
            dialog.dismiss(); // Đóng Dialog chọn biểu tượng
        });
    }


    private void showIconSelectionDialog(ImageView imageView2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_icon_selection, null);
        builder.setView(dialogView);
        builder.setTitle("Chọn biểu tượng");
        builder.setNegativeButton("Đóng", null);
        GridView iconGridView = dialogView.findViewById(R.id.iconGridView);
        AlertDialog dialog = builder.create(); // Tạo Dialog

        // Cài đặt GridView và truyền Dialog, ImageView2
        setupIconGridView(iconGridView, dialog, imageView2);

        builder.setTitle("Chọn biểu tượng");
        builder.setNegativeButton("Đóng", (d, which) -> dialog.dismiss());
        dialog.show();
    }




    /**
     * Tạo View hiển thị icon trong Spinner.
     */
    private View createIconView(int position, View convertView, ViewGroup parent, Integer[] icons) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_item_icon, parent, false);
        }
        ImageView imageView = (ImageView) convertView;
        imageView.setImageResource(icons[position]);
        return convertView;
    }

    /**
     * Thêm loại chi mới vào cơ sở dữ liệu.
     */
    private void addNewCategory(String name) {
        databaseHelper.addLoaiChi(name, selectedIcon);
        refreshCategoryList();
        Toast.makeText(getContext(), "Đã thêm loại chi", Toast.LENGTH_SHORT).show();
    }

    /**
     * Hiển thị dialog chỉnh sửa loại chi.
     */
    private void showEditDialog(LoaiChi loaiChi) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_loaichi, null);
        builder.setView(dialogView);

        EditText editTextName = dialogView.findViewById(R.id.editTextName);
        editTextName.setText(loaiChi.getName());
        Button btnChooseIcon = dialogView.findViewById(R.id.btnChooseIcon);
        ImageView imageView2 = dialogView.findViewById(R.id.imageView2);
        imageView2.setImageResource(loaiChi.getIcon());


        btnChooseIcon.setOnClickListener(v -> showIconSelectionDialog(imageView2));


        // Nút lưu
        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String newName = editTextName.getText().toString().trim();
            if (!newName.isEmpty()) {
                loaiChi.setName(newName);
                loaiChi.setIcon(selectedIcon); // Cập nhật icon
                databaseHelper.updateLoaiChi(loaiChi);
                refreshCategoryList();
                Toast.makeText(getContext(), "Đã cập nhật loại chi", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Vui lòng nhập tên", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", null);
        builder.create().show();
    }

    /**
     * Xóa loại chi khỏi cơ sở dữ liệu.
     */
    private void deleteLoaiChi(LoaiChi loaiChi) {
        if (isLoaiChiUsed(loaiChi.getId())) {
            Toast.makeText(getContext(), "Không thể xóa loại chi này vì đã được sử dụng.", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Bạn có chắc chắn muốn xóa loại chi này?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        databaseHelper.deleteLoaiChi(loaiChi.getId());
                        refreshCategoryList();
                        Toast.makeText(getContext(), "Đã xóa loại chi", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Hủy", null)
                    .create()
                    .show();
        }
    }

    private boolean isLoaiChiUsed(int loaiChiId) {
        return databaseHelper.isLoaiChiUsed(loaiChiId);
    }


    /**
     * Làm mới danh sách loại chi từ cơ sở dữ liệu.
     */
    private void refreshCategoryList() {
        loaiChiList.clear();
        loaiChiList.addAll(databaseHelper.getAllLoaiChi());
        adapter.notifyDataSetChanged();
    }
}
