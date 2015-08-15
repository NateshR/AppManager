package com.nateshr.appmanager.fragments;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.nateshr.appmanager.R;
import com.nateshr.appmanager.adapters.InstalledAdapter;
import com.nateshr.appmanager.classes.InstalledApp;
import com.nateshr.appmanager.sql.InstalledSQL;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by natesh on 8/5/15.
 */

public class InstalledFragment extends Fragment {
    private GridView grid_installed;

    private String[] app_label;
    private ArrayList<byte[]> app_image;
    private ArrayList<InstalledApp> installedAppArrayList;
    private InstalledAdapter installedAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Initializing layout widgets
        View view = inflater.inflate(R.layout.installed_fragment, null, false);
        grid_installed = (GridView) view.findViewById(R.id.grid_installed);
        installedAppArrayList = new ArrayList<InstalledApp>();

        //Fetch data from InstalledSQL database
        InstalledSQL installedSQL = new InstalledSQL( InstalledFragment.this.getActivity());
        installedSQL.Open();
        //If there are rows then fetch from table
        if (installedSQL.getCount() > 0) {
            for (int i = 0; i < installedSQL.getCount(); i++) {
                app_label = installedSQL.getAppLabel();
                app_image = installedSQL.getAppImage();
                InstalledApp installedApp = new InstalledApp(app_label[i], app_image.get(i));
                installedAppArrayList.add(installedApp);

            }
            installedAdapter = new InstalledAdapter( InstalledFragment.this.getActivity(), installedAppArrayList);
            grid_installed.setAdapter(installedAdapter);
            //else fetch from package manager meta data and save in InstalledSQL database
        } else {
            new GetInstalledApps().execute();
        }
        //Register a receiver to update instantaneously
        InstalledFragment.this.getActivity().registerReceiver(broadcastReceiver, new IntentFilter("com.nateshr.appmanager.installed"));
        return view;
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //If new app is installed then display it
            if (intent.getBooleanExtra("APP_ADD", false)) {
                InstalledApp installedApp = new InstalledApp(intent.getStringExtra("APP_LABEL"), intent.getByteArrayExtra("APP_IMAGE"));
                installedAppArrayList.add(installedApp);
                installedAdapter = new InstalledAdapter( InstalledFragment.this.getActivity(), installedAppArrayList);

            }
            //If an app is uninstalled then update the list
            else {
                getActivity().recreate();
            }
            grid_installed.setAdapter(installedAdapter);
        }

    };

    private class GetInstalledApps extends AsyncTask<Void, Void, List<InstalledApp>> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            //Initialize progress dialog
            progressDialog = new ProgressDialog( InstalledFragment.this.getActivity());
            progressDialog.setMessage("Fetching and Saving Installed Apps");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected List<InstalledApp> doInBackground(Void... params) {
            //Save current apps on database
            InstalledSQL installedSQL = new InstalledSQL( InstalledFragment.this.getActivity());
            installedSQL.Open();
            //Get list of installed apps using package manager
            PackageManager packageManager =  InstalledFragment.this.getActivity().getPackageManager();
            List<ApplicationInfo> list = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

            //Save values one by one in InstalledSQL database
            for (int i = 0; i < list.size(); i++) {
                installedSQL.CreateEntry(list.get(i).packageName, list.get(i).loadLabel( InstalledFragment.this.getActivity().getPackageManager()).toString(), getBytes(list.get(i).loadIcon( InstalledFragment.this.getActivity().getPackageManager())));
                InstalledApp installedApp = new InstalledApp(list.get(i).loadLabel( InstalledFragment.this.getActivity().getPackageManager()).toString(), getBytes(list.get(i).loadIcon( InstalledFragment.this.getActivity().getPackageManager())));
                installedAppArrayList.add(installedApp);

            }
            installedSQL.Close();
            installedAdapter = new InstalledAdapter( InstalledFragment.this.getActivity(), installedAppArrayList);

            return installedAppArrayList;
        }

        @Override
        protected void onPostExecute(List<InstalledApp> applicationInfo) {
            super.onPostExecute(applicationInfo);
            //Cancel progress dialog
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }
            //Check if list is not null
            if (applicationInfo != null) {
                //if list is not null, set adapter
                grid_installed.setAdapter(installedAdapter);
            } else {
                Toast.makeText( InstalledFragment.this.getActivity(), "No Installed App found", Toast.LENGTH_SHORT).show();
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
