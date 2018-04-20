package sapphire.common;

import static sapphire.common.Configuration.ProcessEntity.SERVER;

/**
 * Created by SMoon on 2/27/2018.
 */

public class Configuration {
    public final static String WhereToProcessPrefix = "Will process on ";
    public static ProcessEntity WhereToProcess = SERVER;

    // Whether to use IP address or DNS name for connecting to KernelServer.
    // For cloud computing environment, it should use public DNS name.
    // For internal network, it may need to use IP address.
    public static boolean useIpAddress = true;

    public static String getWhereToProcess() {
        return WhereToProcessPrefix + WhereToProcess.toString();
    }

    public enum ProcessEntity { DEVICE, SERVER, UNDECIDED };
}
