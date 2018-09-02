package facerecog;

//import sapphire.app.SapphireObject;
////import sapphire.policy.mobility.explicitmigration.ExplicitMigrationPolicy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class Tracking  {
    public OutputStream out2;
    public BufferedReader in2;
    Recognition recog;
    String line2;
    String bbox_list_str;

    public Tracking(Recognition recog) {

        String cmd = "/home/root1/.virtualenvs/cv/bin/python";
        String path = "/home/root1/code/edgeCV/face-recognition-demo/src/facerecog/";

        ProcessBuilder ps2 = new ProcessBuilder(cmd, path + "tracking.py");
        ps2.redirectErrorStream(true);
        Process pr2 = null;
        try {
            pr2 = ps2.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        out2 = pr2.getOutputStream();
        in2 = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
        this.recog = recog;
    }

    public void processFrame(String frame) throws IOException, InterruptedException {
        out2.write((frame+"\n").getBytes()); //write to file done and ok to proceed
        out2.flush();
//        System.out.println("got frame from generator: " + frame);
        line2 = in2.readLine();
//        System.out.println("got frame for recog: " + line2);

        if ( !( line2.equals("done") || line2.equals("next") )) {
//            System.out.println("Sending frame to recog module now");
            bbox_list_str = recog.processFrame(line2);
//            System.out.println("Got bbox_list_str: " + bbox_list_str);

            // Maybe can't have multiple write to same output stream in same method call.
            out2.write((bbox_list_str+"\n").getBytes());
            out2.flush();
        }





//        while ((frame = in2.readLine()) != null) {
//            bbox_list_str = recog.processFrame(frame);
//        }
    }
}
