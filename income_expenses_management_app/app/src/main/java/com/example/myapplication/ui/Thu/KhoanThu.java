package com.example.myapplication.ui.Thu;

public class KhoanThu {
    private int id;
    private double soTien;
    private String ghiChu;
    private String ngay;
    private int loaiThuId;

    public KhoanThu(int id, double soTien, String ghiChu, String ngay, int loaiThuId) {
        this.id = id;
        this.soTien = soTien;
        this.ghiChu = ghiChu;
        this.ngay = ngay;
        this.loaiThuId = loaiThuId;
    }

    public KhoanThu() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getSoTien() {
        return soTien;
    }

    public void setSoTien(double soTien) {
        this.soTien = soTien;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }

    public int getLoaiThuId() {
        return loaiThuId;
    }

    public void setLoaiThuId(int loaiThuId) {
        this.loaiThuId = loaiThuId;
    }
}
