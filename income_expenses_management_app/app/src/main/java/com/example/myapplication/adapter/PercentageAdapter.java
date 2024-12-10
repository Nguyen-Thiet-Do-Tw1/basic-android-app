package com.example.myapplication.adapter;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

public class PercentageAdapter extends RecyclerView.Adapter<PercentageAdapter.ViewHolder> {

    private final List<Pair<String, Pair<Double, Double>>> dataList;

    // Dữ liệu truyền vào là danh sách các cặp <Tên Loại, <Phần Trăm, Tổng Tiền>>
    public PercentageAdapter(List<Pair<String, Pair<Double, Double>>> dataList) {
        this.dataList = dataList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCategory, txtPercentage, txtAmount;

        public ViewHolder(View itemView) {
            super(itemView);
            txtCategory = itemView.findViewById(R.id.txtCategory);
            txtPercentage = itemView.findViewById(R.id.txtPercentage);
            txtAmount = itemView.findViewById(R.id.txtAmount);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_percentage, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Lấy dữ liệu
        Pair<String, Pair<Double, Double>> item = dataList.get(position);
        String categoryName = item.first; // Tên loại
        double percentage = item.second.first; // Phần trăm
        double amount = item.second.second; // Tổng tiền

        // Gán dữ liệu cho các TextView
        holder.txtCategory.setText(categoryName);
        holder.txtPercentage.setText(String.format("%.2f%%", percentage));
        holder.txtAmount.setText(String.format("%.0f VND", amount));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
