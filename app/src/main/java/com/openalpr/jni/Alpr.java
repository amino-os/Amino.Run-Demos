package com.openalpr.jni;

import com.openalpr.jni.json.JSONException;

/**
 * This Class loads Alpr for license plate recognition on a default machine (e.g., Linux/Windows).
 * It does not work for Android (See org.openalpr for Android JNI) due to following reasons:
 * 1) Native code compiled on different package name.
 * 2) Native code uses different library name (when compiled based on suggestion from Github).
 * 3) Native compiled with different APIs.
 *
 * TODO (2/16/2018, SMoon): If necessary, fix all 1)-3) and compile with same names and APIs so a single code can be used.
 */
public class Alpr {
    static {
        // Load the OpenALPR library at runtime
        // openalprjni.dll (Windows) or libopenalprjni.so (Linux/Mac)
        System.loadLibrary("openalprjni");
    }

    private native void initialize(String country, String configFile, String runtimeDir);
    private native void dispose();

    private native boolean is_loaded();
    private native String native_recognize(String imageFile);
    private native String native_recognize(byte[] imageBytes);
    private native String native_recognize(long imageData, int bytesPerPixel, int imgWidth, int imgHeight);

    private native void set_default_region(String region);
    private native void detect_region(boolean detectRegion);
    private native void set_top_n(int topN);
    private native String get_version();



    public Alpr(String country, String configFile, String runtimeDir)
    {
        try {
            System.out.println("Initialization. Config file: " +configFile + " runtimeDir: " + runtimeDir);
            initialize(country, configFile, runtimeDir);
        } catch (Exception e) {
            System.out.println("Initialization failed: " +e.toString());
            e.printStackTrace();
        }
    }

    public void unload()
    {
        dispose();
    }

    public boolean isLoaded()
    {
        return is_loaded();
    }

    public AlprResults recognize(String imageFile) throws AlprException
    {
        try {
            String json = native_recognize(imageFile);
            return new AlprResults(json);
        } catch (JSONException e)
        {
            throw new AlprException("Unable to parse ALPR results");
        }
    }

    public String recognizeImageOnDefault(byte[] imageBytes) {
        String json = native_recognize(imageBytes);
        return json;
    }

    public AlprResults recognize(byte[] imageBytes) throws AlprException
    {
        try {
            String json = native_recognize(imageBytes);
            System.out.println(json);
            return new AlprResults(json);
        } catch (JSONException e)
        {
            throw new AlprException("Unable to parse ALPR results");
        }
    }


    public AlprResults recognize(long imageData, int bytesPerPixel, int imgWidth, int imgHeight) throws AlprException
    {
        try {
            String json = native_recognize(imageData, bytesPerPixel, imgWidth, imgHeight);
            return new AlprResults(json);
        } catch (JSONException e)
        {
            throw new AlprException("Unable to parse ALPR results");
        }
    }


    public void setTopN(int topN)
    {
        set_top_n(topN);
    }

    public void setDefaultRegion(String region)
    {
        set_default_region(region);
    }

    public void setDetectRegion(boolean detectRegion)
    {
        detect_region(detectRegion);
    }

    public String getVersion()
    {
        return get_version();
    }
}
