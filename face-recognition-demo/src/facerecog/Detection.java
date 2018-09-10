package facerecog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class Detection {
    private OutputStream out2;
    private BufferedReader in2;

    public Detection() {
        String cwd = System.getProperty("user.dir");
        String home = System.getProperty("user.home");
        String cmd = home + "/.virtualenvs/cv/bin/python";
        String path = cwd + "/src/facerecog/";

        String outputType = "file"; // "display": for screen, "file": write to file

        ProcessBuilder ps2 = new ProcessBuilder(cmd, path + "detection.py", outputType);
        ps2.redirectErrorStream(true);
        Process pr2 = null;
        try {
            pr2 = ps2.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        out2 = pr2.getOutputStream();
        in2 = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
    }

    public String processFrame(String frame) throws IOException, InterruptedException {
        out2.write((frame+"\n").getBytes());
        out2.flush();
        return in2.readLine();
    }
}
