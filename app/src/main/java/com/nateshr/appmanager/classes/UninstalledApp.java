package com.nateshr.appmanager.classes;

/**
 * Created by natesh on 9/5/15.
 */
public class UninstalledApp {

    public String app_label;
    public byte[] app_image;
    public String app_uninstall_date;

    public UninstalledApp(String app_label_x, byte[] app_image_x, String app_uninstall_date_x) {
        this.app_label = app_label_x;
        this.app_image = app_image_x;
        this.app_uninstall_date = app_uninstall_date_x;
    }

}
