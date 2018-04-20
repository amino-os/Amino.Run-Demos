package org.openalpr;

import java.io.File;
import java.net.InetSocketAddress;

/**
 * @author arun
 */
public class Constants
{
    //    public final String SERVER_DIRECTORY = "d:\\temp\\";
    public static final String SERVER_DIRECTORY = "./";
    public static final String RUNTIME_DIRECTORY = "./runtime_data/";
    public static final String RUNTIME_ASSET_DIR = "."; // use current folder for now.
    public static final int MAX_NUM_OF_PLATES = 50;
    public static final String RUNTIME_ASSET_DIR_LINUX = "/usr/share/openalpr/runtime_data";
    public static final String OPEN_ALPR_CONF_FILE_LINUX = "/etc/openalpr/openalpr.conf";

    public static String[] natOmsAddress = { "10.8.0.1", "22346" };
    public static String[] natServerKernelAddress = { "10.8.0.1", "31111" };
//    public static String[] natOmsAddress = { "18.219.220.105", "22346" };
//    public static String[] natServerKernelAddress = { "18.219.220.105", "31111" };
//    public static String[] localOmsAddress = { "100.65.154.225", "22346" };
//    public static String[] omsAddress = { "192.168.10.143", "22346" };
//    public static String[] remoteOmsAddress = { "18.188.181.112", "22346" };

//    public static String[] remoteOmsAddress = { "ec2-18-188-150-201.us-east-2.compute.amazonaws.com", "22346" };
//    public static String[] remoteOmsAddress = { "ec2-18-188-181-112.us-east-2.compute.amazonaws.com", "22346" };
//    public static String[] remoteOmsAddress = { "ec2-18-219-59-131.us-east-2.compute.amazonaws.com", "22346" };
//    public static String[] omsAddress = { "ec2-18-216-118-195.us-east-2.compute.amazonaws.com", "22346" };
//    public static String[] omsAddress = { "ec2-18-221-128-50.us-east-2.compute.amazonaws.com", "22346" };


    // Hong's Kindle
//    public static String[] hostAddress = { "192.168.10.129", "22345" };
//    public static String[] kernelAddress = { "192.168.10.129", "55555" };

    // Sungwook's Kindle
//    public static String[] hostAddress = { "192.168.10.28", "22345" };
//    public static String[] kernelAddress = { "192.168.10.28", "55555" };

    // Huawei phone
//    public static String[] hostAddress = { "10.8.0.3", "22345" };
    public static String[] natDeviceKernelAddress = { "10.8.0.3", "55555" };

    // Huawei phone with Guest network
//    public static String[] hostAddress = { "100.65.154.223", "22345" };
//    public static String[] kernelAddress = { "100.65.154.223", "55555" };

    // Max size of pixels to process either at width or height (original picture should be compressed to this max size).
    public static int maxSizeOfPictureToProcess = 1600;

    public static InetSocketAddress getNatServerKernelAddress() {
        return new InetSocketAddress(natServerKernelAddress[0], Integer.parseInt(natServerKernelAddress[1]));
    }
    public static InetSocketAddress getNatDeviceKernelAddress() {
        return new InetSocketAddress(natDeviceKernelAddress[0], Integer.parseInt(natDeviceKernelAddress[1]));
    }
}
