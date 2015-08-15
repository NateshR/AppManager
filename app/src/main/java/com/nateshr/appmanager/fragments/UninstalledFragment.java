package com.nateshr.appmanager.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.nateshr.appmanager.R;
import com.nateshr.appmanager.adapters.UninstalledAdapter;
import com.nateshr.appmanager.classes.UninstalledApp;
import com.nateshr.appmanager.sql.UninstalledSQL;

import java.util.ArrayList;

/**
 * Created by natesh on 8/5/15.
 */
public class UninstalledFragment extends Fragment {
    private ListView list_uninstalled;

    private ArrayList<UninstalledApp> uninstalledAppArrayList;
    UninstalledAdapter uninstalledAdapter;
    private String[] app_label, app_uninstall_date;
    private ArrayList<byte[]> app_image;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Initializing layout widgets
        View view = inflater.inflate(R.layout.uninstalled_fragment, null, false);
        list_uninstalled = (ListView) view.findViewById(R.id.list_uninstalled);

        //Get all the apps uninstalled data
        UninstalledSQL uninstalledSQL = new UninstalledSQL(UninstalledFragment.this.getActivity());
        uninstalledSQL.Open();
        app_label = uninstalledSQL.getAppLabel();
        app_image = uninstalledSQL.getAppImage();
        app_uninstall_date = uninstalledSQL.getAppDate();

        //Create objects from data fetched
        uninstalledAppArrayList = new ArrayList<UninstalledApp>();
        for (int i = 0; i < uninstalledSQL.getCount(); i++) {
            UninstalledApp uninstalledApp = new UninstalledApp(app_label[i], app_image.get(i), app_uninstall_date[i]);
            uninstalledAppArrayList.add(uninstalledApp);
        }

        //Create and set adapter to listView
        uninstalledAdapter = new UninstalledAdapter(UninstalledFragment.this.getActivity(), uninstalledAppArrayList);
        list_uninstalled.setAdapter(uninstalledAdapter);

        //Register a receiver to update instantaneously
        UninstalledFragment.this.getActivity().registerReceiver(broadcastReceiver, new IntentFilter("com.nateshr.appmanager.uninstalled"));
        return view;
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //If new app is uninstalled then display it
            if (intent.getBooleanExtra("APP_ADD",false)) {
                UninstalledApp uninstalledApp = new UninstalledApp(intent.getStringExtra("APP_LABEL"), intent.getByteArrayExtra("APP_IMAGE"), intent.getStringExtra("APP_DATE"));
                uninstalledAppArrayList.add(uninstalledApp);
                uninstalledAdapter = new UninstalledAdapter(UninstalledFragment.this.getActivity(), uninstalledAppArrayList);
            }
            //If new app is installed then update the list
            else {
                UninstalledFragment.this.getActivity().recreate();
            }
            list_uninstalled.setAdapter(uninstalledAdapter);

        }
    };


}
