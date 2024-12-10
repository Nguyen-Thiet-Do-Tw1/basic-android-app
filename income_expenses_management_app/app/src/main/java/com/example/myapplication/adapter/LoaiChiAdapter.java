package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.ui.Chi.LoaiChi;

import java.util.List;

public class LoaiChiAdapter extends RecyclerView.Adapter<LoaiChiAdapter.ViewHolder> {
    private List<LoaiChi> loaiChiList;
    private OnLoaiChiClickListener listener;

    // Interface để xử lý sự kiện click
    public interface OnLoaiChiClickListener {
        void onEditClick(LoaiChi loaiChi);
        void onDeleteClick(LoaiChi loaiChi);
    }

    // Constructor nhận listener để xử lý sự kiện click
    public LoaiChiAdapter(List<LoaiChi> loaiChiList, OnLoaiChiClickListener listener) {
        this.loaiChiList = loaiChiList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loai_chi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LoaiChi loaiChi = loaiChiList.get(position);
        holder.nameTextView.setText(loaiChi.getName());
        holder.iconImageView.setImageResource(loaiChi.getIcon());

        // Xử lý sự kiện click vào nút sửa
        holder.editImageView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(loaiChi);
            }
        });

        // Xử lý sự kiện click vào nút xóa
        holder.deleteImageView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(loaiChi);
            }
        });
    }

    @Override
    public int getItemCount() {
        return loaiChiList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        ImageView iconImageView;
        ImageView editImageView;
        ImageView deleteImageView;

        ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name_chi);
            iconImageView = itemView.findViewById(R.id.icon_chi);
            editImageView = itemView.findViewById(R.id.btn_edit_chi);
            deleteImageView = itemView.findViewById(R.id.btn_del_chi);
        }
    }
}
