package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.ui.Thu.LoaiThu;

import java.util.List;

public class LoaiThuAdapter extends RecyclerView.Adapter<LoaiThuAdapter.ViewHolder> {
    private List<LoaiThu> loaiThuList;
    private LoaiThuAdapter.OnLoaiThuClickListener listener;

    public interface OnLoaiThuClickListener {
        void onEditClick(LoaiThu loaiThu);
        void onDeleteClick(LoaiThu loaiThu);
    }
    // Constructor nhận listener để xử lý sự kiện click
    public LoaiThuAdapter(List<LoaiThu> loaiThuList, LoaiThuAdapter.OnLoaiThuClickListener listener) {
        this.loaiThuList = loaiThuList;
        this.listener = listener;
    }
    public LoaiThuAdapter(List<LoaiThu> loaiThuList) {
        this.loaiThuList = loaiThuList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loai_thu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LoaiThu loaiThu = loaiThuList.get(position);
        holder.nameTextView.setText(loaiThu.getName());
        holder.iconImageView.setImageResource(loaiThu.getIcon());
//         Xử lý sự kiện click vào nút sửa
        holder.edit_thuImageView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(loaiThu);
            }
        });

        // Xử lý sự kiện click vào nút xóa
        holder.delete_thuImageView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(loaiThu);
            }
        });
    }

    @Override
    public int getItemCount() {
        return loaiThuList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        ImageView iconImageView;
        ImageView edit_thuImageView;
        ImageView delete_thuImageView;

        ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name_thu);
            iconImageView = itemView.findViewById(R.id.icon_thu);
            edit_thuImageView = itemView.findViewById(R.id.btn_edit_thu);
            delete_thuImageView = itemView.findViewById(R.id.btn_del_thu);
        }
    }
}
