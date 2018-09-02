package facerecog;

//import sapphire.app.SapphireObject;
//import sapphire.policy.mobility.explicitmigration.ExplicitMigrationPolicy;

//import sapphire.app.SapphireObject;

import java.io.IOException;
import java.io.OutputStream;

public class Detection {
    transient OutputStream out2;
    public Detection() {

        String cmd = "/home/root1/.virtualenvs/cv/bin/python";
        String path = "/home/root1/code/edgeCV/face-recognition-demo/src/facerecog/";

        ProcessBuilder ps2 = new ProcessBuilder(cmd, path + "detection.py");
        ps2.redirectErrorStream(true);
        Process pr2 = null;
        try {
            pr2 = ps2.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        out2 = pr2.getOutputStream();
    }

    public void processFrame(String frame) throws IOException, InterruptedException {
        out2.write((frame+"\n").getBytes()); //write to file done and ok to proceed
        out2.flush();
    }
}
