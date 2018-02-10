package org.openalpr;

import org.openalpr.model.Result;

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

    public String recognizeWithCountryRegionNConfig(String country, String region, String imgFilePath, String configFilePath, int MAX_NUM_OF_PLATES) {
        try {
            OpenALPR instance = OpenALPR.Factory.create();
            String result = instance.recognizeWithCountryRegionNConfig(country, region, imgFilePath, configFilePath, MAX_NUM_OF_PLATES);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Error";
    }
}