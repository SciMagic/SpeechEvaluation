package app;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class App {

    private static Socket s;

    public static byte[] IntToByte(int num) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) ((num >> 24) & 0xff);
        bytes[1] = (byte) ((num >> 16) & 0xff);
        bytes[2] = (byte) ((num >> 8) & 0xff);
        bytes[3] = (byte) (num & 0xff);
        return bytes;
    }

    public static void main(String[] args) throws Exception {

        try {

            s = new Socket("127.0.0.1", 9999);
            InputStream sin = s.getInputStream();
            OutputStream sout = s.getOutputStream();
            InputStream audioStream = App.class.getResource("play_with_moon.wav").openStream();
            int readbyte;
            BufferedReader br = new BufferedReader(new InputStreamReader(sin));

            // while (true) {

            // if (!br.ready()) {
            // continue;
            // }

            // String string = br.readLine();

            // if (string != null) {

            // System.out.println(string);
            // break;

            // }

            // }

            long timeStart = System.currentTimeMillis();

            System.out.println("开始写数据了");
            while ((readbyte = audioStream.read()) != -1) {
                sout.write(readbyte);
            }

            sout.write(-1);

            audioStream.close();
            // sout.close();
            System.out.println("数据写完了");

            long timeWrite = System.currentTimeMillis();

            System.out.println("the time cost for write is :" + (timeWrite - timeStart) / 1000.0);

            while (true) {

                if (!br.ready()) {
                    continue;
                }

                String string = br.readLine();

                if (string == "completed") {

                    break;

                }

                if (string != null) {

                    System.out.println(string);

                }

            }

            long timeComplete = System.currentTimeMillis();
            System.out.println("the time cost for complete is :" + (timeComplete - timeStart) / 1000.0);

        } catch (Exception e) {
            System.out.print(e);
        } finally {

        }

    }
}