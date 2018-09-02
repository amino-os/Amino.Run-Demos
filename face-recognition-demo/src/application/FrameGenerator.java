package application;

//import facerecog.Detection;
//import facerecog.FrameProcess;
import facerecog.Recognition;
import facerecog.Tracking;
import sapphire.kernel.server.KernelServer;
import sapphire.kernel.server.KernelServerImpl;
import sapphire.oms.OMSServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class FrameGenerator {

    public static void main(String[] args) throws IOException, InterruptedException {

//        String cmd = "/home/root1/anaconda3/bin/python";
        String cmd = "/home/root1/.virtualenvs/cv/bin/python";
        String path = "/home/root1/code/edgeCV/face-recognition-demo/src/facerecog/";

        ProcessBuilder ps1 = new ProcessBuilder(cmd, path + "frame_generator.py");
        ps1.redirectErrorStream(true);
        Process pr1 = ps1.start();

        BufferedReader in1 = new BufferedReader(new InputStreamReader(pr1.getInputStream()));
        String frame;

//        try {
//            Recognition recog = new Recognition();
//            Tracking track = new Tracking(recog);
//
//            while ((frame = in1.readLine()) != null) {
//                track.processFrame(frame);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}









        Registry registry;

        try {
            // "192.168.42.140", "22346" - OMS
            registry = LocateRegistry.getRegistry("127.0.0.1", Integer.parseInt("22346"));
            OMSServer server = (OMSServer) registry.lookup("SapphireOMS");

//                "10.0.2.15", "22345" - Kernel server
            KernelServer nodeServer = new KernelServerImpl(
                    new InetSocketAddress("10.0.2.15", Integer.parseInt("22345")),
                    new InetSocketAddress("127.0.0.1", Integer.parseInt("22346")));

            /* recog object has handles to the iostream of recognition.py */
            Recognition recog = (Recognition)server.getAppEntryPoint();
            Tracking track = new Tracking(recog);

            while ((frame = in1.readLine()) != null) {
                track.processFrame(frame);
            }

//            in1.close();
//            recog.in3.close();
//            recog.out3.close();
//            track.out2.close();
//            track.in2.close();










//            Detection detection = new Detection();
////            Tracking tracking =  new Tracking(recog);
////
////            Detection detection = frmProc.processFrame();
//            /* Generate frame */
//            while ((frame = in1.readLine()) != null) {
//                detection.processFrame(frame);
//            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}
