package com.nateshr.appmanager.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class UninstalledSQL {

    public static final String app_name = "app_name";
    public static final String app_label = "app_label";
    public static final String app_image = "app_image";
    public static final String app_uninstall_date = "app_uninstall_date";

    public static final String Database_name = "database_uninstalled";
    public static final String Database_table = "database_table_uninstalled";
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
                    + " BLOB, " + app_uninstall_date + " TEXT); ");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            db.execSQL(" DROP TABLE IF EXISTS " + Database_table);
            onCreate(db);
        }
    }

    public UninstalledSQL(Context c) {
        ourContext = c;
    }

    public UninstalledSQL Open() throws SQLException {
        ourDBhelper = new DBhelper(ourContext);
        ourDatabase = ourDBhelper.getWritableDatabase();
        return this;
    }

    public void Close() {
        ourDBhelper.close();
    }

    public long CreateEntry(String app_name_x, String app_label_x, byte[] app_image_x, String app_uninstall_date_x) {
        ContentValues cv = new ContentValues();
        cv.put(this.app_name, app_name_x);
        cv.put(this.app_label, app_label_x);
        cv.put(this.app_image, app_image_x);
        cv.put(this.app_uninstall_date, app_uninstall_date_x);
        return ourDatabase.insert(Database_table, null, cv);
    }


    public void DeleteAll() {
        ourDatabase.delete(Database_table, null, null);
    }

    public String[] getAppName() {
        String[] columns = {app_name, app_label, app_image, app_uninstall_date};
        Cursor c = ourDatabase.query(Database_table, columns, null, null, null,
                null, null);
        int get = c.getColumnIndex(app_name);

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

    public String[] getAppLabel() {
        String[] columns = {app_name, app_label, app_image, app_uninstall_date};
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

    public ArrayList<byte[]> getAppImage() {
        String[] columns = {app_name, app_label, app_image, app_uninstall_date};
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


    public String[] getAppDate() {
        String[] columns = {app_name, app_label, app_image, app_uninstall_date};
        Cursor c = ourDatabase.query(Database_table, columns, null, null, null,
                null, null);
        int get = c.getColumnIndex(app_uninstall_date);

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
        String[] columns = {app_name, app_label, app_image, app_uninstall_date};
        Cursor c = ourDatabase.query(Database_table, columns, null, null, null, null, null);
        int count = c.getCount();

        return count;
    }

    public long deleteUninstalledApp(String app_name_x) {
        return this.ourDatabase.delete(Database_table, app_name + " =? ", new String[]{app_name_x});
    }

}
