package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.ui.BaoCao.ExpenseIncomeItem;

import java.util.List;

public class ExpenseIncomeAdapter extends RecyclerView.Adapter<ExpenseIncomeAdapter.ViewHolder> {
    private List<ExpenseIncomeItem> items;

    public ExpenseIncomeAdapter(List<ExpenseIncomeItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_baocao, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExpenseIncomeItem item = items.get(position);
        holder.name.setText(item.getName());
        holder.total.setText(String.format("%,.0f", item.getTotal()) + " VND");
        holder.percentage.setText(String.format("%.2f%%", item.getPercentage()));
        holder.icon.setImageResource(item.getIcon());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name, total, percentage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.iconn);
            name = itemView.findViewById(R.id.tenloaii);
            total = itemView.findViewById(R.id.tongtienloai);
            percentage = itemView.findViewById(R.id.phantram);
        }
    }
}

