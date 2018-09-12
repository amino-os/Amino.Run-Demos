package application;

import facerecog.FrameGenerator;
import facerecog.Detection;
import facerecog.Recognition;
import facerecog.Tracking;
import sapphire.common.SapphireObjectID;
import sapphire.kernel.server.KernelServer;
import sapphire.kernel.server.KernelServerImpl;
import sapphire.oms.OMSServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class DemoAppStart {

    public static void main(String[] args) throws IOException, InterruptedException {
        String frame, resp;
        Registry registry;
        String sourceType = "camera"; // "camera": for onboard camera, "video": for video file
        FrameGenerator frameGenerator = new FrameGenerator(sourceType);

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

                    int i = 0;
                    while ((frame = frameGenerator.getFrame()) != null) {
                        // System.out.println("frame from generator: " + frame);
                        /* ignore first few frames to allow for camera warm up */
                        if (i < 3) {
                            i++;
                            continue;
                        }
                        resp = detect.processFrame(frame);
                        // System.out.println("response from detection: " + resp);
                    }
                }
                else if (args[0].equalsIgnoreCase("tracking")) {
                    /* recog is a remote object that has handles to the iostream of recognition.py running on server */
                    SapphireObjectID sapphireObjId = server.createSapphireObject("facerecog.Recognition");
                    Recognition recog = (Recognition)server.acquireSapphireObjectStub(sapphireObjId);
                    Tracking track = new Tracking(recog);

                    int i = 0;
                    while ((frame = frameGenerator.getFrame()) != null) {
                        // System.out.println("frame from generator: " + frame);
                        /* ignore first few frames to allow for camera warm up */
                        if (i < 3) {
                            i++;
                            continue;
                        }
                        track.processFrame(frame);
                    }
                }
                else System.err.println("Incorrect input: please input either detection or tracking");
            } else {
                System.err.println("Incorrect input: please input either detection or tracking");
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}