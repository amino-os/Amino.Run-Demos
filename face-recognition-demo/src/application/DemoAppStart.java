package application;

import facerecog.Detection;
import facerecog.Recognition;
import facerecog.Tracking;
import sapphire.kernel.server.KernelServer;
import sapphire.kernel.server.KernelServerImpl;
import sapphire.oms.OMSServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class DemoAppStart {

    public static void main(String[] args) throws IOException, InterruptedException {

        String cmd = "/home/root1/.virtualenvs/cv/bin/python";
        String path = "/home/root1/code/edgeCV/face-recognition-demo/src/facerecog/";

        ProcessBuilder ps1 = new ProcessBuilder(cmd, path + "frame_generator.py");
        ps1.redirectErrorStream(true);
        Process pr1 = ps1.start();

        BufferedReader in1 = new BufferedReader(new InputStreamReader(pr1.getInputStream()));
        String frame;

        Registry registry;

        try {
            // "192.168.42.140", "22346" - OMS
            registry = LocateRegistry.getRegistry("127.0.0.1", Integer.parseInt("22346"));
            OMSServer server = (OMSServer) registry.lookup("SapphireOMS");

            // "10.0.2.15", "22345" - Kernel server
            KernelServer nodeServer = new KernelServerImpl(
                    new InetSocketAddress("10.0.2.15", Integer.parseInt("22345")),
                    new InetSocketAddress("127.0.0.1", Integer.parseInt("22346")));

            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("detection")) {
                    /* detection runs locally */
                    Detection detect = new Detection();
                    while ((frame = in1.readLine()) != null) {
                        detect.processFrame(frame);
                    }
                }
                else if (args[0].equalsIgnoreCase("tracking")) {
                    /* recog is a remote object that has handles to the iostream of recognition.py running on server */
                    Recognition recog = (Recognition)server.getAppEntryPoint();
                    Tracking track = new Tracking(recog);
                    while ((frame = in1.readLine()) != null) {
                        track.processFrame(frame);
                    }
                }
                else System.err.println("Incorrect input: please input either detection or tracking");
            } else {
                System.err.println("Incorrect input: please input either detection or tracking");
            }

//            in1.close();
//            recog.in3.close();
//            recog.out3.close();
//            track.out2.close();
//            track.in2.close();

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}
