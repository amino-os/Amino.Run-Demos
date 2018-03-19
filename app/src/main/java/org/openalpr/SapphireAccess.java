package org.openalpr;

import java.io.File;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


import sapphire.common.Configuration;
import sapphire.kernel.server.KernelServer;
import sapphire.kernel.server.KernelServerImpl;
import sapphire.oms.OMSServer;

public class SapphireAccess
{
    public static AlprSapphire lr;
    public static long fileUploadTime; // file upload time.
    public static long processingTime; // image recognition processing time.
    private static boolean kernelStarted = false;


    /**
     * Get license plate recognition result for the given image file in the device.
     * @param ANDROID_DATA_DIR
     * @param countryCode
     * @param region
     * @param imageFilePath
     * @param processEntity
     * @return JSON string of the process result.
     */
    public static String getResult(String ANDROID_DATA_DIR, String countryCode, String region, String imageFilePath, Configuration.ProcessEntity processEntity) {
        String result = null, omsAddress = null;
        int omsPort = 0;

        try {
            if (processEntity == Configuration.ProcessEntity.DEVICE) {

                if (!kernelStarted) {
                    // Launch a Sapphire kernel server on the device.
                    String[] kernelArgs = new String[]{
                            Constants.kernelAddress[0], Constants.kernelAddress[1], Constants.localOmsAddress[0], Constants.localOmsAddress[1], processEntity.toString()
                    };

                    KernelServerImpl.main(kernelArgs);
                    kernelStarted = true;
                }
                omsAddress = Constants.localOmsAddress[0];
                omsPort = Integer.parseInt(Constants.localOmsAddress[1]);
            } else {
                // Skip launching Sapphire kernel server on the device as it will connect to the Sapphire kernel in the cloud.
                omsAddress = Constants.remoteOmsAddress[0];
                omsPort = Integer.parseInt(Constants.remoteOmsAddress[1]);
            }

            Registry registry = LocateRegistry.getRegistry(omsAddress, omsPort);
            OMSServer server = (OMSServer) registry.lookup("SapphireOMS");

            KernelServer nodeServer = new KernelServerImpl(new InetSocketAddress(
                    Constants.hostAddress[0], Integer.parseInt(Constants.hostAddress[1])),
                    new InetSocketAddress(omsAddress, omsPort));

            lr = (AlprSapphire) server.getAppEntryPoint(processEntity.toString());

            File file = new File(imageFilePath);
            String openAlprConfFile = ANDROID_DATA_DIR + File.separatorChar + "runtime_data" + File.separatorChar + "openalpr.conf";

            if (processEntity == Configuration.ProcessEntity.SERVER) {
                fileUploadTime = UploadFileToServer(file);
                if (fileUploadTime == -1) {
                    System.out.println("File upload failed for " + imageFilePath);
                    return null;
                };

                openAlprConfFile = Constants.OPEN_ALPR_CONF_FILE_LINUX;
            }

            final long startTime = System.currentTimeMillis();
            result = lr.recognizeImage
                    (countryCode, region, openAlprConfFile, imageFilePath, file.getName(), Constants.MAX_NUM_OF_PLATES, processEntity);
            processingTime = System.currentTimeMillis() - startTime;
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Upload a image file to server.
     * @param file
     * @return Image transfer time, -1 if it failed.
     */
    private static long UploadFileToServer(File file) {
        final long startTime = System.currentTimeMillis();

        try {
            long fileLength = file.length();
            System.out.println("File length = " + fileLength);
            FileInputStream in = new FileInputStream(file);
            byte [] imageData = new byte[2048*1024];

            int mylen = in.read(imageData);
            System.out.println("Size of image = " + mylen);
            while (mylen > 0) {
                lr.saveImage(file.getName(), imageData, mylen);
                mylen = in.read(imageData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

        long elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("File transfer completed. Took " + elapsedTime + " milliseconds.");

        return elapsedTime;
    }
}
