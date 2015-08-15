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
import com.nateshr.appmanager.adapters.UpdatedAdapter;
import com.nateshr.appmanager.classes.UpdatedApp;
import com.nateshr.appmanager.sql.InstalledSQL;

import java.util.ArrayList;

/**
 * Created by natesh on 8/5/15.
 */
public class UpdatedFragment extends Fragment {
    private ListView list_updated;
    private ArrayList<UpdatedApp> updatedAppArrayList;
    UpdatedAdapter updatedAdapter;

    private String[] app_label, app_uninstall_date;
    private ArrayList<byte[]> app_image;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Initializing layout widgets
        View view = inflater.inflate(R.layout.updated_fragment, null, false);
        list_updated = (ListView) view.findViewById(R.id.list_updated);

        //Get all the apps updated data
        InstalledSQL updatedSQL = new InstalledSQL(UpdatedFragment.this.getActivity());
        updatedSQL.Open();
        app_label = updatedSQL.getAppLabel_U();
        app_image = updatedSQL.getAppImage_U();
        app_uninstall_date = updatedSQL.getAppDate_U();

        //Create objects from data fetched
        updatedAppArrayList = new ArrayList<UpdatedApp>();
        for (int i = 0; i < updatedSQL.getCount_U(); i++) {
            UpdatedApp updatedApp = new UpdatedApp(app_label[i], app_image.get(i), app_uninstall_date[i]);
            updatedAppArrayList.add(updatedApp);
        }

        //Create and set adapter to listView
        updatedAdapter = new UpdatedAdapter(UpdatedFragment.this.getActivity(), updatedAppArrayList);
        list_updated.setAdapter(updatedAdapter);

        //Register a receiver to update instantaneously
        UpdatedFragment.this.getActivity().registerReceiver(broadcastReceiver, new IntentFilter("com.nateshr.appmanager.updated"));
        return view;
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //If an app is updated then display it
            UpdatedApp UpdatedApp = new UpdatedApp(intent.getStringExtra("APP_LABEL"), intent.getByteArrayExtra("APP_IMAGE"), intent.getStringExtra("APP_DATE"));
            updatedAppArrayList.add(UpdatedApp);
            updatedAdapter = new UpdatedAdapter(UpdatedFragment.this.getActivity(), updatedAppArrayList);
            list_updated.setAdapter(updatedAdapter);

        }
    };
}
