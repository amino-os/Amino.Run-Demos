package org.openalpr;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.openalpr.model.Result;
import org.openalpr.model.ResultsError;

import java.io.File;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sapphire.app.SapphireObject;
import sapphire.common.Configuration;
import sapphire.policy.MigrationPolicy;

/**
 * Open ALPR Sapphire wrapper.
 */
public class AlprSapphire implements SapphireObject<MigrationPolicy> {

    private HashMap<String, Integer> licensePlatesMap = new HashMap<> ();

    public AlprSapphire() {}

    /**
     * Migrate object from device to cloud.
     * @param inetSocketAddress
     */
    public void migrateObject(InetSocketAddress inetSocketAddress) {

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
     * @param openAlprConfFile
     * @param fileName
     * @param MAX_NUM_OF_PLATES
     * @return
     */
    public Results recognizeImage(
            String country,
            String region,
            String openAlprConfFile,
            String imageFilePath,
            String fileName,
            int MAX_NUM_OF_PLATES,
            Configuration.ProcessEntity processEntity) {

        AlprJNIWrapper alpr;
        String resultJson;

        try {
            alpr = new AlprJNIWrapper();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            return GetExceptionResult("Failed to initialize: " + e.getMessage());
        }

        try {
            resultJson = alpr.recognize(imageFilePath, fileName, processEntity, country, region, openAlprConfFile, MAX_NUM_OF_PLATES);
        } catch (AlprException e) {
            e.printStackTrace();
            System.out.println(e);
            return GetExceptionResult(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            return GetExceptionResult("Unexpected exception: " + e.getMessage());
        }

        try {

            Results results = (resultJson == null)? null: new Gson().fromJson(resultJson, Results.class);
//            Set<Result> newResults = new HashSet<>();

            for (Result result : results.getResults()) {
                String plate = result.getPlate();

                if (licensePlatesMap.containsKey(plate)) {
                    int updatedVal = licensePlatesMap.get(plate) + 1;
                    licensePlatesMap.put(plate, updatedVal);
                    result.setCount(updatedVal);
//                    newResults.add(result);
                } else {
                    licensePlatesMap.put(plate, 1);
                    result.setCount(1);
//                    newResults.add(result);
                }
            }

//            results.setNewResults(new ArrayList<>(newResults));
            return results;

        } catch (JsonSyntaxException e) {
            final ResultsError resultsError = new Gson().fromJson(resultJson, ResultsError.class);
            e.printStackTrace();
            System.out.println(e);
            return GetExceptionResult(resultsError.getMsg());
        } catch (Exception e) {
            final ResultsError resultsError = new Gson().fromJson(resultJson, ResultsError.class);
            e.printStackTrace();
            System.out.println(e);
            return GetExceptionResult("Unexpected exception: " + resultsError.getMsg());
        }
    }

    private Results GetExceptionResult(String exceptionMsg) {
        List<Result> resultException = new ArrayList<Result>(){};
        resultException.add(
                new Result (
                        exceptionMsg, 0.0,0.0, null, 0.0,0.0, null, null) { });
        return new Results(0.0, 0.0, resultException) {};
    }
}