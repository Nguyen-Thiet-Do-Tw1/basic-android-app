package com.example.myapplication.ui.home;

import com.example.myapplication.ui.Chi.KhoanChi;
import com.example.myapplication.ui.Thu.KhoanThu;

public class Transaction {
    public static final int TYPE_KHOAN_CHI = 0;
    public static final int TYPE_KHOAN_THU = 1;

    private int type; // Khoản chi hoặc khoản thu
    private String ghiChu;
    private double soTien;
    private String ngay;
    private int loaiId;

    // Constructor cho khoản chi
    public Transaction(KhoanChi khoanChi) {
        this.type = TYPE_KHOAN_CHI;
        this.ghiChu = khoanChi.getGhiChu();
        this.soTien = khoanChi.getSoTien();
        this.ngay = khoanChi.getNgay();
        this.loaiId = khoanChi.getLoaiChiId();
    }

    // Constructor cho khoản thu
    public Transaction(KhoanThu khoanThu) {
        this.type = TYPE_KHOAN_THU;
        this.ghiChu = khoanThu.getGhiChu();
        this.soTien = khoanThu.getSoTien();
        this.ngay = khoanThu.getNgay();
        this.loaiId = khoanThu.getLoaiThuId();
    }

    public int getType() {
        return type;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public double getSoTien() {
        return soTien;
    }

    public String getNgay() {
        return ngay;
    }

    public int getLoaiId() {
        return loaiId;
    }
}

