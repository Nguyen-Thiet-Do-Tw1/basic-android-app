package com.example.myapplication.ui.Chi;

public class KhoanChi {
    private int id;
    private double soTien;
    private String ghiChu;
    private String ngay;
    private int loaiChiId;

    public KhoanChi(int id, double soTien, String ghiChu, String ngay, int loaiChiId) {
        this.id = id;
        this.soTien = soTien;
        this.ghiChu = ghiChu;
        this.ngay = ngay;
        this.loaiChiId = loaiChiId;
    }

    public KhoanChi() {
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

    public int getLoaiChiId() {
        return loaiChiId;
    }

    public void setLoaiChiId(int loaiChiId) {
        this.loaiChiId = loaiChiId;
    }
}
