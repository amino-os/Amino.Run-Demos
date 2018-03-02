package org.openalpr;


import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import sapphire.kernel.server.KernelServer;
import sapphire.kernel.server.KernelServerImpl;
import sapphire.oms.OMSServer;

public class SapphireAccess
{
    public static OMSServer server;
    public static AlprSapphire lr;
    public static String ANDROID_DATA_DIR;

    public static String getResult(String countryCode, String region, String imageFilePath) {
        String result = null;

        try {
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

            // Upload the image file.
            File file = new File(imageFilePath);
            long fileLength = file.length();
            System.out.println("File length = " + fileLength);
            FileInputStream in = new FileInputStream(file);
            byte [] imageData = new byte[8192*1024];

            try {
                int mylen = in.read(imageData);
                System.out.println("Size of image = " + mylen);
                while (mylen > 0) {
                    lr.saveImage(file.getName(), imageData, mylen);
                    mylen = in.read(imageData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

//            if (lr.isOpenALPRNull()) {
                //lr.create();
//                Utils.copyAssetFolder
//                        (context.getAssets(), "runtime_data", ANDROID_DATA_DIR + File.separatorChar + "runtime_data");
//            };

            result = lr.recognizeImageOnDefault
                    (countryCode, region, Constants.OPEN_ALPR_CONF_FILE_LINUX, file.getName(), Constants.MAX_NUM_OF_PLATES);

        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }
}
