import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
//        String cmd = "/home/root1/anaconda3/bin/python";
        String cmd = "/home/root1/.virtualenvs/cv/bin/python";
        String path = "/home/root1/code/edgeCV/java_wrapper/src/";

        ProcessBuilder ps1 = new ProcessBuilder(cmd, path + "frame_generator.py");
        ProcessBuilder ps2 = new ProcessBuilder(cmd, path + "recognition.py");

        ps1.redirectErrorStream(true);
        ps2.redirectErrorStream(true);

        Process pr1 = ps1.start();
        Process pr2 = ps2.start();

        OutputStream out2 = pr2.getOutputStream();

//        BufferedWriter bw;
//        FileWriter fw;
//        String fileName = "/media/neeraj/scratchpad.txt";

        BufferedReader in1 = new BufferedReader(new InputStreamReader(pr1.getInputStream()));
        BufferedReader in2 = new BufferedReader(new InputStreamReader(pr2.getInputStream()));

        String line1, line2;

        while ((line1 = in1.readLine()) != null) {
            out2.write((line1+"\n").getBytes());
//            System.out.println("Receiving from process pr1 and saving to file => " + line1);
            out2.flush();
            line2 = in2.readLine();
            System.out.println("Receiving the output from the other process pr2 => " + line2);
//            if (line2 == "done") {
//                continue;
//            }
        }

        out2.close();
        in1.close();
        in2.close();
        pr1.waitFor();
        pr2.waitFor();
    }
}