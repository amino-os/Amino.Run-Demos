package org.openalpr;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.openalpr.model.Result;
import org.openalpr.util.Utils;

import java.io.ByteArrayOutputStream;
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

            // Upload the image file.
            File file = new File(absolutePath);
            long fileLength = file.length();
            System.out.println("File length = " + fileLength);
            FileInputStream in = new FileInputStream(file);
            byte [] imageData = new byte[1024*1024];

//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inSampleSize = 4;
//
//            Bitmap bm = BitmapFactory.decodeFile(absolutePath, options);
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);

            try {
                int mylen = in.read(imageData);
                while (mylen > 0) {
                    lr.saveImage(file.getName(), imageData, mylen);
                    mylen = in.read(imageData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (lr.isOpenALPRNull()) {
                lr.create();
                Utils.copyAssetFolder
                        (context.getAssets(), "runtime_data", ANDROID_DATA_DIR + File.separatorChar + "runtime_data");
            };

            result = lr.recognizeWithCountryRegionNConfig
                    ("us", "", file.getName(), openAlprConfFile, MAX_NUM_OF_PLATES);

        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }

}
