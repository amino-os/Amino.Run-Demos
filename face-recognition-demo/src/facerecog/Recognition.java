package facerecog;

//import sapphire.app.SapphireObject;
//import sapphire.policy.mobility.explicitmigration.ExplicitMigrationPolicy;

import sapphire.app.SapphireObject;
import sapphire.policy.mobility.explicitmigration.ExplicitMigrationPolicy;

import java.io.*;

public class Recognition implements SapphireObject<ExplicitMigrationPolicy> {
//public class Recognition {
    transient OutputStream out3;
    transient BufferedReader in3;

    public Recognition() {

        String cmd = "/home/root1/.virtualenvs/cv/bin/python";
        String path = "/home/root1/code/edgeCV/face-recognition-demo/src/facerecog/";

        ProcessBuilder ps3 = new ProcessBuilder(cmd, path + "recognition.py");
        ps3.redirectErrorStream(true);
        Process pr3 = null;
        try {
            pr3 = ps3.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        in3 = new BufferedReader(new InputStreamReader(pr3.getInputStream()));
        out3 = pr3.getOutputStream();
    }

    public String processFrame(String frame) throws IOException, InterruptedException {
        String bbox_list_str;
        out3.write((frame+"\n").getBytes()); //write to file done and ok to proceed
        out3.flush();
        bbox_list_str = in3.readLine();
        return bbox_list_str;
    }

}