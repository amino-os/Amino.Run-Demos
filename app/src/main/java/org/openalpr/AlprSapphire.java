package org.openalpr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;

import sapphire.app.SapphireObject;
import sapphire.common.Configuration;
import sapphire.policy.ShiftPolicy;

/**
 * Open ALPR Sapphire wrapper.
 */
public class AlprSapphire implements SapphireObject<ShiftPolicy> {

    public AlprSapphire() {}

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

    /**
     * Process the image on default machine (e.g., Linux)
     * @param country
     * @param region
     * @param openAlprConfFile
     * @param fileName
     * @param MAX_NUM_OF_PLATES
     * @return
     */
    public String recognizeImage(
            String country,
            String region,
            String openAlprConfFile,
            String imageFilePath,
            String fileName,
            int MAX_NUM_OF_PLATES,
            Configuration.ProcessEntity processEntity) {

        AlprJNIWrapper alpr;
        try {
            alpr = new AlprJNIWrapper();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.toString();
        }

        try {
            String results;

            results = alpr.recognize(imageFilePath, fileName, processEntity, country, region, openAlprConfFile, MAX_NUM_OF_PLATES);

            return results;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error at recognizeImageOnDefault: " + e.getMessage();
        }
    }
}