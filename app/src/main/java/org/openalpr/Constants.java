package org.openalpr;

import java.io.File;

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

    public static String[] omsAddress = { "ec2-18-216-118-195.us-east-2.compute.amazonaws.com", "22346" };
    public static String[] hostAddress = { "192.168.10.125", "22345" };
    public static String PackageName = "org.openalpr";
}
