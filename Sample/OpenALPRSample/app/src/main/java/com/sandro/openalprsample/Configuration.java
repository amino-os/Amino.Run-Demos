package com.sandro.openalprsample;

import static com.sandro.openalprsample.Configuration.ProcessEntity.SERVER;

/**
 * Created by SMoon on 2/27/2018.
 */

public class Configuration {
    public final static String WhereToProcessPrefix = "Will process on ";
    public static ProcessEntity WhereToProcess = SERVER;

    public static String getWhereToProcess() {
        return WhereToProcessPrefix + WhereToProcess.toString();
    }

    public enum ProcessEntity { DEVICE, SERVER };
}
