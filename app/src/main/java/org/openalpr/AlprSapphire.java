package org.openalpr;

import org.openalpr.model.Result;

import java.io.File;
import java.io.FileOutputStream;

import sapphire.app.AppEntryPoint;
import sapphire.app.AppObjectNotCreatedException;
import sapphire.app.SapphireObject;
import sapphire.common.AppObjectStub;
import sapphire.policy.ShiftPolicy;

import static sapphire.runtime.Sapphire.new_;

/**
 * Open ALPR Sapphire wrapper.
 */
public class AlprSapphire implements SapphireObject<ShiftPolicy> {
    private final String SERVER_DIRECTORY = "d:\\temp\\";

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
            String filePath = SERVER_DIRECTORY + fileName;
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
    public String recognizeWithCountryRegionNConfig(String country, String region, String fileName, String configFilePath, int MAX_NUM_OF_PLATES) {
        try {
            OpenALPR instance = OpenALPR.Factory.create();
            String serverFilePath = SERVER_DIRECTORY + fileName;
            String result = instance.recognizeWithCountryRegionNConfig(country, region, serverFilePath, configFilePath, MAX_NUM_OF_PLATES);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Error";
    }
}