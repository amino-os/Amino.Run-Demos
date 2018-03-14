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
    public static OMSServer server;
    public static AlprSapphire lr;

    public static String getResult(String ANDROID_DATA_DIR, String countryCode, String region, String imageFilePath, Configuration.ProcessEntity processEntity) {

        String result = null;

        try {
            if (processEntity == Configuration.ProcessEntity.DEVICE) {
                // Launch a Sapphire kernel server on the device.
                String [] kernelArgs = new String [] {
                        Constants.kernelAddress[0], Constants.kernelAddress[1], Constants.omsAddress[0], Constants.omsAddress[1]
                };

                KernelServerImpl.main(kernelArgs);
            }

            if (server == null) {
                Registry registry = LocateRegistry.getRegistry(Constants.omsAddress[0], Integer.parseInt(Constants.omsAddress[1]));
                server = (OMSServer) registry.lookup("SapphireOMS");

                KernelServer nodeServer = new KernelServerImpl(new InetSocketAddress(
                        Constants.hostAddress[0], Integer.parseInt(Constants.hostAddress[1])),
                        new InetSocketAddress(Constants.omsAddress[0], Integer.parseInt(Constants.omsAddress[1])));
            }

            if (lr == null) {
                lr = (AlprSapphire) server.getAppEntryPoint();
            }

            File file = new File(imageFilePath);
            String openAlprConfFile = ANDROID_DATA_DIR + File.separatorChar + "runtime_data" + File.separatorChar + "openalpr.conf";

            if (processEntity == Configuration.ProcessEntity.SERVER) {

                if (!UploadFileToServer(file)) {
                    System.out.println("File upload failed for " + imageFilePath);
                    return null;
                };

                openAlprConfFile = Constants.OPEN_ALPR_CONF_FILE_LINUX;
            }

            result = lr.recognizeImage
                    (countryCode, region, openAlprConfFile, imageFilePath, file.getName(), Constants.MAX_NUM_OF_PLATES, processEntity);

        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }

    private static boolean UploadFileToServer(File file) {

        // Upload the image file.
        final long starTime = System.currentTimeMillis();

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
            return false;
        }

        double elapsedTime = (System.currentTimeMillis() - starTime)/1000;
        System.out.println("File transfer completed. Took " + elapsedTime + " seconds.");

        return true;
    }
}
