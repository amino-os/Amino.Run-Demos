package org.openalpr;


import android.content.Context;

import org.openalpr.model.Result;
import org.openalpr.util.Utils;

import java.io.File;
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
    public static Context context;
    public static String ANDROID_DATA_DIR;

    public static String getResult(String countryCode, String secondParam, String absolutePath, String openAlprConfFile, int MAX_NUM_OF_PLATES) {
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

            if (lr.isOpenALPRNull()) {
                lr.create();
                Utils.copyAssetFolder
                        (context.getAssets(), "runtime_data", ANDROID_DATA_DIR + File.separatorChar + "runtime_data");
            };

            result = lr.recognizeWithCountryRegionNConfig
                    ("us", "", absolutePath, openAlprConfFile, MAX_NUM_OF_PLATES);

        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }

}
