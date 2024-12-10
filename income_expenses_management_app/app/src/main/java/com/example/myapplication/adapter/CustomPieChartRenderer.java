package com.example.myapplication.adapter;

import com.github.mikephil.charting.renderer.PieChartRenderer;
import com.github.mikephil.charting.charts.PieChart;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class CustomPieChartRenderer extends PieChartRenderer {
    private final PieChart pieChart;
    private final double income;
    private final double expense;
    private final Paint textPaint;

    public CustomPieChartRenderer(PieChart pieChart, double income, double expense) {
        super(pieChart, pieChart.getAnimator(), pieChart.getViewPortHandler());
        this.pieChart = pieChart;
        this.income = income;
        this.expense = expense;

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(30);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void drawExtras(Canvas c) {
        super.drawExtras(c);

        float centerX = pieChart.getWidth() / 2f;
        float centerY = pieChart.getHeight() / 2f;

        // Tạo các dòng văn bản
        String incomeText = "Tổng Thu";
        String incomeValue ="+ " + String.format("%,.0f", income) + "VND";


        String expenseText = "Tổng Chi";
        String expenseValue ="- "+ String.format("%,.0f", expense)+ "VND";

        // Vẽ tổng thu bên phải
        c.drawText(incomeText, centerX + 200, centerY - 40, textPaint); // Dòng đầu tiên
        c.drawText(incomeValue, centerX + 200, centerY, textPaint);     // Dòng thứ hai

        // Vẽ tổng chi bên trái
        c.drawText(expenseText, centerX - 200, centerY - 40, textPaint); // Dòng đầu tiên
        c.drawText(expenseValue, centerX - 200, centerY, textPaint);     // Dòng thứ hai
    }


}
