package com.example.myapplication.ui.Thu;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.myapplication.R;
import com.example.myapplication.adapter.LoaiThuAdapter;
import com.example.myapplication.ui.Chi.LoaiChi;
import com.example.myapplication.ui.database.DatabaseHelper;
import java.util.List;

public class LoaiThuFragment extends Fragment {

    private LoaiThuViewModel mViewModel;
    private RecyclerView recyclerViewLoaiThu;
    private LoaiThuAdapter loaiThuAdapter;
    private DatabaseHelper databaseHelper;
    private int selectedIcon = R.drawable.ic_menu_camera; // Icon mặc định
    private List<LoaiThu> loaiThuList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loai_thu, container, false);

        // Khởi tạo DatabaseHelper và RecyclerView
        initDatabaseAndRecyclerView(view);

        // Thiết lập sự kiện cho nút "Thêm mới"
        setupAddButton(view);

        return view;
    }

    /**
     * Khởi tạo cơ sở dữ liệu và RecyclerView.
     */
    private void initDatabaseAndRecyclerView(View view) {
        databaseHelper = new DatabaseHelper(getContext());
        recyclerViewLoaiThu = view.findViewById(R.id.recyclerViewLoaiThu);

        // Lấy danh sách loại thu từ cơ sở dữ liệu và thiết lập Adapter
        loaiThuList = databaseHelper.getAllLoaiThu();
        loaiThuAdapter = new LoaiThuAdapter(loaiThuList, new LoaiThuAdapter.OnLoaiThuClickListener() {
            @Override
            public void onEditClick(LoaiThu loaiThu) {
                showEditDialog(loaiThu); // Mở dialog chỉnh sửa
            }

            @Override
            public void onDeleteClick(LoaiThu loaiThu) {
                deleteCategory(loaiThu); // Mở hộp thoại xác nhận xóa
            }
        });

        recyclerViewLoaiThu.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewLoaiThu.setAdapter(loaiThuAdapter);
    }

    /**
     * Thiết lập nút FloatingActionButton để thêm loại thu mới.
     */
    private void setupAddButton(View view) {
        view.findViewById(R.id.fab_add_thu).setOnClickListener(v -> showAddDialog());
    }

    /**
     * Hiển thị dialog để thêm loại thu mới.
     */
    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_loaithu, null);
        builder.setView(dialogView);

        // Khởi tạo các thành phần trong dialog
        EditText editTextName = dialogView.findViewById(R.id.editTextName);
        ImageView imageView2 = dialogView.findViewById(R.id.imageView1);
        Button btnChooseIcon = dialogView.findViewById(R.id.btnChooseIcon1);

        btnChooseIcon.setOnClickListener(v -> showIconSelectionDialog(imageView2));

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String name = editTextName.getText().toString().trim();
            if (!name.isEmpty()) {
                addNewCategory(editTextName);
            } else {
                Toast.makeText(getContext(), "Vui lòng nhập tên", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", null);
        builder.create().show();

    }

    /**
     * Cài đặt Spinner để hiển thị các biểu tượng.
     */
    private void setupIconGridView(GridView iconGridView, AlertDialog dialog, ImageView imageView1) {
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
            imageView1.setImageResource(selectedIcon); // Hiển thị biểu tượng trên ImageView2
            dialog.dismiss(); // Đóng Dialog chọn biểu tượng
        });
    }

    /**
     * Thêm loại thu mới vào cơ sở dữ liệu và cập nhật RecyclerView.
     */
    private void addNewCategory(EditText editTextName) {
        String name = editTextName.getText().toString().trim();
        if (!name.isEmpty()) {
            databaseHelper.addLoaiThu(name, selectedIcon);
            refreshCategoryList();
            Toast.makeText(getContext(), "Đã thêm loại Thu", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Vui lòng nhập tên", Toast.LENGTH_SHORT).show();
        }
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
     * Hiển thị dialog để chỉnh sửa loại thu.
     */
    private void showEditDialog(LoaiThu loaiThu) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_loaithu, null);
        builder.setView(dialogView);

        EditText editTextName = dialogView.findViewById(R.id.editTextName);
        editTextName.setText(loaiThu.getName()); // Hiển thị tên loại thu hiện tại
        ImageView imageView2 = dialogView.findViewById(R.id.imageView1);
        Button btnChooseIcon = dialogView.findViewById(R.id.btnChooseIcon1);
        imageView2.setImageResource(loaiThu.getIcon());

        btnChooseIcon.setOnClickListener(v -> showIconSelectionDialog(imageView2));



        builder.setPositiveButton("Lưu", (dialog, which) -> updateCategory(loaiThu, editTextName, imageView2));
        builder.setNegativeButton("Hủy", null);
        builder.create().show();
    }

    /**
     * Cập nhật thông tin loại thu, bao gồm cả tên và icon.
     */
    private void updateCategory(LoaiThu loaiThu, EditText editTextName, ImageView iconSpinner) {
        String newName = editTextName.getText().toString().trim();
        if (!newName.isEmpty()) {
            loaiThu.setName(newName);
            loaiThu.setIcon(selectedIcon); // Cập nhật icon được chọn
            boolean isUpdated = databaseHelper.updateLoaiThu(loaiThu);
            if (isUpdated) {
                refreshCategoryList();
                Toast.makeText(getContext(), "Đã cập nhật loại Thu", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Vui lòng nhập tên", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Hiển thị hộp thoại xác nhận xóa.
     */


    /**
     * Xóa loại thu khỏi cơ sở dữ liệu và cập nhật RecyclerView.
     */


    private void deleteCategory(LoaiThu loaiThu) {
        if (isLoaiThuUsed(loaiThu.getId())) {
            Toast.makeText(getContext(), "Không thể xóa loại thu này vì đã được sử dụng.", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Bạn có chắc chắn muốn xóa loại thu này?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        databaseHelper.deleteLoaiThu(loaiThu.getId());
                        refreshCategoryList();
                        Toast.makeText(getContext(), "Đã xóa loại thu", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Hủy", null)
                    .create()
                    .show();
        }
    }

    private boolean isLoaiThuUsed(int loaiThuId) {
        return databaseHelper.isLoaiThuUsed(loaiThuId);
    }
    /**
     * Cập nhật danh sách loại thu từ cơ sở dữ liệu và làm mới RecyclerView.
     */
    private void refreshCategoryList() {
        loaiThuList.clear();
        loaiThuList.addAll(databaseHelper.getAllLoaiThu());
        loaiThuAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LoaiThuViewModel.class);
    }
}
