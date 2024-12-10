package com.example.myapplication.ui.BaoCao;

public class ExpenseIncomeItem {
    private String name;
    private int icon;
    private double total;
    private double percentage;

    public ExpenseIncomeItem(String name, int icon, double total) {
        this.name = name;
        this.icon = icon;
        this.total = total;
    }

    public ExpenseIncomeItem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}
