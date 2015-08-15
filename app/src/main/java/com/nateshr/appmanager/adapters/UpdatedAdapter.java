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
import com.nateshr.appmanager.classes.UpdatedApp;

import java.util.List;

/**
 * Created by natesh on 8/5/15.
 */
public class UpdatedAdapter extends BaseAdapter {

    private List<UpdatedApp> uninstalledData;
    private Context ourContext;
    private PackageManager packageManager;

    private static LayoutInflater inflater = null;
    private ViewHolder holder;

    public UpdatedAdapter(Context ourContext, List<UpdatedApp> uninstalledData) {
        this.uninstalledData = uninstalledData;
        this.ourContext = ourContext;
        this.packageManager = this.ourContext.getPackageManager();
    }

    @Override
    public int getCount() {
        return this.uninstalledData.size();
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

        TextView uninstalled_text, uninstalled_date;
        ImageView uninstalled_image;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Inflate installed_item layout
        inflater = (LayoutInflater) ourContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.updated_item, null);
        UpdatedApp updatedApp = uninstalledData.get(position);

        //Initializing layout widgets with view holder
        holder = new ViewHolder();
        holder.uninstalled_image = (ImageView) rowView.findViewById(R.id.updated_image);
        holder.uninstalled_text = (TextView) rowView.findViewById(R.id.updated_text);
        holder.uninstalled_date = (TextView) rowView.findViewById(R.id.updated_date);
        rowView.setTag(holder);

        //Setting up layout widgets
        holder = (ViewHolder) rowView.getTag();
        holder.uninstalled_image.setImageBitmap(getImage(updatedApp.app_image));
        holder.uninstalled_text.setText(updatedApp.app_label);
        holder.uninstalled_date.setText(updatedApp.app_uninstall_date);
        return rowView;
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
