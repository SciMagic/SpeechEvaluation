package SpeechEvaluation;

import org.junit.Test;
import java.net.URL;
import edu.cmu.sphinx.result.*;
import java.util.Map;

public class SpeechEvaluationTest {
    @Test
    public void testSomeLibraryMethod() {

        try {

            SpeechEvaluation evaluation = new SpeechEvaluation();
            URL audio = testSpeech.class.getResource("that.wav");
            Map<Integer, WordResult> result = evaluation.eval(audio, "it may be good enough for us");
            System.out.print(result);

        } catch (Exception e) {
            // TODO: handle exception
        }

    }
}