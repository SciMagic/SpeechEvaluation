package SpeechEvaluation;

import edu.cmu.sphinx.result.WordResult;
import java.util.Map;
import java.net.*;
import java.io.*;

/**
 * testSpeech
 */
public class testSpeech {

    private static ServerSocket serverSocket;

    public static void main(String[] args) {

        long currenttime4 = System.currentTimeMillis();

        try {

            // SpeechEvaluation evaluation = new SpeechEvaluation();
            // URL audio =
            // testSpeech.class.getClassLoader().getResource("long_setence.wav");
            // long currenttimef = System.currentTimeMillis();
            // Map<Integer, WordResult> result = evaluation.eval(audio,
            // "When tomorrow turns in today, yesterday, and someday that no more important
            // in your memory, we suddenly realize that we r pushed forward by time. This is
            // not a train in still in which you may feel forward when another train goes
            // by. It is the truth that we've all grown up.And we become different.");
            // System.out.print(result + "\n");
            // long completetimef = System.currentTimeMillis() - currenttimef;
            // System.out.print("it's cost time : " + completetimef / 1000.0 + "\n");

            // long currenttime = System.currentTimeMillis();
            // URL audio2 = testSpeech.class.getClassLoader().getResource("rp-30-i.wav");
            // Map<Integer, WordResult> result2 = evaluation.eval(audio2,
            // "This is a very common type of bow, one showing mainly red and yellow");
            // System.out.print(result2 + "\n");
            // long completetime = System.currentTimeMillis() - currenttime;
            // System.out.print("it's cost time : " + completetime / 1000.0);

            // long currenttime2 = System.currentTimeMillis();
            // URL audio3 =
            // testSpeech.class.getClassLoader().getResource("long_setence_mini.wav");
            // Map<Integer, WordResult> result3 = evaluation.eval(audio3,
            // " When tomorrow turns in today, yesterday, and someday that no more important
            // in your memory, we suddenly realize that we r pushed forward by time. This is
            // not a train in still in which you may feel forward when another train goes
            // by. It is the truth that we've all grown up.And we become different.");
            // System.out.print(result3 + "\n");
            // long completetime2 = System.currentTimeMillis() - currenttime2;
            // System.out.print("it's cost time : " + completetime2 / 1000.0);

            // long currenttime4 = System.currentTimeMillis();
            // SpeechEvaluationLive recoglive = new SpeechEvaluationLive();
            // URL audio4 = testSpeech.class.getClassLoader().getResource("that.wav");

            // recoglive.eval(audio4.openStream(), "that should be good enough for us");
            // // System.out.print(result4 + "\n");
            // recoglive.stopEval();
            // long completetime4 = System.currentTimeMillis() - currenttime4;
            // System.out.print("it's cost time : " + completetime4 / 1000.0);

            System.out.println("1.开启server服务");
            serverSocket = new ServerSocket(9999);
            serverSocket.setSoTimeout(100000);
            Socket clientSoc = serverSocket.accept();
            System.out.println("2.连接成功" + clientSoc.getRemoteSocketAddress());
            InputStream in = clientSoc.getInputStream();
            OutputStream out = clientSoc.getOutputStream();

            // PipedOutputStream pout = new PipedOutputStream();
            // PipedInputStream pin = new PipedInputStream(pout);

            System.out.println("3.评测实例创建开始");
            SpeechEvaluationLive recoglive = new SpeechEvaluationLive();
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));
            pw.println("start push");
            pw.flush();

            System.out.println("4.评测准备开始");
            // Before Monica went to bed, She looked out of her window and saw the moon, the
            // moon looks so near
            // I wish I could play with the moon. Thought monica and reached for it
            currenttime4 = System.currentTimeMillis();
            Map<Integer, WordResult> finalR = recoglive.eval(in,
                    "I wish I could play with the moon. Thought monica and reached for it");
            System.out.println("the final Result is");
            System.out.println(finalR);

        } catch (Exception e) {
            // TODO: handle exception

        } finally {

            long completetime4 = System.currentTimeMillis() - currenttime4;
            System.out.println("it's cost time : " + completetime4 / 1000.0);

        }

    }
}