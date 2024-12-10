package com.example.myapplication.ui.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.myapplication.R;
import com.example.myapplication.ui.BaoCao.ExpenseIncomeItem;
import com.example.myapplication.ui.Chi.KhoanChi;
import com.example.myapplication.ui.Thu.KhoanThu;

import com.example.myapplication.ui.Chi.LoaiChi;
import com.example.myapplication.ui.Thu.LoaiThu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "qlThuChi";
    private static final int DATABASE_VERSION = 5;

    // Table Loại Chi
    public static final String TABLE_LOAI_CHI = "LoaiChi";
    public static final String COLUMN_LOAI_CHI_ID = "id";
    public static final String COLUMN_LOAI_CHI_NAME = "ten";
    public static final String COLUMN_LOAI_CHI_ICON = "src_icon";

    // Table Loại Thu
    public static final String TABLE_LOAI_THU = "LoaiThu";
    public static final String COLUMN_LOAI_THU_ID = "id";
    public static final String COLUMN_LOAI_THU_NAME = "ten";
    public static final String COLUMN_LOAI_THU_ICON = "src_icon";

    // Table Khoan Chi
    public static final String TABLE_KHOAN_CHI = "KhoanChi";
    public static final String COLUMN_KHOAN_CHI_ID = "id";
    public static final String COLUMN_KHOAN_CHI_AMOUNT = "so_tien";
    public static final String COLUMN_KHOAN_CHI_NOTE = "ghi_chu";
    public static final String COLUMN_KHOAN_CHI_DATE = "ngay";
    public static final String COLUMN_KHOAN_CHI_LOAI_CHI_ID = "loai_chi_id";

    // Table Khoan Thu
    public static final String TABLE_KHOAN_THU = "KhoanThu";
    public static final String COLUMN_KHOAN_THU_ID = "id";
    public static final String COLUMN_KHOAN_THU_AMOUNT = "so_tien";
    public static final String COLUMN_KHOAN_THU_NOTE = "ghi_chu";
    public static final String COLUMN_KHOAN_THU_DATE = "ngay";
    public static final String COLUMN_KHOAN_THU_LOAI_THU_ID = "loai_thu_id";


    // Bảng Hạn mức chi tiêu hàng tháng
    public static final String TABLE_MONTHLY_LIMIT = "MonthlyLimit";
    public static final String COLUMN_MONTHLY_LIMIT_ID = "id";
    public static final String COLUMN_MONTHLY_LIMIT_MONTH = "thang"; // YYYY-MM format
    public static final String COLUMN_MONTHLY_LIMIT_AMOUNT = "han_muc"; // hạn mức

    private static final String CREATE_TABLE_MONTHLY_LIMIT =
            "CREATE TABLE " + TABLE_MONTHLY_LIMIT + " (" +
                    COLUMN_MONTHLY_LIMIT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_MONTHLY_LIMIT_MONTH + " TEXT UNIQUE, " + // Tháng phải là duy nhất
                    COLUMN_MONTHLY_LIMIT_AMOUNT + " REAL)";


    // SQL statements to create tables
    private static final String CREATE_TABLE_LOAI_CHI =
            "CREATE TABLE " + TABLE_LOAI_CHI + " (" +
                    COLUMN_LOAI_CHI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_LOAI_CHI_NAME + " TEXT, " +
                    COLUMN_LOAI_CHI_ICON + " INTEGER)";

    private static final String CREATE_TABLE_LOAI_THU =
            "CREATE TABLE " + TABLE_LOAI_THU + " (" +
                    COLUMN_LOAI_THU_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_LOAI_THU_NAME + " TEXT, " +
                    COLUMN_LOAI_THU_ICON + " INTEGER)";

    private static final String CREATE_TABLE_KHOAN_CHI =
            "CREATE TABLE " + TABLE_KHOAN_CHI + " (" +
                    COLUMN_KHOAN_CHI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_KHOAN_CHI_AMOUNT + " REAL, " +
                    COLUMN_KHOAN_CHI_NOTE + " TEXT, " +
                    COLUMN_KHOAN_CHI_DATE + " TEXT, " +
                    COLUMN_KHOAN_CHI_LOAI_CHI_ID + " INTEGER, " +
                    "FOREIGN KEY(" + COLUMN_KHOAN_CHI_LOAI_CHI_ID + ") REFERENCES " +
                    TABLE_LOAI_CHI + "(" + COLUMN_LOAI_CHI_ID + "))";

    private static final String CREATE_TABLE_KHOAN_THU =
            "CREATE TABLE " + TABLE_KHOAN_THU + " (" +
                    COLUMN_KHOAN_THU_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_KHOAN_THU_AMOUNT + " REAL, " +
                    COLUMN_KHOAN_THU_NOTE + " TEXT, " +
                    COLUMN_KHOAN_THU_DATE + " TEXT, " +
                    COLUMN_KHOAN_THU_LOAI_THU_ID + " INTEGER, " +
                    "FOREIGN KEY(" + COLUMN_KHOAN_THU_LOAI_THU_ID + ") REFERENCES " +
                    TABLE_LOAI_THU + "(" + COLUMN_LOAI_THU_ID + "))";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(CREATE_TABLE_LOAI_CHI);
//        db.execSQL(CREATE_TABLE_LOAI_THU);
//        db.execSQL(CREATE_TABLE_KHOAN_CHI);
//        db.execSQL(CREATE_TABLE_KHOAN_THU);
//        insertDefaultLoaiChi(db);
//        insertDefaultLoaiThu(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KHOAN_CHI);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KHOAN_THU);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOAI_CHI);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOAI_THU);
//        onCreate(db);
        if (oldVersion < 5) { // DATABASE_VERSION = 5
            db.execSQL(CREATE_TABLE_MONTHLY_LIMIT);
        }
    }

    public long addLoaiChi(String ten, int srcIcon) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LOAI_CHI_NAME, ten);
        values.put(COLUMN_LOAI_CHI_ICON, srcIcon);
        long id = db.insert(TABLE_LOAI_CHI, null, values);
        db.close();
        return id;
    }
    public boolean updateLoaiChi(LoaiChi loaiChi) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LOAI_CHI_NAME, loaiChi.getName());  // Use the correct column name
        values.put(COLUMN_LOAI_CHI_ICON, loaiChi.getIcon()); // Use the correct column name

        int rowsAffected = db.update(TABLE_LOAI_CHI, values, "id = ?", new String[]{String.valueOf(loaiChi.getId())});
        db.close();
        return rowsAffected > 0;
    }



    public boolean isLoaiChiUsed(int loaiChiId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM KhoanChi WHERE loai_chi_id = ?", new String[]{String.valueOf(loaiChiId)});
        boolean isUsed = cursor.moveToFirst();
        cursor.close();
        db.close();
        return isUsed;
    }
    public boolean isLoaiThuUsed(int loaiThuId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM KhoanThu WHERE loai_thu_id = ?", new String[]{String.valueOf(loaiThuId)});
        boolean isUsed = cursor.moveToFirst();
        cursor.close();
        db.close();
        return isUsed;
    }


    public boolean deleteLoaiThu(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_LOAI_THU, COLUMN_LOAI_THU_ID + " = ?", new String[]{String.valueOf(id)});
        if (rowsDeleted > 0) {
            resetAutoIncrement(TABLE_LOAI_THU, COLUMN_LOAI_THU_ID);
        }
        db.close();
        return rowsDeleted > 0;
    }

    public boolean deleteLoaiChi(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_LOAI_CHI, COLUMN_LOAI_CHI_ID +" = ?", new String[]{String.valueOf(id)});
        // Đặt lại giá trị AUTO_INCREMENT về id cao nhất hiện có
        if (rowsDeleted > 0) {
            resetAutoIncrement(TABLE_LOAI_CHI, COLUMN_LOAI_CHI_ID);
        }
        db.close();
        return rowsDeleted > 0;
    }


    public boolean updateLoaiThu(LoaiThu loaiThu) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LOAI_THU_NAME, loaiThu.getName()); // Cập nhật tên
        values.put(COLUMN_LOAI_THU_ICON, loaiThu.getIcon()); // Cập nhật icon

        int rowsAffected = db.update(TABLE_LOAI_THU, values, COLUMN_LOAI_THU_ID + " = ?", new String[]{String.valueOf(loaiThu.getId())});
        db.close();
        return rowsAffected > 0; // Trả về true nếu cập nhật thành công
    }




    private void resetAutoIncrement(String tableName, String idColumn) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT MAX(" + idColumn + ") AS max_id FROM " + tableName, null);
        int maxId = 0;
        if (cursor.moveToFirst() && cursor.getColumnIndex("max_id") != -1) { // Check column index
            maxId = cursor.getInt(cursor.getColumnIndexOrThrow("max_id"));
        }
        cursor.close();

        db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = " + maxId + " WHERE name = '" + tableName + "'");
    }


    public long addLoaiThu(String ten, int srcIcon) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LOAI_THU_NAME, ten);
        values.put(COLUMN_LOAI_THU_ICON, srcIcon);
        long id = db.insert(TABLE_LOAI_THU, null, values);
        db.close();
        return id;
    }
    public long addKhoanChi(double soTien, String ghiChu, String ngay, int loaiChiId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_KHOAN_CHI_AMOUNT, soTien);
        values.put(COLUMN_KHOAN_CHI_NOTE, ghiChu);
        values.put(COLUMN_KHOAN_CHI_DATE, ngay);
        values.put(COLUMN_KHOAN_CHI_LOAI_CHI_ID, loaiChiId);
        long id = db.insert(TABLE_KHOAN_CHI, null, values);
        db.close();
        return id;
    }
    // Hàm cập nhật thông tin KhoanChi
    public boolean updateKhoanChi(KhoanChi khoanChi) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_KHOAN_CHI_AMOUNT, khoanChi.getSoTien());
        values.put(COLUMN_KHOAN_CHI_NOTE, khoanChi.getGhiChu());
        values.put(COLUMN_KHOAN_CHI_DATE, khoanChi.getNgay());
        values.put(COLUMN_KHOAN_CHI_LOAI_CHI_ID, khoanChi.getLoaiChiId());

        // Thực hiện cập nhật bản ghi dựa trên id
        int rowsAffected = db.update(TABLE_KHOAN_CHI, values, COLUMN_KHOAN_CHI_ID + " = ?", new String[]{String.valueOf(khoanChi.getId())});
        db.close();
        return rowsAffected > 0; // Trả về true nếu cập nhật thành công
    }
    public double getRemainingLimit(String month) {
        SQLiteDatabase db = this.getReadableDatabase();
        double totalSpent = 0;
        double limit = 0;

        // Thêm log để bắt đầu hàm
        Log.d("getRemainingLimit", "Bắt đầu tính toán hạn mức còn lại cho tháng: " + month);

        // Tính tổng chi của tháng
        String queryTotalSpent = "SELECT SUM(" + COLUMN_KHOAN_CHI_AMOUNT + ") FROM " + TABLE_KHOAN_CHI +
                " WHERE strftime('%m-%Y', " + COLUMN_KHOAN_CHI_DATE + ") = ?";
        Cursor cursorSpent = db.rawQuery(queryTotalSpent, new String[]{month});
        if (cursorSpent != null) {
            if (cursorSpent.moveToFirst()) {
                totalSpent = cursorSpent.getDouble(0);
                Log.d("getRemainingLimit", "Tổng chi của tháng " + month + ": " + totalSpent);
            } else {
                Log.d("getRemainingLimit", "Không có khoản chi nào cho tháng " + month);
            }
            cursorSpent.close();
        } else {
            Log.e("getRemainingLimit", "Lỗi khi truy vấn tổng chi cho tháng " + month);
        }
        /*private static final String CREATE_TABLE_MONTHLY_LIMIT =
            "CREATE TABLE " + TABLE_MONTHLY_LIMIT + " (" +
                    COLUMN_MONTHLY_LIMIT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_MONTHLY_LIMIT_MONTH + " TEXT UNIQUE, " + // Tháng phải là duy nhất
                    COLUMN_MONTHLY_LIMIT_AMOUNT + " REAL)";*/

        // Lấy hạn mức tháng
//        String queryLimit = "SELECT SUM(" + COLUMN_MONTHLY_LIMIT_AMOUNT + ") FROM " + TABLE_MONTHLY_LIMIT +
//                " WHERE strftime('%Y-%m', " + COLUMN_MONTHLY_LIMIT_MONTH + ") = ?";
//        Cursor cursorLimit = db.rawQuery(queryLimit, new String[]{month});
//        if (cursorLimit != null) {
//            if (cursorLimit.moveToFirst()) {
//                limit = cursorLimit.getDouble(0);
//                Log.d("getRemainingLimit", "Hạn mức của tháng " + month + ": " + limit);
//            } else {
//                Log.d("getRemainingLimit", "Không có hạn mức nào được thiết lập cho tháng " + month);
//            }
//            cursorLimit.close();
//        } else {
//            Log.e("getRemainingLimit", "Lỗi khi truy vấn hạn mức cho tháng " + month);
//        }
        limit = getMonthlyLimit(month);
        Log.d("getRemainingLimit", "Hạn mức của tháng " + month + ": " + limit);

        db.close();

        // Tính toán hạn mức còn lại
        double remainingLimit = limit - totalSpent;
        Log.d("getRemainingLimit", "Hạn mức còn lại cho tháng " + month + ": " + remainingLimit);

        return remainingLimit;
    }


    public double getTotalChiByMonth(String month) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(" + COLUMN_KHOAN_CHI_AMOUNT + ") FROM " + TABLE_KHOAN_CHI +
                " WHERE strftime('%Y-%m', " + COLUMN_KHOAN_CHI_DATE + ") = ?";
        Cursor cursor = db.rawQuery(query, new String[]{month});
        double totalChi = 0;
        if (cursor.moveToFirst()) {
            totalChi = cursor.getDouble(0);
        }
        cursor.close();
        return totalChi;
    }



    // Hàm xóa KhoanChi
    public boolean deleteKhoanChi(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_KHOAN_CHI, COLUMN_KHOAN_CHI_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsDeleted > 0; // Trả về true nếu xóa thành công
    }


    public long addKhoanThu(double soTien, String ghiChu, String ngay, int loaiThuId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_KHOAN_THU_AMOUNT, soTien);
        values.put(COLUMN_KHOAN_THU_NOTE, ghiChu);
        values.put(COLUMN_KHOAN_THU_DATE, ngay);
        values.put(COLUMN_KHOAN_THU_LOAI_THU_ID, loaiThuId);
        long id = db.insert(TABLE_KHOAN_THU, null, values);
        db.close();
        return id;
    }
    // Hàm cập nhật thông tin KhoanThu
    public boolean updateKhoanThu(KhoanThu khoanThu) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_KHOAN_THU_AMOUNT, khoanThu.getSoTien());
        values.put(COLUMN_KHOAN_THU_NOTE, khoanThu.getGhiChu());
        values.put(COLUMN_KHOAN_THU_DATE, khoanThu.getNgay());
        values.put(COLUMN_KHOAN_THU_LOAI_THU_ID, khoanThu.getLoaiThuId());

        // Thực hiện cập nhật bản ghi dựa trên id
        int rowsAffected = db.update(TABLE_KHOAN_THU, values, COLUMN_KHOAN_THU_ID + " = ?", new String[]{String.valueOf(khoanThu.getId())});
        db.close();
        return rowsAffected > 0; // Trả về true nếu cập nhật thành công
    }

    // Hàm xóa KhoanThu
    public boolean deleteKhoanThu(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_KHOAN_THU, COLUMN_KHOAN_THU_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsDeleted > 0; // Trả về true nếu xóa thành công
    }

    // Lấy tất cả các khoản chi
    public List<KhoanChi> getAllKhoanChi() {
        List<KhoanChi> khoanChiList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_KHOAN_CHI, null, null, null, null, null, COLUMN_KHOAN_CHI_DATE + " DESC");

        if (cursor.moveToFirst()) {
            String[] columnNames = cursor.getColumnNames();
            for (String column : columnNames) {
                Log.d("DatabaseHelper", "Column: " + column); // Debug columns
            }

            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_KHOAN_CHI_ID));
                double soTien = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_KHOAN_CHI_AMOUNT));
                String ghiChu = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KHOAN_CHI_NOTE));
                String ngay = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KHOAN_CHI_DATE));
                int loaiChiId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_KHOAN_CHI_LOAI_CHI_ID));

                KhoanChi khoanChi = new KhoanChi(id, soTien, ghiChu, ngay, loaiChiId);
                khoanChiList.add(khoanChi);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return khoanChiList;
    }


    // Lấy tất cả các khoản thu
    public List<KhoanThu> getAllKhoanThu() {
        List<KhoanThu> khoanThuList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_KHOAN_THU, null, null, null, null, null, COLUMN_KHOAN_THU_DATE + " DESC");

        if (cursor.moveToFirst()) {
            String[] columnNames = cursor.getColumnNames();
            for (String column : columnNames) {
                Log.d("DatabaseHelper", "Column: " + column); // Debug columns
            }
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_KHOAN_THU_ID));
                double soTien = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_KHOAN_THU_AMOUNT));
                String ghiChu = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KHOAN_THU_NOTE));
                String ngay = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KHOAN_THU_DATE));
                int loaiThuId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_KHOAN_THU_LOAI_THU_ID));

                KhoanThu khoanThu = new KhoanThu(id, soTien, ghiChu, ngay, loaiThuId);
                khoanThuList.add(khoanThu);
            } while (cursor.moveToNext());
        }
        cursor.close();
        Log.d("DatabaseHelper", "Số lượng khoản thu: " + khoanThuList.size());

        db.close();
        return khoanThuList;
    }

    public List<LoaiChi> getAllLoaiChi() {
        List<LoaiChi> loaiChiList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_LOAI_CHI, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LOAI_CHI_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOAI_CHI_NAME));
                int iconId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LOAI_CHI_ICON));
                loaiChiList.add(new LoaiChi(id, name, iconId));
            } while (cursor.moveToNext());
        }
        cursor.close();

        Log.d("DatabaseHelper", "Số lượng loại chi: " + loaiChiList.size());
        return loaiChiList;
    }

    public List<LoaiThu> getAllLoaiThu() {
        List<LoaiThu> loaiThuList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_LOAI_THU, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LOAI_THU_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOAI_THU_NAME));
                int iconId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LOAI_THU_ICON));
                loaiThuList.add(new LoaiThu(id, name, iconId));
            } while (cursor.moveToNext());
        }
        cursor.close();

        Log.d("DatabaseHelper", "Số lượng loại thu: " + loaiThuList.size());
        return loaiThuList;
    }

//    private void insertDefaultLoaiChi(SQLiteDatabase db) {
//        ContentValues values1 = new ContentValues();
//        values1.put(COLUMN_LOAI_CHI_NAME, "Ăn uống");
//        values1.put(COLUMN_LOAI_CHI_ICON, R.drawable.ic_menu_camera);
//        db.insert(TABLE_LOAI_CHI, null, values1);
//
//        ContentValues values2 = new ContentValues();
//        values2.put(COLUMN_LOAI_CHI_NAME, "Chi tiêu hàng ngày");
//        values2.put(COLUMN_LOAI_CHI_ICON, R.drawable.ic_menu_camera);
//        db.insert(TABLE_LOAI_CHI, null, values2);
//
//        ContentValues values3 = new ContentValues();
//        values3.put(COLUMN_LOAI_CHI_NAME, "Quần áo");
//        values3.put(COLUMN_LOAI_CHI_ICON, R.drawable.ic_menu_camera);
//        db.insert(TABLE_LOAI_CHI, null, values3);
//    }
//
//    private void insertDefaultLoaiThu(SQLiteDatabase db) {
//        ContentValues values1 = new ContentValues();
//        values1.put(COLUMN_LOAI_THU_NAME, "Lương");
//        values1.put(COLUMN_LOAI_THU_ICON, R.drawable.ic_menu_camera);
//        db.insert(TABLE_LOAI_THU, null, values1);
//
//        ContentValues values2 = new ContentValues();
//        values2.put(COLUMN_LOAI_THU_NAME, "Tiền Thưởng");
//        values2.put(COLUMN_LOAI_THU_ICON, R.drawable.ic_menu_camera);
//        db.insert(TABLE_LOAI_THU, null, values2);
//
//        ContentValues values3 = new ContentValues();
//        values3.put(COLUMN_LOAI_THU_NAME, "Đầu Tư");
//        values3.put(COLUMN_LOAI_THU_ICON, R.drawable.ic_menu_camera);
//        db.insert(TABLE_LOAI_THU, null, values3);
//    }

    public double getTotalThu() {
        double totalThu = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_KHOAN_THU_AMOUNT + ") FROM " + TABLE_KHOAN_THU, null);

        if (cursor.moveToFirst()) {
            totalThu = cursor.getDouble(0); // lấy giá trị từ cột đầu tiên
        }
        cursor.close();
        db.close();
        return totalThu;
    }
    public double getTotalChi() {
        double totalChi = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_KHOAN_CHI_AMOUNT + ") FROM " + TABLE_KHOAN_CHI, null);

        if (cursor.moveToFirst()) {
            totalChi = cursor.getDouble(0); // lấy giá trị từ cột đầu tiên
        }
        cursor.close();
        db.close();
        return totalChi;
    }
    public double getSoDu() {
        double totalThu = getTotalThu();
        double totalChi = getTotalChi();
        return totalThu - totalChi;
    }
    public List<KhoanChi> getKhoanChiByDateRange(String startDate, String endDate) {
        List<KhoanChi> khoanChiList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_KHOAN_CHI + " WHERE " + COLUMN_KHOAN_CHI_DATE + " BETWEEN ? AND ?";
        Cursor cursor = db.rawQuery(query, new String[]{startDate, endDate});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_KHOAN_CHI_ID));
                double soTien = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_KHOAN_CHI_AMOUNT));
                String ghiChu = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KHOAN_CHI_NOTE));
                String ngay = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KHOAN_CHI_DATE));
                int loaiChiId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_KHOAN_CHI_LOAI_CHI_ID));

                khoanChiList.add(new KhoanChi(id, soTien, ghiChu, ngay, loaiChiId));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return khoanChiList;
    }

    public List<KhoanThu> getKhoanThuByDateRange(String startDate, String endDate) {
        List<KhoanThu> khoanThuList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_KHOAN_THU + " WHERE " + COLUMN_KHOAN_THU_DATE + " BETWEEN ? AND ?";
        Cursor cursor = db.rawQuery(query, new String[]{startDate, endDate});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_KHOAN_THU_ID));
                double soTien = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_KHOAN_THU_AMOUNT));
                String ghiChu = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KHOAN_THU_NOTE));
                String ngay = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KHOAN_THU_DATE));
                int loaithuId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_KHOAN_THU_LOAI_THU_ID));
                khoanThuList.add(new KhoanThu(id, soTien, ghiChu, ngay, loaithuId));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return khoanThuList;
    }
    public double getTotalThuByDateRange(String startDate, String endDate) {
        double totalThu = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(" + COLUMN_KHOAN_THU_AMOUNT + ") FROM " + TABLE_KHOAN_THU +
                " WHERE " + COLUMN_KHOAN_THU_DATE + " BETWEEN ? AND ?";
        Cursor cursor = db.rawQuery(query, new String[]{startDate, endDate});

        if (cursor.moveToFirst()) {
            totalThu = cursor.getDouble(0); // Cột đầu tiên chứa tổng
        }
        cursor.close();
        db.close();
        return totalThu;
    }
    public double getTotalChiByDateRange(String startDate, String endDate) {
        double totalChi = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(" + COLUMN_KHOAN_CHI_AMOUNT + ") FROM " + TABLE_KHOAN_CHI +
                " WHERE " + COLUMN_KHOAN_CHI_DATE + " BETWEEN ? AND ?";
        Cursor cursor = db.rawQuery(query, new String[]{startDate, endDate});

        if (cursor.moveToFirst()) {
            totalChi = cursor.getDouble(0); // Cột đầu tiên chứa tổng
        }
        cursor.close();
        db.close();
        return totalChi;
    }
    // Lấy thông tin LoạiThu từ loaiId
    public LoaiThu getLoaiThuById(int loaiId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_LOAI_THU, new String[]{COLUMN_LOAI_THU_ID, COLUMN_LOAI_THU_NAME, COLUMN_LOAI_THU_ICON},
                COLUMN_LOAI_THU_ID + " = ?", new String[]{String.valueOf(loaiId)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            LoaiThu loaiThu = new LoaiThu(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LOAI_THU_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOAI_THU_NAME)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LOAI_THU_ICON))
            );
            cursor.close();
            return loaiThu;
        }
        cursor.close();
        return null;
    }

    // Lấy thông tin LoạiChi từ loaiId
    public LoaiChi getLoaiChiById(int loaiId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_LOAI_CHI, new String[]{COLUMN_LOAI_CHI_ID, COLUMN_LOAI_CHI_NAME, COLUMN_LOAI_CHI_ICON},
                COLUMN_LOAI_CHI_ID + " = ?", new String[]{String.valueOf(loaiId)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            LoaiChi loaiChi = new LoaiChi(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LOAI_CHI_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOAI_CHI_NAME)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LOAI_CHI_ICON))
            );
            cursor.close();
            return loaiChi;
        }
        cursor.close();
        return null;
    }
    public Map<String, Double> getTotalExpenseByCategory() {
        Map<String, Double> expenseByCategory = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + COLUMN_LOAI_CHI_NAME + ", SUM(" + COLUMN_KHOAN_CHI_AMOUNT + ") as TotalAmount " +
                "FROM " + TABLE_KHOAN_CHI + " INNER JOIN " + TABLE_LOAI_CHI +
                " ON " + TABLE_KHOAN_CHI + "." + COLUMN_KHOAN_CHI_LOAI_CHI_ID + " = " + TABLE_LOAI_CHI + "." + COLUMN_LOAI_CHI_ID +
                " GROUP BY " + COLUMN_LOAI_CHI_NAME;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String categoryName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOAI_CHI_NAME));
                double totalAmount = cursor.getDouble(cursor.getColumnIndexOrThrow("TotalAmount"));
                expenseByCategory.put(categoryName, totalAmount);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return expenseByCategory;
    }
    private double getTotalIncome() {
        SQLiteDatabase db = this.getReadableDatabase();
        double totalIncome = 0;

        String query = "SELECT SUM(" + COLUMN_KHOAN_THU_AMOUNT + ") FROM " + TABLE_KHOAN_THU;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            totalIncome = cursor.getDouble(0); // Lấy giá trị tổng thu nhập
        }

        cursor.close();
        db.close();

        return totalIncome;
    }


    public Map<String, Double> getTotalIncomeByCategory() {
        Map<String, Double> incomeByCategory = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + COLUMN_LOAI_THU_NAME + ", SUM(" + COLUMN_KHOAN_THU_AMOUNT + ") as TotalAmount " +
                "FROM " + TABLE_KHOAN_THU + " INNER JOIN " + TABLE_LOAI_THU +
                " ON " + TABLE_KHOAN_THU + "." + COLUMN_KHOAN_THU_LOAI_THU_ID + " = " + TABLE_LOAI_THU + "." + COLUMN_LOAI_THU_ID +
                " GROUP BY " + COLUMN_LOAI_THU_NAME;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String categoryName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOAI_THU_NAME));
                double totalAmount = cursor.getDouble(cursor.getColumnIndexOrThrow("TotalAmount"));
                incomeByCategory.put(categoryName, totalAmount);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return incomeByCategory;
    }
    public String getLoaiChiNameById(int idLoaiChi) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_LOAI_CHI_NAME + " FROM " + TABLE_LOAI_CHI +
                " WHERE " + COLUMN_LOAI_CHI_ID + " = ?";
        Log.d("DB_QUERY", "Query: " + query);  // In câu truy vấn để kiểm tra

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idLoaiChi)});

        if (cursor != null && cursor.moveToFirst()) {
            // Kiểm tra và in ra tên cột
            int columnIndex = cursor.getColumnIndex(COLUMN_LOAI_CHI_NAME);
            if (columnIndex != -1) {
                String loaiChiName = cursor.getString(columnIndex);
                cursor.close();
                return loaiChiName;
            } else {
                Log.e("DB_ERROR", "Column " + COLUMN_LOAI_CHI_NAME + " not found.");
                cursor.close();
                return null;
            }
        } else {
            cursor.close();
            return null;  // Nếu không tìm thấy, trả về null
        }
    }
    public String getLoaiThuNameById(int idLoaiThu) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_LOAI_THU_NAME + " FROM " + TABLE_LOAI_THU +
                " WHERE " + COLUMN_LOAI_THU_ID + " = ?";
        Log.d("DB_QUERY", "Query: " + query);  // In câu truy vấn để kiểm tra

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idLoaiThu)});

        if (cursor != null && cursor.moveToFirst()) {
            // Kiểm tra và in ra tên cột
            int columnIndex = cursor.getColumnIndex(COLUMN_LOAI_THU_NAME);
            if (columnIndex != -1) {
                String loaiThuName = cursor.getString(columnIndex);
                cursor.close();
                return loaiThuName;
            } else {
                Log.e("DB_ERROR", "Column " + COLUMN_LOAI_THU_NAME + " not found.");
                cursor.close();
                return null;
            }
        } else {
            cursor.close();
            return null;  // Nếu không tìm thấy, trả về null
        }
    }
    public List<KhoanChi> getKhoanChiByMonth(int month, int year) {
        List<KhoanChi> expenseList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Câu truy vấn lọc theo cả tháng và năm
            String query = "SELECT * FROM " + TABLE_KHOAN_CHI +
                    " WHERE strftime('%m', " + COLUMN_KHOAN_CHI_DATE + ") = ? " +
                    " AND strftime('%Y', " + COLUMN_KHOAN_CHI_DATE + ") = ?";

            // Lấy dữ liệu theo tham số month và year
            cursor = db.rawQuery(query, new String[]{
                    String.format("%02d", month), // Tháng (2 chữ số)
                    String.valueOf(year)          // Năm
            });

            // Kiểm tra các cột trong cursor
            int columnIdIndex = cursor.getColumnIndexOrThrow(COLUMN_KHOAN_CHI_ID);
            int columnLoaiChiIdIndex = cursor.getColumnIndexOrThrow(COLUMN_KHOAN_CHI_LOAI_CHI_ID);
            int columnSoTienIndex = cursor.getColumnIndexOrThrow(COLUMN_KHOAN_CHI_AMOUNT);
            int columnNgayIndex = cursor.getColumnIndexOrThrow(COLUMN_KHOAN_CHI_DATE);

            // Đọc dữ liệu từ cursor
            if (cursor.moveToFirst()) {
                do {
                    KhoanChi expense = new KhoanChi();
                    expense.setId(cursor.getInt(columnIdIndex));
                    expense.setLoaiChiId(cursor.getInt(columnLoaiChiIdIndex));
                    expense.setSoTien(cursor.getDouble(columnSoTienIndex));
                    expense.setNgay(cursor.getString(columnNgayIndex));
                    expenseList.add(expense);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace(); // Xử lý lỗi nếu cần
        } finally {
            // Đảm bảo tài nguyên được đóng
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return expenseList;
    }

    public List<KhoanThu> getKhoanThuByMonth(int month, int year) {
        List<KhoanThu> incomeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Câu truy vấn lọc theo tháng và năm
            String query = "SELECT * FROM " + TABLE_KHOAN_THU +
                    " WHERE strftime('%m', " + COLUMN_KHOAN_THU_DATE + ") = ?" +
                    " AND strftime('%Y', " + COLUMN_KHOAN_THU_DATE + ") = ?";

            // Thực hiện truy vấn với tháng và năm
            cursor = db.rawQuery(query, new String[]{
                    String.format("%02d", month), // Tháng, dạng 2 chữ số
                    String.valueOf(year)          // Năm
            });

            // Kiểm tra các chỉ số cột trong cursor
            int columnIdIndex = cursor.getColumnIndex(COLUMN_KHOAN_THU_ID);
            int columnLoaiThuIdIndex = cursor.getColumnIndex(COLUMN_KHOAN_THU_LOAI_THU_ID);
            int columnSoTienIndex = cursor.getColumnIndex(COLUMN_KHOAN_THU_AMOUNT);
            int columnNgayIndex = cursor.getColumnIndex(COLUMN_KHOAN_THU_DATE);

            // Kiểm tra nếu thiếu cột
            if (columnIdIndex == -1 || columnLoaiThuIdIndex == -1 || columnSoTienIndex == -1 || columnNgayIndex == -1) {
                // Lỗi nếu thiếu cột trong kết quả truy vấn
                throw new SQLException("Column(s) missing in query result.");
            }

            // Đọc dữ liệu từ cursor và thêm vào danh sách
            if (cursor.moveToFirst()) {
                do {
                    KhoanThu income = new KhoanThu();
                    income.setId(cursor.getInt(columnIdIndex));
                    income.setLoaiThuId(cursor.getInt(columnLoaiThuIdIndex));
                    income.setSoTien(cursor.getDouble(columnSoTienIndex));
                    income.setNgay(cursor.getString(columnNgayIndex));
                    incomeList.add(income);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log lỗi nếu có
        } finally {
            // Đóng cursor và db sau khi sử dụng
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return incomeList;
    }

    public double getTotalChi(int month, int year) {
        SQLiteDatabase db = this.getReadableDatabase();
        double totalChi = 0;

        // Truy vấn tính tổng chi theo tháng và năm
        String query = "SELECT SUM(" + COLUMN_KHOAN_CHI_AMOUNT + ") FROM " + TABLE_KHOAN_CHI +
                " WHERE strftime('%m', " + COLUMN_KHOAN_CHI_DATE + ") = ? AND strftime('%Y', " + COLUMN_KHOAN_CHI_DATE + ") = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.format("%02d", month), String.valueOf(year)});

        if (cursor.moveToFirst()) {
            totalChi = cursor.getDouble(0); // Lấy giá trị tổng chi
        }
        cursor.close();
        db.close();

        return totalChi;
    }
    public double getTotalThu(int month, int year) {
        SQLiteDatabase db = this.getReadableDatabase();
        double totalThu = 0;

        // Truy vấn tính tổng thu theo tháng và năm
        String query = "SELECT SUM(" + COLUMN_KHOAN_THU_AMOUNT + ") FROM " + TABLE_KHOAN_THU +
                " WHERE strftime('%m', " + COLUMN_KHOAN_THU_DATE + ") = ? AND strftime('%Y', " + COLUMN_KHOAN_THU_DATE + ") = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.format("%02d", month), String.valueOf(year)});

        if (cursor.moveToFirst()) {
            totalThu = cursor.getDouble(0); // Lấy giá trị tổng thu
        }
        cursor.close();
        db.close();

        return totalThu;
    }

// xử lý hạn mức
    public void addMonthlyLimit(String month, double limitAmount) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Kiểm tra nếu hạn mức cho tháng này đã tồn tại
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MONTHLY_LIMIT + " WHERE " + COLUMN_MONTHLY_LIMIT_MONTH + " = ?",
                new String[]{month});

        if (cursor.moveToFirst()) {
            // Nếu đã tồn tại, cập nhật lại hạn mức
            ContentValues values = new ContentValues();
            values.put(COLUMN_MONTHLY_LIMIT_AMOUNT, limitAmount);

            db.update(TABLE_MONTHLY_LIMIT, values, COLUMN_MONTHLY_LIMIT_MONTH + " = ?", new String[]{month});
        } else {
            // Nếu chưa tồn tại, thêm mới hạn mức
            ContentValues values = new ContentValues();
            values.put(COLUMN_MONTHLY_LIMIT_MONTH, month);
            values.put(COLUMN_MONTHLY_LIMIT_AMOUNT, limitAmount);

            db.insert(TABLE_MONTHLY_LIMIT, null, values);
        }
        cursor.close();
        db.close();
    }
    public List<ExpenseIncomeItem> getExpenseIncomeByMonthh(int month, int year, boolean isExpense) {
        SQLiteDatabase db = this.getReadableDatabase();
        String table = isExpense ? "KhoanChi" : "KhoanThu";
        String columnLoaiId = isExpense ? "loai_chi_id" : "loai_thu_id";
        String loaiTable = isExpense ? "LoaiChi" : "LoaiThu";

        String query = "SELECT " +
                loaiTable + ".ten AS ten, " +
                loaiTable + ".src_icon AS src_icon, " +
                "SUM(" + table + ".so_tien) AS total " +
                "FROM " + table + " " +
                "JOIN " + loaiTable + " ON " + table + "." + columnLoaiId + " = " + loaiTable + ".id " +
                "WHERE strftime('%m', " + table + ".ngay) = ? AND strftime('%Y', " + table + ".ngay) = ? " +
                "GROUP BY " + loaiTable + ".id";

        Cursor cursor = db.rawQuery(query, new String[] {
                String.format("%02d", month),
                String.valueOf(year)
        });

        List<ExpenseIncomeItem> list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                // Lấy index của các cột
                int nameIndex = cursor.getColumnIndex("ten");
                int iconIndex = cursor.getColumnIndex("src_icon");
                int totalIndex = cursor.getColumnIndex("total");

                // Đảm bảo các cột tồn tại
                if (nameIndex != -1 && iconIndex != -1 && totalIndex != -1) {
                    String name = cursor.getString(nameIndex);
                    int icon = cursor.getInt(iconIndex);
                    double total = cursor.getDouble(totalIndex);

                    // Thêm vào danh sách
                    list.add(new ExpenseIncomeItem(name, icon, total));
                } else {
                    Log.e("DatabaseHelper", "One or more columns not found in cursor");
                }
            } while (cursor.moveToNext());
        } else {
            Log.i("DatabaseHelper", "No data found for the given month and year");
        }

        // Đóng cursor
        cursor.close();
        return list;
    }






    public void upsertMonthlyLimit(String month, double limit) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MONTHLY_LIMIT_MONTH, month);
        values.put(COLUMN_MONTHLY_LIMIT_AMOUNT, limit);

        db.insertWithOnConflict(TABLE_MONTHLY_LIMIT, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public double getMonthlyLimit(String month) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MONTHLY_LIMIT,
                new String[]{COLUMN_MONTHLY_LIMIT_AMOUNT},
                COLUMN_MONTHLY_LIMIT_MONTH + " = ?",
                new String[]{month},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            double limit = cursor.getDouble(0);
            cursor.close();
            return limit;
        }
        return 0; // Nếu không có hạn mức thì trả về 0
    }

    public double getTotalExpenseForMonth(String month) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(" + COLUMN_KHOAN_CHI_AMOUNT + ") FROM " + TABLE_KHOAN_CHI +
                " WHERE strftime('%Y-%m', " + COLUMN_KHOAN_CHI_DATE + ") = ?";
        Cursor cursor = db.rawQuery(query, new String[]{month});

        if (cursor != null && cursor.moveToFirst()) {
            double totalExpense = cursor.getDouble(0);
            cursor.close();
            return totalExpense;
        }
        return 0; // Nếu không có chi tiêu thì trả về 0
    }

    public boolean isOverLimit(String month) {
        double limit = getMonthlyLimit(month);
        double totalExpense = getTotalExpenseForMonth(month);
        return totalExpense > limit;
    }

}
