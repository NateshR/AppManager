package com.nateshr.appmanager.adapters;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nateshr.appmanager.R;
import com.nateshr.appmanager.classes.InstalledApp;

import java.util.List;

/**
 * Created by natesh on 8/5/15.
 */
public class InstalledAdapter extends BaseAdapter {

    private List<InstalledApp> installedData;
    private Context ourContext;
    private PackageManager packageManager;

    private static LayoutInflater inflater = null;
    private ViewHolder holder;

    public InstalledAdapter(Context ourContext, List<InstalledApp> installedData) {
        this.installedData = installedData;
        this.ourContext = ourContext;
        this.packageManager = this.ourContext.getPackageManager();
    }

    @Override
    public int getCount() {
        return this.installedData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {

        TextView installed_text;
        ImageView installed_image;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Inflate installed_item layout
        inflater = (LayoutInflater) ourContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.installed_item, null);
        InstalledApp installedApp = installedData.get(position);

        //Initializing layout widgets with view holder
        holder = new ViewHolder();
        holder.installed_image = (ImageView) rowView.findViewById(R.id.installed_image);
        holder.installed_text = (TextView) rowView.findViewById(R.id.installed_text);
        rowView.setTag(holder);

        //Setting up layout widgets
        holder = (ViewHolder) rowView.getTag();
        holder.installed_image.setImageBitmap(getImage(installedApp.app_image));
        holder.installed_text.setText(installedApp.app_label);
        return rowView;
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
