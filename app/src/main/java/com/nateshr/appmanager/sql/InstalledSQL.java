package com.nateshr.appmanager.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class InstalledSQL {

    public static final String app_name = "app_name";
    public static final String app_label = "app_label";
    public static final String app_image = "app_image";
    public static final String app_update_date = "app_update_date";

    public static final String Database_name = "database_apps";
    public static final String Database_table = "database_table_apps";
    private static final int Database_version = 1;

    private Context ourContext;
    private DBhelper ourDBhelper;
    private SQLiteDatabase ourDatabase;

    // DATABASE
    private class DBhelper extends SQLiteOpenHelper {

        public DBhelper(Context context) {
            super(context, Database_name, null, Database_version);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL("CREATE TABLE " + Database_table + " ( " + app_name
                    + " TEXT, " + app_label + " TEXT, " + app_image
                    + " BLOB, " + app_update_date + " TEXT); ");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            db.execSQL(" DROP TABLE IF EXISTS " + Database_table);
            onCreate(db);
        }
    }

    public InstalledSQL(Context c) {
        ourContext = c;
    }

    public InstalledSQL Open() throws SQLException {
        ourDBhelper = new DBhelper(ourContext);
        ourDatabase = ourDBhelper.getWritableDatabase();
        return this;
    }

    public void Close() {
        ourDBhelper.close();
    }

    public long CreateEntry(String app_name, String app_label, byte[] app_image) {
        ContentValues cv = new ContentValues();
        cv.put(this.app_name, app_name);
        cv.put(this.app_label, app_label);
        cv.put(this.app_image, app_image);
        return ourDatabase.insert(Database_table, null, cv);
    }


    public void DeleteAll() {
        ourDatabase.delete(Database_table, null, null);
    }

    public byte[] getAppImage(String app_name_x) {
        byte[] result = null;
        String[] columns = {app_name, app_label, app_image, app_update_date};
        Cursor c = ourDatabase.query(Database_table, columns, this.app_name + " = ?", new String[]{app_name_x}, null,
                null, null);
        int get = c.getColumnIndex(app_image);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            result = c.getBlob(get);
        }

        return result;
    }

    public ArrayList<byte[]> getAppImage() {
        String[] columns = {app_name, app_label, app_image, app_update_date};
        Cursor c = ourDatabase.query(Database_table, columns, null, null, null,
                null, null);
        int get = c.getColumnIndex(app_image);

        ArrayList<byte[]> result = new ArrayList<byte[]>();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            byte[] result_item = c.getBlob(get);
            result.add(result_item);
        }

        return result;
    }


    public String getAppLabel(String app_name_x) {
        String result = null;
        String[] columns = {app_name, app_label, app_image, app_update_date};
        Cursor c = ourDatabase.query(Database_table, columns, this.app_name + " = ?", new String[]{app_name_x}, null,
                null, null);
        int get = c.getColumnIndex(app_label);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            result = c.getString(get);
        }

        return result;
    }

    public String[] getAppLabel() {
        String[] columns = {app_name, app_label, app_image, app_update_date};
        Cursor c = ourDatabase.query(Database_table, columns, null, null, null,
                null, null);
        int get = c.getColumnIndex(app_label);

        int i = 0;
        int count = 0;
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            count = count + 1;
        }
        String[] result = new String[count];
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            result[i] = c.getString(get);
            i++;
        }

        return result;
    }

    public int getCount() {
        String[] columns = {app_name, app_label, app_image, app_update_date};
        Cursor c = ourDatabase.query(Database_table, columns, null, null, null, null, null);
        int count = c.getCount();

        return count;
    }

    public long deleteUninstalledApp(String app_name_x) {

        return this.ourDatabase.delete(Database_table, app_name + " =? ", new String[]{app_name_x});
    }

    public int getCount_U() {
        String[] columns = {app_name, app_label, app_image, app_update_date};
        Cursor c = ourDatabase.query(Database_table, columns, app_update_date + " != ? ", new String[]{""}, null, null, null);
        int count = c.getCount();

        return count;
    }

    public long updateUninstallDate(String app_update_date, String app_name_x) {
        ContentValues cv = new ContentValues();
        cv.put(this.app_update_date, app_update_date);
        return this.ourDatabase.update(Database_table, cv, app_name + "='" + app_name_x
                + "'", null);
    }

    public String[] getAppLabel_U() {
        String[] columns = {app_name, app_label, app_image, app_update_date};
        Cursor c = ourDatabase.query(Database_table, columns, app_update_date + " != ? ", new String[]{""}, null,
                null, null);
        int get = c.getColumnIndex(app_label);

        int i = 0;
        int count = 0;
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            count = count + 1;
        }
        String[] result = new String[count];
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            result[i] = c.getString(get);
            i++;
        }

        return result;
    }

    public ArrayList<byte[]> getAppImage_U() {
        String[] columns = {app_name, app_label, app_image, app_update_date};
        Cursor c = ourDatabase.query(Database_table, columns, app_update_date + " != ? ", new String[]{""}, null,
                null, null);
        int get = c.getColumnIndex(app_image);

        ArrayList<byte[]> result = new ArrayList<byte[]>();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            byte[] result_item = c.getBlob(get);
            result.add(result_item);
        }

        return result;
    }

    public String[] getAppDate_U() {
        String[] columns = {app_name, app_label, app_image, app_update_date};
        Cursor c = ourDatabase.query(Database_table, columns, app_update_date + " != ? ", new String[]{""}, null,
                null, null);
        int get = c.getColumnIndex(app_update_date);

        int i = 0;
        int count = 0;
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            count = count + 1;
        }
        String[] result = new String[count];
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            result[i] = c.getString(get);
            i++;
        }

        return result;
    }


}
