package SpeechEvaluation;

import java.io.IOException;
import java.io.InputStream;

import edu.cmu.sphinx.util.TimeFrame;
import edu.cmu.sphinx.api.*;

/**
 * Speech recognizer that works with audio resources.
 *
 * @see LiveSpeechRecognizer live speech recognizer
 */
public class ExtendSpeechRecognizer extends AbstractSpeechRecognizer {

    /**
     * Constructs new stream recognizer.
     *
     * @param configuration configuration
     * @throws IOException error occured during model load
     */
    public ExtendSpeechRecognizer(Configuration configuration) throws IOException {
        super(configuration);
    }

    public ExtendSpeechRecognizer(Context context) throws IOException {
        super(context);
    }

    public void startRecognition(InputStream stream) {
        startRecognition(stream, TimeFrame.INFINITE);
    }

    /**
     * Starts recognition process.
     *
     * Starts recognition process and optionally clears previous data.
     *
     * @param stream    input stream to process
     * @param timeFrame time range of the stream to process
     * @see StreamSpeechRecognizer#stopRecognition()
     */
    public void startRecognition(InputStream stream, TimeFrame timeFrame) {
        recognizer.allocate();
        context.setSpeechSource(stream, timeFrame);
    }

    /**
     * Stops recognition process.
     *
     * Recognition process is paused until the next call to startRecognition.
     *
     * @see StreamSpeechRecognizer#startRecognition(InputStream, TimeFrame)
     */
    public void stopRecognition() {
        recognizer.deallocate();
    }
}
