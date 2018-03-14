package com.sandro.openalprsample;

import android.os.AsyncTask;

import org.openalpr.SapphireAccess;

import sapphire.common.Configuration;

/**
 * Created by SMoon on 2/27/2018.
 */
public class OpenAlprSapphire extends AsyncTask<Void, Void, String> {
    private String ANDROID_DATA_DIR;
    private String countryCode;
    private String region;
    private String imageFilePath;
    private Configuration.ProcessEntity processEntity;

    public OpenAlprSapphire(String ANDROID_DATA_DIR, String countryCode, String region, String imageFilePath, Configuration.ProcessEntity processEntity) {
        this.ANDROID_DATA_DIR = ANDROID_DATA_DIR;
        this.countryCode = countryCode;
        this.region = region;
        this.imageFilePath = imageFilePath;
        this.processEntity = processEntity;
    }

    @Override
    protected String doInBackground(Void... params) {
        String result = SapphireAccess.getResult(ANDROID_DATA_DIR, this.countryCode, this.region, this.imageFilePath, processEntity);
        return result;
    }
}
