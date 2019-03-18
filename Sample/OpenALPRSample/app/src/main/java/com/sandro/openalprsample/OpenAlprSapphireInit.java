package com.sandro.openalprsample;

import android.os.AsyncTask;

import org.openalpr.SapphireAccess;


/**
 * Created by SMoon on 2/27/2018.
 */
public class OpenAlprSapphireInit extends AsyncTask<Void, Void, String> {

    SapphireAccess sa;
    public OpenAlprSapphireInit(SapphireAccess sa) {
        this.sa = sa;
    }

    @Override
    protected String doInBackground(Void... params) {
        sa.initialize();
        return null;
    }
}
