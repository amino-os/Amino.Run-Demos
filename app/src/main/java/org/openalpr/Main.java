package org.openalpr;

import com.openalpr.jni.Alpr;
import com.openalpr.jni.AlprPlate;
import com.openalpr.jni.AlprPlateResult;
import com.openalpr.jni.AlprResults;
import java.io.RandomAccessFile;
import java.io.File;

/**
 * Open ALPR Sapphire wrapper.
 */
public class Main {
    final String FileName = "2018-02-14-10-58-41.jpg";
    final String RUNTIME_ASSET_DIR = "/usr/share/openalpr/runtime_data";
    final String openAlprConfFile = "/etc/openalpr/openalpr.conf";
    final String country = "us";

    private void exec2() {
        String configfile = openAlprConfFile;
        String runtimeDataDir = RUNTIME_ASSET_DIR;

        Alpr alpr = new Alpr(country, configfile, runtimeDataDir);

        alpr.setTopN(10);
        alpr.setDefaultRegion("wa");

        // Read an image into a byte array and send it to OpenALPR
//        byte [] imageData = new byte[1024*1024];
        byte[] imageData;

        try {
            File file = new File(FileName);
            long fileLength = file.length();
            System.out.println("File length = " + fileLength);

            RandomAccessFile f = new RandomAccessFile(FileName, "r");
            imageData = new byte[(int)f.length()];
            f.readFully(imageData);        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }

        try {
            AlprResults results = alpr.recognize(imageData);

            System.out.println("OpenALPR Version: " + alpr.getVersion());
            System.out.println("Image Size: " + results.getImgWidth() + "x" + results.getImgHeight());
            System.out.println("Processing Time: " + results.getTotalProcessingTimeMs() + " ms");
            System.out.println("Found " + results.getPlates().size() + " results");

            System.out.format("  %-15s%-8s\n", "Plate Number", "Confidence");
            for (AlprPlateResult result : results.getPlates()) {
                for (AlprPlate plate : result.getTopNPlates()) {
                    if (plate.isMatchesTemplate())
                        System.out.print("  * ");
                    else
                        System.out.print("  - ");
                    System.out.format("%-15s%-8f\n", plate.getCharacters(), plate.getOverallConfidence());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void exec() {
        try {
            String imageFilePath = Constants.SERVER_DIRECTORY + FileName;
            System.out.println("Creating instance.");
            OpenALPR instance = OpenALPR.Factory.create();
            if (instance == null) {
                System.out.println("Instance is still null.");
            }

            System.out.println("Instance created. Calling native API. server file path: " + imageFilePath + " config file path: "+ openAlprConfFile);

//            String result = instance.recognizeWithCountryRegionNConfig(country, region, configFilePath, imageFilePath, MAX_NUM_OF_PLATES);
            String result = instance.recognizeInServer("us", "", openAlprConfFile, imageFilePath, RUNTIME_ASSET_DIR, Constants.MAX_NUM_OF_PLATES);
            System.out.println("Result returned fine");
            System.out.println("Result: " + result);
        } catch (Exception e) {
            System.out.println("There was an error." + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        new Main().exec2();
    }
}