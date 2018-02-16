package org.openalpr;

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

    public static String[] omsAddress = { "ec2-18-216-118-195.us-east-2.compute.amazonaws.com", "22346" };
//    public static String[] omsAddress = { "192.168.10.207", "22346" };
    public static String[] hostAddress = { "192.168.10.125", "22345" };
}
