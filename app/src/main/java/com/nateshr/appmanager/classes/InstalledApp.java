package com.nateshr.appmanager.classes;

/**
 * Created by natesh on 9/5/15.
 */
public class InstalledApp {

    public String app_label;
    public byte[] app_image;

    public InstalledApp(String app_label_x, byte[] app_image_x) {
        this.app_label = app_label_x;
        this.app_image = app_image_x;
    }

}
