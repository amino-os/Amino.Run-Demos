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
    final String country = "us";

    private void execOnDefault() {
        String configfile = Constants.OPEN_ALPR_CONF_FILE_LINUX;
        String runtimeDataDir = Constants.RUNTIME_ASSET_DIR_LINUX;

        Alpr alpr = new Alpr(country, configfile, runtimeDataDir);

        alpr.setTopN(10);
        alpr.setDefaultRegion("");

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

    public static void main(String args[]) {
        new Main().execOnDefault();
    }
}