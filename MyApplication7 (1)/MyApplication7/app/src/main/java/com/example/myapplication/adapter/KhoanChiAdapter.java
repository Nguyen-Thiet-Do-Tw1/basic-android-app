package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.ui.Chi.KhoanChi;
import com.example.myapplication.ui.Chi.KhoanChiFragment;
import com.example.myapplication.ui.Chi.LoaiChi;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class KhoanChiAdapter extends RecyclerView.Adapter<KhoanChiAdapter.ViewHolder> {
    private List<KhoanChi> khoanChiList;
    private List<LoaiChi> loaiChiList; // Danh sách các loại chi
    private Context context;
    private KhoanChiFragment fragment; // Tham chiếu đến KhoanChiFragment

    // Constructor nhận vào danh sách KhoanChi, LoaiChi và fragment
    public KhoanChiAdapter(List<KhoanChi> khoanChiList, List<LoaiChi> loaiChiList, Context context, KhoanChiFragment fragment) {
        this.khoanChiList = khoanChiList;
        this.loaiChiList = loaiChiList;
        this.context = context;
        this.fragment = fragment; // Khởi tạo tham chiếu KhoanChiFragment
    }

    // Phương thức để tìm loại chi dựa trên id_loai_chi
    private LoaiChi getLoaiChiById(int idLoaiChi) {
        for (LoaiChi loaiChi : loaiChiList) {
            if (loaiChi.getId() == idLoaiChi) {
                return loaiChi;
            }
        }
        return null; // Trả về null nếu không tìm thấy
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Tạo view cho mỗi item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_khoan_chi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        KhoanChi khoanChi = khoanChiList.get(position);
        LoaiChi loaiChi = getLoaiChiById(khoanChi.getLoaiChiId());

        // Định dạng ngày từ yyyy-MM-dd sang dd/MM/yyyy
        String formattedDate = khoanChi.getNgay();
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            formattedDate = outputFormat.format(inputFormat.parse(khoanChi.getNgay()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Cập nhật thông tin loại chi và các trường thông tin khác
        if (loaiChi != null) {
            holder.txtLoaiChi.setText(loaiChi.getName());
            holder.iconLoaiChi.setImageResource(loaiChi.getIcon());
        }
        else {
            holder.txtLoaiChi.setText("Chưa phân loại");
            holder.iconLoaiChi.setImageResource(R.drawable.ic_launcher_foreground);
        }

        holder.txtNote.setText(khoanChi.getGhiChu());
        holder.txtDate.setText(formattedDate);
        holder.txtKhoanChi.setText(String.valueOf("- " + String.format("%,.0f", khoanChi.getSoTien())));

        // Thiết lập sự kiện cho các nút sửa và xóa
        holder.btnEdit.setOnClickListener(v -> fragment.editKhoanChi(khoanChi));
        holder.btnDel.setOnClickListener(v -> fragment.deleteKhoanChi(khoanChi));
    }

    @Override
    public int getItemCount() {
        return khoanChiList.size();
    }

    // ViewHolder chứa các thành phần giao diện của mỗi item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtLoaiChi, txtNote, txtDate, txtKhoanChi;
        ImageView btnEdit, btnDel, iconLoaiChi;

        public ViewHolder(View itemView) {
            super(itemView);
            txtLoaiChi = itemView.findViewById(R.id.txtLoaichi);
            txtNote = itemView.findViewById(R.id.txtNote_chi);
            txtDate = itemView.findViewById(R.id.txtDate_chi);
            txtKhoanChi = itemView.findViewById(R.id.txtKhoanchi);
            btnEdit = itemView.findViewById(R.id.btn_edit_chi);
            btnDel = itemView.findViewById(R.id.btn_del_chi);
            iconLoaiChi = itemView.findViewById(R.id.icon_loaichi);
        }
    }
}
