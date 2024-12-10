package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.ui.Thu.KhoanThu;
import com.example.myapplication.ui.Thu.KhoanThuFragment;
import com.example.myapplication.ui.Thu.LoaiThu;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class KhoanThuAdapter extends RecyclerView.Adapter<KhoanThuAdapter.ViewHolder> {
    private List<KhoanThu> khoanThuList;
    private List<LoaiThu> loaiThuList; // Danh sách các loại thu
    private Context context;
    private KhoanThuFragment fragment; // Tham chiếu đến KhoanThuFragment



    public KhoanThuAdapter(List<KhoanThu> khoanThuList, List<LoaiThu> loaiThuList, Context context, KhoanThuFragment fragment) {
        this.khoanThuList = khoanThuList;
        this.loaiThuList = loaiThuList;
        this.context = context;
        this.fragment = fragment; // Khởi tạo tham chiếu KhoanThuFragment

    }

    // Phương thức để tìm loại thu dựa trên id_loai_thu
    private LoaiThu getLoaiThuById(int idLoaiThu) {
        for (LoaiThu loaiThu : loaiThuList) {
            if (loaiThu.getId() == idLoaiThu) {
                return loaiThu;
            }
        }
        return null; // Trả về null nếu không tìm thấy
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_khoan_thu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        KhoanThu khoanThu = khoanThuList.get(position);
        LoaiThu loaiThu = getLoaiThuById(khoanThu.getLoaiThuId());

        // Định dạng ngày từ yyyy-MM-dd sang dd/MM/yyyy
        String formattedDate = khoanThu.getNgay();
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            formattedDate = outputFormat.format(inputFormat.parse(khoanThu.getNgay()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (loaiThu != null) {
            holder.txtLoaiThu.setText(loaiThu.getName());
            holder.iconLoaiThu.setImageResource(loaiThu.getIcon());
        }
        else {
            holder.txtLoaiThu.setText("Chưa phân loại");
            holder.iconLoaiThu.setImageResource(R.drawable.ic_launcher_foreground);
        }

        holder.txtNote.setText(khoanThu.getGhiChu());
        holder.txtDate.setText(formattedDate);
        holder.txtKhoanThu.setText(String.valueOf("+ "+String.format("%,.0f", khoanThu.getSoTien())));
        // Thiết lập sự kiện cho các nút sửa và xóa
        // Sự kiện khi nhấn vào nút sửa
        holder.btnEdit.setOnClickListener(v -> fragment.editKhoanThu(khoanThu));

        // Sự kiện khi nhấn vào nút xóa
        holder.btnDel.setOnClickListener(v -> fragment.deleteKhoanThu(khoanThu));

    }

    @Override
    public int getItemCount() {
        return khoanThuList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtLoaiThu, txtNote, txtDate, txtKhoanThu;
        ImageView btnEdit, btnDel, iconLoaiThu;

        public ViewHolder(View itemView) {
            super(itemView);
            txtLoaiThu = itemView.findViewById(R.id.txtLoaiThu);
            txtNote = itemView.findViewById(R.id.txtNote_thu);
            txtDate = itemView.findViewById(R.id.txtDate_thu);
            txtKhoanThu = itemView.findViewById(R.id.txtKhoanThu);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDel = itemView.findViewById(R.id.btn_del);
            iconLoaiThu = itemView.findViewById(R.id.icon_loaithu);
        }
    }
}



