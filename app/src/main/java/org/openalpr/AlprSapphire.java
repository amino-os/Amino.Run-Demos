package org.openalpr;

import java.io.File;
import java.io.FileOutputStream;

import sapphire.app.SapphireObject;
import sapphire.policy.ShiftPolicy;

/**
 * Open ALPR Sapphire wrapper.
 */
public class AlprSapphire implements SapphireObject<ShiftPolicy> {

    public AlprSapphire() {}

    public String recognize(String imgFilePath, int topN) {
        return "";
    }

    public String recognizeWithCountryNRegion(String country, String region, String imgFilePath, int topN) {
        return "";
    }

    public boolean isOpenALPRNull() {
        return OpenALPR.Factory.isOpenALPRNull();
    }

    public void create() {
        OpenALPR.Factory.create();
    }

    public boolean saveImage(String fileName, byte[] bytes, int len) {
        try {
            String filePath = Constants.SERVER_DIRECTORY + fileName;
            File f = new File(filePath);
            f.createNewFile();
            FileOutputStream out = new FileOutputStream(f, true);
            out.write(bytes, 0, len);
            out.flush();
            out.close();
            System.out.println("Successfully saved a file at " + filePath);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public String recognizeWithCountryRegionNConfig(String country, String region, String configFilePath, String fileName, int MAX_NUM_OF_PLATES) {
        try {
            String imageFilePath = Constants.SERVER_DIRECTORY + fileName;

            System.out.println("Creating instance.");
            OpenALPR instance = OpenALPR.Factory.create();
            if (instance == null) {
                System.out.println("Instance is still null.");
            }

            System.out.println("Instance created. Calling native API. server file path: " + imageFilePath + " config file path: "+ configFilePath);

//            String result = instance.recognizeWithCountryRegionNConfig(country, region, configFilePath, imageFilePath, MAX_NUM_OF_PLATES);
            String result = instance.recognizeInServer(country, region, configFilePath, imageFilePath, Constants.RUNTIME_DIRECTORY, MAX_NUM_OF_PLATES);
            System.out.println("Result returned fine");
            return result;
        } catch (Exception e) {
            System.out.println("There was an error." + e.getMessage());
            e.printStackTrace();
        }

        return "Error";
    }
}