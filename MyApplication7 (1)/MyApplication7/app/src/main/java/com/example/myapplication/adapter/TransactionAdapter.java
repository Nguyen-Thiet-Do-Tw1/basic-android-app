package com.example.myapplication.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.ui.Chi.KhoanChi;
import com.example.myapplication.ui.Chi.LoaiChi;
import com.example.myapplication.ui.Thu.KhoanThu;
import com.example.myapplication.ui.Thu.LoaiThu;
import com.example.myapplication.ui.database.DatabaseHelper;
import com.example.myapplication.ui.home.Transaction;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    private List<Transaction> transactionList;
    private DatabaseHelper databaseHelper;


    // Cập nhật setData để nhận dữ liệu từ HomeFragment
    public void setData(List<Transaction> transactions, DatabaseHelper dbHelper) {
        this.transactionList = transactions;
        this.databaseHelper = dbHelper;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);

        // Định dạng ngày từ yyyy-MM-dd sang dd/MM/yyyy
        String formattedDate = transaction.getNgay();
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            formattedDate = outputFormat.format(inputFormat.parse(transaction.getNgay()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Xử lý hiển thị khoản chi
        if (transaction.getType() == Transaction.TYPE_KHOAN_CHI) {
//            holder.cardView.setCardBackgroundColor(Color.parseColor("#FFF4C1C1"));
            holder.constraintLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.textViewAmount.setTextColor(Color.parseColor("#FFFF0000"));

            LoaiChi loaiChi = databaseHelper.getLoaiChiById(transaction.getLoaiId());
            if (loaiChi != null) {
                holder.loai.setText(loaiChi.getName());
                holder.icon.setImageResource(loaiChi.getIcon());
            }

            holder.textViewTitle.setText(transaction.getGhiChu());
            holder.textViewAmount.setText("- " + String.format("%,.0f", transaction.getSoTien()));
            holder.date.setText(formattedDate); // Gán ngày đã định dạng
        }
        // Xử lý hiển thị khoản thu
        else if (transaction.getType() == Transaction.TYPE_KHOAN_THU) {
//            holder.cardView.setCardBackgroundColor(Color.parseColor("#FFC0F690"));
            holder.constraintLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.textViewAmount.setTextColor(Color.parseColor("#FF4CFF00"));

            LoaiThu loaiThu = databaseHelper.getLoaiThuById(transaction.getLoaiId());
            if (loaiThu != null) {
                holder.loai.setText(loaiThu.getName());
                holder.icon.setImageResource(loaiThu.getIcon());
            }

            holder.textViewTitle.setText(transaction.getGhiChu());
            holder.textViewAmount.setText("+ " + String.format("%,.0f", transaction.getSoTien()));
            holder.date.setText(formattedDate); // Gán ngày đã định dạng
        }
    }


    @Override
    public int getItemCount() {
        return transactionList != null ? transactionList.size() : 0;
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout constraintLayout;
        TextView textViewTitle, textViewAmount, loai, date;
        ImageView icon;
        CardView cardView;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView= itemView.findViewById(R.id.card_tran);
            date=itemView.findViewById(R.id.txtDatetransaction);
            loai=itemView.findViewById(R.id.txtLoai);
            icon=itemView.findViewById(R.id.icon_transaction);
            constraintLayout = itemView.findViewById(R.id.transaction_layout);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewAmount = itemView.findViewById(R.id.textViewAmount);
        }
    }
}
