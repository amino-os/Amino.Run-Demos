package com.sandro.openalprsample;

import android.os.AsyncTask;

import org.openalpr.SapphireAccess;

/**
 * Created by SMoon on 2/27/2018.
 */
public class OpenAlprSapphire extends AsyncTask<Void, Void, String> {
    private String ANDROID_DATA_DIR;
    private String countryCode;
    private String region;
    private String imageFilePath;

    public OpenAlprSapphire(String ANDROID_DATA_DIR, String countryCode, String region, String imageFilePath) {
        this.ANDROID_DATA_DIR = ANDROID_DATA_DIR;
        this.countryCode = countryCode;
        this.region = region;
        this.imageFilePath = imageFilePath;
    }

    @Override
    protected String doInBackground(Void... params) {
        SapphireAccess.ANDROID_DATA_DIR = ANDROID_DATA_DIR;
        String result = SapphireAccess.getResult(this.countryCode, this.region, this.imageFilePath);
        return result;
    }
}
