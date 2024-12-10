package com.example.myapplication.ui.Thu;

public class LoaiThu {
    private int id;
    private String name;
    private int icon;

    public LoaiThu(int id, String name, int icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    public LoaiThu() {
    }
    @Override
    public String toString() {
        return this.name; // Trả về tên của loại thu để hiển thị trong Spinner
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
