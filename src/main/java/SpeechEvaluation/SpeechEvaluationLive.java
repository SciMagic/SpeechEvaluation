package SpeechEvaluation;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.cmu.sphinx.alignment.LongTextAligner;
import edu.cmu.sphinx.alignment.SimpleTokenizer;
import edu.cmu.sphinx.alignment.TextTokenizer;
import edu.cmu.sphinx.linguist.language.grammar.AlignerGrammar;
import edu.cmu.sphinx.linguist.language.ngram.DynamicTrigramModel;

import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.result.WordResult;
import edu.cmu.sphinx.util.Range;
import edu.cmu.sphinx.api.*;

/**
 * SpeechEvaluationLive
 */
public class SpeechEvaluationLive {

    private static final String ACOUSTIC_MODEL_PATH = "resource:/edu/cmu/sphinx/models/en-us/en-us";
    private static final String DICTIONARY_PATH = "resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict";
    private static final String LANGUAGE_PATH = "resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin";

    private static final int TUPLE_SIZE = 3;

    private final Context context;
    private final ExtendSpeechRecognizer recognizer;
    private final AlignerGrammar grammar;
    private final DynamicTrigramModel languageModel;

    private Boolean stopRec = false;

    private List<WordResult> hypothesis = new ArrayList<WordResult>();
    private Map<Integer, WordResult> finalResult = new HashMap<Integer, WordResult>();
    private List<String> evalueStr;

    private TextTokenizer tokenizer;

    public SpeechEvaluationLive() throws MalformedURLException, IOException {

        Configuration configuration = new Configuration();
        configuration.setAcousticModelPath(ACOUSTIC_MODEL_PATH);
        // configuration.setAcousticModelPath(SpeechEvaluation.class.getClassLoader().getResource("en-us").toString());
        configuration.setDictionaryPath(DICTIONARY_PATH);

        // context = new Context(configuration);
        String path = SpeechEvaluationLive.class.getClassLoader().getResource("default.config.xml").toString();
        context = new Context(path, configuration);
        context.setLocalProperty("lexTreeLinguist->languageModel", "dynamicTrigramModel");
        recognizer = new ExtendSpeechRecognizer(context);
        grammar = context.getInstance(AlignerGrammar.class);
        languageModel = context.getInstance(DynamicTrigramModel.class);
        setTokenizer(new SimpleTokenizer());

    }

    public Map<Integer, WordResult> eval(InputStream audioStream, String text) throws IOException {

        try {

            stopRec = false;

            List<String> cleanedText = getTokenizer().expand(text);

            languageModel.setText(cleanedText);

            evalueStr = cleanedText;

            recognizer.startRecognition(audioStream);

            hypothesis.clear();
            Result result;
            while ((result = recognizer.getResult().getResult()) != null) {

                // result = recognizer.getResult().getResult();
                hypothesis.addAll(result.getTimedBestResult(false));

            }

        } catch (Exception e) {
            // TODO: handle exception

        } finally {

            stopEval();

            List<String> wordText = sentenceToWords(evalueStr);
            Range range = new Range(0, wordText.size());
            LongTextAligner aligner = new LongTextAligner(wordText, TUPLE_SIZE);

            List<WordResult> decoderResults = hypothesis;

            List<String> words = new ArrayList<String>();
            for (WordResult wr : hypothesis) {
                words.add(wr.getWord().getSpelling());
            }

            int[] alignment = aligner.align(words, range);
            dumpAlignmentStats(wordText, alignment, decoderResults);
            finalResult.clear();
            for (int j = 0; j < alignment.length; j++) {
                if (alignment[j] != -1) {
                    finalResult.put(alignment[j], hypothesis.get(j));
                }
            }

            // System.out.println(finalResult);
            float match = finalResult.size();
            float total = wordText.size();
            System.out.printf("the match rate is : %f \n", match / total);

        }

        return finalResult;

    }

    public void stopEval() {

        stopRec = true;
        recognizer.stopRecognition();

    }

    private void dumpAlignmentStats(List<String> transcript, int[] alignment, List<WordResult> results) {
        int insertions = 0;
        int deletions = 0;
        int size = transcript.size();

        int[] aid = alignment;
        int lastId = -1;
        for (int ij = 0; ij < aid.length; ++ij) {
            if (aid[ij] == -1) {
                insertions++;
            } else {
                if (aid[ij] - lastId > 1) {
                    deletions += aid[ij] - lastId;
                }
                lastId = aid[ij];
            }
        }

        if (lastId >= 0 && transcript.size() - lastId > 1) {
            deletions += transcript.size() - lastId;
        }

    }

    public List<String> sentenceToWords(List<String> sentenceTranscript) {
        ArrayList<String> transcript = new ArrayList<String>();
        for (String sentence : sentenceTranscript) {
            String[] words = sentence.split("\\s+");
            for (String word : words) {
                if (word.length() > 0)
                    transcript.add(word);
            }
        }
        return transcript;
    }

    public TextTokenizer getTokenizer() {
        return tokenizer;
    }

    public void setTokenizer(TextTokenizer wordExpander) {
        this.tokenizer = wordExpander;
    }

}