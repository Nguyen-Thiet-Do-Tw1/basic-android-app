package com.example.myapplication.ui.Chi;

public class LoaiChi {
    private int id;
    private String name;
    private int icon;

    public LoaiChi(int id, String name, int icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    public LoaiChi() {
    }
    @Override
    public String toString() {
        return this.name; // Trả về tên của loại chi để hiển thị trong Spinner
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
