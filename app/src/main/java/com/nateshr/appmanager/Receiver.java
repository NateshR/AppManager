package com.nateshr.appmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.nateshr.appmanager.sql.InstalledSQL;
import com.nateshr.appmanager.sql.UninstalledSQL;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by natesh on 8/5/15.
 */
public class Receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            if (intent.getAction().equals(intent.ACTION_PACKAGE_REMOVED)) {
                boolean replacing = intent.getBooleanExtra(Intent.EXTRA_REPLACING, false);
                if (!replacing) {
                    Log.d("RECEIVED_INTENT", "REMOVED");
                    //Get package name of app
                    String packageName = intent.getData().toString().substring(8);
                    //Get today's date
                    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                    //Open installedSQL database and fetch app name, app label, app image
                    InstalledSQL installedSQL = new InstalledSQL(context);
                    installedSQL.Open();
                    byte[] image = installedSQL.getAppImage(packageName);
                    String label = installedSQL.getAppLabel(packageName);
                    //Delete value from InstalledSQL table
                    long id = installedSQL.deleteUninstalledApp(packageName);
                    installedSQL.Close();
                    Log.d("debug", packageName + "P " + date + "D " + image + "I " + id);

                    //Create  uninstall sql database using app name, app label, app image
                    UninstalledSQL uninstalledSQL = new UninstalledSQL(context);
                    uninstalledSQL.Open();
                    uninstalledSQL.CreateEntry(packageName, label, image, date);
                    uninstalledSQL.Close();

                    //Send certain values to uninstalled broadcast receiver
                    Intent uninstalledIntent = new Intent("com.nateshr.appmanager.uninstalled");
                    uninstalledIntent.putExtra("APP_ADD", true);
                    uninstalledIntent.putExtra("APP_LABEL", label);
                    uninstalledIntent.putExtra("APP_IMAGE", image);
                    uninstalledIntent.putExtra("APP_DATE", date);
                    context.sendBroadcast(uninstalledIntent);

                    //Notify installed broadcast receiver to update it's list
                    Intent installedIntent = new Intent("com.nateshr.appmanager.installed");
                    installedIntent.putExtra("APP_ADD", false);
                    context.sendBroadcast(installedIntent);
                }


            } else if (intent.getAction().equals(intent.ACTION_PACKAGE_REPLACED)) {
                Log.d("RECEIVED_INTENT", "REPLACED");
                //Get package name of app
                String packageName = intent.getData().toString().substring(8);
                //Get today's date
                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                //Open installedSQL database and fetch app name, app label, app image
                InstalledSQL installedSQL = new InstalledSQL(context);
                installedSQL.Open();
                byte[] image = installedSQL.getAppImage(packageName);
                String label = installedSQL.getAppLabel(packageName);
                //Update row in InstalledSQL i.e update date
                long id = installedSQL.updateUninstallDate(date, packageName);
                installedSQL.Close();
                Log.d("debug", packageName + "P " + date + "D " + image + "I " + id);

                //Send certain values to updated broadcast receiver
                Intent updatedIntent = new Intent("com.nateshr.appmanager.updated");
                updatedIntent.putExtra("APP_LABEL", label);
                updatedIntent.putExtra("APP_IMAGE", image);
                updatedIntent.putExtra("APP_DATE", date);
                context.sendBroadcast(updatedIntent);

            } else if (intent.getAction().equals(intent.ACTION_PACKAGE_ADDED)) {
                boolean replacing = intent.getBooleanExtra(Intent.EXTRA_REPLACING, false);
                if (!replacing) {
                    Log.d("RECEIVED_INTENT", "ADDED");
                    //Get package name of app
                    String packageName = intent.getData().toString().substring(8);
                    try {
                        //Fetch icon and label from package manager using package name of the app added
                        ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(packageName, 0);
                        Drawable icon = context.getPackageManager().getApplicationIcon(applicationInfo);
                        String label = context.getPackageManager().getApplicationLabel(applicationInfo).toString();

                        //Insert row in InstalledSQL database
                        InstalledSQL installedSQL = new InstalledSQL(context);
                        installedSQL.Open();
                        installedSQL.CreateEntry(packageName, label, getBytes(icon));
                        installedSQL.Close();
                        Log.v("debug", icon + "D " + label + "L ");

                        //Delete row from Uninstalled table
                        UninstalledSQL uninstalledSQL = new UninstalledSQL(context);
                        uninstalledSQL.Open();
                        long id = uninstalledSQL.deleteUninstalledApp(packageName);
                        Log.d("debug", id + "id");
                        uninstalledSQL.Close();

                        //Send certain values to installed broadcast receiver
                        Intent installedIntent = new Intent("com.nateshr.appmanager.installed");
                        installedIntent.putExtra("APP_ADD", true);
                        installedIntent.putExtra("APP_LABEL", label);
                        installedIntent.putExtra("APP_IMAGE", getBytes(icon));
                        context.sendBroadcast(installedIntent);

                        //Notify uninstalled broadcast receiver to update it's list
                        Intent uninstalledIntent = new Intent("com.nateshr.appmanager.uninstalled");
                        uninstalledIntent.putExtra("APP_ADD", false);
                        context.sendBroadcast(uninstalledIntent);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();

                    }
                }
            }
        }
    }

    // convert from bitmap to byte array
    public byte[] getBytes(Drawable drawable) {
        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        } else {
            Bitmap bitmap_temp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            bitmap = bitmap_temp;
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        return stream.toByteArray();
    }

}
