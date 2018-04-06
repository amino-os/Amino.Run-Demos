package org.openalpr;

import java.io.File;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


import sapphire.common.Configuration;
import sapphire.kernel.common.GlobalKernelReferences;
import sapphire.kernel.server.KernelServer;
import sapphire.kernel.server.KernelServerImpl;
import sapphire.oms.OMSServer;


public class SapphireAccess
{
    public AlprSapphire lr;
    public static long fileUploadTime; // file upload time.
    public static long processingTime; // image recognition processing time.

    private static Configuration.ProcessEntity previousEntity = Configuration.ProcessEntity.UNDECIDED;
    private static boolean kernelReady = false;

    public void initialize() {

        try {
            String[] kernelArgs = new String[]{
                    Constants.kernelAddress[0],
                    Constants.kernelAddress[1],
                    Constants.localOmsAddress[0],
                    Constants.localOmsAddress[1],
                    Configuration.ProcessEntity.DEVICE.toString(),
                    Constants.remoteOmsAddress[0],
                    Constants.remoteOmsAddress[1]
            };

            // Launch local Sapphire kernel server.
            KernelServerImpl.main(kernelArgs);
            kernelReady = true;
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw e;
        }
    }

    public AlprSapphire getNewAppEntryPoint(Configuration.ProcessEntity processEntity, boolean startNewApp) {
        String omsAddress;
        int omsPort;

        try {
            if (processEntity == Configuration.ProcessEntity.DEVICE) {
                omsAddress = Constants.localOmsAddress[0];
                omsPort = Integer.parseInt(Constants.localOmsAddress[1]);
            } else {
                omsAddress = Constants.remoteOmsAddress[0];
                omsPort = Integer.parseInt(Constants.remoteOmsAddress[1]);
            }

            Registry registry = LocateRegistry.getRegistry(omsAddress, omsPort);
            OMSServer server = (OMSServer) registry.lookup("SapphireOMS");

//            KernelServer nodeServer = new KernelServerImpl(
//                    new InetSocketAddress(
//                            Constants.hostAddress[0], Integer.parseInt(Constants.hostAddress[1])),
//                    new InetSocketAddress(omsAddress, omsPort),
//                    null);

            while (!kernelReady) {Thread.sleep(100);}
            if (startNewApp) {
                lr = (AlprSapphire) server.getAppEntryPoint(processEntity.toString());
            } else {
                //lr = (AlprSapphire) server.getExistingAppEntryPoint(processEntity.toString());
            }
            return lr;
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Get license plate recognition result for the given image file in the device.
     * @param ANDROID_DATA_DIR
     * @param countryCode
     * @param region
     * @param imageFilePath
     * @param processEntity
     * @return JSON string of the process result.
     */
    public Results getResult(String ANDROID_DATA_DIR, String countryCode, String region, String imageFilePath, Configuration.ProcessEntity processEntity) {
        Results results = null;

        if (previousEntity == Configuration.ProcessEntity.DEVICE && processEntity == Configuration.ProcessEntity.SERVER) {

            // Migrate object from device to server.
            // Object migration is one way only for now.
            // TODO (4/2/2018, SungwookM): Use NAT to allow object migration between device and server.
            lr.migrateObjectToDifferentOMS();
            GlobalKernelReferences.nodeServer.getKernelClient();
//
//            KernelServer nodeServer = new KernelServerImpl(
//                    new InetSocketAddress(
//                            Constants.hostAddress[0], Integer.parseInt(Constants.hostAddress[1])),
//                    new InetSocketAddress(Constants.remoteOmsAddress[0], Integer.parseInt(Constants.remoteOmsAddress[1])),
//                    null);
            //lr = getNewAppEntryPoint(processEntity, false);
        }

        if (previousEntity == Configuration.ProcessEntity.UNDECIDED || (previousEntity == Configuration.ProcessEntity.SERVER && processEntity == Configuration.ProcessEntity.DEVICE)) {
            lr = getNewAppEntryPoint(processEntity, true);
        }

        try {
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
            results = lr.recognizeImage
                    (countryCode, region, openAlprConfFile, imageFilePath, file.getName(), Constants.MAX_NUM_OF_PLATES, processEntity);

            processingTime = System.currentTimeMillis() - startTime;
            previousEntity = processEntity;
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return results;
    }

    /**
     * Upload a image file to server.
     * @param file
     * @return Image transfer time, -1 if it failed.
     */
    private long UploadFileToServer(File file) {
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
