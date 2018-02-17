package org.openalpr;

import com.openalpr.jni.Alpr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;

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

    /**
     * Process the image on default machine (e.g., Linux)
     * @param country
     * @param region
     * @param configFilePath
     * @param fileName
     * @param MAX_NUM_OF_PLATES
     * @return
     */
    public String recognizeImageOnDefault(String country, String region, String openAlprConfFile, String fileName, int MAX_NUM_OF_PLATES) {

        Alpr alpr;
        try {
            alpr = new Alpr(country, openAlprConfFile, Constants.RUNTIME_ASSET_DIR_LINUX);
            alpr.setTopN(MAX_NUM_OF_PLATES);
            alpr.setDefaultRegion("");

        } catch (Exception e) {
            System.out.println("There was an error at Initialization: " + e.getMessage());
            e.printStackTrace();
            return "Error: " + e.toString();
        }

        // Read an image into a byte array and send it to OpenALPR
        byte[] imageData;

        try {
            RandomAccessFile f = new RandomAccessFile(fileName, "r");
            imageData = new byte[(int)f.length()];
            f.readFully(imageData);        }
        catch (Exception e) {
            System.out.println("There was an error at RandomAccessFile: " + e.getMessage());
            e.printStackTrace();
            return "Error: " + e.toString();
        }

        try {
            String results = alpr.recognizeImageOnDefault(imageData);

            return results;
//
//            System.out.println("OpenALPR Version: " + alpr.getVersion());
//            System.out.println("Image Size: " + results.getImgWidth() + "x" + results.getImgHeight());
//            System.out.println("Processing Time: " + results.getTotalProcessingTimeMs() + " ms");
//            System.out.println("Found " + results.getPlates().size() + " results");
//
//            System.out.format("  %-15s%-8s\n", "Plate Number", "Confidence");
//            for (AlprPlateResult result : results.getPlates()) {
//                for (AlprPlate plate : result.getTopNPlates()) {
//                    if (plate.isMatchesTemplate())
//                        System.out.print("  * ");
//                    else
//                        System.out.print("  - ");
//                    System.out.format("%-15s%-8f\n", plate.getCharacters(), plate.getOverallConfidence());
//                }
//            }
        } catch (Exception e) {
            System.out.println("There was an error at recognizeImageOnDefault." + e.getMessage());
            e.printStackTrace();
            return "Error at recognizeImageOnDefault: " + e.getMessage();
        }
    }
}