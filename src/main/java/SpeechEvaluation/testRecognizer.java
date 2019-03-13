package SpeechEvaluation;

import java.io.InputStream;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.Context;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import edu.cmu.sphinx.result.WordResult;

/**
 * A simple example that shows how to transcribe a continuous audio file that
 * has multiple utterances in it.
 */
public class testRecognizer {

    public static void main(String[] args) throws Exception {

        Configuration configuration = new Configuration();

        // Load model from the jar
        // configuration.setAcousticModelPath(testRecognizer.class.getClassLoader().getResource("voxforge_en_us").toString());
        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");

        // You can also load model from folder
        // configuration.setAcousticModelPath("file:en-us");

        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");

        Context context = new Context(
                testRecognizer.class.getClassLoader().getResource("default.config.xml").toString(), configuration);

        // Context context = new Context(configuration);
        ExtendSpeechRecognizer recognizer = new ExtendSpeechRecognizer(context);

        long currenttime = System.currentTimeMillis();
        // StreamSpeechRecognizer recognizer = new
        // StreamSpeechRecognizer(configuration);
        InputStream stream = testRecognizer.class.getClassLoader().getResourceAsStream("that.wav");

        // Simple recognition with generic model
        recognizer.startRecognition(stream);
        SpeechResult result;
        while ((result = recognizer.getResult()) != null) {

            System.out.format("Hypothesis: %s\n", result.getHypothesis());

            System.out.println("List of recognized words and their times:");
            for (WordResult r : result.getWords()) {
                System.out.println(r);
            }

            System.out.println("Best 3 hypothesis:");
            for (String s : result.getNbest(3))
                System.out.println(s);

        }
        recognizer.stopRecognition();

        long completetime = System.currentTimeMillis() - currenttime;

        System.out.print("it's cost time : " + completetime / 1000);

        long currenttime2 = System.currentTimeMillis();

        InputStream stream2 = SpeechEvaluation.class.getClassLoader().getResourceAsStream("rp-30.wav");

        // Simple recognition with generic model
        recognizer.startRecognition(stream2);
        SpeechResult result2;
        while ((result2 = recognizer.getResult()) != null) {

            System.out.println("List of recognized words and their times:");
            for (WordResult r : result2.getWords()) {
                System.out.println(r);
            }

        }
        recognizer.stopRecognition();

        long completetime2 = System.currentTimeMillis() - currenttime2;

        System.out.print("it's cost time : " + completetime2 / 1000.0);

    }
}