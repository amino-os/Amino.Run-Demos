package org.openalpr;

/**
 * Open ALPR wrapper.
 */
public class AlprJNIWrapper implements OpenALPR {

    static {
        try {
            System.out.println("Loading openalpr-native library.");
            System.loadLibrary("openalprjni");
            System.out.println("Successfully loaded.");
        }
        catch(Exception e) {
            System.out.println("Exception loading openalpr-native library: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Added methods for Ubuntu server.
    public String recognizeInServer(String country, String region, String configFilePath, String imgFilePath, String runtimeDir, int topN) {
        try {
            System.out.println("[recognizeInServer] initialize");

            initialize(country, configFilePath, runtimeDir);
            System.out.println("[recognizeInServer] set_top_n");
            set_top_n(topN);
            System.out.println("[recognizeInServer] set_default_region");
            set_default_region(country);
            System.out.println("[recognizeInServer] native_recognize");
            String result = native_recognize(imgFilePath);
            System.out.println("[recognizeInServer] Received result : " + result);

            return result;
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();
        }
        return "Error";
    }

    public native void initialize(String country, String configFile, String runtimeDir);
    public native void initialize(String country, String configFile);
    public native String native_recognize(String imageFile);
    public native void set_default_region(String region);
    public native void set_top_n(int topN);
    // End of added methods for Ubuntu server.

    /* (non-Javadoc)
     * @see org.openalpr.Alpr#recognize(java.lang.String, int)
     */
    @Override
    public native String recognize(String imgFilePath, int topN);

    /* (non-Javadoc)
     * @see org.openalpr.Alpr#recognizeWithCountryNRegion(java.lang.String, java.lang.String, java.lang.String, int)
     */
    @Override
    public native String recognizeWithCountryNRegion(String country, String region,
                                                     String imgFilePath, int topN);

    /* (non-Javadoc)
     * @see org.openalpr.Alpr#recognizeWithCountryRegionNConfig(java.lang.String, java.lang.String, java.lang.String, java.lang.String, int)
     */
    @Override
    public native String recognizeWithCountryRegionNConfig(String country,
                                                           String region, String imgFilePath, String configFilePath, int topN);

    /*
     * (non-Javadoc)
     * @see org.openalpr.Alpr#version()
     */
    @Override
    public native String version();
}